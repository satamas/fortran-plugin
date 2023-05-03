package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import org.jetbrains.fortran.lang.psi.FortranStmt

open class FortranStmtImpl(node: ASTNode?) : FortranCompositeElementImpl(node!!), FortranStmt
