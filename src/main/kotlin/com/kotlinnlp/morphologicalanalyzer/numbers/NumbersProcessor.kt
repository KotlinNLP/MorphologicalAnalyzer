/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

import com.kotlinnlp.morphologicalanalyzer.numbers.grammar.*
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParamsFactory
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerCommon
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerEN
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerIT
import com.kotlinnlp.neuraltokenizer.Token
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * A text processor to recognize numeric expressions.
 *
 * @param langCode ISO 639-2 code of the language in which to analyze the text
 * @param enableSubexpressions if false do not perform the analysis of numeric sub-expressions (inside other
 *                             recognized expressions)
 * @param debug if true enables the print of debug messages on stderr
 */
class NumbersProcessor(
  langCode: String,
  private val enableSubexpressions: Boolean = true,
  private val debug: Boolean = false
) {

  /**
   * Language-specific parameters.
   */
  private val langParams: LanguageParams = LanguageParamsFactory.factory(langCode)

  /**
   * Process the input text searching for numeric expressions.
   *
   * @param text the text to process
   * @param tokens the list of tokens that compose the input text
   *
   * @return the list of number tokens found
   */
  fun findNumbers(text: String, tokens: List<Token>): List<Number> {

    return if (text.trim().isNotEmpty()) {

      val listener: ListenerCommon = this.buildListener(tokens)

      try {
        ParseTreeWalker().walk(listener as ParseTreeListener, this.buildParseTree(text))
      } catch (e: RuntimeException) {}

      listener.getNumbers()

    } else {
      listOf()
    }
  }

  /**
   * @param tokens the list of tokens that compose the input text
   *
   * @return the listener for the current language
   */
  private fun buildListener(tokens: List<Token>): ListenerCommon {

    val listenerClass: KClass<*> = when (this.langParams.language) {
      "en" -> ListenerEN::class
      "it" -> ListenerIT::class
      else -> throw RuntimeException("Listener not available for language '${this.langParams.language}'")
    }

    return listenerClass.primaryConstructor!!.call(
      this.langParams,
      this,
      tokens,
      this.debug,
      this.enableSubexpressions
    ) as ListenerCommon
  }

  /**
   * @param text the input text
   *
   * @return the parse tree of the given tokens stream
   */
  private fun buildParseTree(text: String): ParserRuleContext {

    val lexer: Lexer = this.buildLexer(charStream = CharStreams.fromString(text))
    val tokensStream = CommonTokenStream(lexer)

    return when (this.langParams.language) {
      "en" -> NumbersENParser(tokensStream).enableSLL().root()
      "it" -> NumbersITParser(tokensStream).enableSLL().root()
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

    return this
  }
}