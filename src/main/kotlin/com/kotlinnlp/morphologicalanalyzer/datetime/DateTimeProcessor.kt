/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.*

/**
 * A text processor that recognizes date-time expressions in it.
 *
 * @param langCode ISO 639-2 code of the language in which to analyze the text
 */
class DateTimeProcessor(private val langCode: String) {

  /**
   * Get the date-times recognized in a given text.
   *
   * @param text the input text
   * @param tokens the list of tokens that compose the [text]
   *
   * @return a list of date-time objects (empty if none has been found)
   */
  fun findDateTimes(text: String, tokens: List<RealToken>): List<DateTime> {

    return if (text.trim().isNotEmpty()) {

      val listener = DateTimeListener(tokens)
      val parser: DateTimeParser = this.buildParser(text)

      try {
        ParseTreeWalker().walk(listener, parser.root())
      } catch (e: RuntimeException) {}

      listener.getDateTimes()

    } else {
      listOf()
    }
  }

  /**
   * @param text the input text
   *
   * @return a new ANTLR DateTime parser
   */
  private fun buildParser(text: String): DateTimeParser {

    val lexer = this.buildLexer(charStream = CharStreams.fromString(text))
    val tokenStream = CommonTokenStream(lexer)
    val parser = DateTimeParser(tokenStream)

    return parser.enableSLL()
  }

  /**
   * @param charStream the input char stream
   *
   * @return an ANTLR DateTime lexer for the given language
   */
  private fun buildLexer(charStream: CharStream): Lexer = when (this.langCode) {
    "en" -> LexerEN(charStream)
    "it" -> LexerIT(charStream)
    else -> throw RuntimeException("Lexer not available for language '${this.langCode}'")
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