package com.hadroy.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadroy.storyapp.data.model.StoryItem
import com.hadroy.storyapp.databinding.ItemRowStoryBinding
import com.hadroy.storyapp.view.DetailStoryActivity

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val listStory = ArrayList<StoryItem>()

    class StoryViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryItem, itemView: View) {
            val imgPhoto = binding.ivItemPhoto
            val tvName = binding.tvItemName
            val tvDescription = binding.tvItemDescription

            tvName.text = story.name
            tvDescription.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(imgPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY_DATA, story)
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "image_story"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description")
                    )
                itemView.context.startActivity(intent, optionCompat.toBundle())
            }
        }

    }

    fun setListStory(newListStory: List<StoryItem>) {
        val diffCallback = StoryDiffCallback(listStory, newListStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listStory.clear()
        this.listStory.addAll(newListStory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoryViewHolder(binding)
    }

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story, holder.itemView)
    }
}