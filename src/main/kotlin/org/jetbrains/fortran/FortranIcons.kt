package org.jetbrains.fortran

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

object FortranIcons {
    val fileTypeIcon = IconLoader.getIcon("/icons/fortran.png")

    val mainProgramIcon = AllIcons.Nodes.Parameter
    val functionIcon = AllIcons.Nodes.Field
    val subroutineIcon = AllIcons.Nodes.Static
    val moduleIcon = AllIcons.Nodes.Method
    val submoduleIcon = AllIcons.Nodes.AbstractMethod
    val separateModuleSubprogramIcon = AllIcons.Nodes.Static
    val blockDataIcon = AllIcons.Javaee.DataSourceImport

    // types
    val typeIcon = AllIcons.Nodes.Class

    // variables
    val variableIcon = AllIcons.Nodes.Variable
    val methodIcon = AllIcons.Nodes.Method
}