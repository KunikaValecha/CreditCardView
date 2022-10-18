package com.github.credit_card_view

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.github.credit_card_view.data.CardField
import com.github.credit_card_view.data.CardSide
import com.github.credit_card_view.data.ExpiryDate
import com.github.credit_card_view.databinding.LayoutCreditCardViewBinding
import com.github.credit_card_view.util.*
import com.github.credit_card_view.util.AlphabetOnlyInputFilter
import com.github.credit_card_view.util.MMYYDateFilter
import com.wajahatkarim3.easyflipview.EasyFlipView
import java.util.*

/**
 * Credit/Debit card view with outline animation
 * **/
class CreditCardView : ConstraintLayout {

    //Binding
    private lateinit var binding: LayoutCreditCardViewBinding

    //Outline
    private var outlineBaseColor: Int? = null
    private var outlineErrorColor: Int? = null

    //Card Number Char Separator
    private var separator: String = Constants.DEFAULT_CARD_NUMBER_SEPARATOR

    //Current Card Side
    private var currentCardSide: CardSide = CardSide.FRONT

    //Outline
    private var widthAnimator: ValueAnimator? = null
    private var heightAnimator: ValueAnimator? = null
    private var translateAnimator: ViewPropertyAnimator? = null

    // Front Outline drawable for normal state
    private val baseOutlineFront by lazy {
        val drawable = GradientDrawable()
        drawable.setStroke(1.dp, outlineBaseColor!!)
        drawable.cornerRadius = 5.dp.toFloat()
        drawable
    }

    // Front Outline drawable for error state
    private val errorOutlineFront by lazy {
        val drawable = GradientDrawable()
        drawable.setStroke(1.dp, outlineErrorColor!!)
        drawable.cornerRadius = 5.dp.toFloat()
        drawable
    }
    // Back Outline drawable for normal state
    private val baseOutlineBack by lazy {
        val drawable = GradientDrawable()
        drawable.setStroke(1.dp, outlineBaseColor!!)
        drawable.cornerRadius = 8.dp.toFloat()
        drawable
    }

    // Back Outline drawable for error state
    private val errorOutlineBack by lazy {
        val drawable = GradientDrawable()
        drawable.setStroke(1.dp, outlineErrorColor!!)
        drawable.cornerRadius = 8.dp.toFloat()
        drawable
    }

    //Min Card Length
    private var minCardLength = Constants.DEFAULT_MIN_CARD_NUMBER_LENGTH

    //Max Card Length
    private var maxCardLength = Constants.DEFAULT_MAX_CARD_NUMBER_LENGTH

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(attributeSet)
    }

    private fun init() {
        binding = LayoutCreditCardViewBinding.inflate(LayoutInflater.from(context), this, false)
        this.addView(binding.root)
    }

    private fun init(attributeSet: AttributeSet) {
        init()
        val typedArray =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.CreditCardView, 0, 0)

        getOutlineState(typedArray)

        setCardFrontSideUIState(typedArray)

        setCardBackSideUIState(typedArray)

        setCvvViewUIState(typedArray)

        setCardNumberViewUIState(typedArray)

        setCardNameViewUIState(typedArray)

        setCardExpiryViewUIState(typedArray)

        updateEditState(typedArray)

        adjustFrontAndBackSideSize()

        autoFocusIfNeeded(typedArray)

        setEditTextInputFilters()

        setupTextChangedListener(typedArray)

        setFocusChangedListener()

        setIMEActionListener()

        setupClickListeners()

        setOnFlipListener()

        typedArray.recycle()
    }

    /**
     * Function to get normal & error state color of outline
     * **/
    private fun getOutlineState(typedArray: TypedArray) {
        outlineBaseColor =
            typedArray.getColor(R.styleable.CreditCardView_card_outline_base_color, Color.WHITE)

        outlineErrorColor =
            typedArray.getColor(R.styleable.CreditCardView_card_outline_error_color, Color.RED)
    }

    /**
     * Function to update UI of front side of the card
     * **/
    private fun setCardFrontSideUIState(typedArray: TypedArray) {
        val frontGradientStart =
            typedArray.getColor(R.styleable.CreditCardView_card_frontGradientStart, Color.BLACK)

        val frontGradientEnd =
            typedArray.getColor(R.styleable.CreditCardView_card_frontGradientEnd, Color.BLACK)

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(frontGradientStart, frontGradientEnd)
        )

        binding.frontView.clTopView.background = gradient
    }

    /**
     * Function to update UI of back side of the card
     * **/
    private fun setCardBackSideUIState(typedArray: TypedArray) {
        val backGradientStart =
            typedArray.getColor(R.styleable.CreditCardView_card_backGradientStart, Color.BLACK)

        val backGradientEnd =
            typedArray.getColor(R.styleable.CreditCardView_card_backGradientEnd, Color.BLACK)

        val backStripColor =
            typedArray.getColor(R.styleable.CreditCardView_card_backStripColor, Color.BLACK)

        val backStrip2Color =
            typedArray.getColor(R.styleable.CreditCardView_card_backStrip2Color, Color.BLACK)

        binding.backView.strip2.backgroundTintList = ColorStateList.valueOf(backStrip2Color)

        val gradientBack = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(backGradientStart, backGradientStart, backGradientEnd)
        )

        binding.backView.clBackView.background = gradientBack

        binding.backView.strip1.setBackgroundColor(backStripColor)
    }

    /**
     * Function to update CVV View UI
     * **/
    private fun setCvvViewUIState(typedArray: TypedArray) {
        val cvvBackgroundColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cvvBackgroundColor, Color.WHITE)

        val cvvHintColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cvvHintColor, Color.WHITE)

        val cvvTextColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cvvTextColor, Color.WHITE)

        binding.backView.etCvv.backgroundTintList = ColorStateList.valueOf(cvvBackgroundColor)
        binding.backView.etCvv.setTextColor(cvvTextColor)
        binding.backView.etCvv.setHintTextColor(cvvHintColor)
    }

    /**
     * Function to update Card Number View UI
     * **/
    private fun setCardNumberViewUIState(typedArray: TypedArray) {
        val cardNumberTextColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardNumberTextColor, Color.WHITE)

        val cardNumberHintColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardNumberHintColor, Color.WHITE)

        binding.frontView.etCardNumber.setHintTextColor(cardNumberHintColor)
        binding.frontView.etCardNumber.setTextColor(cardNumberTextColor)

        minCardLength = typedArray.getInteger(
            R.styleable.CreditCardView_card_minCardNumberLength,
            Constants.DEFAULT_MIN_CARD_NUMBER_LENGTH
        )
        maxCardLength = typedArray.getInteger(
            R.styleable.CreditCardView_card_maxCardNumberLength,
            Constants.DEFAULT_MAX_CARD_NUMBER_LENGTH
        )

        binding.frontView.etCardNumber.filters = arrayOf(InputFilter.LengthFilter(maxCardLength))

        separator = typedArray.getString(R.styleable.CreditCardView_card_separator)
            ?: Constants.DEFAULT_CARD_NUMBER_SEPARATOR
    }

    /**
     * Function to update Card Name View UI
     * **/
    private fun setCardNameViewUIState(typedArray: TypedArray) {
        //Header
        val cardNameHeaderColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardNameHeaderColor, Color.WHITE)
        binding.frontView.tvHeaderName.setTextColor(cardNameHeaderColor)

        //EditText
        val cardNameTextColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardNameTextColor, Color.WHITE)

        val cardNameHintColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardNameHintColor, Color.WHITE)

        binding.frontView.etName.setHintTextColor(cardNameHintColor)
        binding.frontView.etName.setTextColor(cardNameTextColor)
    }

    /**
     * Function to update Card Expiry View UI
     * **/
    private fun setCardExpiryViewUIState(typedArray: TypedArray) {
        val cardExpiryHeaderColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardExpiryHeaderColor, Color.WHITE)

        val cardExpiryTextColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardExpiryTextColor, Color.WHITE)

        val cardExpiryHintColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardExpiryHintColor, Color.WHITE)

        binding.frontView.tvHeaderExpDate.setTextColor(cardExpiryHeaderColor)

        binding.frontView.etExpiry.setHintTextColor(cardExpiryHintColor)
        binding.frontView.etExpiry.setTextColor(cardExpiryTextColor)
    }

    /**
     * Function to update card editable state
     * **/
    private fun updateEditState(typedArray: TypedArray) {
        val isFrontSideEditable =
            typedArray.getBoolean(R.styleable.CreditCardView_card_isFrontSideEditable, true)

        binding.frontView.etCardNumber.isEnabled = isFrontSideEditable
        binding.frontView.etCardNumber.isClickable = isFrontSideEditable

        binding.frontView.etName.isEnabled = isFrontSideEditable
        binding.frontView.etName.isClickable = isFrontSideEditable

        binding.frontView.etExpiry.isEnabled = isFrontSideEditable
        binding.frontView.etExpiry.isClickable = isFrontSideEditable

        val isBackSideEditable =
            typedArray.getBoolean(R.styleable.CreditCardView_card_isBackSideEditable, true)

        binding.backView.etCvv.isEnabled = isBackSideEditable
        binding.backView.etCvv.isClickable = isBackSideEditable
    }

    /**
     * Function to make sure the front side view & back side view are of same size
     * **/
    private fun adjustFrontAndBackSideSize() {
        binding.frontView.root.post {
            val params: ViewGroup.LayoutParams = binding.backView.root.layoutParams
            params.width = binding.frontView.root.width
            params.height = binding.frontView.root.height
            binding.backView.root.layoutParams = params
        }
    }

    /**
     * Function to auto focus on card number
     * **/
    private fun autoFocusIfNeeded(typedArray: TypedArray) {
        val shouldAutoFocusOnCardNumber =
            typedArray.getBoolean(R.styleable.CreditCardView_card_autoFocusCardNumber, false)

        if (shouldAutoFocusOnCardNumber) {
            binding.frontView.etCardNumber.postDelayed({
                binding.frontView.etCardNumber.requestFocus()
                binding.frontView.etCardNumber.showKeyboard()
            }, 500)
        }

    }

    /**
     * Function to set input filters on edit texts
     * **/
    private fun setEditTextInputFilters() {
        binding.frontView.etName.filters = arrayOf(
            AlphabetOnlyInputFilter(),
            InputFilter.AllCaps()
        )

        binding.frontView.etExpiry.filters = arrayOf(
            NumberOnlyInputFilter(),
            MMYYDateFilter()
        )
    }

    /**
     * Text Changed Listener
     * **/
    private fun setupTextChangedListener(typedArray: TypedArray) {
        val cardNumberTextColor =
            typedArray.getColor(R.styleable.CreditCardView_card_cardNumberTextColor, Color.WHITE)

        binding.frontView.etCardNumber.doOnTextChanged { text, _, _, _ ->
            binding.frontView.outline.layoutParams?.width = binding.frontView.etCardNumber.width
            binding.frontView.outline.requestLayout()
        }

        binding.frontView.etCardNumber.addTextChangedListener(
            CreditCardTextFormatter(
                separator = separator,
                textColor = cardNumberTextColor
            )
        )

        binding.frontView.etCardNumber.doAfterTextChanged {
            updateOutlineState(false)
        }

        binding.frontView.etName.doAfterTextChanged {
            updateOutlineState(false)
        }

        binding.frontView.etExpiry.doAfterTextChanged {
            updateOutlineState(false)
        }

        binding.backView.etCvv.doAfterTextChanged {
            binding.backView.outlineCvv.background = baseOutlineBack
        }
    }

    /**
     * Focus Changed Listener
     * **/
    private fun setFocusChangedListener() {
        binding.frontView.etCardNumber.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                animateOutline(view)
            }
        }

        binding.frontView.etName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                animateOutline(view)
            }
        }

        binding.frontView.etExpiry.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                animateOutline(view)
            }
        }
    }

    /**
     * IME Action Listener for keyboard
     * **/
    private fun setIMEActionListener() {
        binding.frontView.etExpiry.setOnImeActionNextListener {
            if (isCardNumberValid()) {
                if (isCardNameValid()) {
                    if (isCardExpiryValid()) {
                        binding.frontView.outline.isVisible = false
                        binding.flipView.flipTheView()
                    } else {
                        binding.frontView.etExpiry.showKeyboard()
                        updateOutlineState(true)
                    }
                } else {
                    binding.frontView.etName.showKeyboard()
                    updateOutlineState(true)
                }
            } else {
                binding.frontView.etCardNumber.showKeyboard()
                updateOutlineState(true)
            }
        }

        binding.backView.etCvv.setOnImeActionDoneListener {
            if (isCvvValid()) {
                binding.flipView.flipTheView()
                binding.backView.etCvv.hideKeyboard()
            }
        }
    }

    /**
     * Click Listener
     * **/
    private fun setupClickListeners() {
        binding.frontView.root.setOnClickListener {
            binding.flipView.flipTheView()
        }
        binding.backView.root.setOnClickListener {
            binding.flipView.flipTheView()
        }
    }

    /**
     * Function to animate outline
     * **/
    private fun animateOutline(toView: View) {
        binding.frontView.outline.isVisible = true
        widthAnimator?.cancel()
        heightAnimator?.cancel()
        translateAnimator?.cancel()

        val duration = 200L
        val newWidth = toView.width
        val newHeight = toView.height
        widthAnimator = ValueAnimator.ofInt(binding.frontView.outline.width, newWidth)
        widthAnimator?.duration = duration
        widthAnimator?.interpolator = LinearInterpolator()
        widthAnimator?.addUpdateListener {
            binding.frontView.outline.layoutParams?.width = it.animatedValue as Int
            binding.frontView.outline.requestLayout()
        }
        widthAnimator?.start()

        heightAnimator = ValueAnimator.ofInt(binding.frontView.outline.height, newHeight)
        heightAnimator?.duration = duration
        heightAnimator?.interpolator = LinearInterpolator()
        heightAnimator?.addUpdateListener {
            binding.frontView.outline.layoutParams?.height = it.animatedValue as Int
            binding.frontView.outline.requestLayout()
        }
        heightAnimator?.start()

        translateAnimator = binding.frontView.outline.animate()
            ?.x(toView.x)
            ?.y(toView.y)
            ?.setDuration(duration)
            ?.setInterpolator(LinearInterpolator())
        translateAnimator?.start()
    }

    private fun updateOutlineState(isInErrorState: Boolean) {
        if (isInErrorState)
            binding.frontView.outline.background = errorOutlineFront
        else
            binding.frontView.outline.background = baseOutlineFront
    }

    /**
     * Card Flip Listener Internal
     * **/
    private fun setOnFlipListener() {
        binding.flipView.setOnFlipListener { _, newCurrentSide ->
            when (newCurrentSide) {
                EasyFlipView.FlipState.FRONT_SIDE -> {
                    //Do nothing in this case
                    currentCardSide = CardSide.FRONT
                    binding.root.hideKeyboard()
                }
                EasyFlipView.FlipState.BACK_SIDE -> {
                    currentCardSide = CardSide.BACK
                    binding.backView.etCvv.post {
                        binding.backView.etCvv.showKeyboard()
                    }
                }
            }
        }
    }

    /**
     * Function to validate card expiry
     * **/
    private fun isCardExpiryValid(): Boolean {
        val mmYY = binding.frontView.etExpiry.text
        val temp = mmYY?.split(Constants.DEFAULT_EXPIRY_DATE_SEPARATOR)
        val mm = temp?.getOrNull(0)?.toIntOrNull().orZero()
        val yy = temp?.getOrNull(1)?.toIntOrNull().orZero()
        val calendar = Calendar.getInstance()
        val currentYear =
            calendar.get(Calendar.YEAR) % 100 //For last two digits i.e. 2022 % 100 = 22
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        return if (yy > currentYear)
            (mm in 1..12) && (yy >= currentYear)
        else
            (mm in currentMonth..12) && (yy >= currentYear)
    }

    /**
     * Function to validate card cvv
     * **/
    private fun isCvvValid(): Boolean {
        val cvvLength = binding.backView.etCvv.text?.length.orZero()
        return cvvLength >= Constants.DEFAULT_MIN_CVV_LENGTH && cvvLength <= Constants.DEFAULT_MAX_CVV_LENGTH
    }

    /**
     * Function to validate card number
     * **/
    private fun isCardNumberValid(): Boolean {
        val number = binding.frontView.etCardNumber.text
        return number.isNullOrBlank().not() && number?.length.orZero() >= minCardLength
    }

    /**
     * Function to validate card name
     * **/
    private fun isCardNameValid(): Boolean {
        return binding.frontView.etName.text.isNullOrBlank().not()
    }

    fun getCardNumber(): String? =
        binding.frontView.etCardNumber.text?.replace(separator.toRegex(), "")

    fun getNameOnCard(): String? = binding.frontView.etName.text?.toString()

    fun getExpiryDate(): ExpiryDate? {
        if (isCardExpiryValid()) {
            val expiry = binding.frontView.etExpiry.text
            val temp = expiry?.split(Constants.DEFAULT_EXPIRY_DATE_SEPARATOR)
            val mm = temp?.getOrNull(0)?.toIntOrNull().orZero()
            val yy = temp?.getOrNull(1)?.toIntOrNull().orZero()
            return ExpiryDate(month = mm, year = yy)
        }
        return null
    }

    fun getCvv(): String? = binding.backView.etCvv.text?.toString()

    fun getCurrentCardSide(): CardSide = currentCardSide

    fun flipCard(cardSide: CardSide? = null) {
        when (cardSide) {
            CardSide.FRONT -> {
                if (binding.flipView.currentFlipState != EasyFlipView.FlipState.FRONT_SIDE) {
                    binding.flipView.flipTheView()
                }
            }
            CardSide.BACK -> {
                if (binding.flipView.currentFlipState != EasyFlipView.FlipState.BACK_SIDE) {
                    binding.flipView.flipTheView()
                }
            }
            else -> binding.flipView.flipTheView()
        }
    }

    fun setBankName(bankName: String) {
        binding.frontView.frontTvBankName.text = bankName
        binding.backView.backTvBankName.text = bankName
    }

    fun setCardProviderLogo(logo: Bitmap) {
        binding.frontView.ivCardTypeFront.setImageBitmap(logo)
    }

    fun setErrorStateToField(cardField: CardField) {
        when (cardField) {
            CardField.CARD_NUMBER -> {
                animateOutline(binding.frontView.etCardNumber)
                updateOutlineState(true)
            }
            CardField.CARD_NAME -> {
                animateOutline(binding.frontView.etName)
                updateOutlineState(true)
            }
            CardField.CARD_EXPIRY -> {
                animateOutline(binding.frontView.etExpiry)
                updateOutlineState(true)
            }
            CardField.CARD_CVV -> {
                binding.backView.outlineCvv.background = errorOutlineBack
            }
        }
    }

}