package com.nammashale.shalepride.ui.admin

import androidx.lifecycle.*
import com.nammashale.shalepride.data.repository.*
import kotlinx.coroutines.launch

class AdminViewModel(
    private val mealRepository: MealRepository,
    private val postRepository: PostRepository,
    private val studentStarRepository: StudentStarRepository,
    private val feedbackRepository: FeedbackRepository
) : ViewModel() {

    val mealCount: LiveData<Int> = mealRepository.getMealCount().asLiveData()
    val postCount: LiveData<Int> = postRepository.getPostCount().asLiveData()
    val starCount: LiveData<Int> = studentStarRepository.getStudentStarCount().asLiveData()
    val feedbackCount: LiveData<Int> = feedbackRepository.getFeedbackCount().asLiveData()

    fun createPost(title: String, desc: String, imageUrl: String, type: String, author: String) {
        viewModelScope.launch {
            postRepository.createPost(title, desc, imageUrl, type, author)
        }
    }
}
