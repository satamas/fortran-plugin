package org.jetbrains.fortran.lang.parser

import com.intellij.lang.ForeignLeafType
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.lang.psi.FortranIncludeForeignLeafType

/**
 * Created by Sergei on 13.04.17.
 * The class for manual parsing of the external rules in the bnf grammar
 * This is a copy of generated grammar to test, how it compiles
 */

object FortranParserUtil {
    @JvmStatic
    fun parseIdentifier(builder: PsiBuilder, level: Int): Boolean {
        return IdentifierParser().parse(builder, level)
    }

    @JvmStatic
    fun parseKeyword(builder: PsiBuilder, level: Int, keyword: String): Boolean {
        return KeywordParser(keyword).parse(builder, level)
    }

    @JvmStatic
    fun parseLabeledDoConstruct(builder: PsiBuilder, level: Int): Boolean {
        return LabeledDoConstructParser().parse(builder, level)
    }

    fun getUnwrappedTokenType(base: IElementType?): IElementType? {
        var unwrappedType = base
        while (unwrappedType is ForeignLeafType) {
            unwrappedType = unwrappedType.delegate
        }
        return unwrappedType
    }

    fun cloneTTwithBase(oldType: IElementType?, newType: IElementType): IElementType {
        return if (oldType is FortranIncludeForeignLeafType) {
            val newDelegate = cloneTTwithBase(oldType.delegate, newType)
            FortranIncludeForeignLeafType(newDelegate, oldType.tokenText)
        } else {
            newType
        }
    }

    @JvmStatic
    fun nextTokenIs(builder: PsiBuilder, token: IElementType): Boolean =
            GeneratedParserUtilBase.nextTokenIs(builder, token)

    @JvmStatic
    fun nextTokenIs(builder: PsiBuilder, frameName: String, vararg tokens: IElementType): Boolean =
            GeneratedParserUtilBase.nextTokenIs(builder, frameName, *tokens)

    @JvmStatic
    fun nextTokenIsSmart(builder: PsiBuilder, token: IElementType): Boolean =
            GeneratedParserUtilBase.nextTokenIsSmart(builder, token)

    @JvmStatic
    fun nextTokenIsSmart(builder: PsiBuilder, vararg tokens: IElementType): Boolean =
            GeneratedParserUtilBase.nextTokenIsSmart(builder, *tokens)

    @JvmStatic
    fun consumeTokens(builder: PsiBuilder, pin: Int, vararg tokens: IElementType): Boolean =
            GeneratedParserUtilBase.consumeTokens(builder, pin, *tokens)


    @JvmStatic
    fun consumeTokenSmart(builder: PsiBuilder, token: IElementType): Boolean =
            consumeToken(builder, getUnwrappedTokenType(token))

    @JvmStatic
    fun consumeToken(builder: PsiBuilder, token: IElementType?): Boolean =
            GeneratedParserUtilBase.consumeToken(builder, token)

    //Method proxies to GeneratedParserUtil. The only way to override a couple of methods from there

    @JvmField
    val TRUE_CONDITION: Parser = object : Parser {
        override fun parse(builder: PsiBuilder?, level: Int) = true
    }

    @JvmStatic
    fun eof(builder: PsiBuilder, level: Int): Boolean = GeneratedParserUtilBase.eof(builder, level)

    @JvmStatic
    fun current_position_(builder: PsiBuilder): Int = GeneratedParserUtilBase.current_position_(builder)

    @JvmStatic
    fun recursion_guard_(builder: PsiBuilder, level: Int, funcName: String): Boolean =
            GeneratedParserUtilBase.recursion_guard_(builder, level, funcName)

    @JvmStatic
    fun empty_element_parsed_guard_(builder: PsiBuilder, funcName: String, pos: Int): Boolean =
            GeneratedParserUtilBase.empty_element_parsed_guard_(builder, funcName, pos)

    @JvmStatic
    fun create_token_set_(vararg tokenTypes: IElementType): TokenSet =
            GeneratedParserUtilBase.create_token_set_(*tokenTypes)

    @JvmStatic
    fun consumeTokenSmart(builder: PsiBuilder, token: String): Boolean =
            GeneratedParserUtilBase.consumeTokenSmart(builder, token)

    @JvmStatic
    fun consumeToken(builder: PsiBuilder, text: String): Boolean =
            GeneratedParserUtilBase.consumeToken(builder, text)

    @JvmStatic
    fun consumeToken(builder: PsiBuilder, text: String, caseSensitive: Boolean): Boolean =
            GeneratedParserUtilBase.consumeToken(builder, text, caseSensitive)

    @JvmStatic
    fun nextTokenIs(builder: PsiBuilder, tokenText: String): Boolean =
            GeneratedParserUtilBase.nextTokenIs(builder, tokenText)

    // here's the new section API for compact parsers & less IntelliJ platform API exposure
    @JvmField
    val _NONE_ = GeneratedParserUtilBase._NONE_
    @JvmField
    val _COLLAPSE_ = GeneratedParserUtilBase._COLLAPSE_
    @JvmField
    val _LEFT_ = GeneratedParserUtilBase._LEFT_
    @JvmField
    val _LEFT_INNER_ = GeneratedParserUtilBase._LEFT_INNER_
    @JvmField
    val _AND_ = GeneratedParserUtilBase._AND_
    @JvmField
    val _NOT_ = GeneratedParserUtilBase._NOT_
    @JvmField
    val _UPPER_ = GeneratedParserUtilBase._COLLAPSE_

    // simple enter/exit methods pair that doesn't require frame object
    @JvmStatic
    fun enter_section_(builder: PsiBuilder): PsiBuilder.Marker =
            GeneratedParserUtilBase.enter_section_(builder)

    @JvmStatic
    fun exit_section_(builder: PsiBuilder, marker: PsiBuilder.Marker, elementType: IElementType?, result: Boolean) =
            GeneratedParserUtilBase.exit_section_(builder, marker, elementType, result)

    @JvmStatic
    fun enter_section_(builder: PsiBuilder, level: Int, modifiers: Int, frameName: String?): PsiBuilder.Marker =
            GeneratedParserUtilBase.enter_section_(builder, level, modifiers, frameName)

    @JvmStatic
    fun enter_section_(builder: PsiBuilder, level: Int, modifiers: Int): PsiBuilder.Marker =
            GeneratedParserUtilBase.enter_section_(builder, level, modifiers)

    @JvmStatic
    fun enter_section_(builder: PsiBuilder, level: Int, modifiers: Int, elementType: IElementType?, frameName: String?): PsiBuilder.Marker =
            GeneratedParserUtilBase.enter_section_(builder, level, modifiers, elementType, frameName)

    @JvmStatic
    fun exit_section_(builder: PsiBuilder, level: Int, marker: PsiBuilder.Marker, result: Boolean, pinned: Boolean,
                      eatMore: Parser?) =
            GeneratedParserUtilBase.exit_section_(builder, level, marker, result, pinned, eatMore)

    @JvmStatic
    fun exit_section_(builder: PsiBuilder, level: Int, marker: PsiBuilder.Marker, elementType: IElementType?,
                      result: Boolean, pinned: Boolean, eatMore: Parser?) =
            GeneratedParserUtilBase.exit_section_(builder, level, marker, elementType, result, pinned, eatMore)


    @JvmStatic
    fun report_error_(builder: PsiBuilder, result: Boolean): Boolean =
            GeneratedParserUtilBase.report_error_(builder, result)

    @JvmStatic
    fun report_error_(builder: PsiBuilder, state: GeneratedParserUtilBase.ErrorState, advance: Boolean) =
            GeneratedParserUtilBase.report_error_(builder, state, advance)

    @JvmStatic
    fun adapt_builder_(root: IElementType, builder: PsiBuilder, parser: PsiParser): PsiBuilder =
            adapt_builder_(root, builder, parser, null)

    @JvmStatic
    fun adapt_builder_(root: IElementType, builder: PsiBuilder, parser: PsiParser, extendsSets: Array<TokenSet>?): PsiBuilder {
        val state = GeneratedParserUtilBase.ErrorState()
        GeneratedParserUtilBase.ErrorState.initState(state, builder, root, extendsSets)
        return FortranBuilderAdapter(builder, state, parser)
    }

    @JvmStatic
    fun addVariant(builder: PsiBuilder, text: String) =
            GeneratedParserUtilBase.addVariant(builder, text)

    interface Parser : GeneratedParserUtilBase.Parser
}


