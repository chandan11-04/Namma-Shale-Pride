package com.nammashale.shalepride.data.local.dao

import androidx.room.*
import com.nammashale.shalepride.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE type = :type ORDER BY timestamp DESC")
    fun getPostsByType(type: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Update
    suspend fun updatePost(post: PostEntity)

    @Delete
    suspend fun deletePost(post: PostEntity)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePostById(id: Long)

    @Query("SELECT COUNT(*) FROM posts WHERE type = :type")
    fun getPostCountByType(type: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM posts")
    fun getTotalPostCount(): Flow<Int>
}
