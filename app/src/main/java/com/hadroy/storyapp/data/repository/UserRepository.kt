package com.hadroy.storyapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.hadroy.storyapp.R
import com.hadroy.storyapp.data.ResultState
import com.hadroy.storyapp.data.api.ApiService
import com.hadroy.storyapp.data.model.ErrorResponse
import com.hadroy.storyapp.data.model.LoginResult
import com.hadroy.storyapp.preferences.UserPreferences
import retrofit2.HttpException
import java.net.SocketTimeoutException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val application: Application,
    private val pref: UserPreferences
) {

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.register(name, email, password)
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

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val response = apiService.login(email, password)
            saveToken(response.loginResult)
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


    suspend fun saveToken(data: LoginResult) {
        pref.saveUserLogin(data)
    }

    fun getUserLogin(): LiveData<LoginResult> {
        return pref.getUserLogin().asLiveData()
    }

    suspend fun deleteLogin() {
        pref.deleteUserLogin()
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, application: Application, pref: UserPreferences) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, application, pref)
            }.also { instance = it }
    }
}