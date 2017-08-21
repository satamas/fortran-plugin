package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranDataPathImplMixin
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import org.jetbrains.fortran.FortranFileType


class FortranPathReferenceImpl(element: FortranDataPathImplMixin) :
        FortranReferenceBase<FortranDataPathImplMixin>(element), FortranReference {

    override val FortranDataPathImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> {
        // module rename
        val useStmt = PsiTreeUtil.getParentOfType(element, FortranUseStmt::class.java)
        if (useStmt != null && element.parent is FortranRenameStmt) {
            val moduleNamesPsi = useStmt.dataPath!!.reference.multiResolve().filterNotNull()
            for (moduleNamePsi in moduleNamesPsi) {
                val modulePsi = PsiTreeUtil.getParentOfType(moduleNamePsi, FortranModule::class.java)

                if (modulePsi != null) {
                    return modulePsi.variables.filter { element.referenceName.equals(it.name, true)}
                            .plus(modulePsi.subprograms.filter { element.referenceName.equals(it.name, true) }).toList()
                }
            }
            return emptyList()
        }


        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
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

        // other files
        val vFiles = FileBasedIndex.getInstance()
                .getContainingFiles(FileTypeIndex.NAME, FortranFileType, GlobalSearchScope.projectScope(element.project))

        for (file in vFiles) {
            val psiFile = PsiManager.getInstance(element.project).findFile(file) as FortranFile
            names.addAll(psiFile.children.filter { it is FortranProgramUnit }
                    .map { it -> (it as FortranProgramUnit).unit }
                    .filterNotNull()
                    .filter { element.referenceName.equals(it.name, true) })
            names.addAll(psiFile.children.filter { it is FortranFunctionSubprogram }
                    .flatMap { f -> (f as FortranFunctionSubprogram).variables.filter { element.referenceName.equals(it.name, true) } }
                    .filterNotNull())

        }

        // modules
        if (element.parent !is FortranUseStmt) { // We do not need recursion here
            for (module in outerProgramUnit.usedModules) {
                val onlyIsUsed = !(module.parent as FortranUseStmt).onlyStmtList.isEmpty()
                val onlyList = (module.parent as FortranUseStmt).onlyStmtList
                        .map{ it.entityDecl?.name?.toLowerCase() }.filterNotNull()
                val renameList = (module.parent as FortranUseStmt).renameStmtList
                        .plus((module.parent as FortranUseStmt).onlyStmtList.map{ it.renameStmt})
                        .map{ it?.dataPath?.referenceName?.toLowerCase() }.filterNotNull()

                val moduleNamesPsi = module.reference.multiResolve()
                for (moduleNamePsi in moduleNamesPsi) {
                    val modulePsi = PsiTreeUtil.getParentOfType(moduleNamePsi, FortranModule::class.java)

                    if (modulePsi != null) {
                        names.addAll(modulePsi.variables.filter {
                            element.referenceName.equals(it.name, true)
                                    && element.referenceName.toLowerCase() !in renameList
                                    && (onlyList.isEmpty() || element.referenceName.toLowerCase() in onlyList)
                        })
                        names.addAll(modulePsi.subprograms.filter {
                            element.referenceName.equals(it.name, true)
                                    && element.referenceName.toLowerCase() !in renameList
                                    && (!onlyIsUsed || element.referenceName.toLowerCase() in onlyList)
                        })
                    }
                }
            }
        }
        return names.toList()
    }
}