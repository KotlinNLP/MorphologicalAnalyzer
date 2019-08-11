/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.numbers.grammar.*
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParamsFactory
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerCommon
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerEN
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerFR
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.ListenerIT
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import kotlin.reflect.KClass
import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.misc.ParseCancellationException
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.full.*

/**
 * A text processor to recognize numeric expressions.
 *
 * @param language the language in which to analyze the text
 * @param enableSubexpressions if false do not perform the analysis of numeric sub-expressions (inside other recognized expressions)
 * @param debug if true enables the print of debug messages on stderr
 */
class NumbersProcessor(
  private val language: Language,
  private val enableSubexpressions: Boolean = true,
  private val debug: Boolean = false
) {

  companion object {

    /**
     * Associate the available languages to the related parser [KClass].
     */
    private val parsersMap: Map<Language, KClass<out Parser>> = mapOf(
      Language.English to NumbersENParser::class,
      Language.Italian to NumbersITParser::class,
      Language.French to NumbersFRParser::class
    )

    /**
     * Associate the available languages to the related lexer [KClass].
     */
    private val lexersMap: Map<Language, KClass<out Lexer>> = mapOf(
      Language.English to LexerEN::class,
      Language.Italian to LexerIT::class,
      Language.French to LexerFR::class
    )

    /**
     * Associate the available languages to the related listener [KClass].
     */
    private val listenersMap: Map<Language, KClass<*>> = mapOf(
      Language.English to ListenerEN::class,
      Language.Italian to ListenerIT::class,
      Language.French to ListenerFR::class
    )

    /**
     * The set of available languages.
     */
    val AVAILABLE_LANGUAGES: Set<Language> = this.listenersMap.keys

    /**
     * Check requirements.
     */
    init {
      require(this.parsersMap.keys.containsAll(this.lexersMap.keys))
      require(this.parsersMap.keys.containsAll(this.listenersMap.keys))
    }
  }

  /**
   * Print a debug message if debug is enabled.
   *
   * @param message the debug message to print
   */
  private fun debugPrint(message: String) {

    if (this.debug) System.err.println(message)
  }

  /**
   * Language-specific parameters.
   */
  private val langParams: LanguageParams by lazy { LanguageParamsFactory.factory(language) }

  /**
   * Process the input text searching for numeric expressions.
   *
   * @param text the text to process
   * @param tokens the list of tokens that compose the input text
   * @param mode the Antlr parsing mode to use
   * @param offset the offset of the text in the containing text (default 0)
   *
   * @return the list of number tokens found
   */
  fun findNumbers(text: String,
                  tokens: List<RealToken>,
                  mode: String = "SLL+LL",
                  offset: Int = 0): List<Number>  =
    this.privateFindNumbers(
      text = text,
      tokens = tokens.mapIndexed { i, t -> IndexedValue(index = i, value = t) },
      mode = mode,
      offset = offset)

  /**
   * Process the input text searching for numeric expressions.
   *
   * @param text the text to process
   * @param tokens the list of tokens that compose the input text
   * @param mode the Antlr parsing mode to use
   * @param offset the offset of the text in the containing text (default 0)
   *
   * @return the list of number tokens found
   */
  internal fun privateFindNumbers(text: String,
                                  tokens: List<IndexedValue<RealToken>>,
                                  mode: String = "SLL+LL",
                                  offset: Int = 0): List<Number> {

    var split = false
    var fallback = false
    var sll = false

    when (mode) {
      "SLL" -> sll = true
      "SLL+LL" -> { sll = true; fallback = true }
      "LL" -> sll = false
      "split" -> split = true
      else -> throw IllegalArgumentException("Invalid mode '$mode'")
    }

    return if (text.trim().isNotEmpty()) {

      val listener: ListenerCommon = this.buildListener(tokens = tokens, offset = offset)

      if (split) {

        debugPrint("Executing split parsing")

        findNumbersWithSplitParsing(text = text, tokens = tokens, offset = offset)

      } else {

        val root = try {

          debugPrint("Executing ${if (sll) "SLL" else "LL"} parsing")

          this.buildParseTree(text, SLL = sll)

        } catch (ex: ParseCancellationException) {

          if (fallback) {

            debugPrint("Executing fallback LL")

            this.buildParseTree(text, SLL = false)

          } else {
            throw ex
          }
        }

        ParseTreeWalker().walk(listener as ParseTreeListener, root)

        return listener.getNumbers()
      }
    } else listOf()
  }

  /**
   * Find the numbers using the split parsing strategy
   *
   * @param text the string to be parsed
   * @param tokens the list of tokens that compose the input text
   * @param offset the offset of the text in the containing text
   *
   * @return the list of number tokens found
   */
  private fun findNumbersWithSplitParsing(text: String, tokens: List<IndexedValue<RealToken>>, offset: Int): List<Number> {

    val lexer: Lexer = this.buildLexer(charStream = CharStreams.fromString(text))
    val chunkFinder = ChunkFinder(
      parserClass = parsersMap[this.language]
        ?: throw RuntimeException("Parser not available for language '${this.language}'"),
      debug = this.debug)

    return chunkFinder.find(lexer.allTokens as List<Token>).flatMap { (chunkText, chunkOffset) ->
      privateFindNumbers(text = chunkText, tokens = tokens, offset = offset + chunkOffset)
    }
  }

  /**
   * @param tokens the list of tokens that compose the input text
   * @param offset
   *
   * @return the listener for the current language
   */
  private fun buildListener(tokens: List<IndexedValue<RealToken>>, offset: Int): ListenerCommon {

    val listenerClass: KClass<*> = listenersMap[this.language]
      ?: throw RuntimeException("Listener not available for language '${this.language}'")

    return listenerClass.primaryConstructor!!.call(
      this.langParams,
      this,
      tokens,
      offset,
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
    val parserClass: KClass<out Parser> = parsersMap[this.language]
      ?: throw RuntimeException("Parser not available for language '${this.language}'")
    val parser: Parser = parserClass.constructors
      .first {
        it.parameters.size == 1 && it.parameters.single().type.isSubtypeOf(TokenStream::class.starProjectedType)
      }
      .call(tokensStream)

    if (SLL) parser.enableSLL()

    return try {
      parser::class.declaredMemberFunctions.first { it.name == "root" }.call(parser) as ParserRuleContext // parser.root()
    } catch (e: InvocationTargetException) {
      throw e.targetException // using the reflection the exceptions are wrapped into an InvocationTargetException
    }
  }

  /**
   * @param charStream the input char stream
   *
   * @return an ANTLR Numbers lexer for the given language
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
    this.errorHandler = BailErrorStrategy()

    return this
  }
}