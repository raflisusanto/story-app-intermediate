package com.example.storyappsubmission.helper

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setupCombinedText(tvCombinedText: TextView, textResource: Int, targetActivity: Class<*>) {
    val combinedText = getString(textResource)
    val spannableString = SpannableString(combinedText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            val intent = Intent(this@setupCombinedText, targetActivity)
            startActivity(intent)
        }
    }

    // Set clickable part
    val clickablePartStart = 27
    spannableString.setSpan(clickableSpan, clickablePartStart, combinedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    // Set color for clickable part
    spannableString.setSpan(ForegroundColorSpan(Color.GRAY), clickablePartStart, combinedText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    tvCombinedText.text = spannableString
    tvCombinedText.movementMethod = LinkMovementMethod.getInstance() // Make the textview clickable
}

fun String.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}