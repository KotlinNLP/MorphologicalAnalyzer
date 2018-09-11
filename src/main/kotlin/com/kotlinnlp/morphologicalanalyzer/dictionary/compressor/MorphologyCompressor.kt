/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.dictionary.compressor

import com.beust.klaxon.JsonObject
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.linguisticdescription.morphology.POS
import com.kotlinnlp.linguisticdescription.morphology.properties.MorphologyProperty
import com.kotlinnlp.linguisticdescription.morphology.properties.MorphologyPropertyFactory
import com.kotlinnlp.linguisticdescription.InvalidPOS
import com.kotlinnlp.linguisticdescription.morphology.SingleMorphology
import java.io.Serializable

/**
 * A helper that optimizes the memory load of the morphologies, mapping them to indices.
 */
class MorphologyCompressor : Serializable {

  companion object {

    /**
     * Private val used to serialize the class (needed by Serializable).
     */
    @Suppress("unused")
    private const val serialVersionUID: Long = 1L

    /**
     * The multiplier coefficient of the POS index, used to encode a morphology.
     */
    private const val POS_INDEX_COEFF: Int = 1.0e03.toInt()

    /**
     * The multiplier coefficient of the lemma index, used to encode a morphology.
     */
    private const val LEMMA_INDEX_COEFF: Long = 1.0e06.toLong()
  }

  /**
   * The BiMap of unique indices to lemmas.
   */
  private val lemmasBiMap: BiMap<Int, String> = HashBiMap.create()

  /**
   * The BiMap of POS tags associated by index.
   */
  private val indicesToAnnotations: BiMap<Int, String> = HashBiMap.create(
    POS.values().withIndex().associate { (i, it) -> i to it.annotation }
  )

  /**
   * The inverse map of [indicesToAnnotations].
   */
  private val annotationsToIndices: BiMap<String, Int> = this.indicesToAnnotations.inverse()

  /**
   * The BiMap of unique indices to morphology properties.
   */
  private val propertiesBiMap: BiMap<Int, Properties> = HashBiMap.create()

  /**
   * @param morphologyObj a single morphology JSON object
   *
   * @return the index representing the encoding of the [morphologyObj]
   */
  fun encodeMorphology(morphologyObj: JsonObject): Long {

    val posAnnotation: String = morphologyObj.string("type")!!

    // Note: morphologies with POS 'NUM' cannot be put in the dictionary because they have an adding 'numericForm'
    // property. They should be created by the Morphological Analyzer only.
    if (posAnnotation !in this.annotationsToIndices || posAnnotation == "NUM") throw InvalidPOS(posAnnotation)

    val lemmaIndex: Int = this.encodeLemma(morphologyObj.string("lemma")!!)
    val posIndex: Int = this.annotationsToIndices.getValue(posAnnotation)
    val propertiesIndex: Int = this.encodeJSONProperties(morphologyObj.obj("properties")!!)

    return LEMMA_INDEX_COEFF * lemmaIndex + POS_INDEX_COEFF * posIndex + propertiesIndex
  }

  /**
   * Decode an encoded morphology entry.
   *
   * @param morphologyEntryCodes the list of encoded morphologies of an entry (one element for a single morphology)
   *
   * @return the decoded morphology entry
   */
  fun decodeMorphology(morphologyEntryCodes: List<String>): List<Morphology> {

    val tmpEntry: TmpEntry = this.decodeTmpEntry(morphologyEntryCodes)

    return MorphologyExploder(tmpEntry).explodedEntries.map { entry ->
      Morphology(morphologies = entry.morphologies.map {
        SingleMorphology(lemma = it.lemma, pos = it.pos, properties = this.mapProperties(it.properties))
      })
    }
  }

  /**
   * @param lemma a lemma
   *
   * @return the index that encodes the given lemma
   */
  private fun encodeLemma(lemma: String): Int {

    val inversedMap: Map<String, Int> = this.lemmasBiMap.inverse()

    return if (lemma in inversedMap) {
      inversedMap.getValue(lemma)
    } else {
      val index: Int = this.lemmasBiMap.size
      this.lemmasBiMap[index] = lemma
      index
    }
  }

  /**
   * @param propertiesObject the JSON object of the properties of a morphology
   *
   * @return the index that encodes the given properties
   */
  private fun encodeJSONProperties(propertiesObject: JsonObject): Int {

    val properties = Properties(propertiesObject.filter { it.value != null }.map { Pair(it.key, it.value as String) })
    val inversedMap: Map<Properties, Int> = this.propertiesBiMap.inverse()

    return if (properties in inversedMap) {
      inversedMap.getValue(properties)
    } else {
      val index: Int = this.propertiesBiMap.size
      this.propertiesBiMap[index] = properties
      index
    }
  }

  /**
   * Decode an encoded entry to a temporary entry.
   *
   * @param morphologyEntryCodes the list of encoded morphologies of an entry (one element for a single morphology)
   *
   * @return a temporary entry object
   */
  private fun decodeTmpEntry(morphologyEntryCodes: List<String>) = TmpEntry(
    morphologies = morphologyEntryCodes.map { this.decodeTmpMorphology(it) }
  )

  /**
   * Decode an encoded morphology to a temporary morphology.
   *
   * @param morphologyCode an encoded morphology
   *
   * @return a temporary morphology object
   */
  private fun decodeTmpMorphology(morphologyCode: String): TmpMorphology {

    val encodedMorphology: Long = morphologyCode.toLong()

    return TmpMorphology(
      lemma = this.decodeLemma(encodedMorphology),
      pos = this.decodePOS(encodedMorphology),
      properties = this.decodeProperties(encodedMorphology)
    )
  }

  /**
   * @param encodedMorphology an encoded morphology
   *
   * @return the lemma of the given [encodedMorphology]
   */
  private fun decodeLemma(encodedMorphology: Long): String =
    this.lemmasBiMap[(encodedMorphology / LEMMA_INDEX_COEFF).toInt()]!!

  /**
   * @param encodedMorphology an encoded morphology
   *
   * @return the POS of the given [encodedMorphology]
   */
  private fun decodePOS(encodedMorphology: Long): POS {

    val posRemainder: Int = (encodedMorphology % LEMMA_INDEX_COEFF).toInt()
    val posAnnotation: String = this.indicesToAnnotations.getValue(posRemainder / POS_INDEX_COEFF)

    return POS.byAnnotation(posAnnotation)
  }

  /**
   * @param encodedMorphology an encoded morphology
   *
   * @return the properties of the given [encodedMorphology]
   */
  private fun decodeProperties(encodedMorphology: Long): Properties =
    this.propertiesBiMap.getValue((encodedMorphology % POS_INDEX_COEFF).toInt())

  /**
   * @param properties the properties object of a morphology
   *
   * @return a map of properties names to [MorphologyProperty] objects
   */
  private fun mapProperties(properties: Properties): Map<String, MorphologyProperty> =
    properties.list.associate {
      Pair(
        it.first,
        MorphologyPropertyFactory(propertyName = it.first, valueAnnotation = it.second)
      )
    }
}
