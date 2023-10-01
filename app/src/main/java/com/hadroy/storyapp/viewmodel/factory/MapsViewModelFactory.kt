package com.hadroy.storyapp.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository
import com.hadroy.storyapp.di.Injection
import com.hadroy.storyapp.viewmodel.MapsViewModel

class MapsViewModelFactory(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(userRepository, storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(
                    Injection.provideUserRepository(application),
                    Injection.provideStoryRepository(application)
                )
            }.also { instance = it }
    }
}