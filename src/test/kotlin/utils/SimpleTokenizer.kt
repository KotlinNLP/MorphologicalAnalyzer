/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package utils

import com.kotlinnlp.neuraltokenizer.Token

/**
 * A simple text tokenizer by chars.
 */
object SimpleTokenizer {

  /**
   * Tokenize a text by chars.
   *
   * @param text the input text
   *
   * @return the list of tokens
   */
  fun tokenize(text: String): List<Token> = text.mapIndexed { i, c ->
    Token(id = i, form = c.toString(), startAt = i, endAt = i, isSpace = c.isWhitespace())
  }
}