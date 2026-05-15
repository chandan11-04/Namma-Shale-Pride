package com.nammashale.shalepride.data.local.dao

import androidx.room.*
import com.nammashale.shalepride.data.local.entity.FeedbackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {

    @Query("SELECT * FROM feedback ORDER BY timestamp DESC")
    fun getAllFeedback(): Flow<List<FeedbackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: FeedbackEntity): Long

    @Delete
    suspend fun deleteFeedback(feedback: FeedbackEntity)

    @Query("DELETE FROM feedback WHERE id = :id")
    suspend fun deleteFeedbackById(id: Long)

    @Query("SELECT COUNT(*) FROM feedback")
    fun getFeedbackCount(): Flow<Int>
}
