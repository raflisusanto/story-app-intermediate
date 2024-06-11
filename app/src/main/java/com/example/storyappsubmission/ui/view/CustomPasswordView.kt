package com.example.storyappsubmission.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyappsubmission.R

class CustomPasswordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    // Shown when pass is visible
    private var visibilityOnIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24)!!
    // Shown when pass is not visible
    private var visibilityOffIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)!!
    private var isPasswordVisible = false

    init {
        // Set the initial icon
        setButtonDrawables(visibilityOffIcon)

        // Set input type
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun showPassword() {
        isPasswordVisible = true
        setButtonDrawables(visibilityOnIcon)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    private fun hidePassword() {
        isPasswordVisible = false
        setButtonDrawables(visibilityOffIcon)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun setButtonDrawables(drawable: Drawable?) {
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        if (event.action == android.view.MotionEvent.ACTION_UP && event.rawX >= right - compoundDrawables[2].bounds.width()) {
            if (isPasswordVisible) hidePassword() else showPassword()
            performClick()
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text != null && text.length < 8) setError("Password Minimal 8 Karakter", null)
    }
}
