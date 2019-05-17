/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.languageparams

import com.beust.klaxon.Klaxon
import com.kotlinnlp.linguisticdescription.language.Language
import java.io.File
import java.io.InputStream

/**
 * The [LanguageParams] factory.
 */
internal object LanguageParamsFactory {

  /**
   * Create a [LanguageParams] object.
   *
   * @param language the language for which to provide the params
   *
   * @return a [LanguageParams] object populated with the values specific for the given language
   */
  fun factory(language: Language): LanguageParams = try {

    Klaxon().parse<LanguageParams>(getJsonStream(language))!!

  } catch (e: java.io.FileNotFoundException) {

    throw NotImplementedError("Language '%s' is not implemented".format(language))
  }

  /**
   * @param language the language for which to provide the params object
   *
   * @return the input stream of the JSON file with the params for the given language
   */
  private fun getJsonStream(language: Language): InputStream {

    val resPath: String = sequenceOf("numbers", language.isoCode, "langparams.json")
      .joinToString(prefix = File.separator, separator = File.separator)

    return LanguageParamsFactory.javaClass.getResourceAsStream(resPath)
  }
}
