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


fun main(args: Array<String>) {

  timing()
//  debugging()
  test_grammar()
}

fun test_grammar(){

  // Warming up the Antlr caches
  val numbersProcessor = NumbersProcessor(getLanguageByIso("en"))
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
        modality = "SLL"
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

  val strings = listOf("""abc 123 def""",
                       """ Il Sole ripreso in falsi colori dal Solar Dynamics Observatory della NASA nella banda dell'ultravioletto.
Il Sole è la stella madre del sistema solare, e di gran lunga il suo principale componente. La sua grande massa gli permette di sostenere la fusione nucleare, che rilascia enormi quantità di energia, per la maggior parte irradiata nello spazio come radiazione elettromagnetica, in particolare luce visibile.

Il Sole viene classificato come una nana gialla, anche se come nome è ingannevole in quanto, rispetto ad altre stelle nella nostra galassia, il Sole è piuttosto grande e luminoso. Le stelle vengono classificate in base al diagramma Hertzsprung-Russell, un grafico che mette in relazione la temperatura effettiva e la luminosità delle stelle. In generale più una stella è calda più è luminosa: le stelle che seguono questo modello sono appartenenti alla sequenza principale, e il sole si trova proprio al centro di questa sequenza. Tuttavia stelle più luminose e calde del Sole sono rare, mentre stelle meno luminose e più fredde sono molto comuni.[35] La luminosità del Sole è in costante crescita, e si è stimato che all'inizio della sua storia aveva soltanto il 75% della luminosità che mostra attualmente.[36]

Il Sole è una stella di I popolazione, ed è nato nelle fasi successive dell'evoluzione dell'Universo. Esso contiene più elementi pesanti dell'idrogeno e dell'elio (metalli) rispetto alle più vecchie stelle di popolazione II.[37] Gli elementi più pesanti dell'idrogeno e dell'elio si formarono nei nuclei di stelle antiche ormai esplose, così la prima generazione di stelle dovette terminare il suo ciclo vitale prima che l'universo potesse essersi arricchito di questi elementi. Le stelle più antiche osservate contengono infatti pochi metalli, mentre quelle di più recente formazione ne sono più ricche. Questa alta metallicità si pensa sia stata cruciale nello sviluppo di un sistema planetario da parte del Sole, poiché i pianeti si formano dall'accumulo di metalli.[38]

Insieme alla luce il Sole irradia un flusso continuo di particelle cariche (plasma), noto anche come vento solare. Questo flusso di particelle si propaga verso l'esterno a circa 1,5 milioni di chilometri all'ora,[39], crea una tenue atmosfera (l'Eliosfera) e permea il sistema solare per almeno 100 UA (cfr. Eliopausa) formando il mezzo interplanetario."""
  )

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
         language: Language) {

  val numbersProcessor = NumbersProcessor(language)
  var res: List<Number>? = null
  var m = 0

  val executionTime = measureTimeMillis {

    for (j in 1..n) {

      ++m
      res = numbersProcessor.findNumbers(str, SimpleTokenizer.tokenize(str), modality = modality)
    }
  }

  println("Execution $modality Num: $m Time: $executionTime ms Tokens: ${res?.count()}")

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