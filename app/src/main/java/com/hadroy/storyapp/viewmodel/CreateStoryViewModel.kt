package com.hadroy.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadroy.storyapp.data.model.LoginResult
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository
import java.io.File

class CreateStoryViewModel(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun uploadStory(token: String, file: File, description: String) =
        storyRepository.uploadStory(token, file, description)

    fun getLoginUser(): LiveData<LoginResult> = userRepository.getUserLogin()
}