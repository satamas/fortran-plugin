// This is a generated file. Not intended for manual editing.
package org.jetbrains.fortran.lang;

import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.fortran.FortranLanguage;
import org.jetbrains.fortran.lang.psi.impl.*;

public interface FortranNodeTypes {
    IFileElementType FORTRAN_FILE = new IFileElementType(FortranLanguage.INSTANCE);

    FortranNodeType PROGRAM = new FortranNodeType("PROGRAM", FortranProgram.class);
    FortranNodeType PRINT_STATEMENT = new FortranNodeType("PRINT", FortranPrintStatement.class);
    FortranNodeType INTEGER_CONSTANT = new FortranNodeType("INTEGER_CONSTANT", FortranElementImpl.class);
    FortranNodeType STRING_LITERAL = new FortranNodeType("STRING_LITERAL", FortranElementImpl.class);

    FortranNodeType BINARY_EXPRESSION = new FortranNodeType("BINARY_EXPRESSION", FortranBinaryExpression.class);
    FortranNodeType REFERENCE_EXPRESSION = new FortranNodeType("REFERENCE_EXPRESSION", FortranReferenceExpression.class);
    FortranNodeType OPERATION_REFERENCE = new FortranNodeType("OPERATION_REFERENCE", FortranOperationReference.class);
    FortranNodeType PARENTHESIZED = new FortranNodeType("PARENTHESIZED_EXPRESSION", FortranParenthesizedExpression.class);
    FortranNodeType ARGUMENT_LIST = new FortranNodeType("ARGUMENT_LIST", FortranArgumentsList.class);
    FortranNodeType CALL_OR_ACCESS_EXPRESSION = new FortranNodeType("CALL_OR_ACCESS_EXPRESSION", FortranArgumentsList.class);
    FortranNodeType VALUE_ARGUMENT_LIST = new FortranNodeType("VALUE_ARGUMENT_LIST", FortranElementImpl.class);
}
