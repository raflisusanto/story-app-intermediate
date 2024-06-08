package com.example.storyappsubmission.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.databinding.StoryCardBinding
import com.example.storyappsubmission.helper.loadImage

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    class StoryViewHolder(private val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
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
            // todo: navigate to detail
//            val context = holder.itemView.context
//            val goToUserDetailsActivity = Intent(context, UserDetailActivity::class.java)
//            goToUserDetailsActivity.putExtra(EXTRA_ID, story.id)
//            context.startActivity(goToUserDetailsActivity)
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

        var EXTRA_ID = "extra_id"
    }
}