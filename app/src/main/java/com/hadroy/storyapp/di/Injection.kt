package com.hadroy.storyapp.di

import android.app.Application
import com.hadroy.storyapp.data.api.ApiConfig
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository
import com.hadroy.storyapp.preferences.UserPreferences
import com.hadroy.storyapp.preferences.dataStore

object Injection {

    fun provideUserRepository(application: Application): UserRepository {
        val pref = UserPreferences.getInstance(application.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, application, pref)
    }

    fun provideStoryRepository(application: Application): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, application)
    }
}