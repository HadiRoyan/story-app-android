package com.hadroy.storyapp.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hadroy.storyapp.data.model.StoryItem
import com.hadroy.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    companion object {
        const val EXTRA_STORY_DATA = "extra_story_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTopBar()

        val storyItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra<StoryItem>(
                EXTRA_STORY_DATA,
                StoryItem::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<StoryItem>(EXTRA_STORY_DATA)
        }

        if (storyItem != null) {
            binding.tvDetailName.text = storyItem.name
            binding.tvDetailDescription.text = storyItem.description
            Glide.with(this@DetailStoryActivity)
                .load(storyItem.photoUrl)
                .centerCrop()
                .into(binding.ivDetailPhoto)
        }
    }

    private fun setTopBar() {
        binding.topAppBar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
    }
}