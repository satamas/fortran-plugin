package org.jetbrains.fortran

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object FortranIcons {
    val fileTypeIcon = IconLoader.getIcon("/icons/fortran_logo_64x64.png", FortranIcons::class.java)

    val mainProgramIcon : Icon = AllIcons.Nodes.Parameter
    val functionIcon : Icon = AllIcons.Nodes.Field
    val subroutineIcon : Icon = AllIcons.Nodes.Static
    val moduleIcon : Icon = AllIcons.Nodes.Method
    val submoduleIcon : Icon = AllIcons.Nodes.AbstractMethod
    val separateModuleSubprogramIcon : Icon = AllIcons.Nodes.Static
    val blockDataIcon: Icon = AllIcons.Nodes.DataTables

    // types
    val typeIcon : Icon = AllIcons.Nodes.Class
    val interfaceIcon : Icon = AllIcons.Nodes.Interface
    // variables
    val variableIcon : Icon = AllIcons.Nodes.Variable
}