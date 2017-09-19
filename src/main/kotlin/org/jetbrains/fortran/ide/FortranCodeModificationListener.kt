package org.jetbrains.fortran.ide

import com.intellij.openapi.project.Project
import com.intellij.pom.PomManager
import com.intellij.pom.PomModelAspect
import com.intellij.pom.event.PomModelEvent
import com.intellij.pom.event.PomModelListener
import com.intellij.pom.tree.TreeAspect
import com.intellij.pom.tree.events.TreeChangeEvent
import com.intellij.psi.impl.PsiModificationTrackerImpl
import com.intellij.psi.util.PsiModificationTracker
import org.jetbrains.fortran.lang.psi.FortranFile

class FortranCodeModificationListener(
        modificationTracker: PsiModificationTracker,
        project: Project,
        private val treeAspect: TreeAspect
) {
    init {
        val model = PomManager.getModel(project)
        @Suppress("NAME_SHADOWING")
        val modificationTracker = modificationTracker as PsiModificationTrackerImpl
        model.addModelListener(object: PomModelListener {
            override fun isAspectChangeInteresting(aspect: PomModelAspect): Boolean {
                return aspect == treeAspect
            }

            override fun modelChanged(event: PomModelEvent) {
                val changeSet = event.getChangeSet(treeAspect) as TreeChangeEvent? ?: return
                val file = changeSet.rootElement.psi.containingFile as? FortranFile ?: return
                if (changeSet.changedElements.isNotEmpty()) {
                    if (file.isPhysical) {
                        modificationTracker.incCounter()
                    }
                }
            }
        })
    }
}

