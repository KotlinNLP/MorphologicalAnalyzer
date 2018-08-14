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

const val bigstr_EN ="""
    The Sun is the star at the center of the Solar System. It is a nearly perfect sphere of hot plasma,[14][15] with internal convective motion that generates a magnetic field via a dynamo process.[16] It is by far the most important source of energy for life on Earth. Its diameter is about 1.39 million three kilometers, i.e. 109 times that of Earth, and its mass is about 330,000 times that of Earth, accounting for about 99.86% of the total mass of the Solar System.[17] About three quarters of the Sun's mass consists of one thousand five hydrogen (~73%); the rest is mostly helium (~25%), with much smaller quantities of heavier elements, including oxygen, carbon, neon, and iron.[18]

The Sun is a G-type main-sequence star (G2V) based on its spectral class. As such, it is informally and not completely accurately referred to as a yellow dwarf (its light is closer to white than yellow). It formed approximately 4.6 billion twelve [a][10][19] years ago from the gravitational collapse of matter within a region of a large molecular cloud. Most of this matter gathered in the center, whereas the rest flattened into an orbiting disk that became the Solar System. The central mass became so hot and dense that it eventually initiated nuclear fusion in its core. It is thought that almost all stars form by this process.

The Sun is roughly middle-aged; it has not changed dramatically for more than four billion[a] years, and will remain fairly stable for more than another five billion years. It currently fuses about 600 million tons of hydrogen into helium every second, converting 4 million tons of matter into energy every second as a result. This energy, which can take between 10,000 and 170,000 years to escape from its core, is the source of the Sun's light and heat. In about 5 billion years, when hydrogen fusion in its core has diminished to the point at which the Sun is no longer in hydrostatic equilibrium, the core of the Sun will experience a marked increase in density and temperature while its outer layers expand to eventually become a red giant. It is calculated that the Sun will become sufficiently large to engulf the current orbits of Mercury and Venus, and render Earth uninhabitable. After this, it will shed its outer layers and become a dense type of cooling star known as a white dwarf, which no longer produces energy by fusion, but still glows and gives off heat from its previous fusion.
"""

const val bigstr_IT = """ Il Sole ripreso in falsi colori dal Solar Dynamics Observatory della NASA nella banda dell'ultravioletto.
Il Sole è la stella madre del sistema solare, e di gran lunga il suo principale componente. La sua grande massa gli permette di sostenere la fusione nucleare, che rilascia enormi quantità di energia, per la maggior parte irradiata nello spazio come radiazione elettromagnetica, in particolare luce visibile.

Il Sole viene classificato come una nana gialla, anche se come nome è ingannevole in quanto, rispetto ad altre stelle nella nostra galassia, il Sole è piuttosto grande e luminoso. Le stelle vengono classificate in base al diagramma Hertzsprung-Russell, un grafico che mette in relazione la temperatura effettiva e la luminosità delle stelle. In generale più una stella è calda più è luminosa: le stelle che seguono questo modello sono appartenenti alla sequenza principale, e il sole si trova proprio al centro di questa sequenza. Tuttavia stelle più luminose e calde del Sole sono rare, mentre stelle meno luminose e più fredde sono molto comuni.[35] La luminosità del Sole è in costante crescita, e si è stimato che all'inizio della sua storia aveva soltanto il 75% della luminosità che mostra attualmente.[36]

Il Sole è una stella di I popolazione, ed è nato nelle fasi successive dell'evoluzione dell'Universo. Esso contiene più elementi pesanti dell'idrogeno e dell'elio (metalli) rispetto alle più vecchie stelle di popolazione II.[37] Gli elementi più pesanti dell'idrogeno e dell'elio si formarono nei nuclei di stelle antiche ormai esplose, così la prima generazione di stelle dovette terminare il suo ciclo vitale prima che l'universo potesse essersi arricchito di questi elementi. Le stelle più antiche osservate contengono infatti pochi metalli, mentre quelle di più recente formazione ne sono più ricche. Questa alta metallicità si pensa sia stata cruciale nello sviluppo di un sistema planetario da parte del Sole, poiché i pianeti si formano dall'accumulo di metalli.[38]

Insieme alla luce il Sole irradia un flusso continuo di particelle cariche (plasma), noto anche come vento solare. Questo flusso di particelle si propaga verso l'esterno a circa 1,5 milioni di chilometri all'ora,[39], crea una tenue atmosfera (l'Eliosfera) e permea il sistema solare per almeno 100 UA (cfr. Eliopausa) formando il mezzo interplanetario."""
var execution = 0


fun main(args: Array<String>) {

//  timing()
//  debugging()
//  test_grammar()
//  example()
  timing2()
//  tokens()
}

fun example() {

//  val str = "one dog and two million and one cats"
val str = bigstr_EN

  val numbersProcessor = NumbersProcessor(getLanguageByIso("en"), debug = false)
  // Warming up the Antlr caches
  numbersProcessor.findNumbers(
    text= str,
    tokens = SimpleTokenizer.tokenize(str),
    modality = "SLL+LL"
  )

  val language = getLanguageByIso("en")

//  val res_1: List<Number> = test(str = str, language = language, n = 1, modality = "SLL+LL")!!
  val res_2: List<Number> = test(str = str, language = language, n = 1, modality = "split")!!

//  println("Res1: $res_1")
  println("Res2: $res_2")

  //  println( "The two expressions are ${if(res_1 == res_2) "EQUAL" else "NOT EQUAL"}" )
}

fun tokens(){

  val language = getLanguageByIso("en")
  val str = bigstr_EN

//  test(str = str, language = language, n = 1, modality = "SLL+LL")!!.forEach {
//
//    println(it.original)
  //  }


  test(str = str, language = language, n = 1, modality = "split")!!.forEach {

    println(it.original)
  }
}

fun timing2() {

  //  val str = "one dog and two million and one cats"
  val str = bigstr_EN

  val numbersProcessor = NumbersProcessor(getLanguageByIso("en"), debug = false)
  // Warming up the Antlr caches
  numbersProcessor.findNumbers(
    text= str,
    tokens = SimpleTokenizer.tokenize(str),
    modality = "SLL+LL"
  )

//  val modality = "SLL+LL"
  val modality = "split"
  val language = getLanguageByIso("en")

  test(str = str, language = language, n = 1000, modality = modality)!!
  test(str = str, language = language, n = 1000, modality = modality)!!
  test(str = str, language = language, n = 1000, modality = modality)!!
  test(str = str, language = language, n = 1000, modality = modality)!!

  //  println("Res1: $res_1")
  //  println("Res2: $res_2")

  //  println( "The two expressions are ${if(res_1 == res_2) "EQUAL" else "NOT EQUAL"}" )
}


fun test_grammar(){

  // Warming up the Antlr caches
  val numbersProcessor = NumbersProcessor(getLanguageByIso("en"), debug = true)
  numbersProcessor.findNumbers(
    text= "",
    tokens = SimpleTokenizer.tokenize(""),
    modality = "SLL"
  )

  val strings = listOf("two", "two hundred", "two one", "two hundred one", "two hundred one thousand", "one thousand", "one thousand five")

  strings.forEach{str ->

    print("Testing \"$str\" ")
    var ok = false
    try {
      numbersProcessor.findNumbers(
        text = str,
        tokens = SimpleTokenizer.tokenize(str),
//        modality = "SLL"
        modality = "split"
      )
      ok = true
    }
    catch (ex: Exception) {

      println("FAIL")
    }

    if(ok) println("OK")
  }
}

fun debugging(){

  val str = """two hundred one"""

  val numbersProcessor = NumbersProcessor(getLanguageByIso("en"))

  val res = numbersProcessor.findNumbers(
    text= str,
    tokens = SimpleTokenizer.tokenize(str),
    modality = "SLL"
  )

  println(res)
}

/**
 * Perform timing tests on various texts.
 */
fun timing() {

  val language: Language = getLanguageByIso("it")

  val strings = listOf("""abc 123 def""",bigstr_IT )

  var nstr = 0
  strings.forEach{ str ->

    println("Test string ${++nstr}")
    test(str = str, language = language, n = 1000, modality = "SLL+LL")
    test(str = str, language = language, n = 1000, modality = "SLL+LL")
    test(str = str, language = language, n = 1000, modality = "SLL")
    test(str = str, language = language, n = 1000, modality = "LL")
  }

  println("\nExiting...")
}

fun test(str: String,
         n: Int,
         modality: String,
         language: Language): List<Number>? {

  val numbersProcessor = NumbersProcessor(language)
  var res: List<Number>? = null
  var m = 0

  ++execution

  val executionTime = measureTimeMillis {

    for (j in 1..n) {

      ++m
      res = numbersProcessor.findNumbers(str, SimpleTokenizer.tokenize(str), modality = modality)
    }
  }

  println("Execution $execution Modality: $modality Num: $m Time: $executionTime ms Tokens: ${res?.count()}")

  return res

//  if(res != null) {
//
//    println(res)
//  }
}

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