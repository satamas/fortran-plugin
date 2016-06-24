// This is a generated file. Not intended for manual editing.
package org.jetbrains.fortran.lang;

import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.fortran.FortranLanguage;
import org.jetbrains.fortran.lang.psi.impl.FortranElementImpl;
import org.jetbrains.fortran.lang.psi.impl.FortranPrintStatement;
import org.jetbrains.fortran.lang.psi.impl.FortranProgram;

public interface FortranNodeTypes {
    IFileElementType FORTRAN_FILE = new IFileElementType(FortranLanguage.INSTANCE);

    FortranNodeType PROGRAM = new FortranNodeType("PROGRAM", FortranProgram.class);
    FortranNodeType PRINT_STATEMENT = new FortranNodeType("PRINT", FortranPrintStatement.class);
    FortranNodeType INTEGER_CONSTANT = new FortranNodeType("INTEGER_CONSTANT", FortranElementImpl.class);
}
