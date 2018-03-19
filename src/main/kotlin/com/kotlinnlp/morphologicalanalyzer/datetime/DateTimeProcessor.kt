/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import com.kotlinnlp.neuraltokenizer.Token
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.*

/**
 * A text processor that recognizes date-time expressions in it.
 */
object DateTimeProcessor {

  /**
   * Get the date-times recognized in a given text.
   *
   * @param text the input text
   * @param tokens the list of tokens that compose the [text]
   * @param langCode the iso-a2 code of the language in which to analyze the text
   *
   * @return a list of date-time objects (empty if none has been found)
   */
  fun getDateTimes(text: String, tokens: List<Token>, langCode: String): List<DateTime> {

    return if (text.trim().isNotEmpty()) {

      val charStream: CharStream = CharStreams.fromString(text)
      val lexer = this.buildLexer(charStream = charStream, langCode = langCode)
      val tokenStream = CommonTokenStream(lexer)
      val parser = DateTimeParser(tokenStream)
      val tree: RuleNode = parser.root()
      val walker = ParseTreeWalker()
      val listener = DateTimeListener(tokens)

      walker.walk(listener, tree)

      listener.getDateTimes()

    } else {
      listOf()
    }
  }

  /**
   * @param charStream the input char stream
   * @param langCode the language iso-a2 code
   *
   * @return a lexer for the given language
   */
  private fun buildLexer(charStream: CharStream, langCode: String): Lexer = when (langCode) {
    "en" -> LexerEN(charStream)
    "it" -> LexerIT(charStream)
    else -> throw RuntimeException("Lexer not available for language '$langCode'")
  }
}