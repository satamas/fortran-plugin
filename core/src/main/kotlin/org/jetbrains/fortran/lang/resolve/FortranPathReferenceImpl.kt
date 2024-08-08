package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranDataPathImplMixin
import org.jetbrains.fortran.lang.psi.mixin.FortranSubModuleImplMixin

class FortranPathReferenceImpl(element: FortranDataPathImplMixin) :
        FortranReferenceBase<FortranDataPathImplMixin>(element), FortranReference {

    override val FortranDataPathImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun resolveInner(incompleteCode: Boolean): List<FortranNamedElement> {
        val result: List<FortranNamedElement> = resolveWithoutFiltering(incompleteCode)
        return if (incompleteCode) {
            result
        } else {
            result.filter { element.referenceName.equals(it.name, true) }
        }
    }

    private fun resolveWithoutFiltering(incompleteCode: Boolean): List<FortranNamedElement> {
        // module rename
        val useStmt = PsiTreeUtil.getParentOfType(element, FortranUseStmt::class.java)
        if (useStmt != null) {
            return when (element.parent) {
                is FortranRenameStmt -> resolveModuleRename(useStmt, incompleteCode)
                is FortranOnlyStmt -> resolveOnlyStmt(element.parent as FortranOnlyStmt, incompleteCode)
                else -> resolveModules()
            }
        }

        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        // types should be done first of all
        if (element is FortranTypeName) {
            return resolveTypes(programUnit, incompleteCode)
        }

        // inside interface
        val interfaceBody = programUnit.parent as? FortranInterfaceSpecification
        if (interfaceBody != null) {
            return collectNamesInInterfaceBody(programUnit)
        }

        // resolve paths like a%b%c
        if (element.firstChild is FortranDataPath) {
            return resolveDifficultPath()
        }

        return resolveName(programUnit, incompleteCode)
    }

    // private methods which a parts of the resolveInner
    private fun resolveModuleRename(useStmt: FortranUseStmt, incompleteCode: Boolean) =
            useStmt.dataPath!!.reference.multiResolve()
                    .mapNotNull { PsiTreeUtil.getParentOfType(it, FortranModule::class.java) }
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), false) }.toList()

    private fun resolveOnlyStmt(onlyStmt: FortranOnlyStmt, incompleteCode: Boolean): List<FortranNamedElement> {
        val useStmt = onlyStmt.parent as FortranUseStmt

        return useStmt.dataPath?.reference?.multiResolve()?.flatMap {
            findNamePsiInModule(PsiTreeUtil.getParentOfType(it, FortranModule::class.java), incompleteCode, mutableSetOf(), false)
        } ?: emptyList()
    }

    private fun resolveTypes(programUnit: FortranProgramUnit, incompleteCode: Boolean): List<FortranNamedElement> {
        val types = programUnit.types.toMutableSet()
        val outerProgramUnit: FortranProgramUnit
        // if we are internal program unit
        if (programUnit.parent is FortranModuleSubprogramPart
                || programUnit.parent is FortranInternalSubprogramPart) {
            outerProgramUnit = PsiTreeUtil.getParentOfType(programUnit, FortranProgramUnit::class.java) ?: programUnit
            types.addAll(outerProgramUnit.types)
        } else {
            outerProgramUnit = programUnit
        }

        types.addAll(collectRenamedNames(programUnit))

        // modules
        if (element.parent !is FortranUseStmt) { // We do not need recursion here
            val allModules = collectAllUseStmts(programUnit, outerProgramUnit)
            for (module in allModules) {
                types.addAll(importNamesFromModule(module, incompleteCode, mutableSetOf(), true).toList())
            }
        }

        // submodules
        if (outerProgramUnit is FortranModule) {
            types.addAll(findSubModulesInProjectFiles(outerProgramUnit.name, null)
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), true) }.toList())
        } else if (outerProgramUnit is FortranSubModuleImplMixin && outerProgramUnit.name?.count { it == ':' } == 1) {
            types.addAll(findSubModulesInProjectFiles(outerProgramUnit.getModuleName(), outerProgramUnit.getPersonalName())
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), true) }.toList())
        }
        return types.toList()
    }

    private fun resolveDifficultPath(): List<FortranNamedElement> {
        val innerPart = (element.firstChild.reference as FortranPathReferenceImpl).multiResolve()

        val innerPartTypeStmt = innerPart.map {
            PsiTreeUtil.getParentOfType(it, FortranTypeDeclarationStmt::class.java)
                    ?: PsiTreeUtil.getParentOfType(it, FortranDataComponentDefStmt::class.java)
        }.firstOrNull()

        val innerType = when (innerPartTypeStmt) {
            is FortranTypeDeclarationStmt -> innerPartTypeStmt.derivedTypeSpec?.typeName
            is FortranDataComponentDefStmt -> innerPartTypeStmt.derivedTypeSpec?.typeName
            else -> null
        } ?: return emptyList()

        // we should find type declaration. If it is in use -> rename we'll search for the origin name (several times)
        var type = innerType.reference.multiResolve().firstOrNull()
        while (type?.parent is FortranRenameStmt) {
            type = (type.parent as FortranRenameStmt).dataPath?.reference?.multiResolve()?.firstOrNull()
        }

        return PsiTreeUtil.getParentOfType(type, FortranDerivedTypeDef::class.java)?.variables ?: emptyList()
    }

    private fun resolveName(programUnit: FortranProgramUnit, incompleteCode: Boolean): List<FortranNamedElement> {
        val outerProgramUnit: FortranProgramUnit
        // local variables
        val names = programUnit.variables.toMutableSet()
        if (element.referenceName.equals(programUnit.unit?.name, true))
            names.add(programUnit.unit as FortranNamedElement)
        // interfaces
        val fortranBlock = PsiTreeUtil.getStubChildOfType(programUnit, FortranBlock::class.java)
        names.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlock, FortranInterfaceBlock::class.java)
                .flatMap { it.subprograms })
        // enums
        names.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlock, FortranEnumDef::class.java)
                .flatMap { it.variables })

        // if we are real program unit
        if (programUnit.parent !is FortranModuleSubprogramPart
                && programUnit.parent !is FortranInternalSubprogramPart) {
            outerProgramUnit = programUnit
            names.addAll(programUnit.subprograms)
        } else {
            outerProgramUnit = PsiTreeUtil.getParentOfType(programUnit, FortranProgramUnit::class.java) ?: programUnit
            names.addAll(outerProgramUnit.variables)
            names.addAll(outerProgramUnit.subprograms)
            // interfaces
            val fortranBlockOuter = PsiTreeUtil.getStubChildOfType(outerProgramUnit, FortranBlock::class.java)
            names.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlockOuter, FortranInterfaceBlock::class.java)
                    .flatMap { it.subprograms })
            // enums
            names.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlockOuter, FortranEnumDef::class.java)
                    .flatMap { it.variables })
        }

        names.addAll(resolveInProjectFiles())
        names.addAll(collectRenamedNames(programUnit))

        // modules
        if (element.parent !is FortranUseStmt || element.parent !is FortranDataPath) { // We do not need recursion here
            val allModules = collectAllUseStmts(programUnit, outerProgramUnit)
            for (module in allModules) {
                names.addAll(importNamesFromModule(module, incompleteCode, mutableSetOf(), false).toList())
            }
        }

        // submodules
        if (outerProgramUnit is FortranModule) {
            names.addAll(findSubModulesInProjectFiles(outerProgramUnit.name, null)
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), false) }.toList())
        } else if (outerProgramUnit is FortranSubModuleImplMixin && outerProgramUnit.name?.count { it == ':' } == 1) {
            names.addAll(findSubModulesInProjectFiles(outerProgramUnit.getModuleName(), outerProgramUnit.getPersonalName())
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), false) }.toList())
        }

        // let's check interfaces here. Maybe some declarations are excessive
        if (names.any { it.parent is FortranInterfaceStmt }) {
            names.removeIf { it.parent !is FortranInterfaceStmt }
        }

        // let's try to live without real declaration
        if (!incompleteCode && names.none { element.referenceName.equals(it.name, true) }) {
            val implicitStmts = PsiTreeUtil.findChildrenOfType(programUnit, FortranImplicitStmt::class.java)
            if (implicitStmts.isEmpty() || implicitStmts.all { it.implicitSpecList.isNotEmpty() }) {

                val outerProgramUnitBlock = PsiTreeUtil.findChildOfType(outerProgramUnit, FortranBlock::class.java)
                val outerProgramUnitStmt = if (outerProgramUnit.firstChild != outerProgramUnitBlock) outerProgramUnit.firstChild else null
                val firstUsage = PsiTreeUtil.findChildrenOfType(outerProgramUnitStmt, FortranDataPath::class.java)
                        .filter { it.parent !is FortranRenameStmt }.filterImplicitDeclaration()
                        ?: PsiTreeUtil.findChildrenOfType(outerProgramUnitBlock, FortranDataPath::class.java)
                                .filter { it.parent !is FortranRenameStmt }.filterImplicitDeclaration()
                        ?: PsiTreeUtil.findChildrenOfType(programUnit, FortranDataPath::class.java)
                                .filter { it.parent !is FortranRenameStmt }.filterImplicitDeclaration()
                if (firstUsage != null) {
                    names.add(firstUsage)
                }
            }
        }

        return names.toList()
    }

    // global search
    private fun resolveInProjectFiles(): List<FortranNamedElement> {
        val result: MutableSet<FortranNamedElement> = mutableSetOf()

        val vFiles = collectAllProjectFiles()
        for (file in vFiles) {
            val psiFile = PsiManager.getInstance(element.project).findFile(file)
            val programUnits = programUnitsFromFile(psiFile)
            result.addAll(programUnits.mapNotNull { it.unit })
            result.addAll(programUnits.filterIsInstance(FortranFunctionSubprogram::class.java)
                    .flatMap { f -> f.variables }
            )
        }
        return result.toList()
    }

    private fun resolveModules() = collectAllProjectFiles()
            .asSequence()
            .map { PsiManager.getInstance(element.project).findFile(it) }
            .map { programUnitsFromFile(it) }
            .flatten()
            .filterIsInstance<FortranModule>()
            .mapNotNull { it.unit }
            .toList()


    private fun findSubModulesInProjectFiles(moduleName: String?, subModuleName: String?): MutableSet<FortranSubmodule> {
        val result: MutableSet<FortranSubmodule> = mutableSetOf()
        if (moduleName == null) return result
        val vFiles = collectAllProjectFiles()

        result.addAll(vFiles.map { programUnitsFromFile(PsiManager.getInstance(element.project).findFile(it)) }
                .flatten().filterIsInstance(FortranSubModuleImplMixin::class.java)
                .filter {
                    moduleName.equals(it.getModuleName(), true)
                            && (subModuleName == null || it.getSubModuleName().equals(subModuleName, true))
                })
        return result
    }

    private fun findNamePsiInModule(module: FortranProgramUnit?, incompleteCode: Boolean, allSeenModules: MutableSet<String>,
                                    onlyTypes: Boolean): MutableSet<FortranNamedElement> {
        if (module == null || module.name == null || (module !is FortranModule && module !is FortranSubmodule)) {
            return mutableSetOf()
        }
        // loop check
        if (allSeenModules.contains(module.name!!.lowercase())) return mutableSetOf()
        allSeenModules.add(module.name!!.lowercase())

        val allNames: MutableSet<FortranNamedElement> = mutableSetOf()

        val allModules = collectAllUseStmts(module)
        allModules.forEach { allNames.addAll(importNamesFromModule(it, incompleteCode, allSeenModules, onlyTypes)) }
        allNames.addAll(collectRenamedNames(module))


        if (!onlyTypes) {
            allNames.addAll(module.subprograms)
        }
        allNames.addAll(module.types
                .plus(module.variables)
                .plus(PsiTreeUtil.findChildrenOfType(PsiTreeUtil.findChildOfType(module, FortranBlock::class.java),
                        FortranInterfaceBlock::class.java).flatMap { it.subprograms })
                .plus(PsiTreeUtil.findChildrenOfType(if (module is FortranModule) module.block else (module as FortranSubmodule).block,
                        FortranEnumDef::class.java).flatMap { (it as FortranEnumDef).variables }))
        // interfaces
        val fortranBlock = PsiTreeUtil.getStubChildOfType(module, FortranBlock::class.java)
        allNames.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlock, FortranInterfaceBlock::class.java)
                .flatMap { it.subprograms })
        // enums
        allNames.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlock, FortranEnumDef::class.java)
                .flatMap { it.variables })


        // submodules
        if (module is FortranModule) {
            allNames.addAll(findSubModulesInProjectFiles(module.name, null)
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), onlyTypes) }.toList())
        } else if ((module as FortranSubModuleImplMixin).name?.count { it == ':' } == 1) {
            allNames.addAll(findSubModulesInProjectFiles(module.getModuleName(), module.getPersonalName())
                    .flatMap { findNamePsiInModule(it, incompleteCode, mutableSetOf(), onlyTypes) }.toList())
        }
        allSeenModules.remove(module.name!!.lowercase())
        return allNames
    }

    // util methods
    private fun collectAllUseStmts(programUnit: FortranProgramUnit, outerProgramUnit: FortranProgramUnit)
            : MutableSet<FortranDataPath> {
        val allModules = outerProgramUnit.usedModules.toMutableSet()
        if (programUnit != outerProgramUnit) allModules.addAll(programUnit.usedModules)
        return allModules
    }

    private fun collectAllUseStmts(programUnit: FortranProgramUnit?): MutableSet<FortranDataPath> {
        return if (programUnit != null) collectAllUseStmts(programUnit, programUnit) else emptySet<FortranDataPath>().toMutableSet()
    }


    private fun collectAllProjectFiles() = FileTypeIndex.getFiles(FortranFileType, GlobalSearchScope.projectScope(element.project))
            .plus(FileTypeIndex.getFiles(FortranFixedFormFileType, GlobalSearchScope.projectScope(element.project)))

    private fun programUnitsFromFile(psiFile: PsiFile?) = if (psiFile is FortranFile) {
        psiFile.programUnits
    } else {
        (psiFile as? FortranFixedFormFile)?.programUnits ?: emptyList()
    }

    private fun collectRenamedNames(programUnit: FortranProgramUnit): MutableSet<FortranNamedElement> {
        val fortranBlock = PsiTreeUtil.getChildOfType(programUnit, FortranBlock::class.java)
        val useStmts = PsiTreeUtil.getStubChildrenOfTypeAsList(fortranBlock, FortranUseStmt::class.java)
        val namesInRenameStmtList = useStmts.flatMap {
            PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranRenameStmt::class.java)
        }.mapNotNull { it.entityDecl }
        val namesInOnlyStmtList = useStmts.flatMap {
            PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranOnlyStmt::class.java)
        }.flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranRenameStmt::class.java) }
                .mapNotNull { it.entityDecl }
        return (namesInRenameStmtList + namesInOnlyStmtList).toMutableSet()
    }

    private fun importNamesFromModule(module: FortranDataPath, incompleteCode: Boolean, allSeenModules: MutableSet<String>,
                                      onlyTypes: Boolean): Collection<FortranNamedElement> {
        val useStmt = module.parent as FortranUseStmt
        if (useStmt.onlyStmtList.isNotEmpty()) {
            if (incompleteCode) {
                return useStmt.onlyStmtList.mapNotNull {
                    val renameStmt = it.renameStmt
                    val localNameElement: FortranNamedElement? = if (renameStmt == null) it.dataPath else renameStmt.entityDecl
                    localNameElement
                }
            }
            return useStmt.onlyStmtList.mapNotNull { it.dataPath }.flatMap {
                val resolvedResult = it.reference.multiResolve()
                resolvedResult.ifEmpty { listOf(it) }
            }
        }
        val renameInOnly = useStmt.onlyStmtList.map { it.renameStmt }
        val renameList = ((module.parent as FortranUseStmt).renameStmtList + renameInOnly)
                .mapNotNull { it?.dataPath?.referenceName?.lowercase() }
        return module.reference.multiResolve().flatMap {
            findNamePsiInModule(PsiTreeUtil.getParentOfType(it, FortranModule::class.java), incompleteCode, allSeenModules, onlyTypes)
        }.filter {
            element.referenceName.lowercase() !in renameList
        }
    }

    private fun collectNamesInInterfaceBody(body: FortranProgramUnit): List<FortranNamedElement> {
        return body.variables + PsiTreeUtil.getChildOfType(body, FortranBeginUnitStmt::class.java)!!.entityDecl!!
    }

    private fun <T : FortranDataPath> Iterable<T>.filterImplicitDeclaration(): T? = asSequence()
            .filter { PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) is FortranProgramUnit }
            .filter { it.name?.equals(element.name, true) ?: false }
            .filter { (it as FortranDataPath).firstChild !is FortranDataPath }
            .firstOrNull()
}


