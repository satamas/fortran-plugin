package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
//import org.jetbrains.fortran.lang.FortranTypes;

import static org.jetbrains.fortran.lang.FortranTypes.*;/*
import static org.jetbrains.fortran.lang.parser.FortranParser.block; // убивает всё нафиг
import static org.jetbrains.fortran.lang.parser.FortranParser.end_do;
*/

//import static org.jetbrains.fortran.lang.FortranTypes.*;
//import static org.jetbrains.fortran.lang.parser.FortranParser.*;

/**
 * Created by Sergei on 13.04.17.
 * The class for manual parsing of the external rules in the bnf grammar
 * This is a copy of generated grammar to test, how it compiles
 */
public class FortranParserUtil extends GeneratedParserUtilBase {

    public static boolean parseLabeledDoConstruct (PsiBuilder builder_, int level_, Parser label_do_stmt) {
        if (!recursion_guard_(builder_, level_, "label_do_construct")) return false;
        if (!nextTokenIs(builder_, "<label do construct>", DO, IDENTIFIER)) return false;
        boolean result_, pinned_;
        PsiBuilder.Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, LABELED_DO_CONSTRUCT, "<label do construct>");
        result_ = label_do_stmt.parse(builder_, level_ + 1);
        pinned_ = result_; // pin = 1
      //  result_ = result_ && report_error_(builder_, block(builder_, level_ + 1));
      //  result_ = pinned_ && end_do(builder_, level_ + 1) && result_;
        exit_section_(builder_, level_, marker_, result_, pinned_, null);
        return result_;/* || pinned_;*/
    }

}
