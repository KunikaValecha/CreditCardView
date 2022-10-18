package com.github.credit_card_view.util

import android.text.InputFilter
import android.text.Spanned
import androidx.core.text.isDigitsOnly

internal class MMYYDateFilter(
    private val separator: Char = Constants.DEFAULT_EXPIRY_DATE_SEPARATOR
) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dStart: Int,
        dEnd: Int
    ): CharSequence? {
        if (dest != null && dest.length > 4) return ""
        return if (source?.isDigitsOnly().orFalse()) {
            if (source?.length == 1 && dStart == 2)
                "$separator$source"
            else
                source
        } else ""
    }
}