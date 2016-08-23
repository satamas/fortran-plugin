// This is a generated file. Not intended for manual editing.
package org.jetbrains.fortran.lang;

import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.fortran.FortranLanguage;
import org.jetbrains.fortran.lang.psi.FortranElement;
import org.jetbrains.fortran.lang.psi.impl.*;

public interface FortranNodeTypes {
    IFileElementType FORTRAN_FILE = new IFileElementType(FortranLanguage.INSTANCE);

    FortranNodeType PROGRAM = new FortranNodeType("PROGRAM", FortranProgram.class);
    FortranNodeType FUNCTION = new FortranNodeType("FUNCTION", FortranProgram.class);
    FortranNodeType SUBROUTINE = new FortranNodeType("SUBROUTINE", FortranProgram.class);
    FortranNodeType BLOCK_DATA = new FortranNodeType("BLOCK_DATA", FortranProgram.class);

    FortranNodeType PRINT_STATEMENT = new FortranNodeType("PRINT", FortranPrintStatement.class);

    FortranNodeType BLOCK_DATA_STATEMENT = new FortranNodeType("BLOCK_DATA_STATEMENT", FortranElementImpl.class);

    FortranNodeType SUBROUTINE_STATEMENT = new FortranNodeType("SUBROUTINE_STATEMENT", FortranElementImpl.class);
    FortranNodeType FUNCTION_STATEMENT = new FortranNodeType("FUNCTION_STATEMENT", FortranElementImpl.class);
    FortranNodeType PARAMS = new FortranNodeType("PARAMS", FortranElementImpl.class);
    FortranNodeType END_STATEMENT = new FortranNodeType("END_STATEMENT", FortranElementImpl.class);

    FortranNodeType PROGRAM_STATEMENT = new FortranNodeType("PROGRAM_STATEMENT", FortranElementImpl.class);
    FortranNodeType END_PROGRAM_STATEMENT = new FortranNodeType("END_PROGRAM_STATEMENT", FortranElementImpl.class);

    FortranNodeType ENTRY_STATEMENT = new FortranNodeType("ENTRY_STATEMENT", FortranElementImpl.class);

    FortranNodeType FORMAT_STATEMENT = new FortranNodeType("FORMAT_STATEMENT", FortranElementImpl.class);
    FortranNodeType FORMAT_SPECIFICATION = new FortranNodeType("FORMAT_SPECIFICATION", FortranElementImpl.class);

    FortranNodeType SAVE_STATMENT = new FortranNodeType("SAVE_STATEMENT", FortranElementImpl.class);

    FortranNodeType DIMENSION_STATEMENT = new FortranNodeType("DIMENSION_STATEMENT", FortranElementImpl.class);

    FortranNodeType IMPLICIT_STATEMENT = new FortranNodeType("IMPLICIT_STATEMENT", FortranElementImpl.class);
    FortranNodeType IMPLICIT_SPECIFICATION = new FortranNodeType("IMPLICIT_SPECIFICATION", FortranElementImpl.class);
    FortranNodeType LETTER_RANGE_LIST = new FortranNodeType("LETTER_RANGE_LIST", FortranElementImpl.class);
    FortranNodeType LETTER_RANGE = new FortranNodeType("LETTER_RANGE", FortranElementImpl.class);

    FortranNodeType PARAMETER_STATEMENT = new FortranNodeType("PARAMETER_STATEMENT", FortranElementImpl.class);
    FortranNodeType PARAMETER = new FortranNodeType("PARAMETER", FortranElementImpl.class);

    FortranNodeType DATA_STATEMENT = new FortranNodeType("DATA_STATEMENT", FortranElementImpl.class);
    FortranNodeType DATA_IMPLIED_DO_OBJECT = new FortranNodeType("DATA_IMPLIED_DO_OBJECT", FortranElementImpl.class);
    FortranNodeType DATA_STATEMENT_VALUE = new FortranNodeType("DATA_STATEMENT_VALUE", FortranElementImpl.class);

    FortranNodeType EQUIVALENCE_STATEMENT = new FortranNodeType("EQUIVALENCE_STATEMENT", FortranElementImpl.class);
    FortranNodeType EQUIVALENCE_SET = new FortranNodeType("EQUIVALENCE_SET", FortranElementImpl.class);

    FortranNodeType EXTERNAL_STATEMENT = new FortranNodeType("EXTERNAL_STATEMENT", FortranElementImpl.class);
    FortranNodeType INTRINSIC_STATEMENT = new FortranNodeType("INTRINSIC_STATEMENT", FortranElementImpl.class);

    FortranNodeType COMMON_STATEMENT = new FortranNodeType("COMMON_STATEMENT", FortranElementImpl.class);
    FortranNodeType COMMON_BLOCK_LIST = new FortranNodeType("COMMON_BLOCK_LIST", FortranElementImpl.class);
    FortranNodeType COMMON_BLOCK = new FortranNodeType("COMMON_BLOCK", FortranElementImpl.class);
    FortranNodeType COMMON_BLOCK_NAME = new FortranNodeType("COMMON_BLOCK_NAME", FortranElementImpl.class);

    FortranNodeType INTEGER_CONSTANT = new FortranNodeType("INTEGER_CONSTANT", FortranElementImpl.class);
    FortranNodeType FLOATING_POINT_CONSTANT = new FortranNodeType("FLOATING_POINT_CONSTANT", FortranElementImpl.class);
    FortranNodeType DOUBLE_PRECISION_CONSTANT = new FortranNodeType("DOUBLE_PRECISION_CONSTANT", FortranElementImpl.class);
    FortranNodeType COMPLEX_CONSTANT = new FortranNodeType("COMPLEX_CONSTANT", FortranElementImpl.class);

    FortranNodeType BOOLEAN_CONSTANT = new FortranNodeType("BOOLEAN_CONSTANT", FortranElementImpl.class);
    FortranNodeType STRING_CONSTANT = new FortranNodeType("STRING_CONSTANT", FortranElementImpl.class);

    FortranNodeType TYPE_DECLARATION_STATEMENT = new FortranNodeType("TYPE_DECLARATION_STATEMENT", FortranElementImpl.class);
    FortranNodeType ENTITY_DECLARATION = new FortranNodeType("ENTITY_DECLARATION", FortranElementImpl.class);
    FortranNodeType TYPE_REFERENCE = new FortranNodeType("TYPE_REFERENCE", FortranElementImpl.class);

    FortranNodeType ARRAY_SPECIFICATION = new FortranNodeType("ARRAY_SPECIFICATION", FortranElementImpl.class);
    FortranNodeType SIZE_SPECIFICATION = new FortranNodeType("SIZE_SPECIFICATION", FortranElementImpl.class);

    FortranNodeType ASSIGNMENT_EXPRESSION = new FortranNodeType("ASSIGNMENT_EXPRESSION", FortranElementImpl.class);
    FortranNodeType BINARY_EXPRESSION = new FortranNodeType("BINARY_EXPRESSION", FortranBinaryExpression.class);
    FortranNodeType PREFIX_EXPRESSION = new FortranNodeType("PREFIX_EXPRESSION", FortranElementImpl.class);
    FortranNodeType REFERENCE_EXPRESSION = new FortranNodeType("REFERENCE_EXPRESSION", FortranReferenceExpression.class);
    FortranNodeType OPERATION_REFERENCE = new FortranNodeType("OPERATION_REFERENCE", FortranOperationReference.class);
    FortranNodeType PARENTHESIZED = new FortranNodeType("PARENTHESIZED_EXPRESSION", FortranParenthesizedExpression.class);
    FortranNodeType ARGUMENT_LIST = new FortranNodeType("ARGUMENT_LIST", FortranArgumentsList.class);
    FortranNodeType CALL_OR_ACCESS_EXPRESSION = new FortranNodeType("CALL_OR_ACCESS_EXPRESSION", FortranArgumentsList.class);
    FortranNodeType VALUE_ARGUMENT_LIST = new FortranNodeType("VALUE_ARGUMENT_LIST", FortranElementImpl.class);

    FortranNodeType VARIABLE = new FortranNodeType("VARIABLE", FortranElementImpl.class);
    FortranNodeType SUBSTRING_RANGE = new FortranNodeType("SUBSTRING_RANGE", FortranElementImpl.class);
    FortranNodeType SUBSCRIPT_LIST = new FortranNodeType("SUBSCRIPT_LIST", FortranElementImpl.class);

    FortranNodeType LABEL_DEFINITION = new FortranNodeType("LABEL_DEFINITION", FortranElementImpl.class);
}
