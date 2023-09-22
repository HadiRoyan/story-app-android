package com.hadroy.storyapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hadroy.storyapp.R
import com.hadroy.storyapp.data.ResultState
import com.hadroy.storyapp.data.model.LoginResult
import com.hadroy.storyapp.databinding.ActivityCreateStoryBinding
import com.hadroy.storyapp.util.Utils.getImageUri
import com.hadroy.storyapp.util.Utils.reduceFileImage
import com.hadroy.storyapp.util.Utils.uriToFile
import com.hadroy.storyapp.viewmodel.CreateStoryViewModel
import com.hadroy.storyapp.viewmodel.factory.CreateStoryViewModelFactory

class CreateStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var data: LoginResult
    private var currentImageUri: Uri? = null

    companion object {
        private const val TAG = "CreateStoryActivity"
    }

    private val viewModel: CreateStoryViewModel by viewModels {
        CreateStoryViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getLoginUser().observe(this) {
            if (it != null) {
                Log.d(TAG, "getUserLogin: ${it.token}")
                data = it
                setupAction(data.token)
            }
        }

    }

    private fun setupAction(token: String) {
        binding.cameraButton.setOnClickListener {
            startCamera()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.uploadButton.setOnClickListener {
            val description = binding.edCreateDescription.text.toString()

            if (description.isNotEmpty()) {
                binding.edCreateDescriptionLayout.isErrorEnabled = false
                uploadImage(token, description)
            } else {
                binding.edCreateDescriptionLayout.error =
                    resources.getString(R.string.description_required)
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage(token: String, description: String) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            viewModel.uploadStory(token, imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showToast(result.data.message)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast(resources.getString(R.string.image_required))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}