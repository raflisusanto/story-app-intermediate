package com.example.storyappsubmission.helper

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

private const val MAXIMAL_SIZE = 1000000

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
    val config: Configuration = resources.configuration
    val currentLocale: Locale = config.locales.get(0)
    var clickablePartStart = 27

    if (currentLocale.language.equals(Locale("en").language)) {
        // Do something specific for English locale
        clickablePartStart = 25
    }

    spannableString.setSpan(clickableSpan, clickablePartStart, combinedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    // Set color for clickable part
    spannableString.setSpan(ForegroundColorSpan(Color.GRAY), clickablePartStart, combinedText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    tvCombinedText.text = spannableString
    tvCombinedText.movementMethod = LinkMovementMethod.getInstance() // Make the textview clickable
}

fun String.showToast(context: Context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .transform(CenterCrop(), RoundedCorners(8))
        .into(this)
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}