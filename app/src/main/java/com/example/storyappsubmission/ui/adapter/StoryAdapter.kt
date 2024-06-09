package com.example.storyappsubmission.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.databinding.StoryCardBinding
import com.example.storyappsubmission.helper.loadImage
import com.example.storyappsubmission.ui.StoryDetailActivity

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    class StoryViewHolder(val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            with(binding) {
                tvCardTitle.text = story.name
                tvCardDesc.text = story.description

                story.photoUrl?.let {
                    ivThumbnail.loadImage(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)

        // Navigate to Detail Listener
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    androidx.core.util.Pair(holder.binding.ivThumbnail, "thumbnail"),
                    androidx.core.util.Pair(holder.binding.tvCardTitle, "title"),
                    androidx.core.util.Pair(holder.binding.tvCardDesc, "desc"),
                )

            val goToUserDetailsActivity = Intent(context, StoryDetailActivity::class.java)
            goToUserDetailsActivity.putExtra(EXTRA_STORY, story)
            context.startActivity(goToUserDetailsActivity, optionsCompat.toBundle())
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }

        var EXTRA_STORY = "extra_story"
    }
}