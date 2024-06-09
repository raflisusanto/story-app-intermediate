package com.example.storyappsubmission.helper

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import com.example.storyappsubmission.BuildConfig
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun setupEmailValidation(etEmail: EditText, tilEmail: TextInputLayout) {
    etEmail.doOnTextChanged { text, _, _, _ ->
        val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        if (!isValidEmail) {
            tilEmail.error = "Email tidak valid"
        } else {
            tilEmail.error = null
        }
    }
}

fun setupPasswordValidation(cvPassword: EditText, tilPassword: TextInputLayout) {
    cvPassword.doOnTextChanged { text, _, _, _ ->
        if (text.toString().length < 8) {
            tilPassword.error = "Password harus lebih dari 8 karakter"
        } else {
            tilPassword.error = null
        }
    }
}

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}