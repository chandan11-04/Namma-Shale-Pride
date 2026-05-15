package com.nammashale.shalepride.data.repository

import com.nammashale.shalepride.data.local.dao.PostDao
import com.nammashale.shalepride.data.local.entity.PostEntity
import com.nammashale.shalepride.data.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostRepository(private val postDao: PostDao) {

    fun getAllPosts(): Flow<List<Post>> {
        return postDao.getAllPosts().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getPostsByType(type: String): Flow<List<Post>> {
        return postDao.getPostsByType(type).map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun createPost(
        title: String,
        description: String,
        imageUrl: String,
        type: String,
        authorName: String
    ): Result<Post> {
        return try {
            val entity = PostEntity(
                title = title,
                description = description,
                imageUrl = imageUrl,
                type = type,
                authorName = authorName,
                timestamp = System.currentTimeMillis()
            )
            val id = postDao.insertPost(entity)
            Result.success(entity.copy(id = id).toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(id: Long) {
        postDao.deletePostById(id)
    }

    fun getPostCount(): Flow<Int> = postDao.getTotalPostCount()

    fun getPostCountByType(type: String): Flow<Int> = postDao.getPostCountByType(type)

    private fun PostEntity.toModel(): Post {
        return Post(
            id = id,
            title = title,
            description = description,
            imageUrl = imageUrl,
            type = type,
            authorName = authorName,
            timestamp = timestamp
        )
    }
}
