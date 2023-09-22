package com.hadroy.storyapp.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadroy.storyapp.data.repository.UserRepository
import com.hadroy.storyapp.di.Injection
import com.hadroy.storyapp.viewmodel.LoginViewModel

class LoginViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    Injection.provideUserRepository(
                        application
                    )
                )
            }.also { instance = it }
    }
}