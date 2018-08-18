/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeParser
import com.kotlinnlp.morphologicalanalyzer.numbers.grammar.*
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParamsFactory
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerCommon
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerEN
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerIT
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.misc.ParseCancellationException

/**
 * A text processor to recognize numeric expressions.
 *
 * @param language the language in which to analyze the text
 * @param enableSubexpressions if false do not perform the analysis of numeric sub-expressions (inside other recognized expressions)
 * @param debug if true enables the print of debug messages on stderr
 */
class NumbersProcessor(
  language: Language,
  private val enableSubexpressions: Boolean = true,
  private val debug: Boolean = false
) {

  /**
   * Print a debug message if debug is enabled.
   *
   * @param message the debug message to print
   */
  fun debugPrint(message: String) {

    if (this.debug) System.err.println(message)
  }

  /**
   * Language-specific parameters.
   */
  private val langParams: LanguageParams = LanguageParamsFactory.factory(language)

  /**
   * Process the input text searching for numeric expressions.
   *
   * @param text the text to process
   * @param tokens the list of tokens that compose the input text
   * @param modality the Antlr parsing modality to use
   * @param offset the offset of the text in the containing text
   *
   * @return the list of number tokens found
   */
  fun findNumbers(text: String,
                  tokens: List<RealToken>,
                  modality: String = "SLL+LL",
                  offset: Int = 0): List<Number> {

    var split = false
    var fallback = false
    var SLL = false

    if (modality == "SLL") {

      SLL = true

    } else if (modality == "SLL+LL") {

      SLL = true
      fallback = true

    } else if (modality == "LL") {

      SLL = false

    } else if (modality == "split") {

      split = true

    } else throw IllegalArgumentException("Modality '$modality' not implemented")

    return if (text.trim().isNotEmpty()) {

      val listener: ListenerCommon = this.buildListener(tokens)

      if (split) {

        debugPrint("Executing split parsing")

        findNumbersWithSplitParsing(text, tokens)

      } else {

        val root = try{

          debugPrint("Executing ${if (SLL) "SLL" else "LL"} parsing")

          this.buildParseTree(text, SLL = SLL)

        } catch (ex: ParseCancellationException) {

          if (fallback) {

            debugPrint("Executing fallback LL")

            this.buildParseTree(text, SLL = false)

          } else throw ex
        }

        ParseTreeWalker().walk(listener as ParseTreeListener, root)

        val numbers = listener.getNumbers()

        return if( offset > 0 ) numbers.map { it.copy(

          startToken = offset + it.startToken,
          endToken = offset + it.endToken

        )} else numbers
      }
    } else listOf()
  }

  /**
   * Find the numbers using the split parsing strategy
   *
   * @param text the string to be parsed
   * @param tokens the list of tokens that compose the input text
   *
   * @return the list of number tokens found
   */
  private fun findNumbersWithSplitParsing(text: String, tokens: List<RealToken>): List<Number> {

    val lexer: Lexer = this.buildLexer(charStream = CharStreams.fromString(text))

    return ChunkFinder(debug)
      .find(lexer.allTokens as List<Token>)
      .flatMap { findNumbers(text = it.text, tokens = tokens, offset = it.offset) }
  }

  /**
   * @param tokens the list of tokens that compose the input text
   *
   * @return the listener for the current language
   */
  private fun buildListener(tokens: List<RealToken>): ListenerCommon {

    val listenerClass: KClass<*> = when (this.langParams.language) {
      "en" -> ListenerEN::class
      "it" -> ListenerIT::class
      else -> throw RuntimeException("Listener not available for language '${this.langParams.language}'")
    }

    return listenerClass.primaryConstructor!!.call(
      this.langParams,
      this,
      tokens,
//      this.debug,
      false,
      this.enableSubexpressions
    ) as ListenerCommon
  }

  /**
   * @param text the input text
   *
   * @return the parse tree of the given tokens stream
   */
  private fun buildParseTree(text: String, SLL: Boolean): ParserRuleContext {

    val lexer: Lexer = this.buildLexer(charStream = CharStreams.fromString(text))
    val tokensStream = CommonTokenStream(lexer)

    return when (this.langParams.language) {
      "en" -> {
        NumbersENParser(tokensStream).apply { if(SLL) this.enableSLL() }.root()
      }
      "it" -> {
        NumbersITParser(tokensStream).apply { if(SLL) this.enableSLL() }.root()
      }
      else -> throw RuntimeException("Parser not available for language '${this.langParams.language}'")
    }
  }

  /**
   * @param charStream the input char stream
   *
   * @return an ANTLR Numbers lexer for the given language
   */
  private fun buildLexer(charStream: CharStream): Lexer = when (this.langParams.language) {
    "en" -> LexerEN(charStream)
    "it" -> LexerIT(charStream)
    else -> throw RuntimeException("Lexer not available for language '${this.langParams.language}'")
  }

  /**
   * Enable the SSL prediction mode of this parser and return it.
   *
   * @return this parser
   */
  private fun <T: Parser>T.enableSLL(): T {

    this.interpreter.predictionMode = PredictionMode.SLL
    this.removeErrorListeners()
    this.setErrorHandler(BailErrorStrategy())

    return this
  }
}