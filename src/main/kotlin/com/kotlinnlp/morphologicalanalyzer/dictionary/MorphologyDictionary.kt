/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.dictionary

import com.beust.klaxon.*
import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.morphologicalanalyzer.dictionary.compressor.MorphologyCompressor
import com.kotlinnlp.utils.Serializer
import com.kotlinnlp.utils.forEachLine
import com.kotlinnlp.utils.getLinesCount
import com.kotlinnlp.utils.progressindicator.ProgressIndicatorBar
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

/**
 * The dictionary of morphologies that maps forms to morphologies.
 *
 * @property language the dictionary language
 * @param allowDefaultProperties allow to assign default values to grammatical properties that are not specified
 */
class MorphologyDictionary(val language: Language, allowDefaultProperties: Boolean) : Serializable {

  companion object {

    /**
     * Private val used to serialize the class (needed by Serializable).
     */
    @Suppress("unused")
    private const val serialVersionUID: Long = 1L

    /**
     * The prefix used to define morphologies by reference.
     */
    const val REF_PREFIX = "REF:"

    /**
     * A regular expression that matches all the alternative apostrophes chars.
     */
    val APOSTROPHES_REGEX = Regex("[‘’´`❜❛]")

    /**
     * Load a [MorphologyDictionary] from the JSONL file with the given [filename].
     *
     * @param filename the morphologies dictionary filename
     * @param language the language of the dictionary (needed to explode accents, default = unknown)
     * @param allowDefaultProperties allow to assign default values to grammatical properties that are not specified
     *                               (default = false)
     * @param verbose whether to print the reading progress (default = true)
     *
     * @return a new Morphology Dictionary
     */
    fun load(filename: String,
             language: Language = Language.Unknown,
             allowDefaultProperties: Boolean = false,
             verbose: Boolean = true): MorphologyDictionary {

      val dictionary = MorphologyDictionary(language = language, allowDefaultProperties = allowDefaultProperties)

      val jsonParser = Parser()
      val progress = ProgressIndicatorBar(getLinesCount(filename))

      forEachLine(filename) { line ->

        val entryObj = jsonParser.parse(StringBuilder(line)) as JsonObject

        dictionary.addEntry(
          forms = getForms(entryObj),
          encodedMorphologies = entryObj.array<JsonObject>("morpho")!!.map {
            dictionary.compressor.encodeMorphology(it)
          })

        if (verbose) progress.tick()
      }

      // Attention: explodeByAccents() must be called before setMultiWords()
      if (AccentsHelper.isLanguageSupported(language)) {
        if (verbose) println("Exploding accentuated forms...")
        dictionary.alternativesCount += AccentsHelper(language = language, verbose = verbose)
          .explodeByAccents(dictionary.morphologyMap)
      }

      if (verbose) println("Setting multi-words expressions...")
      dictionary.setMultiWords()

      return dictionary
    }

    /**
     * Read a [MorphologyDictionary] (serialized) from an input stream and decode it.
     *
     * @param inputStream the [InputStream] from which to read the serialized [MorphologyDictionary]
     *
     * @return the [MorphologyDictionary] read from [inputStream] and decoded
     */
    fun load(inputStream: InputStream): MorphologyDictionary = Serializer.deserialize(inputStream)

    /**
     * @param entryObj the JsonObject of a input file entry
     *
     * @return the list of forms of the given entry
     */
    private fun getForms(entryObj: JsonObject): List<String> = try {

      entryObj.array("form")!!

    } catch (e: ClassCastException) {

      listOf(entryObj.string("form")!!)
    }
  }

  /**
   * The size of the dictionary (number of entries, excluding the references).
   */
  var size: Int = 0
    private set

  /**
   * The number of forms that are alternatives of others.
   */
  var alternativesCount: Int = 0
    private set

  /**
   * The number of multi-words expressions in the dictionary (excluding the references).
   */
  var multiwordsCount: Int = 0
    private set

  /**
   * The compressor of morphologies.
   */
  private val compressor = MorphologyCompressor(allowDefaultProperties)

  /**
   * The map of forms to [Entry] objects.
   */
  private val morphologyMap = mutableMapOf<String, String>()

  /**
   * The list of multi-words.
   */
  private val multiWords = mutableListOf<String>()

  /**
   * The map of single forms to the list of multi-words that they introduce (as indices of [multiWords]).
   */
  private val startMultiWordsMap = mutableMapOf<String, MutableList<Int>>()

  /**
   * The map of forms to the multi-words expressions in which they are involved.
   */
  private val wordsToMultiWords = mutableMapOf<String, MutableList<Int>>()

  /**
   * @param form a form to search in the dictionary
   *
   * @return the [Entry] related to the given [form] if present, otherwise null
   */
  operator fun get(form: String): Entry? {

    val encodedEntry: String? = this.morphologyMap[form.normalize()]?.let {
      if (it.startsWith(REF_PREFIX)) this.morphologyMap[it.removePrefix(REF_PREFIX)] else it
    }

    return if (encodedEntry != null) {

      val forms: List<String> = form.split(" ")
      val morphologies = mutableListOf<Morphology>()

      encodedEntry.split("\t").forEach { it ->
        morphologies.addAll(this.compressor.decodeMorphology(morphologyEntryCodes = it.split(',')))
      }

      Entry(form = form, multipleForm = if (forms.size > 1) forms else null, morphologies = morphologies)

    } else {
      null
    }
  }

  /**
   * Get the multi-words in which the given [word] is involved.
   *
   * @param word a single form to search in the dictionary
   * @param includeAlternatives whether the alternative forms must be included in the returned list (default = true)
   *
   * @return the list of multi-words in which the given [word] is involved (empty if no one is found)
   */
  fun getMultiWordsIncluding(word: String, includeAlternatives: Boolean = true): List<String> =
    this.wordsToMultiWords[word.normalize()]?.let {
      this.indicesToMultiWords(indices = it, includeAlternatives = includeAlternatives)
    } ?: listOf()

  /**
   * Get the multi-words introduced by a given [startWord].
   *
   * @param startWord a single form to search in the dictionary
   * @param includeAlternatives whether the alternative forms must be included in the returned list (default = true)
   *
   * @return the list of multi-words that the given [startWord] introduces (empty if no one is found)
   */
  fun getMultiWordsIntroducedBy(startWord: String, includeAlternatives: Boolean = true): List<String> =
    this.startMultiWordsMap[startWord.normalize()]?.let {
      this.indicesToMultiWords(indices = it, includeAlternatives = includeAlternatives)
    } ?: listOf()

  /**
   * Serialize this [MorphologyDictionary] and write it to an output stream.
   *
   * @param outputStream the [OutputStream] in which to write this serialized [MorphologyDictionary]
   */
  fun dump(outputStream: OutputStream) = Serializer.serialize(this, outputStream)

  /**
   * Add a new entry to the morphology map or add new [encodedMorphologies] to it if already present.
   *
   * @param forms the list of forms of the entry
   * @param encodedMorphologies the encoded morphologies of the entry, given from the [compressor]
   */
  private fun addEntry(forms: List<String>, encodedMorphologies: List<Long>) {

    val uniqueForm: String = forms.joinToString(separator = " ") { it.normalize() }
    val morphologyString: String = encodedMorphologies.joinToString(separator = ",")

    this.morphologyMap[uniqueForm] = if (uniqueForm in this.morphologyMap) {
      this.morphologyMap.getValue(uniqueForm) + "\t" + morphologyString
    } else {
      this.size++
      morphologyString
    }
  }

  /**
   * Set the multi-words into the dictionary.
   *
   * It should be called after the dictionary has been filled.
   */
  private fun setMultiWords() {

    this.morphologyMap.keys
      .filter { it.indexOf(' ') >= 0 }
      .forEach { this.addMultiWord(it) }
  }

  /**
   * Add a multi-words expression with the given [form].
   *
   * @param form the form of the multi-words expression
   */
  private fun addMultiWord(form: String) {

    val words: Set<String> = form.split(' ').toSet() // the set is used for forms containing the same word more times
    val startWord: String = words.first()
    val multiWordIndex: Int = this.multiWords.size

    this.multiWords.add(form)

    if (startWord !in this.startMultiWordsMap) this.startMultiWordsMap[startWord] = mutableListOf()
    this.startMultiWordsMap.getValue(startWord).add(multiWordIndex)

    words.forEach {
      if (it !in this.wordsToMultiWords) this.wordsToMultiWords[it] = mutableListOf()
      this.wordsToMultiWords.getValue(it).add(multiWordIndex)
    }

    if (!this.isReference(form)) this.multiwordsCount++
  }

  /**
   * @param indices a list of indices
   * @param includeAlternatives whether the alternative forms must be included in the returned list
   *
   * @return the list of multi-words expression that are mapped to the given [indices]
   */
  private fun indicesToMultiWords(indices: List<Int>, includeAlternatives: Boolean): List<String> =
    indices
      .map { this.multiWords[it] }
      .let { multiWords -> if (includeAlternatives) multiWords else multiWords.filter { !this.isReference(it) } }

  /**
   * @param form a form of the dictionary
   *
   * @return a boolean indicating whether the given [form] references another one
   */
  private fun isReference(form: String): Boolean = this.morphologyMap.getValue(form).startsWith(REF_PREFIX)

  /**
   * Normalize this string to search it in the dictionary.
   *
   * @return a new normalized string
   */
  private fun String.normalize(): String = this.toLowerCase().replace(APOSTROPHES_REGEX, "'")
}
