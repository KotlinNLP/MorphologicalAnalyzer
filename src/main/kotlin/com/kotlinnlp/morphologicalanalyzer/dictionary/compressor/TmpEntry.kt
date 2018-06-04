/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.dictionary.compressor

/**
 * A temporary morphology entry.
 *
 * @property morphologies a list of temporary morphologies
 */
data class TmpEntry(val morphologies: List<TmpMorphology>)
