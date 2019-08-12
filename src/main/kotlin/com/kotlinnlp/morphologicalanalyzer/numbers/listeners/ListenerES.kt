/** Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.listeners

import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor
import com.kotlinnlp.morphologicalanalyzer.numbers.grammar.NumbersESBaseListener
import com.kotlinnlp.morphologicalanalyzer.numbers.grammar.NumbersESParser
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers.ListenerCommonHelper
import org.antlr.v4.runtime.ParserRuleContext

/**
* Listen to ANTLR parser events and handle them.
* Specific for Spanish language (es).
*
* @property langParams language-specific parameters
* @property processor a numbers processor to process sub-expressions
* @property tokens the list of tokens that compose the input text
* @property offset the offset of the input text in the containing text
* @property debug if true it enables the print of debug messages on stderr
* @property enableSubexpressions if false do not perform the analysis of numeric sub-expressions (inside other
*                                recognized expressions)
*/
@Suppress("FunctionName") // for the automatically generated rule methods
internal class ListenerES(
  override val langParams: LanguageParams,
  override val processor: NumbersProcessor,
  override val tokens: List<IndexedValue<RealToken>>,
  override val offset: Int,
  override val debug: Boolean = false,
  override val enableSubexpressions: Boolean = true
): NumbersESBaseListener(), ListenerCommon {

  /**
   * Helper for the listener.
   */
  override val helper = ListenerCommonHelper(langParams)

  /**
   * The listener of the 'exit base_number' event.
   *
   * @param ctx the context of the 'base_number' rule just parsed
   */
  override fun exitBase_number(ctx: NumbersESParser.Base_numberContext?) =
    super<ListenerCommon>.exitBase_number(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit number' event.
   *
   * @param ctx the context of the 'number' rule just parsed
   */
  override fun exitNumber(ctx: NumbersESParser.NumberContext) =
    super<ListenerCommon>.exitNumber(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_unit' event.
   *
   * @param ctx the context of the 'w_unit' rule just parsed
   */
  override fun exitW_unit(ctx: NumbersESParser.W_unitContext) =
    super<ListenerCommon>.exitW_unit(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit ten_pref' event.
   *
   * @param ctx the context of the 'ten_pref' rule just parsed
   */
  override fun exitTen_pref(ctx: NumbersESParser.Ten_prefContext) =
    super<ListenerCommon>.exitTen_pref(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit d_ten_pref' event.
   *
   * @param ctx the context of the 'd_ten_pref' rule just parsed
   */
  override fun exitD_ten_pref(ctx: NumbersESParser.D_ten_prefContext) =
    super<ListenerCommon>.exitD_ten_pref(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit thousand' event.
   *
   * @param ctx the context of the 'thousand' rule just parsed
   */
  override fun exitThousand(ctx: NumbersESParser.ThousandContext?) =
    super<ListenerCommon>.exitThousand(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit millions' event.
   *
   * @param ctx the context of the 'millions' rule just parsed
   */
  override fun exitMillions(ctx: NumbersESParser.MillionsContext?) =
    super<ListenerCommon>.exitMillions(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit millions_prefix' event.
   *
   * @param ctx the context of the 'millions_prefix' rule just parsed
   */
  override fun exitMillions_prefix(ctx: NumbersESParser.Millions_prefixContext?) =
    super<ListenerCommon>.exitMillions_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_million' event.
   *
   * @param ctx the context of the 'one_million' rule just parsed
   */
  override fun exitOne_million(ctx: NumbersESParser.One_millionContext?) =
    super<ListenerCommon>.exitOne_million(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_million_prefix' event.
   *
   * @param ctx the context of the 'one_million_prefix' rule just parsed
   */
  override fun exitOne_million_prefix(ctx: NumbersESParser.One_million_prefixContext?) =
    super<ListenerCommon>.exitOne_million_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit billions_prefix' event.
   *
   * @param ctx the context of the 'billions_prefix' rule just parsed
   */
  override fun exitBillions_prefix(ctx: NumbersESParser.Billions_prefixContext?) =
    super<ListenerCommon>.exitBillions_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit billions' event.
   *
   * @param ctx the context of the 'billions' rule just parsed
   */
  override fun exitBillions(ctx: NumbersESParser.BillionsContext) =
    super<ListenerCommon>.exitBillions(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit trillions' event.
   *
   * @param ctx the context of the 'trillions' rule just parsed
   */
  override fun exitTrillions(ctx: NumbersESParser.TrillionsContext?) =
    super<ListenerCommon>.exitTrillions(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit trillions_prefix' event.
   *
   * @param ctx the context of the 'trillions_prefix' rule just parsed
   */
  override fun exitTrillions_prefix(ctx: NumbersESParser.Trillions_prefixContext?) =
    super<ListenerCommon>.exitTrillions_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit quadrillions' event.
   *
   * @param ctx the context of the 'quadrillions' rule just parsed
   */
  override fun exitQuadrillions(ctx: NumbersESParser.QuadrillionsContext?) =
    super<ListenerCommon>.exitQuadrillions(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit quadrillions_prefix' event.
   *
   * @param ctx the context of the 'quadrillions_prefix' rule just parsed
   */
  override fun exitQuadrillions_prefix(ctx: NumbersESParser.Quadrillions_prefixContext?) =
    super<ListenerCommon>.exitQuadrillions_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_billion' event.
   *
   * @param ctx the context of the 'one_billion' rule just parsed
   */
  override fun exitOne_billion(ctx: NumbersESParser.One_billionContext?) =
    super<ListenerCommon>.exitOne_billion(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_billion_prefix' event.
   *
   * @param ctx the context of the 'one_billion_prefix' rule just parsed
   */
  override fun exitOne_billion_prefix(ctx: NumbersESParser.One_billion_prefixContext?) =
    super<ListenerCommon>.exitOne_billion_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_trillion' event.
   *
   * @param ctx the context of the 'one_trillion' rule just parsed
   */
  override fun exitOne_trillion(ctx: NumbersESParser.One_trillionContext?) =
    super<ListenerCommon>.exitOne_trillion(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_trillion_prefix' event.
   *
   * @param ctx the context of the 'one_trillion_prefix' rule just parsed
   */
  override fun exitOne_trillion_prefix(ctx: NumbersESParser.One_trillion_prefixContext?) =
    super<ListenerCommon>.exitOne_trillion_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_quadrillion' event.
   *
   * @param ctx the context of the 'one_quadrillion' rule just parsed
   */
  override fun exitOne_quadrillion(ctx: NumbersESParser.One_quadrillionContext?) =
    super<ListenerCommon>.exitOne_quadrillion(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit one_quadrillion_prefix' event.
   *
   * @param ctx the context of the 'one_quadrillion_prefix' rule just parsed
   */
  override fun exitOne_quadrillion_prefix(ctx: NumbersESParser.One_quadrillion_prefixContext?) =
    super<ListenerCommon>.exitOne_quadrillion_prefix(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_1_999k' event.
   *
   * @param ctx the context of the 'n_1_999k' rule just parsed
   */
  override fun exitN_1_999k(ctx: NumbersESParser.N_1_999kContext?) =
    super<ListenerCommon>.exitN_1_999k(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_1_999m' event.
   *
   * @param ctx the context of the 'n_1_999m' rule just parsed
   */
  override fun exitN_1_999m(ctx: NumbersESParser.N_1_999mContext?) =
    super<ListenerCommon>.exitN_1_999m(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_1_999i' event.
   *
   * @param ctx the context of the 'n_1_999i' rule just parsed
   */
  override fun exitN_1_999i(ctx: NumbersESParser.N_1_999iContext?) =
    super<ListenerCommon>.exitN_1_999i(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_1_999b' event.
   *
   * @param ctx the context of the 'n_1_999b' rule just parsed
   */
  override fun exitN_1_999b(ctx: NumbersESParser.N_1_999bContext?) =
    super<ListenerCommon>.exitN_1_999b(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit thousand_pref' event.
   *
   * @param ctx the context of the 'thousand_pref' rule just parsed
   */
  override fun exitThousand_pref(ctx: NumbersESParser.Thousand_prefContext) =
    super<ListenerCommon>.exitThousand_pref(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_10_99' event.
   *
   * @param ctx the context of the 'n_10_99' rule just parsed
   */
  override fun exitN_10_99(ctx: NumbersESParser.N_10_99Context) =
    super<ListenerCommon>.exitN_10_99(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit hundredthousand' event.
   *
   * @param ctx the context of the 'hundredthousand' rule just parsed
   */
  override fun exitHundredthousand(ctx: NumbersESParser.HundredthousandContext?) =
    super<ListenerCommon>.exitHundredthousand(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit hundred' event.
   *
   * @param ctx the context of the 'hundred' rule just parsed
   */
  override fun exitHundred(ctx: NumbersESParser.HundredContext) =
    super<ListenerCommon>.exitHundred(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_1_99' event.
   *
   * @param ctx the context of the 'n_1_99' rule just parsed
   */
  override fun exitN_1_99(ctx: NumbersESParser.N_1_99Context?) =
    super<ListenerCommon>.exitN_1_99(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit n_1_999' event.
   *
   * @param ctx the context of the 'n_1_999' rule just parsed
   */
  override fun exitN_1_999(ctx: NumbersESParser.N_1_999Context?) =
    super<ListenerCommon>.exitN_1_999(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit digit_number' event.
   *
   * @param ctx the context of the 'digit_number' rule just parsed
   */
  override fun exitDigit_number(ctx: NumbersESParser.Digit_numberContext) =
    super<ListenerCommon>.exitDigit_number(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit max_2_digits' event.
   *
   * @param ctx the context of the 'max_2_digits' rule just parsed
   */
  override fun exitMax_2_digits(ctx: NumbersESParser.Max_2_digitsContext?) =
    super<ListenerCommon>.exitMax_2_digits(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit max_3_digits' event.
   *
   * @param ctx the context of the 'max_3_digits' rule just parsed
   */
  override fun exitMax_3_digits(ctx: NumbersESParser.Max_3_digitsContext?) =
    super<ListenerCommon>.exitMax_3_digits(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit max_6_digits' event.
   *
   * @param ctx the context of the 'max_6_digits' rule just parsed
   */
  override fun exitMax_6_digits(ctx: NumbersESParser.Max_6_digitsContext?) =
    super<ListenerCommon>.exitMax_6_digits(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit max_6_digits_with_div' event.
   *
   * @param ctx the context of the 'max_6_digits_with_div' rule just parsed
   */
  override fun exitMax_6_digits_with_div(ctx: NumbersESParser.Max_6_digits_with_divContext?) =
    super<ListenerCommon>.exitMax_6_digits_with_div(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit max_9_digits' event.
   *
   * @param ctx the context of the 'max_9_digits' rule just parsed
   */
  override fun exitMax_9_digits(ctx: NumbersESParser.Max_9_digitsContext?) =
    super<ListenerCommon>.exitMax_9_digits(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit max_9_digits_with_div' event.
   *
   * @param ctx the context of the 'max_9_digits_with_div' rule just parsed
   */
  override fun exitMax_9_digits_with_div(ctx: NumbersESParser.Max_9_digits_with_divContext?) =
    super<ListenerCommon>.exitMax_9_digits_with_div(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit seq_of_w_unit' event.
   *
   * @param ctx the context of the 'seq_of_w_unit' rule just parsed
   */
  override fun exitSeq_of_w_unit(ctx: NumbersESParser.Seq_of_w_unitContext) =
    super<ListenerCommon>.exitSeq_of_w_unit(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit of_rules' event.
   *
   * @param ctx the context of the 'of_rules' rule just parsed
   */
  override fun exitOf_rules(ctx: NumbersESParser.Of_rulesContext?) =
    super<ListenerCommon>.exitOf_rules(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit prefix_of_rules' event.
   *
   * @param ctx the context of the 'prefix_of_rules' rule just parsed
   */
  override fun exitPrefix_of_rules(ctx: NumbersESParser.Prefix_of_rulesContext?) =
    super<ListenerCommon>.exitPrefix_of_rules(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit of_rules_body' event.
   *
   * @param ctx the context of the 'of_rules_body' rule just parsed
   */
  override fun exitOf_rules_body(ctx: NumbersESParser.Of_rules_bodyContext) =
    super<ListenerCommon>.exitOf_rules_body(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_200' event.
   *
   * @param ctx the context of the 'W_hundred_200' rule just parsed
   */
  override fun exitW_hundred_200(ctx: NumbersESParser.W_hundred_200Context) = super<ListenerCommon>.exitW_hundred_200(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_300' event.
   *
   * @param ctx the context of the 'W_hundred_300' rule just parsed
   */
  override fun exitW_hundred_300(ctx: NumbersESParser.W_hundred_300Context) = super<ListenerCommon>.exitW_hundred_300(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_400' event.
   *
   * @param ctx the context of the 'W_hundred_400' rule just parsed
   */
  override fun exitW_hundred_400(ctx: NumbersESParser.W_hundred_400Context) = super<ListenerCommon>.exitW_hundred_400(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_500' event.
   *
   * @param ctx the context of the 'W_hundred_500' rule just parsed
   */
  override fun exitW_hundred_500(ctx: NumbersESParser.W_hundred_500Context) = super<ListenerCommon>.exitW_hundred_500(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_600' event.
   *
   * @param ctx the context of the 'W_hundred_600' rule just parsed
   */
  override fun exitW_hundred_600(ctx: NumbersESParser.W_hundred_600Context) = super<ListenerCommon>.exitW_hundred_600(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_700' event.
   *
   * @param ctx the context of the 'W_hundred_700' rule just parsed
   */
  override fun exitW_hundred_700(ctx: NumbersESParser.W_hundred_700Context) = super<ListenerCommon>.exitW_hundred_700(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_800' event.
   *
   * @param ctx the context of the 'W_hundred_800' rule just parsed
   */
  override fun exitW_hundred_800(ctx: NumbersESParser.W_hundred_800Context) = super<ListenerCommon>.exitW_hundred_800(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_hundred_900' event.
   *
   * @param ctx the context of the 'W_hundred_900' rule just parsed
   */
  override fun exitW_hundred_900(ctx: NumbersESParser.W_hundred_900Context) = super<ListenerCommon>.exitW_hundred_900(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_1' event.
   *
   * @param ctx the context of the 'w_1' rule just parsed
   */
  override fun exitW_1(ctx: NumbersESParser.W_1Context) = super<ListenerCommon>.exitW_1(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_1_art' event.
   *
   * @param ctx the context of the 'w_1_art' rule just parsed
   */
  override fun exitW_1_art(ctx: NumbersESParser.W_1_artContext) =
    super<ListenerCommon>.exitW_1_art(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_2' event.
   *
   * @param ctx the context of the 'w_2' rule just parsed
   */
  override fun exitW_2(ctx: NumbersESParser.W_2Context) = super<ListenerCommon>.exitW_2(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_3' event.
   *
   * @param ctx the context of the 'w_3' rule just parsed
   */
  override fun exitW_3(ctx: NumbersESParser.W_3Context) = super<ListenerCommon>.exitW_3(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_4' event.
   *
   * @param ctx the context of the 'w_4' rule just parsed
   */
  override fun exitW_4(ctx: NumbersESParser.W_4Context) = super<ListenerCommon>.exitW_4(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_5' event.
   *
   * @param ctx the context of the 'w_5' rule just parsed
   */
  override fun exitW_5(ctx: NumbersESParser.W_5Context) = super<ListenerCommon>.exitW_5(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_6' event.
   *
   * @param ctx the context of the 'w_6' rule just parsed
   */
  override fun exitW_6(ctx: NumbersESParser.W_6Context) = super<ListenerCommon>.exitW_6(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_7' event.
   *
   * @param ctx the context of the 'w_7' rule just parsed
   */
  override fun exitW_7(ctx: NumbersESParser.W_7Context) = super<ListenerCommon>.exitW_7(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_8' event.
   *
   * @param ctx the context of the 'w_8' rule just parsed
   */
  override fun exitW_8(ctx: NumbersESParser.W_8Context) = super<ListenerCommon>.exitW_8(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_9' event.
   *
   * @param ctx the context of the 'w_9' rule just parsed
   */
  override fun exitW_9(ctx: NumbersESParser.W_9Context) = super<ListenerCommon>.exitW_9(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_0' event.
   *
   * @param ctx the context of the 'w_0' rule just parsed
   */
  override fun exitW_0(ctx: NumbersESParser.W_0Context) = super<ListenerCommon>.exitW_0(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_10' event.
   *
   * @param ctx the context of the 'w_10' rule just parsed
   */
  override fun exitW_10(ctx: NumbersESParser.W_10Context) = super<ListenerCommon>.exitW_10(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_11' event.
   *
   * @param ctx the context of the 'w_11' rule just parsed
   */
  override fun exitW_11(ctx: NumbersESParser.W_11Context) = super<ListenerCommon>.exitW_11(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_12' event.
   *
   * @param ctx the context of the 'w_12' rule just parsed
   */
  override fun exitW_12(ctx: NumbersESParser.W_12Context) = super<ListenerCommon>.exitW_12(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_13' event.
   *
   * @param ctx the context of the 'w_13' rule just parsed
   */
  override fun exitW_13(ctx: NumbersESParser.W_13Context) = super<ListenerCommon>.exitW_13(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_14' event.
   *
   * @param ctx the context of the 'w_14' rule just parsed
   */
  override fun exitW_14(ctx: NumbersESParser.W_14Context) = super<ListenerCommon>.exitW_14(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_15' event.
   *
   * @param ctx the context of the 'w_15' rule just parsed
   */
  override fun exitW_15(ctx: NumbersESParser.W_15Context) = super<ListenerCommon>.exitW_15(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_16' event.
   *
   * @param ctx the context of the 'w_16' rule just parsed
   */
  override fun exitW_16(ctx: NumbersESParser.W_16Context) = super<ListenerCommon>.exitW_16(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_17' event.
   *
   * @param ctx the context of the 'w_17' rule just parsed
   */
  override fun exitW_17(ctx: NumbersESParser.W_17Context) = super<ListenerCommon>.exitW_17(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_18' event.
   *
   * @param ctx the context of the 'w_18' rule just parsed
   */
  override fun exitW_18(ctx: NumbersESParser.W_18Context) = super<ListenerCommon>.exitW_18(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_19' event.
   *
   * @param ctx the context of the 'w_19' rule just parsed
   */
  override fun exitW_19(ctx: NumbersESParser.W_19Context) = super<ListenerCommon>.exitW_19(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_20' event.
   *
   * @param ctx the context of the 'w_20' rule just parsed
   */
  override fun exitW_20(ctx: NumbersESParser.W_20Context) = super<ListenerCommon>.exitW_20(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_20_pref' event.
   *
   * @param ctx the context of the 'w_20_pref' rule just parsed
   */
  override fun exitW_20_pref(ctx: NumbersESParser.W_20_prefContext) =
    super<ListenerCommon>.exitW_20_pref(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_30' event.
   *
   * @param ctx the context of the 'w_30' rule just parsed
   */
  override fun exitW_30(ctx: NumbersESParser.W_30Context) = super<ListenerCommon>.exitW_30(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_40' event.
   *
   * @param ctx the context of the 'w_40' rule just parsed
   */
  override fun exitW_40(ctx: NumbersESParser.W_40Context) = super<ListenerCommon>.exitW_40(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_50' event.
   *
   * @param ctx the context of the 'w_50' rule just parsed
   */
  override fun exitW_50(ctx: NumbersESParser.W_50Context) = super<ListenerCommon>.exitW_50(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_60' event.
   *
   * @param ctx the context of the 'w_60' rule just parsed
   */
  override fun exitW_60(ctx: NumbersESParser.W_60Context) = super<ListenerCommon>.exitW_60(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_70' event.
   *
   * @param ctx the context of the 'w_70' rule just parsed
   */
  override fun exitW_70(ctx: NumbersESParser.W_70Context) = super<ListenerCommon>.exitW_70(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_80' event.
   *
   * @param ctx the context of the 'w_80' rule just parsed
   */
  override fun exitW_80(ctx: NumbersESParser.W_80Context) = super<ListenerCommon>.exitW_80(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit w_90' event.
   *
   * @param ctx the context of the 'w_90' rule just parsed
   */
  override fun exitW_90(ctx: NumbersESParser.W_90Context) = super<ListenerCommon>.exitW_90(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit d_unit' event.
   *
   * @param ctx the context of the 'd_unit' rule just parsed
   */
  override fun exitD_unit(ctx: NumbersESParser.D_unitContext) =
    super<ListenerCommon>.exitD_unit(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_1_acc' event.
   *
   * @param ctx the context of the 'w_1_acc' rule just parsed
   */
  override fun exitW_1_acc(ctx: NumbersESParser.W_1_accContext) = super<ListenerCommon>.exitW_1_acc(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_2_acc' event.
   *
   * @param ctx the context of the 'w_2_acc' rule just parsed
   */
  override fun exitW_2_acc(ctx: NumbersESParser.W_2_accContext) = super<ListenerCommon>.exitW_2_acc(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_3_acc' event.
   *
   * @param ctx the context of the 'w_3_acc' rule just parsed
   */
  override fun exitW_3_acc(ctx: NumbersESParser.W_3_accContext) = super<ListenerCommon>.exitW_3_acc(ctx as ParserRuleContext)

  /**
   * The listener of the 'exit W_6_acc' event.
   *
   * @param ctx the context of the 'w_6_acc' rule just parsed
   */
  override fun exitW_6_acc(ctx: NumbersESParser.W_6_accContext) = super<ListenerCommon>.exitW_6_acc(ctx as ParserRuleContext)
}