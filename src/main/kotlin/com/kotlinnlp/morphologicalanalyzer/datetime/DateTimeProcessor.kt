/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.*
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.DateTime
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

/**
 * A text processor that recognizes date-time expressions in it.
 *
 * @param language the language in which to analyze the text
 */
class DateTimeProcessor(private val language: Language) {

  companion object {

    /**
     * Associate the available languages to the related lexer [KClass].
     */
    private val lexersMap: Map<Language, KClass<out Lexer>> = mapOf(
      Language.English to LexerEN::class,
      Language.Italian to LexerIT::class,
      Language.French to LexerFR::class,
      Language.Spanish to LexerES::class
    )

    /**
     * The set of available languages.
     */
    val AVAILABLE_LANGUAGES: Set<Language> = this.lexersMap.keys
  }

  /**
   * Get the date-times recognized in a given text.
   *
   * @param text the input text
   * @param tokens the list of tokens that compose the text
   * @param offset the offset of the text in the containing text (default 0)
   *
   * @return a list of date-time objects (empty if none has been found)
   */
  fun findDateTimes(text: String, tokens: List<RealToken>, offset: Int = 0): List<DateTime> {

    return if (text.trim().isNotEmpty()) {

      val listener = DateTimeListener(tokens = tokens, offset = offset)
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
  private fun buildLexer(charStream: CharStream): Lexer {

    val lexerClass: KClass<out Lexer> = lexersMap[this.language]
      ?: throw RuntimeException("Lexer not available for language '${this.language}'")

    return lexerClass.constructors
      .first { it.parameters.size == 1 && it.parameters.single().type.isSubtypeOf(CharStream::class.starProjectedType) }
      .call(charStream)
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