package com.juztoss.revolut.view

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.TextView

/**
 * EditText that provides a listener for user input.
 */
class UserCatcherEditText : AppCompatEditText {

    private var suppressListener = false
    private var changeListener: (() -> Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!suppressListener) {
                    changeListener?.invoke()
                }
            }
        })
    }

    fun setUserInputListener(listener: () -> Unit) {
        this.changeListener = listener
    }


    override fun setText(text: CharSequence, type: TextView.BufferType) {
        suppressListener = true
        super.setText(text, type)
        suppressListener = false
    }
}