package com.hadroy.storyapp.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadroy.storyapp.data.repository.UserRepository
import com.hadroy.storyapp.di.Injection
import com.hadroy.storyapp.viewmodel.RegisterViewModel

class RegisterViewModelFactory(
    private val userRepository: UserRepository,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(
                    Injection.provideUserRepository(
                        application
                    )
                )
            }.also { instance = it }
    }
}