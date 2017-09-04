package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranDataPathImplMixin
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType
import org.jetbrains.fortran.lang.core.stubs.*
import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner
import org.jetbrains.fortran.lang.psi.impl.FortranNameStmtImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranSubModuleImplMixin

class FortranPathReferenceImpl(element: FortranDataPathImplMixin) :
        FortranReferenceBase<FortranDataPathImplMixin>(element), FortranReference {

    override val FortranDataPathImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun isReferenceTo(element: PsiElement?): Boolean {
        val stmt = element?.parent
        val file = stmt?.parent?.parent
        if (stmt is FortranNameStmtImpl && (file is FortranFile || file is FortranFixedFormFile) ) {
            return true
        }
        return super.isReferenceTo(element)
    }

    override fun resolveInner(): List<FortranNamedElement> {
        // module rename
        val useStmt = PsiTreeUtil.getParentOfType(element, FortranUseStmt::class.java)
        if (useStmt != null && element.parent is FortranRenameStmt) {
            return resolveModuleRename(useStmt)
        }

        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        // types should be done first of all
        if (element is FortranTypeName) {
            return resolveTypes(programUnit)
        }

        // resolve paths like a%b%c
        if (element.firstChild is FortranDataPath)
        {
            return resolveDifficultPath()
        }

        return resolveName(programUnit)
    }

    // private methods which a parts of the resolveInner
    private fun resolveModuleRename(useStmt: FortranUseStmt) =
            useStmt.dataPath!!.reference.multiResolve().filterNotNull()
            .map { PsiTreeUtil.getParentOfType(it, FortranModule::class.java) }.filterNotNull()
            .flatMap { findNamePsiInModule(it, mutableSetOf(), false) }.toList()


    private fun resolveTypes(programUnit: FortranProgramUnit) : List<FortranNamedElement> {
        val types = programUnit.types.filter { element.referenceName.equals(it.name, true) }
                .toMutableSet()
        val outerProgramUnit : FortranProgramUnit
        // if we are internal program unit
        if (programUnit.parent is FortranModuleSubprogramPart
                || programUnit.parent is FortranInternalSubprogramPart) {
            outerProgramUnit = PsiTreeUtil.getParentOfType(programUnit, FortranProgramUnit::class.java) ?: programUnit
            types.addAll(outerProgramUnit.types.filter { element.referenceName.equals(it.name, true) })
        } else {
            outerProgramUnit = programUnit
        }

        // renamed types
        types.addAll(programUnit.variables.filter{ PsiTreeUtil.getParentOfType(it, FortranRenameStmt::class.java) != null })

        // modules
        if (element.parent !is FortranUseStmt) { // We do not need recursion here
            val allModules = collectAllModules(programUnit, outerProgramUnit)
            for (module in allModules) {
                types.addAll(findModuleInProjectFiles(module.referenceName).filterNotNull()
                        .flatMap { findNamePsiInModule(it, mutableSetOf(), true) }.toList())
            }
        }

        // submodules
        if (outerProgramUnit is FortranModule) {
            types.addAll(findSubModulesInProjectFiles(outerProgramUnit.name, null).filterNotNull()
                    .flatMap { findNamePsiInModule(it, mutableSetOf(), true) }.toList())
        } else if (outerProgramUnit is FortranSubModuleImplMixin && outerProgramUnit.name?.count { it == ':' } == 1) {
            types.addAll(findSubModulesInProjectFiles(outerProgramUnit.getModuleName(), outerProgramUnit.getPersonalName()).filterNotNull()
                    .flatMap { findNamePsiInModule(it, mutableSetOf(), true) }.toList())
        }
        return types.toList()
    }

    private fun resolveDifficultPath() : List<FortranNamedElement> {
        val innerPart = (element.firstChild.reference as FortranPathReferenceImpl).multiResolve().filterNotNull()

        val innerPartTypeStmt = innerPart.map { PsiTreeUtil.getParentOfType(it, FortranTypeDeclarationStmt::class.java)
                ?: PsiTreeUtil.getParentOfType(it, FortranDataComponentDefStmt::class.java) }.firstOrNull()

        val innerType = if (innerPartTypeStmt is FortranTypeDeclarationStmt) {
            innerPartTypeStmt.derivedTypeSpec?.typeName
        } else if (innerPartTypeStmt is FortranDataComponentDefStmt){
            innerPartTypeStmt.derivedTypeSpec?.typeName
        } else null

        if (innerType == null) {
            return emptyList()
        } else {
            // we should find type declaration. If it is in use -> rename we'll search for the origin name (several times)
            var type = innerType.reference.multiResolve().firstOrNull()
            while (type?.parent is FortranRenameStmt) {
                type = (type.parent as FortranRenameStmt).dataPath?.reference?.multiResolve()?.firstOrNull()
            }

            return PsiTreeUtil.getParentOfType(type, FortranDerivedTypeDef::class.java)?.variables
                    ?.filter { element.referenceName.equals(it.name, true) }?.toMutableList() ?: emptyList()
        }
    }

    private fun resolveName(programUnit: FortranProgramUnit) : List<FortranNamedElement> {
        val outerProgramUnit : FortranProgramUnit
        // local variables
        val names = programUnit.variables.filter { element.referenceName.equals(it.name, true) }
                .toMutableSet()
        if (element.referenceName.equals(programUnit.unit?.name, true) ) names.add(programUnit.unit as FortranNamedElement)

        // if we are real program unit
        if (programUnit.parent !is FortranModuleSubprogramPart
                && programUnit.parent !is FortranInternalSubprogramPart) {
            names.addAll(programUnit.subprograms.filter { element.referenceName.equals(it.name, true) })

            outerProgramUnit = programUnit
        } else {
            outerProgramUnit = PsiTreeUtil.getParentOfType(programUnit, FortranProgramUnit::class.java) ?: programUnit
            names.addAll(outerProgramUnit.variables.filter { element.referenceName.equals(it.name, true) })
            names.addAll(outerProgramUnit.subprograms.filter { element.referenceName.equals(it.name, true) })
        }

        names.addAll(resolveInProjectFiles())

        // modules
        if (element.parent !is FortranUseStmt) { // We do not need recursion here
            val allModules = collectAllModules(programUnit, outerProgramUnit)
            for (module in allModules) {
                names.addAll(findModuleInProjectFiles(module.referenceName).filterNotNull()
                        .flatMap { findNamePsiInModule(it, mutableSetOf(), false) }.toList())
            }
        }

        // submodules
        if (outerProgramUnit is FortranModule) {
            names.addAll(findSubModulesInProjectFiles(outerProgramUnit.name, null).filterNotNull()
                    .flatMap { findNamePsiInModule(it, mutableSetOf(), false) }.toList())
        } else if (outerProgramUnit is FortranSubModuleImplMixin && outerProgramUnit.name?.count { it == ':' } == 1) {
            names.addAll(findSubModulesInProjectFiles(outerProgramUnit.getModuleName(), outerProgramUnit.getPersonalName()).filterNotNull()
                    .flatMap { findNamePsiInModule(it, mutableSetOf(), false) }.toList())
        }

        // let's try to live without real declaration
        if (names.isEmpty()) {
            val implicitStmts = PsiTreeUtil.findChildrenOfType(programUnit, FortranImplicitStmt::class.java)
            if (implicitStmts.isEmpty() || implicitStmts.all { !it.implicitSpecList.isEmpty()}) {
                val firstUsage = PsiTreeUtil.findChildrenOfType(outerProgramUnit.firstChild, FortranDataPath::class.java)
                        .filterImplicitDeclaration() ?:
                        PsiTreeUtil.findChildrenOfType(PsiTreeUtil.findChildOfType(outerProgramUnit, FortranBlock::class.java ), FortranDataPath::class.java)
                        .filterImplicitDeclaration() ?:
                        PsiTreeUtil.findChildrenOfType(programUnit, FortranDataPath::class.java)
                        .filterImplicitDeclaration()
                if (firstUsage != null) {
                    names.add(firstUsage)
                }
            }

        }

        return names.toList()
    }

    // global search
    private fun resolveInProjectFiles() : MutableSet<FortranNamedElement> {
        val result : MutableSet<FortranNamedElement> = mutableSetOf()
        // other files
        val vFiles = collectAllProjectFiles()

        for (file in vFiles) {
            val psiFile = PsiManager.getInstance(element.project).findFile(file)
            val fileStub = fortranFileStub(psiFile)
            if (fileStub != null) {
                val unitStubs = fileStub.childrenStubs
                        .filter { it is FortranProgramUnitStub }
                        .filter { (it as FortranProgramUnitStub).name.equals(element.referenceName, true) }
                result.addAll(unitStubs.map { (it.psi as FortranProgramUnit).unit }.filterNotNull())
                result.addAll(unitStubs.map { it.psi }.filterIsInstance(FortranFunctionSubprogram::class.java)
                        .flatMap { f -> f.variables.filter { element.referenceName.equals(it.name, true) } }
                        .filterNotNull())
            } else if (psiFile != null) {
                val unitPsi = psiFile.children.filter { it is FortranProgramUnit }
                        .filter { element.referenceName.equals((it as FortranProgramUnit).name, true) }

                result.addAll(unitPsi.map { it -> (it as FortranProgramUnit).unit }.filterNotNull())
                result.addAll(unitPsi.filterIsInstance(FortranFunctionSubprogram::class.java)
                        .flatMap { f -> f.variables.filter { element.referenceName.equals(it.name, true) } }
                        .filterNotNull())
            }
        }
        return result
    }

    private fun findModuleInProjectFiles(moduleName : String?) : MutableSet<FortranModule> {
        val result : MutableSet<FortranModule> = mutableSetOf()
        if (moduleName == null) return result
        val vFiles = collectAllProjectFiles()

        for (file in vFiles) {
            val psiFile = PsiManager.getInstance(element.project).findFile(file)
            val fileStub = fortranFileStub(psiFile)
            if (fileStub != null) {
                result.addAll(fileStub.childrenStubs
                        .filter { it is FortranProgramUnitStub }
                        .filter { (it as FortranProgramUnitStub).name.equals(moduleName, true) }
                        .filter { it.psi is FortranModule }.map { (it.psi as FortranModule) }.filterNotNull())
            } else if (psiFile != null) {
                result.addAll(psiFile.children.filter { it is FortranModule }
                        .filter { moduleName.equals((it as FortranProgramUnit).name, true) }
                        .map { it -> (it as FortranModule) }.filterNotNull())
            }
        }
        return result
    }

    private fun findSubModulesInProjectFiles(moduleName : String?, subModuleName : String?) : MutableSet<FortranSubmodule> {
        val result : MutableSet<FortranSubmodule> = mutableSetOf()
        if (moduleName == null) return result
        val vFiles = collectAllProjectFiles()

        for (file in vFiles) {
            val psiFile = PsiManager.getInstance(element.project).findFile(file)
            val fileStub = fortranFileStub(psiFile)
            if (fileStub != null) {
                result.addAll(fileStub.childrenStubs
                        .filter { it is FortranProgramUnitStub }.filter { it.psi is FortranSubModuleImplMixin }
                        .map { (it.psi as FortranSubModuleImplMixin) }.filterNotNull()
                        .filter {
                            it.getModuleName().equals(moduleName, true)
                            && (subModuleName == null || it.getSubModuleName().equals(subModuleName, true))
                        })

            } else if (psiFile != null) {
                result.addAll(psiFile.children.filter { it is FortranSubModuleImplMixin }
                        .filter {
                            moduleName.equals((it as FortranSubModuleImplMixin).getModuleName(), true)
                            && (subModuleName == null || it.getSubModuleName().equals(subModuleName, true))
                        }
                        .map { it -> (it as FortranSubModuleImplMixin) }.filterNotNull())
            }
        }
        return result
    }

    fun findNamePsiInModule(module : FortranProgramUnit?, allSeenModules : MutableSet<String>,
                            onlyTypes : Boolean) : MutableSet<FortranNamedElement> {
        if (module == null || module.name == null || (module !is FortranModule && module !is FortranSubmodule)) {
            return mutableSetOf()
        }
        // loop check
        if (allSeenModules.contains(module.name!!.toLowerCase())) return mutableSetOf()
        allSeenModules.add(module.name!!.toLowerCase())

        val allNames: MutableSet<FortranNamedElement> = mutableSetOf()
        if (module.stub == null) {
            val allModules = collectAllModules(module)
            for (m in allModules) {
                val onlyIsUsed = !(m.parent as FortranUseStmt).onlyStmtList.isEmpty()
                val onlyList = (m.parent as FortranUseStmt).onlyStmtList
                        .map { it.entityDecl?.name?.toLowerCase() }.filterNotNull()
                val renameList = (m.parent as FortranUseStmt).renameStmtList
                        .plus((m.parent as FortranUseStmt).onlyStmtList.map { it.renameStmt })
                        .map { it?.dataPath?.referenceName?.toLowerCase() }.filterNotNull()

                allNames.addAll(m.reference.multiResolve().flatMap {
                    findNamePsiInModule(PsiTreeUtil.getParentOfType(it, FortranModule::class.java), allSeenModules, onlyTypes)
                }.filter {
                    element.referenceName.equals(it.name, true)
                            && element.referenceName.toLowerCase() !in renameList
                            && (!onlyIsUsed || element.referenceName.toLowerCase() in onlyList)
                })

            }
            if (!onlyTypes) {
                allNames.addAll(module.subprograms.filter { element.referenceName.equals(it.name, true) })
            }
            allNames.addAll(module.types.filter { element.referenceName.equals(it.name, true) }
                    .plus(module.variables.filter { element.referenceName.equals(it.name, true) } ))
        } else {
            if (!onlyTypes) {
                // subprograms with a sought for name
                val subprograms = module.stub.findChildStubByType(FortranModuleSubprogramPartStub.Type)
                allNames.addAll(subprograms?.childrenStubs?.filter { it is FortranProgramUnitStub }
                        ?.filter { element.referenceName.equals((it as FortranProgramUnitStub).name, true) }
                        ?.map {it.psi as FortranNamedElement}?.filterNotNull() ?: emptyList())
            }

            // variables with a sought for name
            val block = module.stub.findChildStubByType(FortranBlockStub.Type)
            if (!onlyTypes) {
                allNames.addAll(block?.childrenStubs?.filter { it is FortranStatementStub }
                    ?.filter { it.psi is FortranTypeDeclarationStmt }
                    ?.flatMap { it.childrenStubs }
                        ?. filter{ it is FortranEntityDeclStub }
                                ?.filter { element.referenceName.equals((it as FortranEntityDeclStub).name, true) }
                        ?.map{it.psi as FortranEntityDecl}?.filterNotNull()
                     ?: emptyList())

            }
            // types
            allNames.addAll(block?.childrenStubs?.filter { it is FortranDerivedTypeDefStub }
                    ?.filter { element.referenceName.equals((it as FortranDerivedTypeDefStub).name, true) }
                    ?.map { it.psi as FortranNamedElement}?.filterNotNull()
                    ?: emptyList())

            // вложенные модули
            val useStmtStubs = block?.childrenStubs?.filter{ it is FortranStatementStub}
                    ?.filter{it.psi is FortranUseStmt}?.toList() ?: emptyList()

            for (m in useStmtStubs) {
                var onlyIsUsed = false
                val renamed : MutableSet<String> = mutableSetOf()
                for (c in m.childrenStubs) {
                    if (c is FortranOnlyStmtStub) {
                        onlyIsUsed = true
                        if (c.childrenStubs[0] is FortranDataPathStub
                                && (c.childrenStubs[0] as FortranDataPathStub).name.equals(element.referenceName, true)) {
                            allNames.add(c.childrenStubs[0].psi as FortranNamedElement)
                        } else if (c.childrenStubs[0] is FortranRenameStmtStub
                                && (c.childrenStubs[0].childrenStubs[0] as FortranEntityDeclStub).name.equals(element.referenceName, true)) {
                            allNames.add(c.childrenStubs[0].childrenStubs[0].psi as FortranNamedElement)
                        }
                    } else if (c is FortranRenameStmtStub && (c.childrenStubs[0] as FortranEntityDeclStub).name.equals(element.referenceName, true)) {
                        allNames.add(c.childrenStubs[0].psi as FortranNamedElement)
                        renamed.add((c.childrenStubs[1] as FortranDataPathStub).name!!)
                    }
                }
                if (!onlyIsUsed && element.referenceName.toLowerCase() !in renamed) {
                    allNames.addAll(findModuleInProjectFiles((m.childrenStubs[0] as FortranDataPathStub).name)
                            .flatMap{ findNamePsiInModule(it, allSeenModules, onlyTypes)})
                }
            }
        }

        // submodules
        if (module is FortranModule) {
            allNames.addAll(findSubModulesInProjectFiles(module.name, null).filterNotNull()
                    .flatMap { findNamePsiInModule(it, mutableSetOf(), onlyTypes) }.toList())
        } else if ((module as FortranSubModuleImplMixin).name?.count { it == ':' } == 1){
            allNames.addAll(findSubModulesInProjectFiles(module.getModuleName(), module.getPersonalName())
                    .filterNotNull().flatMap { findNamePsiInModule(it, mutableSetOf(), onlyTypes) }.toList())
        }
        allSeenModules.remove(module.name!!.toLowerCase())
        return allNames
    }

    // util methods
    fun collectAllModules(programUnit: FortranProgramUnit, outerProgramUnit: FortranProgramUnit)
            : MutableSet<FortranDataPath> {
        val allModules = outerProgramUnit.usedModules.toMutableSet()
        if (programUnit != outerProgramUnit) allModules.addAll(programUnit.usedModules)
        return allModules
    }

    fun collectAllModules(programUnit: FortranProgramUnit?) : MutableSet<FortranDataPath> {
        return if (programUnit != null) collectAllModules(programUnit, programUnit) else emptySet<FortranDataPath>().toMutableSet()
    }

    fun fortranFileStub(file : PsiFile?) = if (file is FortranFile) file.stub else (file as? FortranFixedFormFile)?.stub

    fun collectAllProjectFiles() = FileTypeIndex.getFiles(FortranFileType, GlobalSearchScope.projectScope(element.project))
            .plus(FileTypeIndex.getFiles(FortranFixedFormFileType, GlobalSearchScope.projectScope(element.project)))

    fun <T : FortranDataPath> kotlin.collections.Iterable<T>.filterImplicitDeclaration(): T? =
        filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) is FortranProgramUnit }
        .filter { it.name?.equals(element.name, true) ?: false }
        .filter { (it as FortranDataPath).firstChild !is FortranDataPath }
        .toMutableList().firstOrNull()

}


