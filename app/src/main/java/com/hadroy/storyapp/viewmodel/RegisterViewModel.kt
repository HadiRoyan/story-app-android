package com.hadroy.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hadroy.storyapp.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}