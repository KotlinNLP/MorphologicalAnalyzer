/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.languageparams

import com.beust.klaxon.Klaxon
import java.io.File
import java.nio.file.Paths

/**
 * The [LanguageParams] factory.
 */
internal object LanguageParamsFactory {

  /**
   * Create a [LanguageParams] object.
   *
   * @param language the ISO 639-2 code of the language for which to provide the params
   *
   * @return a [LanguageParams] object populated with the values specific for the given language
   */
  fun factory(language: String): LanguageParams = try {

    Klaxon().parse<LanguageParams>(getJsonResource(language))!!

  } catch (e: java.io.FileNotFoundException) {

    throw TODO("Language '%s' is not implemented".format(language))
  }

  /**
   * Returns a [File] object that points to the json params file for the language requested.
   *
   * @param language the two letters ISO code of the language for which to provide the params object
   *
   * @return the pointer to the json params file for the given language
   */
  private fun getJsonResource(language: String): String {

    val resPath: String = Paths.get("/", "numbers", language, "langparams.json").toString()

    return LanguageParamsFactory.javaClass.getResource(resPath).readText()
  }
}
