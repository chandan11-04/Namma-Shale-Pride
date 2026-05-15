package com.nammashale.shalepride.ui.home

import androidx.lifecycle.*
import com.nammashale.shalepride.data.model.Post
import com.nammashale.shalepride.data.repository.PostRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val postRepository: PostRepository) : ViewModel() {

    val posts: LiveData<List<Post>> = postRepository.getAllPosts().asLiveData()

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun refresh() {
        _isRefreshing.value = true
        _isRefreshing.value = false
    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            postRepository.deletePost(id)
        }
    }

    fun createPost(title: String, desc: String, imageUrl: String, type: String, author: String) {
        viewModelScope.launch {
            postRepository.createPost(title, desc, imageUrl, type, author)
        }
    }
}
