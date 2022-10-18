package com.github.credit_card_view.util

import android.text.InputFilter
import android.text.Spanned

class NumberOnlyInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Spanned?,
        p4: Int,
        p5: Int
    ): CharSequence? {
        val containsDigitOnly = source?.all { it.isDigit() }.orFalse()
        return if (containsDigitOnly) source else ""
    }
}