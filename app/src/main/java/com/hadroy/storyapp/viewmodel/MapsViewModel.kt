package com.hadroy.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository

class MapsViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getStoriesWithLocation(token: String) = storyRepository.getStoriesWithLocation(token)

    fun getUserLogin() = userRepository.getUserLogin()
}