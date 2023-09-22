package com.hadroy.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadroy.storyapp.data.model.LoginResult
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun deleteUserLogin() {
        viewModelScope.launch {
            userRepository.deleteLogin()
        }
    }

    fun getUserLogin(): LiveData<LoginResult> {
        return userRepository.getUserLogin()
    }

    fun getAllStory(token: String) = storyRepository.getAllStory(token)

}