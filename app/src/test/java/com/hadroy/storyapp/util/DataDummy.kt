package com.hadroy.storyapp.util

import com.hadroy.storyapp.data.model.StoryItem
import com.hadroy.storyapp.data.model.StoryResponse

object DataDummy {

    fun generateDummyStory(): List<StoryItem> {
        val stories = mutableListOf<StoryItem>()
        for (i in 1..10) {
            val storyItem = StoryItem(
                id = i.toString(),
                name = "name $i",
                description = "desc $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                photoUrl = "photo $i",
                createdAt = "createdAr $i"
            )
            stories.add(storyItem)
        }

        return stories
    }

    fun generateDummyStoryResponse(): StoryResponse {
        val stories = mutableListOf<StoryItem>()
        for (i in 1..10) {
            val storyItem = StoryItem(
                id = i.toString(),
                name = "name $i",
                description = "desc $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                photoUrl = "photo $i",
                createdAt = "createdAr $i"
            )
            stories.add(storyItem)
        }

        return StoryResponse(
            stories,
            false,
            "Success"
        )
    }
}