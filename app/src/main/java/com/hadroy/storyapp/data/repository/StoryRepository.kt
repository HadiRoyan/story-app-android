package com.hadroy.storyapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.hadroy.storyapp.R
import com.hadroy.storyapp.data.ResultState
import com.hadroy.storyapp.data.api.ApiService
import com.hadroy.storyapp.data.model.ErrorResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val application: Application
) {


    fun getAllStory(token: String) = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.getStories("Bearer $token")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(ResultState.Error(errorMessage.toString()))
        } catch (e: SocketTimeoutException) {
            emit(ResultState.Error(application.resources.getString(R.string.no_internet_message)))
        } catch (e: Exception) {
            Log.e(TAG, "getAllStory: ${e.message}", e)
            emit(ResultState.Error(application.resources.getString(R.string.something_failed_message)))
        }
    }

    fun uploadStory(token: String, imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val accessToken = "Bearer $token"
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.postStory(accessToken, multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: SocketTimeoutException) {
            emit(ResultState.Error(application.resources.getString(R.string.no_internet_message)))
        } catch (e: Exception) {
            Log.e(TAG, "getAllStory: ${e.message}", e)
            emit(ResultState.Error(application.resources.getString(R.string.something_failed_message)))
        }
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, application: Application) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, application)
            }.also { instance = it }
    }
}