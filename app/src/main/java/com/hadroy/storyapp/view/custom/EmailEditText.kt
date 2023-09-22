package com.hadroy.storyapp.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.hadroy.storyapp.R

class EmailEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val message = resources.getString(R.string.invalid_email)
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmail()) {
                        error = null
                    } else {
                        setError(message, null)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun CharSequence.isEmail(): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
            return true
        }
        return false
    }
}