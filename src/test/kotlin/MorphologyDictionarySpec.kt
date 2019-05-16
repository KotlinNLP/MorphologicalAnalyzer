/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.linguisticdescription.morphology.morphologies.relations.Adverb
import com.kotlinnlp.linguisticdescription.morphology.morphologies.relations.Preposition
import com.kotlinnlp.linguisticdescription.morphology.morphologies.relations.Verb
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Article
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Noun
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Pronoun
import com.kotlinnlp.linguisticdescription.morphology.properties.*
import com.kotlinnlp.linguisticdescription.morphology.properties.Number
import com.kotlinnlp.morphologicalanalyzer.dictionary.Entry
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 *
 */
class MorphologyDictionarySpec : Spek({

  describe("a MorphologyDictionary") {

    val dictionary: MorphologyDictionary = MorphologyDictionary.load(
      filename = MorphologyDictionary::class.java.classLoader.getResource("test_dictionary.jsonl").file,
      language = Language.Italian)

    context("loading") {

      it("should have the expected size") {
        assertEquals(6, dictionary.size)
      }

      it("should have the expected multi-words count") {
        assertEquals(2, dictionary.multiwordsCount)
      }

      it("should have the expected alternative forms count") {
        assertEquals(6, dictionary.alternativesCount)
      }

      it("should contain the 'form1' form") {
        assertNotNull(dictionary["form1"])
      }

      it("should contain the 'form2' form") {
        assertNotNull(dictionary["form2"])
      }

      it("should contain the 'form3.1 form3.2 form3.3' form") {
        assertNotNull(dictionary["form3.1 form3.2 form3.3"])
      }

      it("should contain the 'form4' form") {
        assertNotNull(dictionary["form4"])
      }

      it("should contain the 'form with_è accentuated' form") {
        assertNotNull(dictionary["form with_è accentuated"])
      }

      it("should contain the 'only_è' form") {
        assertNotNull(dictionary["only_è"])
      }

      it("should return null when trying to get a form not present") {
        assertNull(dictionary["form3"])
      }
    }

    context("forms with one morphology") {

      on("the 'form1' form") {

        val entry: Entry = dictionary["form1"]!!

        it("should have the expected form") {
          assertEquals("form1", entry.form)
        }

        it("should have a null multiple form") {
          assertNull(entry.multipleForm)
        }

        it("should contain 1 morphology") {
          assertEquals(1, entry.morphologies.size)
        }

        it("should contain a Multiple morphology") {
          assertEquals(Morphology.Type.Multiple, entry.morphologies.first().type)
        }

        it("should contain 2 components in its multiple morphology") {
          assertEquals(2, entry.morphologies.first().components.size)
        }

        it("should contain the expected first component of its multiple morphology") {
          assertEquals(Preposition.Base(lemma = "lemma1", oov = false), entry.morphologies.first().components[0])
        }

        it("should contain the expected second component of its multiple morphology") {
          assertEquals(
            Article.Base(
              lemma = "lemma2",
              oov = false,
              gender = Gender.Feminine,
              number = Number.Plural,
              case = GrammaticalCase.Accusative
            ),
            entry.morphologies.first().components[1]
          )
        }
      }
    }

    context("forms with more morphologies") {

      on("the 'form2' form") {

        val entry: Entry = dictionary["form2"]!!

        it("should have the expected form") {
          assertEquals("form2", entry.form)
        }

        it("should have a null multiple form") {
          assertNull(entry.multipleForm)
        }

        it("should contain 2 morphologies") {
          assertEquals(2, entry.morphologies.size)
        }

        it("should contain a first Single morphology") {
          assertEquals(Morphology.Type.Single, entry.morphologies[0].type)
        }

        it("should contain the first expected morphology") {
          assertEquals(
            Noun.Common.Base(
              lemma = "lemma3",
              oov = false,
              case = GrammaticalCase.Unknown,
              degree = Degree.Base,
              gender = Gender.Masculine,
              number = Number.Singular,
              person = Person.Third
            ),
            entry.morphologies[0].components.first()
          )
        }

        it("should contain a second Single morphology") {
          assertEquals(Morphology.Type.Single, entry.morphologies[1].type)
        }

        it("should contain the second expected morphology") {
          assertEquals(
            Verb.Base(
              lemma = "lemma4",
              oov = false,
              gender = Gender.Feminine,
              number = Number.Singular,
              person = Person.Third,
              mood = Mood.Indicative,
              tense = Tense.Present
            ),
            entry.morphologies[1].components.first()
          )
        }
      }
    }

    context("multiple forms (multi-words expressions)") {

      on("the 'form3.1 form3.2 form3.3' form") {

        val entry: Entry = dictionary["form3.1 form3.2 form3.3"]!!

        it("should have the expected form") {
          assertEquals("form3.1 form3.2 form3.3", entry.form)
        }

        it("should have the expected multiple form") {
          assertEquals(listOf("form3.1", "form3.2", "form3.3"), entry.multipleForm)
        }

        it("should contain 1 morphology") {
          assertEquals(1, entry.morphologies.size)
        }

        it("should contain a Single morphology") {
          assertEquals(Morphology.Type.Single, entry.morphologies.first().type)
        }

        it("should contain the expected morphology") {
          assertEquals(
            Preposition.Base(lemma = "lemma5 lemma6 lemma7", oov = false),
            entry.morphologies.first().components[0]
          )
        }
      }
    }

    context("forms with morphology containing multiple (exploding) properties") {

      on("the 'form4' form") {

        val entry: Entry = dictionary["form4"]!!

        it("should have the expected form") {
          assertEquals("form4", entry.form)
        }

        it("should have a null multiple form") {
          assertNull(entry.multipleForm)
        }

        it("should contain 4 morphologies") {
          assertEquals(4, entry.morphologies.size)
        }

        it("should contain Multiple morphologies") {
          assertTrue { entry.morphologies.all { it.type == Morphology.Type.Multiple } }
        }

        it("should contain 2 components in its multiple morphologies") {
          assertTrue { entry.morphologies.all { it.components.size == 2 } }
        }

        it("should contain the expected first entry of its first morphology") {
          assertEquals(
            Verb.Base(
              lemma = "lemma8",
              oov = false,
              gender = Gender.Undefined,
              number = Number.Singular,
              person = Person.Third,
              mood = Mood.Indicative,
              tense = Tense.Present
            ),
            entry.morphologies[0].components[0]
          )
        }

        it("should contain the expected second component of its first morphology") {
          assertEquals(
            Pronoun.Base(
              lemma = "lemma9",
              oov = false,
              gender = Gender.Masculine,
              number = Number.Singular,
              person = Person.Third,
              case = GrammaticalCase.Dative
            ),
            entry.morphologies[0].components[1]
          )
        }

        it("should contain the expected first component of its second morphology") {
          assertEquals(
            Verb.Base(
              lemma = "lemma8",
              oov = false,
              gender = Gender.Undefined,
              number = Number.Singular,
              person = Person.Third,
              mood = Mood.Indicative,
              tense = Tense.Present
            ),
            entry.morphologies[1].components[0]
          )
        }

        it("should contain the expected second component of its second morphology") {
          assertEquals(
            Pronoun.Base(
              lemma = "lemma9",
              oov = false,
              gender = Gender.Masculine,
              number = Number.Plural,
              person = Person.Third,
              case = GrammaticalCase.Dative
            ),
            entry.morphologies[1].components[1]
          )
        }

        it("should contain the expected first component of its third morphology") {
          assertEquals(
            Verb.Base(
              lemma = "lemma8",
              oov = false,
              gender = Gender.Undefined,
              number = Number.Singular,
              person = Person.Third,
              mood = Mood.Subjunctive,
              tense = Tense.Present
            ),
            entry.morphologies[2].components[0]
          )
        }

        it("should contain the expected second component of its third morphology") {
          assertEquals(
            Pronoun.Base(
              lemma = "lemma9",
              oov = false,
              gender = Gender.Masculine,
              number = Number.Singular,
              person = Person.Third,
              case = GrammaticalCase.Dative
            ),
            entry.morphologies[2].components[1]
          )
        }

        it("should contain the expected first component of its fourth morphology") {
          assertEquals(
            Verb.Base(
              lemma = "lemma8",
              oov = false,
              gender = Gender.Undefined,
              number = Number.Singular,
              person = Person.Third,
              mood = Mood.Subjunctive,
              tense = Tense.Present
            ),
            entry.morphologies[3].components[0]
          )
        }

        it("should contain the expected second component of its fourth morphology") {
          assertEquals(
            Pronoun.Base(
              lemma = "lemma9",
              oov = false,
              gender = Gender.Masculine,
              number = Number.Plural,
              person = Person.Third,
              case = GrammaticalCase.Dative
            ),
            entry.morphologies[3].components[1]
          )
        }
      }
    }

    context("forms with accentuated words") {

      on("the 'form with_è accentuated' form") {

        val entry: Entry = dictionary["form with_è accentuated"]!!

        it("should have the expected multiple form") {
          assertEquals(listOf("form", "with_è", "accentuated"), entry.multipleForm)
        }

        it("should contain 1 morphology") {
          assertEquals(1, entry.morphologies.size)
        }

        it("should contain a Single morphology") {
          assertEquals(Morphology.Type.Single, entry.morphologies.first().type)
        }

        it("should contain the expected morphology") {
          assertEquals(
            Adverb.Modal(lemma = "lemma10", degree = Degree.Base, oov = false),
            entry.morphologies.first().components.first())
        }
      }

      on("the querying the alternative forms of 'form with_è accentuated'") {

        val expectedMorpho = listOf(Morphology(Adverb.Modal(lemma = "lemma10", oov = false, degree = Degree.Base)))

        it("should return the same entry when querying `form with_e' accentuated`") {
          assertEquals(
            Entry(
              form = "form with_e' accentuated",
              multipleForm = listOf("form", "with_e'", "accentuated"),
              morphologies = expectedMorpho),
            dictionary["form with_e' accentuated"])
        }

        it("should return the same entry when querying `form with_é accentuated`") {
          assertEquals(
            Entry(
              form = "form with_é accentuated",
              multipleForm = listOf("form", "with_é", "accentuated"),
              morphologies = expectedMorpho),
            dictionary["form with_é accentuated"])
        }
      }

      on("the 'only_è' form") {

        val entry: Entry = dictionary["only_è"]!!

        it("should have the expected form") {
          assertEquals("only_è", entry.form)
        }

        it("should have null multiple form") {
          assertNull(entry.multipleForm)
        }

        it("should contain 1 morphology") {
          assertEquals(1, entry.morphologies.size)
        }

        it("should contain a Single morphology") {
          assertEquals(Morphology.Type.Single, entry.morphologies.first().type)
        }

        it("should contain the expected morphology") {
          assertEquals(
            Adverb.Modal(lemma = "lemma11", oov = false, degree = Degree.Base),
            entry.morphologies.first().components.first())
        }
      }

      on("the querying the alternative forms of 'only_è'") {

        val expectedMorpho = listOf(Morphology(Adverb.Modal(lemma = "lemma11", oov = false, degree = Degree.Base)))

        it("should return the same entry when querying `only_e'`") {
          assertEquals(
            Entry(form = "only_e'", multipleForm = null, morphologies = expectedMorpho),
            dictionary["only_e'"])
        }

        it("should return the same entry when querying `only_é`") {
          assertEquals(
            Entry(form = "only_é", multipleForm = null, morphologies = expectedMorpho),
            dictionary["only_é"])
        }
      }
    }
  }
})
