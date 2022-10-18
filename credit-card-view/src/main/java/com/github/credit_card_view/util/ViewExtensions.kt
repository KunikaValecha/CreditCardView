package com.github.credit_card_view.util

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

internal fun View.showKeyboard() {
    this.postDelayed({
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}

internal fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

internal fun Int?.orZero() = this ?: 0

internal fun Boolean?.orFalse() = this ?: false

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

internal fun EditText.setOnImeActionDoneListener(onDone: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_DONE) {
            onDone.invoke()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

internal fun EditText.setOnImeActionNextListener(onNext: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_NEXT) {
            onNext.invoke()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

