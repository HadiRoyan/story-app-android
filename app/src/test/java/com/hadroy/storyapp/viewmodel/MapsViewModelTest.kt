package com.hadroy.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hadroy.storyapp.data.ResultState
import com.hadroy.storyapp.data.model.StoryResponse
import com.hadroy.storyapp.data.repository.StoryRepository
import com.hadroy.storyapp.data.repository.UserRepository
import com.hadroy.storyapp.util.DataDummy
import com.hadroy.storyapp.util.MainDispatcherRule
import com.hadroy.storyapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var mapsViewModel: MapsViewModel

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(userRepository, storyRepository)
    }

    @Test
    fun `when Get Story With Location Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val expectedStory = MutableLiveData<ResultState<StoryResponse>>()
        expectedStory.value = ResultState.Success(dummyStory)
        Mockito.`when`(storyRepository.getStoriesWithLocation("TOKEN")).thenReturn(expectedStory)

        val actualStory: ResultState<StoryResponse> =
            mapsViewModel.getStoriesWithLocation("TOKEN").getOrAwaitValue()

        assertNotNull(actualStory)
        assertEquals(actualStory, ResultState.Success(dummyStory))
    }

    @Test
    fun `when Get Story With Location Should Return Error`() = runTest {
        val expectedStory = MutableLiveData<ResultState<StoryResponse>>()
        expectedStory.value = ResultState.Error("error")
        Mockito.`when`(storyRepository.getStoriesWithLocation("TOKEN")).thenReturn(expectedStory)

        val actualStory: ResultState<StoryResponse> =
            mapsViewModel.getStoriesWithLocation("TOKEN").getOrAwaitValue()

        assertNotNull(actualStory)
        assertEquals(actualStory, ResultState.Error("error"))
    }
}