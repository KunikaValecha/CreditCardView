package com.github.credit_card_view.util

import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.core.text.toSpannable

class CreditCardTextFormatter(
    private val separator: String = Constants.DEFAULT_CARD_NUMBER_SEPARATOR,
    private val textColor: Int,
    private val divider: Int = 5
) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        if (s == null) {
            return
        }
        val oldString = s.toString()
        val newString = getNewString(oldString)
        if (newString != oldString) {
            val final = getNewString(oldString).toSpannable()
            final.setSpan(
                ForegroundColorSpan(textColor),
                0,
                final.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            s.replace(0, oldString.length, final)
        }
    }

    private fun getNewString(value: String): String {

        var newString = value.replace(separator, "")

        var divider = this.divider
        while (newString.length >= divider) {
            newString = newString.substring(
                0,
                divider - 1
            ) + this.separator + newString.substring(divider - 1)
            divider += this.divider + separator.length - 1
        }
        return newString
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}
