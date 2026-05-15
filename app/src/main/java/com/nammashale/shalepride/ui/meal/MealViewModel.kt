package com.nammashale.shalepride.ui.meal

import androidx.lifecycle.*
import com.nammashale.shalepride.data.model.Meal
import com.nammashale.shalepride.data.repository.MealRepository
import com.nammashale.shalepride.data.repository.PostRepository
import com.nammashale.shalepride.util.Constants
import kotlinx.coroutines.launch

class MealViewModel(
    private val mealRepository: MealRepository,
    private val postRepository: PostRepository? = null
) : ViewModel() {

    val allMeals: LiveData<List<Meal>> = mealRepository.getAllMeals().asLiveData()
    val todayMeal: LiveData<Meal?> = mealRepository.getTodayMeal().asLiveData()

    private val _uploadResult = MutableLiveData<Result<Meal>>()
    val uploadResult: LiveData<Result<Meal>> = _uploadResult

    private val _isMealUploadedToday = MutableLiveData<Boolean>()
    val isMealUploadedToday: LiveData<Boolean> = _isMealUploadedToday

    fun checkTodayMeal() {
        viewModelScope.launch {
            _isMealUploadedToday.value = mealRepository.isMealUploadedToday()
        }
    }

    fun uploadMeal(menu: String, imageUrl: String, uploadedBy: String) {
        viewModelScope.launch {
            val result = mealRepository.uploadMeal(menu, imageUrl, uploadedBy)
            _uploadResult.value = result

            // Also create a post so it appears in home feed
            result.onSuccess { meal ->
                postRepository?.createPost(
                    title = "Today's Meal Update",
                    description = "Menu: $menu",
                    imageUrl = imageUrl,
                    type = Constants.POST_TYPE_MEAL,
                    authorName = uploadedBy
                )
            }

            checkTodayMeal()
        }
    }

    fun deleteMeal(id: Long) {
        viewModelScope.launch {
            mealRepository.deleteMeal(id)
            checkTodayMeal()
        }
    }
}
