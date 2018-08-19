/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.Token
import kotlin.reflect.KClass
import kotlin.reflect.full.staticProperties

class ChunkFinder(private val debug: Boolean = false, private val parserClass: KClass<out Parser>) {

  /**
   * The accumulator that holds the current word
   */
  private var wordAccum = ""

  /**
   * The accumulator the current expression
   */
  private var exprAccum = ""

  /**
   * The offset of the current token
   */
  private var offset = 0

  /**
   * The offset of the current expression
   */
  private var exprOffset = 0

  /**
   * The current parsing status
   */
  private var status = 0

  /**
   * The list of possibile numeric expressions
   */
  private val possibileNumericExpressions = mutableListOf<StringWithOffset>()

  private val staticPropertiesCache = mutableMapOf<String, Int>()

  /**
   * Process a list of tokens, return a list of possible numeric expressions to process through the Antlr grammar and their offsets relative to the original text
   *
   * @param tokens the tokens to process
   *
   * @return List<Pair<Int,String>>
   */
  fun find(tokens: List<Token>): List<StringWithOffset> {

    tokens.forEach { t ->

      debugPrint("Stat: $status Type: ${t.type} Text: '${t.text}' Wacc: '$wordAccum' Eacc: '$exprAccum'")

      when (status) {

        0 -> processTokenStatus0(t)
        1 -> processTokenStatus1(t)
        2 -> processTokenStatus2(t)
      }

      offset += t.text.length
    }

    debugPrint("END Stat: $status Wacc: '$wordAccum' Eacc: '$exprAccum'")

    val tmp = exprAccum + wordAccum

    if (status == 1 && tmp.isNotEmpty()) {

      addPossibleNumericExpression(text = tmp)
    }

    return possibileNumericExpressions
  }

  /**
   * Returns the internal value for a static property of the parser class
   *
   * @param name the property name
   *
   * @return the property value
   */
  private fun getParserStaticMember(name: String): Int {

    if(staticPropertiesCache.containsKey(name)) return staticPropertiesCache[name]!!

    val v = parserClass
      .staticProperties
      .first {it.name == name}
      .get() as Int

    staticPropertiesCache[name] = v

    return v
  }

  /**
   * Returns a list of internal values for the static property of the parser class whose name was provided
   *
   * @param names the names of the inquired properties
   *
   * @return a list of property values
   */
  private fun getParserStaticMemberList(vararg names: String): List<Int> = names.map { getParserStaticMember(it) }

  /**
   * Process the current token, we are searching for possible numeric expressions
   *
   * @param t the current token
   */
  private fun processTokenStatus0(t: Token) {

    if (t.type == getParserStaticMember("ANY")) {

      status = 2
    }
    else if (! getParserStaticMemberList("WORDDIV", "WS", "EOL", "EOF").contains(t.type)) {

      wordAccum = t.text
      exprAccum = ""
      status = 1
      exprOffset = offset
    }
  }

  /**
   * Process the current token, we are inside a potential numeric expression
   *
   * @param t the current token
   */
  private fun processTokenStatus1(t: Token) {

    if (t.type == getParserStaticMember("ANY")) {

      addPossibleNumericExpression(text = exprAccum)
      wordAccum = ""
      exprAccum = ""
      status = 2

    } else if (t.type == getParserStaticMember("WS")) {

      exprAccum += wordAccum + t.text
      wordAccum = ""
    }
    else if (getParserStaticMemberList("WORDDIV", "EOL", "EOF").contains(t.type)) {

      exprAccum += wordAccum
      addPossibleNumericExpression(text = exprAccum)
      wordAccum = ""
      exprAccum = ""
      status = 0

    } else {

      wordAccum += t.text
    }
  }

  /**
   * Process the current token, we are looking for the end of the current word to discard it
   *
   * @param t the current token
   */
  private fun processTokenStatus2(t: Token) {

    if (getParserStaticMemberList("WORDDIV", "WS", "EOL").contains(t.type)) {

      status = 0
    }
  }

  private fun addPossibleNumericExpression(text: String) {

    debugPrint("+ $exprOffset: '$text'")

    possibileNumericExpressions.add(StringWithOffset(text = text, offset = exprOffset))

  }

  /**
   * Print a debug message if debug is enabled.
   *
   * @param message the debug message to print
   */
  private fun debugPrint(message: String) {

    if (this.debug) System.err.println(message)
  }
}
