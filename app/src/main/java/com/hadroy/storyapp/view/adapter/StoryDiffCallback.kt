package com.hadroy.storyapp.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hadroy.storyapp.data.model.StoryItem

class StoryDiffCallback(
    private val oldStoryList: List<StoryItem>,
    private val newStoryList: List<StoryItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldStoryList.size
    }

    override fun getNewListSize(): Int {
        return newStoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStoryList[oldItemPosition].id == newStoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStory = oldStoryList[oldItemPosition]
        val newStory = newStoryList[newItemPosition]

        return oldStory.id == newStory.id &&
                oldStory.name == newStory.name &&
                oldStory.description == newStory.description &&
                oldStory.photoUrl == newStory.photoUrl

    }
}