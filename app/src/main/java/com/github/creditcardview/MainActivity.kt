package com.github.creditcardview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.creditcardview.R
import com.github.credit_card_view.CreditCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ccv = findViewById<CreditCardView>(R.id.creditCardView)

        ccv.setBankName(getString(R.string.dummy_bank_name))

        ccv.setCardProviderLogo(
            ContextCompat.getDrawable(this, R.drawable.ic_mastercard)?.toBitmap()!!
        )
    }
}