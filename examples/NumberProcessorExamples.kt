/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.language.getLanguageByIso
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.linguisticdescription.sentence.token.properties.Position
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor
import kotlin.system.measureTimeMillis
import com.kotlinnlp.morphologicalanalyzer.numbers.Number

private const val bigstr_EN = """
    The Sun is the star at the center of the Solar System. It is a nearly perfect sphere of hot plasma,[14][15] with internal convective motion that generates a magnetic field via a dynamo process.[16] It is by far the most important source of energy for life on Earth. Its diameter is about 1.39 million three kilometers, i.e. 109 times that of Earth, and its mass is about 330,000 times that of Earth, accounting for about 99.86% of the total mass of the Solar System.[17] About three quarters of the Sun's mass consists of one thousand five hydrogen (~73%); the rest is mostly helium (~25%), with much smaller quantities of heavier elements, including oxygen, carbon, neon, and iron.[18]

The Sun is a G-type main-sequence star (G2V) based on its spectral class. As such, it is informally and not completely accurately referred to as a yellow dwarf (its light is closer to white than yellow). It formed approximately 4.6 billion twelve [a][10][19] years ago from the gravitational collapse of matter within a region of a large molecular cloud. Most of this matter gathered in the center, whereas the rest flattened into an orbiting disk that became the Solar System. The central mass became so hot and dense that it eventually initiated nuclear fusion in its core. It is thought that almost all stars form by this process.

The Sun is roughly middle-aged; it has not changed dramatically for more than four billion[a] years, and will remain fairly stable for more than another five billion years. It currently fuses about 600 million tons of hydrogen into helium every second, converting 4 million tons of matter into energy every second as a result. This energy, which can take between 10,000 and 170,000 years to escape from its core, is the source of the Sun's light and heat. In about 5 billion years, when hydrogen fusion in its core has diminished to the point at which the Sun is no longer in hydrostatic equilibrium, the core of the Sun will experience a marked increase in density and temperature while its outer layers expand to eventually become a red giant. It is calculated that the Sun will become sufficiently large to engulf the current orbits of Mercury and Venus, and render Earth uninhabitable. After this, it will shed its outer layers and become a dense type of cooling star known as a white dwarf, which no longer produces energy by fusion, but still glows and gives off heat from its previous fusion.
"""

private const val bigstr_SLL_EN = """
    The Sun is the star at the center of the Solar System. It is a nearly perfect sphere of hot plasma,[14][15] with internal convective motion that generates a magnetic field via a dynamo process.[16] It is by far the most important source of energy for life on Earth. Its diameter is about 1.39 million kilometers, i.e. 109 times that of Earth, and its mass is about 330,000 times that of Earth, accounting for about 99.86% of the total mass of the Solar System.[17] About three quarters of the Sun's mass consists of one thousand hydrogen (~73%); the rest is mostly helium (~25%), with much smaller quantities of heavier elements, including oxygen, carbon, neon, and iron.[18]

The Sun is a G-type main-sequence star (G2V) based on its spectral class. As such, it is informally and not completely accurately referred to as a yellow dwarf (its light is closer to white than yellow). It formed approximately 4.6 billion  [a][10][19] years ago from the gravitational collapse of matter within a region of a large molecular cloud. Most of this matter gathered in the center, whereas the rest flattened into an orbiting disk that became the Solar System. The central mass became so hot and dense that it eventually initiated nuclear fusion in its core. It is thought that almost all stars form by this process.

The Sun is roughly middle-aged; it has not changed dramatically for more than four billion[a] years, and will remain fairly stable for more than another five billion years. It currently fuses about 600 million tons of hydrogen into helium every second, converting 4 million tons of matter into energy every second as a result. This energy, which can take between 10,000 and 170,000 years to escape from its core, is the source of the Sun's light and heat. In about 5 billion years, when hydrogen fusion in its core has diminished to the point at which the Sun is no longer in hydrostatic equilibrium, the core of the Sun will experience a marked increase in density and temperature while its outer layers expand to eventually become a red giant. It is calculated that the Sun will become sufficiently large to engulf the current orbits of Mercury and Venus, and render Earth uninhabitable. After this, it will shed its outer layers and become a dense type of cooling star known as a white dwarf, which no longer produces energy by fusion, but still glows and gives off heat from its previous fusion.
"""

private const val bigstr_IT = """ Il Sole ripreso in falsi colori dal Solar Dynamics Observatory della NASA nella banda dell'ultravioletto.
Il Sole è la stella madre del sistema solare, e di gran lunga il suo principale componente. La sua grande massa gli permette di sostenere la fusione nucleare, che rilascia enormi quantità di energia, per la maggior parte irradiata nello spazio come radiazione elettromagnetica, in particolare luce visibile.

Il Sole viene classificato come una nana gialla, anche se come nome è ingannevole in quanto, rispetto ad altre stelle nella nostra galassia, il Sole è piuttosto grande e luminoso. Le stelle vengono classificate in base al diagramma Hertzsprung-Russell, un grafico che mette in relazione la temperatura effettiva e la luminosità delle stelle. In generale più una stella è calda più è luminosa: le stelle che seguono questo modello sono appartenenti alla sequenza principale, e il sole si trova proprio al centro di questa sequenza. Tuttavia stelle più luminose e calde del Sole sono rare, mentre stelle meno luminose e più fredde sono molto comuni.[35] La luminosità del Sole è in costante crescita, e si è stimato che all'inizio della sua storia aveva soltanto il 75% della luminosità che mostra attualmente.[36]

Il Sole è una stella di I popolazione, ed è nato nelle fasi successive dell'evoluzione dell'Universo. Esso contiene più elementi pesanti dell'idrogeno e dell'elio (metalli) rispetto alle più vecchie stelle di popolazione II.[37] Gli elementi più pesanti dell'idrogeno e dell'elio si formarono nei nuclei di stelle antiche ormai esplose, così la prima generazione di stelle dovette terminare il suo ciclo vitale prima che l'universo potesse essersi arricchito di questi elementi. Le stelle più antiche osservate contengono infatti pochi metalli, mentre quelle di più recente formazione ne sono più ricche. Questa alta metallicità si pensa sia stata cruciale nello sviluppo di un sistema planetario da parte del Sole, poiché i pianeti si formano dall'accumulo di metalli.[38]

Insieme alla luce il Sole irradia un flusso continuo di particelle cariche (plasma), noto anche come vento solare. Questo flusso di particelle si propaga verso l'esterno a circa 1,5 milioni di chilometri all'ora,[39], crea una tenue atmosfera (l'Eliosfera) e permea il sistema solare per almeno 100 UA (cfr. Eliopausa) formando il mezzo interplanetario."""

private const val odd_situations = """4 a "3" a4b
a4a
a4
4
4b
b4b
b4a
4a
7
"""

private var testDebug = true
//const val testDebug = false

private var execution = 0

fun main(args: Array<String>) {

  //  timing()
  //  debugging()
  //  test_grammar()
  //  example()
  timing2(bigstr_SLL_EN, language = "en")
  //    tokens(odd_situations, language = "en")
  //    tokens(bigstr_EN, language = "en")
  //    tokens("due miliardi di miliardi and padding after.", language = "it")

  //  test_sll(bigstr_SLL_EN, "en")
}

private fun warmupNumberProcessor(): NumbersProcessor {

  val r = NumbersProcessor(getLanguageByIso("en"), debug = true)

  r.findNumbers(text= "", tokens = SimpleTokenizer.tokenize(""), mode = "SLL")

  return r
}

private fun example() {

  //  val str = "one dog and two million and one cats"
  val str = bigstr_EN

  warmupNumberProcessor()
  val language = getLanguageByIso("en")

  //  val res_1: List<Number> = test(str = str, language = language, n = 1, mode = "SLL+LL")!!
  val res_2: List<Number> = test(str = str, language = language, n = 1, mode = "split")!!

  //  println("Res1: $res_1")
  println("Res2: $res_2")

  //  println( "The two expressions are ${if(res_1 == res_2) "EQUAL" else "NOT EQUAL"}" )
}

private fun tokens(str: String, language: String = "en") {

  val lang = getLanguageByIso(language)

  test(str = str, language = lang, n = 1, mode = "SLL+LL")!!.forEach {

    println("Found (SLL+LL): '${it.original}'")
  }

  test(str = str, language = lang, n = 1, mode = "split")!!.forEach {

    println("Found (split): '${it.original}'")
  }
}

private fun timing2(str: String, language: String) {

  testDebug = false

  warmupNumberProcessor()

  val lang = getLanguageByIso(language)

  var mode = "SLL+LL"

  test(str = str, language = lang, n = 1000, mode = mode)!!
  test(str = str, language = lang, n = 1000, mode = mode)!!
  test(str = str, language = lang, n = 1000, mode = mode)!!
  test(str = str, language = lang, n = 1000, mode = mode)!!

  println("")

  mode = "split"
  test(str = str, language = lang, n = 1000, mode = mode)!!
  test(str = str, language = lang, n = 1000, mode = mode)!!
  test(str = str, language = lang, n = 1000, mode = mode)!!
  test(str = str, language = lang, n = 1000, mode = mode)!!
}

private fun test_grammar() {

  val numbersProcessor = warmupNumberProcessor()

  val strings = listOf("two", "two hundred", "two one", "two hundred one", "two hundred one thousand",
    "one thousand", "one thousand five")

  strings.forEach{str ->

    print("Testing \"$str\" ")
    var ok = false
    try {
      numbersProcessor.findNumbers(text = str, tokens = SimpleTokenizer.tokenize(str), mode = "split")
      ok = true
    } catch (ex: Exception) {
      println("FAIL")
    }

    if(ok) println("OK")
  }
}

private fun test_sll(text: String, language: String) {

  val numbersProcessor = NumbersProcessor(getLanguageByIso(language), debug = testDebug)

  try {
    numbersProcessor.findNumbers(text = text, tokens = SimpleTokenizer.tokenize(text), mode = "SLL")
    println("OK")
  } catch (ex: Exception) {
    println("FAIL")
  }
}

private fun debugging() {

  val str = """two hundred one"""

  val numbersProcessor = NumbersProcessor(getLanguageByIso("en"))
  val res = numbersProcessor.findNumbers(text = str, tokens = SimpleTokenizer.tokenize(str), mode = "SLL")

  println(res)
}

/**
 * Perform timing tests on various texts.
 */
private fun timing() {

  val language: Language = getLanguageByIso("it")
  val strings = listOf("""abc 123 def""",bigstr_IT )
  var nstr = 0

  strings.forEach{ str ->

    println("Test string ${++nstr}")

    test(str = str, language = language, n = 1000, mode = "SLL+LL")
    test(str = str, language = language, n = 1000, mode = "SLL+LL")
    test(str = str, language = language, n = 1000, mode = "SLL")
    test(str = str, language = language, n = 1000, mode = "LL")
  }

  println("\nExiting...")
}

private fun test(str: String, n: Int, mode: String, language: Language): List<Number>? {

  val numbersProcessor = NumbersProcessor(language, debug = testDebug)
  var res: List<Number>? = null
  var m = 0

  ++execution

  val executionTime = measureTimeMillis {

    for (j in 1..n) {
      ++m
      res = numbersProcessor.findNumbers(text = str, tokens = SimpleTokenizer.tokenize(str), mode = mode)
    }
  }

  println("Execution $execution Modality: $mode Num: $m Time: $executionTime ms Tokens: ${res?.count()}")

  return res
}

private object SimpleTokenizer {

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