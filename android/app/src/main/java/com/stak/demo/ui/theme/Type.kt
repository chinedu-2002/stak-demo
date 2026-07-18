package com.stak.demo.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.stak.demo.R

/**
 * Sora (variable, weights instantiated per style) is the UI face across the app.
 * Squarish Sans CT is the display face for the STAK wordmark and big numerals.
 */
@OptIn(ExperimentalTextApi::class)
private fun sora(weight: FontWeight) = Font(
	R.font.sora_variable,
	weight = weight,
	style = FontStyle.Normal,
	variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

@OptIn(ExperimentalTextApi::class)
val Sora = FontFamily(
	sora(FontWeight.Light),
	sora(FontWeight.Normal),
	sora(FontWeight.Medium),
	sora(FontWeight.SemiBold),
	sora(FontWeight.Bold),
)

val Squarish = FontFamily(Font(R.font.squarish_sans_ct))
val SquarishSC = FontFamily(Font(R.font.squarish_sans_ct_sc))
