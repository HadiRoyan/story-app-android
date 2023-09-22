package com.hadroy.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hadroy.storyapp.data.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun getUserLogin() = userRepository.getUserLogin()
}