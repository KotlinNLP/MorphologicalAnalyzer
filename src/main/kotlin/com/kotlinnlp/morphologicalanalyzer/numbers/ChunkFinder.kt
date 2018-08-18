/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

import com.kotlinnlp.morphologicalanalyzer.numbers.grammar.NumbersENParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.atn.TokensStartState

class ChunkFinder(val debug: Boolean = false){

  /**
   * The accumulator that holds the current word
   */
  var wordAccum = ""

  /**
   * The accumulator the current expression
   */
  var exprAccum = ""

  /**
   * The offset of the current token
   */
  var offset = 0

  /**
   * The offset of the current expression
   */
  var exprOffset = 0

  /**
   * The current parsing status
   */
  var status = 0

  /**
   * The list of possibile numeric expressions
   */
  val possibileNumericExpressions = mutableListOf<StringWithOffset>()

  /**
   * Process a list of tokens, return a list of possible numeric expressions to process through the Antlr grammar and their offsets relative to the original text
   *
   * @param tokens the tokens to process
   *
   * @return List<Pair<Int,String>>
   */
  fun find(tokens: List<Token>): List<StringWithOffset> {

    tokens.forEach { t ->

      debugPrint("Stat: $status Type: ${t.type}) Text: '${t.text}' Wacc: '${wordAccum}' Eacc: '${exprAccum}'")

      when (status) {

        0 -> processTokenStatus0(t)
        1 -> processTokenStatus1(t)
        2 -> processTokenStatus2(t)
      }

      offset += t.text.length
    }

    if (status == 1 && exprAccum.isNotEmpty()) {

      addPossibleNumericExpression(text = exprAccum)
    }

    return possibileNumericExpressions
  }

  /**
   * Process the current token, we are searching for possible numeric expressions
   *
   * @param t the current token
   */
  private fun processTokenStatus0(t: Token) {

    if (t.type == NumbersENParser.ANY) {

      status = 2
    }
    else if (! listOf(NumbersENParser.WORDDIV, NumbersENParser.WS, NumbersENParser.EOL, NumbersENParser.EOF ).contains(t.type)) {

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

    if (t.type == NumbersENParser.ANY) {

      addPossibleNumericExpression(text = exprAccum)
      wordAccum = ""
      exprAccum = ""
      status = 2

    } else if (t.type == NumbersENParser.WS) {

      exprAccum += wordAccum + t.text
      wordAccum = ""
    }
    else if (listOf(NumbersENParser.WORDDIV, NumbersENParser.EOL, NumbersENParser.EOF ).contains(t.type)) {

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

    if (listOf(NumbersENParser.WORDDIV, NumbersENParser.WS, NumbersENParser.EOL).contains(t.type)) {

      status = 0
    }
  }

  private fun addPossibleNumericExpression(text: String) {

    debugPrint("+ $offset: $text")

    possibileNumericExpressions.add(StringWithOffset(text = exprAccum, offset = exprOffset))

  }

  /**
   * Print a debug message if debug is enabled.
   *
   * @param message the debug message to print
   */
  fun debugPrint(message: String) {

    if (this.debug) System.err.println(message)
  }
}
