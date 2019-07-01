/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.listeners

import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers.AnnotationsAccumulator
import com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers.ListenerCommonHelper
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNodeImpl
import java.math.BigInteger

/**
 * Interface to be implemented by the ANTLR listener classes.
 * It makes available common methods to handle the parser events.
 */
@Suppress("FunctionName") // for the automatically generated rule methods
internal interface ListenerCommon {

  /**
   * Language-specific parameters.
   */
  val langParams: LanguageParams

  /**
   * A numbers processor to process sub-expressions.
   */
  val processor: NumbersProcessor

  /**
   * The list of tokens that compose the input text.
   */
  val tokens: List<IndexedValue<RealToken>>

  /**
   * The offset of the input text in the containing text.
   */
  val offset: Int

  /**
   * If true it enables the print of debug messages on stderr.
   */
  val debug: Boolean

  /**
   * If false do not perform the analysis of numeric sub-expressions (inside other recognized expressions).
   */
  val enableSubexpressions: Boolean

  /**
   * Helper for the listener.
   */
  val helper: ListenerCommonHelper

  /**
   * Get a list containing the collected number expressions.
   *
   * @return a list of numbers
   */
  fun getNumbers(): List<Number> = this.helper.numbers

  /**
   * Collect the annotations of the nodes that are children of the one passed as context.
   *
   * @param ctx the rule context of a node
   * @param type the name to be associated to the collected value
   */
  fun collectChildrenAnnotations(ctx: ParserRuleContext, type: String) {

    val accumulator = AnnotationsAccumulator()

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    this.setTreeValue(ctx, type = type, value = accumulator.getConcatValues())
  }

  /**
   * Set the rule type and value on the parse tree.
   *
   * @param node a parse tree node to annotate
   * @param type the rule type
   * @param value the value of the matched rule
   */
  fun setTreeValue(node: ParseTree, type: String, value: String) {

    debugPrint("Annotating '%s' with value '%s'".format(type, value))

    this.helper.treeValues[node] = Pair(type, value)
  }

  /**
   * Get the value annotated for a given node.
   *
   * @param node a parse tree node
   *
   * @return a [Pair] containing the type and the value annotated for the given node
   */
  fun getTreeValue(node: ParseTree): Pair<String, String> = this.helper.treeValues.getValue(node)

  /**
   * @param node a parse tree node
   *
   * @return a boolean indicating whether there is a value annotated for the given node
   */
  fun hasTreeValue(node: ParseTree): Boolean = node in this.helper.treeValues

  /**
   * Print a debug message if debug is enabled.
   *
   * @param message the debug message to print
   */
  fun debugPrint(message: String) {

    if (this.debug) System.err.println(message)
  }

  /**
   * Visit a parse tree context, searching for annotated nodes.
   * When a node is found accumulate the value in the correct slot type and stop the recursion.
   *
   * @param accumulator the accumulator to populate
   * @param ctx the context in which to search for annotations
   */
  fun visitParseTree(accumulator: AnnotationsAccumulator, ctx: ParserRuleContext) {

    debugPrint("Searching annotated nodes under node type: %s".format(ctx.javaClass))

    if (this.hasTreeValue(ctx)) {

      val (type, value) = this.getTreeValue(ctx)

      debugPrint("Found annotation of type %s value '%s'".format(type, value))

      accumulator.push(type = type, value = value)

    } else {

      this.visitParseTreeChildren(ctx = ctx, accumulator = accumulator)
    }
  }

  /**
   * Visit children nodes to accumulate the values of their annotations.
   *
   * @param ctx the parent context in which to search for annotations
   * @param accumulator the accumulator to populate
   */
  fun visitParseTreeChildren(ctx: ParserRuleContext, accumulator: AnnotationsAccumulator) {

    ctx.children?.forEach {
      when {
        it !is TerminalNodeImpl -> visitParseTree(ctx = it as ParserRuleContext, accumulator = accumulator)
        it.text == this.langParams.numbers["0"] -> accumulator.push(type = "zero", value = "0")
        else -> debugPrint("Ignoring terminal node %s".format(it.text))
      }
    }
  }

  /**
   * Search for numeric sub-expressions inside a recognized expression.
   * The [numericExpr] is split by spaces, tabs and newlines.
   *
   * @param numericExpr a numeric expression
   * @param exprOffset the offset position of the [numericExpr] within the input text
   *
   * @return a list of numbers
   */
  fun findSubexpressions(numericExpr: String, exprOffset: Int): List<Number> =

    if (this.helper.whiteSpacesRegex.containsMatchIn(numericExpr)) {

      this.helper.spaceSplitterRegex.findAll(numericExpr).toList().flatMap { match ->

        val matchOffset: Int = this.offset + exprOffset + match.groups[1]!!.range.start
        val matchTokenOffset: Int = this.tokens.first { it.value.position.end >= matchOffset }.index
        val subTokens: List<IndexedValue<RealToken>> = this.tokens.subList(matchTokenOffset, this.tokens.size)

        debugPrint("Processing subexpression '${match.groupValues[1]}'")

        this.processor.privateFindNumbers(text = match.groupValues[1], tokens = subTokens, offset = matchOffset)
      }

    } else {
      listOf()
    }

  /**
   * Process the annotations found inside a "million" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun millions(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var prefix: String? = null
    var from0To999K = "000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {
      when (it.first) {
        "D_1_999k" -> if (prefix == null) prefix = it.second else from0To999K = it.second
        "Max_6_digits" -> if (prefix == null) prefix = it.second else from0To999K = it.second
      }
    }

    this.setTreeValue(
      ctx as ParseTree,
      type = "Millions",
      value = "%d%06d".format(prefix!!.toLong(), from0To999K.toLong()))
  }

  /**
   * Process the annotations found inside a "one million" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun oneMillion(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var from0To999K = "000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {
      when (it.first) {
        "D_1_999k" -> from0To999K = it.second
        "Max_6_digits" -> from0To999K = it.second
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "One_million", value = "1%06d".format(from0To999K.toLong()))
  }

  /**
   * Process the annotations found inside a "billion" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun billions(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var prefix: String? = null
    var millions = "000000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {

      when (it.first) {
        "D_1_999k" -> prefix = it.second
        "Max_6_digits" -> prefix = it.second
        "D_1_999m" -> millions = it.second
        "Max_9_digits" -> if(prefix == null) prefix = it.second else millions = it.second
      }
    }

    this.setTreeValue(
      ctx as ParseTree,
      type = "Billions",
      value = "%d%09d".format(prefix!!.toLong(), millions.toLong()))
  }

  /**
   * Process the annotations found inside a "one billion" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun oneBillion(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var millions = "000000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {
      when (it.first) {
        "D_1_999m" -> millions = it.second
        "Max_9_digits" -> millions = it.second
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "One_billion", value = "1%09d".format(millions.toLong()))
  }

  /**
   * Process the annotations found inside a "trillion" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun trillions(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var prefix: String? = null
    var billions = "000000000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {
      when (it.first) {
        "D_1_999k" -> prefix = it.second
        "Max_6_digits" -> prefix = it.second
        "Millions" -> billions = it.second
        "Billions" -> billions = it.second
        "D_1_999i" -> billions = it.second
      }
    }

    this.setTreeValue(
      ctx as ParseTree,
      type = "Trillions",
      value = "%d%012d".format( prefix!!.toLong(), billions.toLong()))
  }

  /**
   * Process the annotations found inside a "one trillion" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun oneTrillion(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var billions = "000000000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {
      if (it.first == "D_1_999i") billions = it.second
    }

    this.setTreeValue(ctx as ParseTree, type = "One_trillion", value = "1%012d".format(billions.toLong()))
  }

  /**
   * Process the annotations found inside a "quadrillion" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun quadrillions(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()
    var prefix: String? = null
    var trillions = "000000000000000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    accumulator.forEachAnnotation {
      when (it.first) {
        "D_1_999k" -> prefix = it.second
        "Max_6_digits" -> prefix = it.second
        "Trillions" -> trillions = it.second
        "D_1_999b" -> trillions = it.second
      }
    }

    this.setTreeValue(
      ctx as ParseTree,
      type = "Quadrillions",
      value = "%d%015d".format( prefix!!.toLong(), trillions.toLong()))
  }

  /**
   * Process the annotations found inside a "one quadrillion" rule.
   *
   * @param ctx the context to be searched for annotations
   */
  fun oneQuadrillion(ctx: ParseTree) {

    val accumulator = AnnotationsAccumulator()

    this.visitParseTree(accumulator = accumulator, ctx = ctx as ParserRuleContext)

    var trillions = "000000000000000"

    accumulator.forEachAnnotation {
      if (it.first == "D_1_999b") trillions = it.second
    }

    this.setTreeValue(ctx as ParseTree, type = "One_quadrillion", value = "1%015d".format(trillions.toLong()))
  }

  /**
   * The listener of the 'exit w_unit' event.
   *
   * @param ctx the context of the 'w_unit' rule just parsed
   */
  fun exitW_unit(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "Unit")

  /**
   * The listener of the 'exit ten_pref' event.
   *
   * @param ctx the context of the 'ten_pref' rule just parsed
   */
  fun exitTen_pref(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "Ten_pref")

  /**
   * The listener of the 'exit d_ten_pref' event.
   *
   * @param ctx the context of the 'd_ten_pref' rule just parsed
   */
  fun exitD_ten_pref(ctx: ParserRuleContext) =
    this.setTreeValue(ctx as ParseTree, type = "D_ten_pref", value = ctx.text.toString())

  /**
   * The listener of the 'exit thousand' event.
   *
   * @param ctx the context of the 'thousand' rule just parsed
   */
  fun exitThousand(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()
    var thousandPref = "1"
    var from0To999 = "000"

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    accumulator.forEachAnnotation {
      when (it.first) {
        "Thousand_pref" -> thousandPref = it.second
        "D_1_999" -> from0To999 = it.second
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "Thousand", value = "%s%03d".format(thousandPref, from0To999.toLong()))
  }

  /**
   * The listener of the 'exit millions' event.
   *
   * @param ctx the context of the 'millions' rule just parsed
   */
  fun exitMillions(ctx: ParserRuleContext) = millions(ctx as ParseTree)

  /**
   * The listener of the 'exit millions_prefix' event.
   *
   * @param ctx the context of the 'millions_prefix' rule just parsed
   */
  fun exitMillions_prefix(ctx: ParserRuleContext) = millions(ctx as ParseTree)

  /**
   * The listener of the 'exit one_million' event.
   *
   * @param ctx the context of the 'one_million' rule just parsed
   */
  fun exitOne_million(ctx: ParserRuleContext) = oneMillion(ctx as ParseTree)

  /**
   * The listener of the 'exit one_million_prefix' event.
   *
   * @param ctx the context of the 'one_million_prefix' rule just parsed
   */
  fun exitOne_million_prefix(ctx: ParserRuleContext) = oneMillion(ctx as ParseTree)

  /**
   * The listener of the 'exit billions_prefix' event.
   *
   * @param ctx the context of the 'billions_prefix' rule just parsed
   */
  fun exitBillions_prefix(ctx: ParserRuleContext) = billions(ctx as ParseTree)

  /**
   * The listener of the 'exit billions' event.
   *
   * @param ctx the context of the 'billions' rule just parsed
   */
  fun exitBillions(ctx: ParserRuleContext) = billions(ctx as ParseTree)

  /**
   * The listener of the 'exit trillions' event.
   *
   * @param ctx the context of the 'trillions' rule just parsed
   */
  fun exitTrillions(ctx: ParserRuleContext) = trillions(ctx as ParseTree)

  /**
   * The listener of the 'exit trillions_prefix' event.
   *
   * @param ctx the context of the 'trillions_prefix' rule just parsed
   */
  fun exitTrillions_prefix(ctx: ParserRuleContext) = trillions(ctx as ParseTree)

  /**
   * The listener of the 'exit quadrillions' event.
   *
   * @param ctx the context of the 'quadrillions' rule just parsed
   */
  fun exitQuadrillions(ctx: ParserRuleContext) = quadrillions(ctx as ParseTree)

  /**
   * The listener of the 'exit quadrillions_prefix' event.
   *
   * @param ctx the context of the 'quadrillions_prefix' rule just parsed
   */
  fun exitQuadrillions_prefix(ctx: ParserRuleContext) = quadrillions(ctx as ParseTree)

  /**
   * The listener of the 'exit one_billion' event.
   *
   * @param ctx the context of the 'one_billion' rule just parsed
   */
  fun exitOne_billion(ctx: ParserRuleContext) = oneBillion(ctx as ParseTree)

  /**
   * The listener of the 'exit one_billion_prefix' event.
   *
   * @param ctx the context of the 'one_billion_prefix' rule just parsed
   */
  fun exitOne_billion_prefix(ctx: ParserRuleContext) = oneBillion(ctx as ParseTree)

  /**
   * The listener of the 'exit one_trillion' event.
   *
   * @param ctx the context of the 'one_trillion' rule just parsed
   */
  fun exitOne_trillion(ctx: ParserRuleContext) = oneTrillion(ctx as ParseTree)

  /**
   * The listener of the 'exit one_trillion_prefix' event.
   *
   * @param ctx the context of the 'one_trillion_prefix' rule just parsed
   */
  fun exitOne_trillion_prefix(ctx: ParserRuleContext) = oneTrillion(ctx as ParseTree)

  /**
   * The listener of the 'exit one_quadrillion' event.
   *
   * @param ctx the context of the 'one_quadrillion' rule just parsed
   */
  fun exitOne_quadrillion(ctx: ParserRuleContext) = oneQuadrillion(ctx as ParseTree)

  /**
   * The listener of the 'exit one_quadrillion_prefix' event.
   *
   * @param ctx the context of the 'one_quadrillion_prefix' rule just parsed
   */
  fun exitOne_quadrillion_prefix(ctx: ParserRuleContext) = oneQuadrillion(ctx as ParseTree)

  /**
   * The listener of the 'exit n_1_999k' event.
   *
   * @param ctx the context of the 'n_1_999k' rule just parsed
   */
  fun exitN_1_999k(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    this.setTreeValue(ctx as ParseTree, type = "D_1_999k", value = accumulator.firstValue)
  }

  /**
   * The listener of the 'exit n_1_999m' event.
   *
   * @param ctx the context of the 'n_1_999m' rule just parsed
   */
  fun exitN_1_999m(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    this.setTreeValue(ctx as ParseTree, type = "D_1_999m", value = accumulator.firstValue)
  }

  /**
   * The listener of the 'exit n_1_999i' event.
   *
   * @param ctx the context of the 'n_1_999i' rule just parsed
   */
  fun exitN_1_999i(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    this.setTreeValue(ctx as ParseTree, type = "D_1_999i", value = accumulator.firstValue)
  }

  /**
   * The listener of the 'exit n_1_999b' event.
   *
   * @param ctx the context of the 'n_1_999b' rule just parsed
   */
  fun exitN_1_999b(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    this.setTreeValue(ctx as ParseTree, type = "D_1_999b", value = accumulator.firstValue)
  }

  /**
   * The listener of the 'exit thousand_pref' event.
   *
   * @param ctx the context of the 'thousand_pref' rule just parsed
   */
  fun exitThousand_pref(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "Thousand_pref")

  /**
   * The listener of the 'exit n_10_99' event.
   *
   * @param ctx the context of the 'n_10_99' rule just parsed
   */
  fun exitN_10_99(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()
    var tens = ""
    var units = "0"

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    accumulator.forEachAnnotation {
      when (it.first) {
        "W_tens" -> tens = it.second
        "W_unit" -> units = it.second
        "D_1_99" -> units = it.second
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "D_10_99", value = "%s%s".format(tens, units))
  }

  /**
   * The listener of the 'exit n_tens_80' event.
   *
   * @param ctx the context of the 'n_tens_80' rule just parsed
   */
  fun exitN_tens_80(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()
    var tens = ""
    var units = "0"

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    accumulator.forEachAnnotation {
      when (it.first) {
        "W_tens" -> tens = it.second
        "W_unit" -> units = it.second
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "D_1_99", value = "%s%s".format(tens, units))
  }

  /**
   * The listener of the 'exit hundredthousand' event.
   *
   * @param ctx the context of the 'hundredthousand' rule just parsed
   */
  fun exitHundredthousand(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()
    var tenPref = "1"
    var from0To99 = "00"
    var from0To999 = "000"
    var max3Digits: String? = null

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    accumulator.forEachAnnotation {

      when (it.first) {
        "Max_3_digits" -> max3Digits = it.second
        "Ten_pref" -> tenPref = it.second
        "D_ten_pref" -> tenPref = it.second
        "D_1_99" -> from0To99 = it.second
        "D_1_999" -> from0To999 = it.second
        "Hundred" -> max3Digits = it.second
        "Max_2_digits" -> from0To99 = it.second
      }
    }

    this.setTreeValue(
      ctx as ParseTree,
      type = "Hundred_thousand",
      value = if(max3Digits != null)
        "%s%03d".format(max3Digits, from0To999.toLong())
      else
        "%s%02d%03d".format(tenPref, from0To99.toLong(), from0To999.toLong()))
  }

  /**
   * The listener of the 'exit hundred' event.
   * TODO: make a specific override for the Italian listener?
   *
   * @param ctx the context of the 'hundred' rule just parsed
   */
  fun exitHundred(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()
    var tenPref = "1"
    var from0To99 = "00"

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    accumulator.forEachAnnotation {
      when (it.first) {
        "Ten_pref" -> tenPref = it.second
        "D_ten_pref" -> tenPref = it.second
        "D_1_99" -> from0To99 = it.second
        "Max_2_digits" -> from0To99 = it.second
        "N_tens_80" -> from0To99 = it.second // case with elided hundred prefix (valid of IT: 'centottanta')
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "Hundred", value = "%s%02d".format(tenPref, from0To99.toLong()))
  }

  /**
   * The listener of the 'exit n_1_99' event.
   *
   * @param ctx the context of the 'n_1_99' rule just parsed
   */
  fun exitN_1_99(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "D_1_99")

  /**
   * The listener of the 'exit n_1_999' event.
   *
   * @param ctx the context of the 'n_1_999' rule just parsed
   */
  fun exitN_1_999(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "D_1_999")

  /**
   * The listener of the 'exit digit_number' event.
   *
   * @param ctx the context of the 'digit_number' rule just parsed
   */
  fun exitDigit_number(ctx: ParserRuleContext) {

    var str = ctx.text.toString()

    str = str.replace(this.langParams.thousandSeparator, "")
    str = str.replace(regex = this.helper.trailingZeroRegex, replacement = "$1")

    this.setTreeValue(ctx as ParseTree, type = "Digit_number", value = str)
  }

  /**
   * The listener of the 'exit max_2_digits' event.
   *
   * @param ctx the context of the 'max_2_digits' rule just parsed
   */
  fun exitMax_2_digits(ctx: ParserRuleContext) =
    this.setTreeValue(ctx as ParseTree, type = "Max_2_digits", value = ctx.text.toString())

  /**
   * The listener of the 'exit max_3_digits' event.
   *
   * @param ctx the context of the 'max_3_digits' rule just parsed
   */
  fun exitMax_3_digits(ctx: ParserRuleContext) =
    this.setTreeValue(ctx as ParseTree, type = "Max_3_digits", value = ctx.text.toString())

  /**
   * The listener of the 'exit max_6_digits' event.
   *
   * @param ctx the context of the 'max_6_digits' rule just parsed
   */
  fun exitMax_6_digits(ctx: ParserRuleContext) =
    this.setTreeValue(
      ctx as ParseTree,
      type = "Max_6_digits",
      value = ctx.text.toString().replace(oldValue = this.langParams.thousandSeparator, newValue = ""))

  /**
   * The listener of the 'exit max_6_digits_with_div' event.
   *
   * @param ctx the context of the 'max_6_digits_with_div' rule just parsed
   */
  fun exitMax_6_digits_with_div(ctx: ParserRuleContext) =
    this.setTreeValue(
      ctx as ParseTree,
      type = "Max_6_digits",
      value = ctx.text.toString().replace(oldValue = this.langParams.thousandSeparator, newValue = ""))

  /**
   * The listener of the 'exit max_9_digits' event.
   *
   * @param ctx the context of the 'max_9_digits' rule just parsed
   */
  fun exitMax_9_digits(ctx: ParserRuleContext) =
    this.setTreeValue(
      ctx as ParseTree,
      type = "Max_9_digits",
      value = ctx.text.toString().replace(oldValue = this.langParams.thousandSeparator, newValue = ""))

  /**
   * The listener of the 'exit max_9_digits_with_div' event.
   *
   * @param ctx the context of the 'max_9_digits_with_div' rule just parsed
   */
  fun exitMax_9_digits_with_div(ctx: ParserRuleContext) {

    val str = ctx.text.toString().replace(oldValue = this.langParams.thousandSeparator, newValue = "")

    this.setTreeValue(ctx as ParseTree, type = "Max_9_digits", value = str)
  }

  /**
   * The listener of the 'exit seq_of_w_unit' event.
   *
   * @param ctx the context of the 'seq_of_w_unit' rule just parsed
   */
  fun exitSeq_of_w_unit(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "Seq_of_w_unit")

  /**
   * The listener of the 'exit of_rules' event.
   *
   * @param ctx the context of the 'of_rules' rule just parsed
   */
  fun exitOf_rules(ctx: ParserRuleContext) {

    val accumulator = AnnotationsAccumulator()
    var prefix = ""
    var body = ""
    var tail: Long = 0

    this.visitParseTree(accumulator = accumulator, ctx = ctx)

    accumulator.forEachAnnotation {
      when (it.first) {
        "Prefix_of_rules" -> prefix = it.second
        "Of_rules_body" -> body = it.second
        "Base_number" -> tail = it.second.toLong()
      }
    }

    this.setTreeValue(
      ctx as ParseTree,
      type = "Of_rules",
      value = (BigInteger("%d%s".format(prefix.toLong(), body)) + BigInteger.valueOf(tail)).toString())
  }

  /**
   * The listener of the 'exit prefix_of_rules' event.
   *
   * @param ctx the context of the 'prefix_of_rules' rule just parsed
   */
  fun exitPrefix_of_rules(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "Prefix_of_rules")

  /**
   * The listener of the 'exit of_rules_body' event.
   *
   * @param ctx the context of the 'of_rules_body' rule just parsed
   */
  fun exitOf_rules_body(ctx: ParserRuleContext) {

    var str = ""

    ctx.children.forEach {

      str += when (it.text) {
        this.langParams.words["of"] -> ""
        this.langParams.suff.getValue("million").getValue("plur") -> "000000"
        this.langParams.suff.getValue("billion").getValue("plur") -> "000000000"
        this.langParams.suff.getValue("trillion").getValue("plur") -> "000000000000"
        this.langParams.suff.getValue("quadrillion").getValue("plur") -> "000000000000000"
        else ->
          if (this.helper.spacesRegex.matchEntire(it.text) == null)
            throw IllegalArgumentException("Token not recognized: '%s'".format(it.text))
          else
            ""
      }
    }

    this.setTreeValue(ctx as ParseTree, type = "Of_rules_body", value = str)
  }

  /**
   * The listener of the 'exit base_number' event.
   *
   * @param ctx the context of the 'base_number' rule just parsed
   */
  fun exitBase_number(ctx: ParserRuleContext) = this.collectChildrenAnnotations(ctx, "Base_number")

  /**
   * The listener of the 'exit number' event.
   *
   * @param ctx the context of the 'number' rule just parsed
   */
  fun exitNumber(ctx: ParserRuleContext) {

    debugPrint("Exiting number: %s".format(ctx.text))

    this.addNewNumber(token = this.buildNumber(ctx), ctx = ctx)
  }

  /**
   * The listener of the 'exit w_1' event.
   *
   * @param ctx the context of the 'w_1' rule just parsed
   */
  fun exitW_1(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "1")

  /**
   * The listener of the 'exit w_1_art' event.
   *
   * @param ctx the context of the 'w_1_art' rule just parsed
   */
  fun exitW_1_art(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "1")

  /**
   * The listener of the 'exit w_2' event.
   *
   * @param ctx the context of the 'w_2' rule just parsed
   */
  fun exitW_2(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "2")

  /**
   * The listener of the 'exit w_3' event.
   *
   * @param ctx the context of the 'w_3' rule just parsed
   */
  fun exitW_3(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "3")

  /**
   * The listener of the 'exit w_4' event.
   *
   * @param ctx the context of the 'w_4' rule just parsed
   */
  fun exitW_4(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "4")

  /**
   * The listener of the 'exit w_5' event.
   *
   * @param ctx the context of the 'w_5' rule just parsed
   */
  fun exitW_5(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "5")

  /**
   * The listener of the 'exit w_6' event.
   *
   * @param ctx the context of the 'w_6' rule just parsed
   */
  fun exitW_6(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "6")

  /**
   * The listener of the 'exit w_7' event.
   *
   * @param ctx the context of the 'w_7' rule just parsed
   */
  fun exitW_7(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "7")

  /**
   * The listener of the 'exit w_8' event.
   *
   * @param ctx the context of the 'w_8' rule just parsed
   */
  fun exitW_8(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "8")

  /**
   * The listener of the 'exit w_9' event.
   *
   * @param ctx the context of the 'w_9' rule just parsed
   */
  fun exitW_9(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "9")

  /**
   * The listener of the 'exit w_0' event.
   *
   * @param ctx the context of the 'w_0' rule just parsed
   */
  fun exitW_0(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "0")

  /**
   * The listener of the 'exit w_10' event.
   *
   * @param ctx the context of the 'w_10' rule just parsed
   */
  fun exitW_10(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "10")

  /**
   * The listener of the 'exit w_11' event.
   *
   * @param ctx the context of the 'w_11' rule just parsed
   */
  fun exitW_11(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "11")

  /**
   * The listener of the 'exit w_12' event.
   *
   * @param ctx the context of the 'w_12' rule just parsed
   */
  fun exitW_12(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "12")

  /**
   * The listener of the 'exit w_13' event.
   *
   * @param ctx the context of the 'w_13' rule just parsed
   */
  fun exitW_13(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "13")

  /**
   * The listener of the 'exit w_14' event.
   *
   * @param ctx the context of the 'w_14' rule just parsed
   */
  fun exitW_14(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "14")

  /**
   * The listener of the 'exit w_15' event.
   *
   * @param ctx the context of the 'w_15' rule just parsed
   */
  fun exitW_15(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "15")

  /**
   * The listener of the 'exit w_16' event.
   *
   * @param ctx the context of the 'w_16' rule just parsed
   */
  fun exitW_16(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "16")

  /**
   * The listener of the 'exit w_17' event.
   *
   * @param ctx the context of the 'w_17' rule just parsed
   */
  fun exitW_17(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "17")

  /**
   * The listener of the 'exit w_18' event.
   *
   * @param ctx the context of the 'w_18' rule just parsed
   */
  fun exitW_18(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "18")

  /**
   * The listener of the 'exit w_19' event.
   *
   * @param ctx the context of the 'w_19' rule just parsed
   */
  fun exitW_19(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = "19")

  /**
   * The listener of the 'exit w_20' event.
   *
   * @param ctx the context of the 'w_20' rule just parsed
   */
  fun exitW_20(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "2")

  /**
   * The listener of the 'exit w_20_pref' event.
   *
   * @param ctx the context of the 'w_20_pref' rule just parsed
   */
  fun exitW_20_pref(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "2")

  /**
   * The listener of the 'exit w_30' event.
   *
   * @param ctx the context of the 'w_30' rule just parsed
   */
  fun exitW_30(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "3")

  /**
   * The listener of the 'exit w_40' event.
   *
   * @param ctx the context of the 'w_40' rule just parsed
   */
  fun exitW_40(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "4")

  /**
   * The listener of the 'exit w_50' event.
   *
   * @param ctx the context of the 'w_50' rule just parsed
   */
  fun exitW_50(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "5")

  /**
   * The listener of the 'exit w_60' event.
   *
   * @param ctx the context of the 'w_60' rule just parsed
   */
  fun exitW_60(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "6")

  /**
   * The listener of the 'exit w_70' event.
   *
   * @param ctx the context of the 'w_70' rule just parsed
   */
  fun exitW_70(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "7")

  /**
   * The listener of the 'exit w_80' event.
   *
   * @param ctx the context of the 'w_80' rule just parsed
   */
  fun exitW_80(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "8")

  /**
   * The listener of the 'exit w_90' event.
   *
   * @param ctx the context of the 'w_90' rule just parsed
   */
  fun exitW_90(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_tens", value = "9")

  /**
   * The listener of the 'exit d_unit' event.
   *
   * @param ctx the context of the 'd_unit' rule just parsed
   */
  fun exitD_unit(ctx: ParserRuleContext) = this.setTreeValue(ctx as ParseTree, type = "W_unit", value = ctx.text)

  /**
   * Add a new token found if it has a match within the [tokens] list.
   * If enabled, sub-expressions are searched and added.
   *
   * @param token the number token to add
   * @param ctx the ANTLR context of the token
   */
  private fun addNewNumber(token: Number, ctx: ParserRuleContext) {

    if (token.startToken >= 0 && token.endToken >= 0) {

      this.helper.numbers.add(token)

      if (this.enableSubexpressions)
        this.helper.numbers.addAll(this.findSubexpressions(numericExpr = ctx.text, exprOffset = ctx.start.startIndex))

    } else {
      debugPrint("Cannot find a match of the number '${token.original}' within the tokens.")
    }
  }

  /**
   * Build a token given its integer and decimal parts as strings.
   *
   * @param ctx the context of the 'number' rule
   *
   * @return a new number token
   */
  private fun buildNumber(ctx: ParserRuleContext): Number {

    val (integer, decimal) = this.getNumberComponents(ctx)

    return Number(
      startToken = this.tokens.first { (it.value.position.end - this.offset) >= ctx.start.startIndex }.index,
      endToken = this.tokens.first { (it.value.position.end - this.offset) >= ctx.stop.stopIndex }.index,
      value = decimal?.let { "$integer.$decimal".toDouble() }
        ?: integer.let { it.toIntOrNull() ?: it.toLongOrNull() ?: it.toBigInteger() } as kotlin.Number,
      asWord = this.helper.digitToWordConverter.convert(integer = integer, decimal = decimal),
      original = ctx.text
    )
  }

  /**
   * @param ctx the ANTLR context of the number
   *
   * @return a pair containing the <integer, decimal?> parts of the parsed number
   */
  private fun getNumberComponents(ctx: ParserRuleContext): Pair<String, String?> {

    var accumulator = AnnotationsAccumulator()
    var intPart: String? = null

    ctx.children.forEach {

      if (it is TerminalNodeImpl) {

        if (it.text == this.langParams.wordDecimalSeparator || it.text == this.langParams.digitDecimalSeparator) {

          intPart = accumulator.getConcatValues()
          accumulator = AnnotationsAccumulator()

          debugPrint("Found decimal separator, saving integer part and starting accumulating decimal part")
        }

      } else {
        this.visitParseTree(accumulator = accumulator, ctx = it as ParserRuleContext)
      }
    }

    return this.buildComponents(accumulator = accumulator, intPart = intPart)
  }

  /**
   * Build the integer and decimal parts of a number.
   *
   * @param accumulator the annotations accumulator
   * @param intPart the integer part already accumulated
   *
   * @return a pair containing the <integer, decimal?> parts of the parsed number
   */
  private fun buildComponents(accumulator: AnnotationsAccumulator, intPart: String?): Pair<String, String?> {

    val digitSep: String = this.langParams.digitDecimalSeparator
    val accumulatedValues: String = accumulator.getConcatValues()

    val components: Pair<String, String?> = when {
      intPart != null -> Pair(intPart, accumulatedValues)
      accumulatedValues.contains(digitSep) -> accumulatedValues.split(digitSep).let { Pair(it[0], it[1]) }
      else -> Pair(accumulatedValues, null)
    }

    return components.let { Pair(it.first.trimLeadingZeros(), it.second?.trimEnd('0')) }
  }

  /**
   * Trim the leading zeros of a string that contains a number.
   * If the the string contains only '0' chars, only one of them is kept.
   *
   * @return a new string with the leading zeros trimmed
   */
  private fun String.trimLeadingZeros(): String =
    this.replace(this@ListenerCommon.helper.leadingZeroesRegex, "$1").let { if (it.isEmpty()) "0" else it }
}
