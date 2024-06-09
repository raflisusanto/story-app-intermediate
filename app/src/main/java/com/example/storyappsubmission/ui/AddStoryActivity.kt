package com.example.storyappsubmission.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.storyappsubmission.databinding.ActivityAddStoryBinding
import com.example.storyappsubmission.helper.getImageUri
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.helper.uriToFile
import com.example.storyappsubmission.viewmodel.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        binding.btnUpload.setButtonLabel("Upload Story")
        setContentView(binding.root)

        // ViewModels
        storyViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.btnUpload.showLoading()
            else binding.btnUpload.hideLoading()
        }

        storyViewModel.feedbackToast.observe(this) {
            it.showToast(this)
        }

        // Setup buttons
        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnUpload.setOnClick {
            uploadImage()
        }

        binding.etDescription.doOnTextChanged { desc, _, _, _ ->
            if (desc.toString().isNotEmpty()) {
                binding.etDescription.error = null
            } else {
                binding.etDescription.error = "Silahkan tulis deskripsi"
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            binding.ivThumbnail.visibility = View.GONE
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivThumbnail.setImageURI(it)
            binding.ivThumbnail.visibility = View.VISIBLE
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        if (currentImageUri == null) {
            "Silahkan pilih gambar dulu".showToast(this)
        }

        currentImageUri?.let { uri ->
            if (binding.etDescription.text.toString().isEmpty()) return

            val imageFile = uriToFile(uri, this)
            val description = binding.etDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            storyViewModel.addNewStory(multipartBody, requestBody) { goToHomeActivity() }
        }
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}