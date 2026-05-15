package com.nammashale.shalepride.ui.feedback

import androidx.lifecycle.*
import com.nammashale.shalepride.data.model.Feedback
import com.nammashale.shalepride.data.repository.FeedbackRepository
import kotlinx.coroutines.launch

class FeedbackViewModel(private val repository: FeedbackRepository) : ViewModel() {
    val allFeedback: LiveData<List<Feedback>> = repository.getAllFeedback().asLiveData()

    private val _submitResult = MutableLiveData<Result<Feedback>>()
    val submitResult: LiveData<Result<Feedback>> = _submitResult

    fun submitFeedback(message: String, isAnonymous: Boolean, name: String, email: String) {
        viewModelScope.launch {
            _submitResult.value = repository.submitFeedback(message, isAnonymous, name, email)
        }
    }

    fun deleteFeedback(id: Long) {
        viewModelScope.launch { repository.deleteFeedback(id) }
    }
}
