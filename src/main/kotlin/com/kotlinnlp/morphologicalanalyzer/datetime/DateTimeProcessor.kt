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
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.*

/**
 * A text processor that recognizes date-time expressions in it.
 */
object DateTimeProcessor {

  /**
   * The min size of a chunk to be processed.
   */
  private const val MIN_CHUNK_SIZE = 3

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

      val parser: DateTimeParser = this.buildParser(text = text, langCode = langCode)
      val listener = DateTimeListener(tokens)

      ParseTreeWalker().walk(listener, parser.root())

      listener.getDateTimes()

    } else {
      listOf()
    }
  }

  /**
   *
   */
  fun getDateTimesByChunks(text: String, tokens: List<Token>, langCode: String): List<DateTime> {

    val chunks: List<String> = this.getChunks(text = text, langCode = langCode)

    return chunks.flatMap { this.getDateTimes(text = it, tokens = tokens, langCode = langCode) }
  }

  /**
   *
   */
  private fun getChunks(text: String, langCode: String): List<String> {

    val chunks: MutableList<String> = mutableListOf()
    var chunkStart = 0
    var chunkEnd = -1

    this.forEachLexerTokensPosition(text = text, langCode = langCode) { (start, end) ->

      if (start != chunkEnd + 1) {
        chunks.add(text.substring(chunkStart, chunkEnd + 1))
        chunkStart = start
      }

      chunkEnd = end
    }

    return chunks.map { it.trim() }.filter { it.length >= MIN_CHUNK_SIZE }
  }

  /**
   *
   */
  private fun forEachLexerTokensPosition(text: String, langCode: String, callback: (Pair<Int, Int>) -> Unit) {

    this.buildLexer(charStream = CharStreams.fromString(text), langCode = langCode).allTokens
      .filter { it.type != DateTimeParser.OTHER_TOKEN }
      .forEach { callback(Pair(it.startIndex, it.stopIndex)) }
  }

  /**
   * @param text the input text
   * @param langCode the iso-a2 code of the language in which to analyze the text
   *
   * @return a new ANTLR DateTime parser
   */
  private fun buildParser(text: String, langCode: String): DateTimeParser {

    val lexer = this.buildLexer(charStream = CharStreams.fromString(text), langCode = langCode)
    val tokenStream = CommonTokenStream(lexer)
    val parser = DateTimeParser(tokenStream)

    parser.interpreter.predictionMode = PredictionMode.SLL
    parser.removeErrorListeners()

    return parser
  }

  /**
   * @param charStream the input char stream
   * @param langCode the language iso-a2 code
   *
   * @return an ANTLR DateTime lexer for the given language
   */
  private fun buildLexer(charStream: CharStream, langCode: String): Lexer = when (langCode) {
    "en" -> LexerEN(charStream)
    "it" -> LexerIT(charStream)
    else -> throw RuntimeException("Lexer not available for language '$langCode'")
  }
}