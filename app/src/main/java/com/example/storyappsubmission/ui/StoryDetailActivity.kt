package com.example.storyappsubmission.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.databinding.ActivityStoryDetailBinding
import com.example.storyappsubmission.helper.loadImage
import com.example.storyappsubmission.ui.adapter.StoryAdapter

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Requires Tiramisu (SDK >= 33)
        @Suppress("CanBeVal") var story: ListStoryItem?
        if (Build.VERSION.SDK_INT >= 33) {
            story = intent.getParcelableExtra(StoryAdapter.EXTRA_STORY, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            story = intent.getParcelableExtra(StoryAdapter.EXTRA_STORY)
        }

        if (story != null) {
            with(binding) {
                tvCardTitle.text = story.name
                tvCardDesc.text = story.description
                story.photoUrl?.let {
                    ivThumbnail.loadImage(it)
                }
            }
        }

    }
}