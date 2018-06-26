/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package utils

import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.linguisticdescription.sentence.token.properties.Position

/**
 * A simple text tokenizer by chars.
 */
object SimpleTokenizer {

  /**
   * @property form the token form
   * @property position the position of the token in the original text
   */
  data class Token(override val form: String, override val position: Position) : RealToken

  /**
   * Tokenize a text by chars.
   *
   * @param text the input text
   *
   * @return the list of tokens
   */
  fun tokenize(text: String): List<RealToken> = text.mapIndexed { i, c ->
    Token(form = c.toString(), position = Position(start = i, end = i, index = i))
  }
}