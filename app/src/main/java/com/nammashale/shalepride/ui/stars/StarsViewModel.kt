package com.nammashale.shalepride.ui.stars

import androidx.lifecycle.*
import com.nammashale.shalepride.data.model.StudentStar
import com.nammashale.shalepride.data.repository.PostRepository
import com.nammashale.shalepride.data.repository.StudentStarRepository
import com.nammashale.shalepride.util.Constants
import kotlinx.coroutines.launch

class StarsViewModel(
    private val repository: StudentStarRepository,
    private val postRepository: PostRepository? = null
) : ViewModel() {
    val allStars: LiveData<List<StudentStar>> = repository.getAllStudentStars().asLiveData()

    fun addStar(name: String, className: String, achievement: String, photoUrl: String) {
        viewModelScope.launch {
            repository.addStudentStar(name, className, achievement, photoUrl)

            // Also create a post so it appears in home feed
            postRepository?.createPost(
                title = "⭐ Student Star: $name",
                description = "$achievement - Class $className",
                imageUrl = photoUrl,
                type = Constants.POST_TYPE_ACHIEVEMENT,
                authorName = "Admin"
            )
        }
    }

    fun deleteStar(id: Long) {
        viewModelScope.launch { repository.deleteStudentStar(id) }
    }
}
