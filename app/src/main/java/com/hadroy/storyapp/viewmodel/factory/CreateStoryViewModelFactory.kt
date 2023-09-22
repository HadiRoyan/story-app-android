package com.hadroy.storyapp.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository
import com.hadroy.storyapp.di.Injection
import com.hadroy.storyapp.viewmodel.CreateStoryViewModel

class CreateStoryViewModelFactory(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) -> {
                CreateStoryViewModel(storyRepository, userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: CreateStoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CreateStoryViewModelFactory(
                    Injection.provideStoryRepository(application),
                    Injection.provideUserRepository(application)
                )
            }.also { instance = it }
    }
}