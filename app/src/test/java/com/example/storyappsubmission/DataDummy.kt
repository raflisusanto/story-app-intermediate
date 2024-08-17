package com.example.storyappsubmission

import com.example.storyappsubmission.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val stories: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "https://example.com/photo$i.jpg",
                createdAt = "2024-08-17T12:34:56Z",
                name = "Story $i",
                description = "This is a description for story $i.",
                lon = 100.0 + i,
                id = "story_id_$i",
                lat = 10.0 + i
            )
            stories.add(story)
        }
        return stories
    }
}