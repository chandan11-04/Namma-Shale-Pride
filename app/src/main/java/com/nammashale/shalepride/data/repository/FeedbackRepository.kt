package com.nammashale.shalepride.data.repository

import com.nammashale.shalepride.data.local.dao.FeedbackDao
import com.nammashale.shalepride.data.local.entity.FeedbackEntity
import com.nammashale.shalepride.data.model.Feedback
import com.nammashale.shalepride.util.SentimentAnalyzer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedbackRepository(private val feedbackDao: FeedbackDao) {

    fun getAllFeedback(): Flow<List<Feedback>> {
        return feedbackDao.getAllFeedback().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun submitFeedback(
        message: String,
        isAnonymous: Boolean,
        senderName: String,
        senderEmail: String
    ): Result<Feedback> {
        return try {
            if (message.isBlank()) {
                return Result.failure(Exception("Feedback message is required"))
            }

            val sentiment = SentimentAnalyzer.analyze(message)
            val entity = FeedbackEntity(
                message = message,
                isAnonymous = isAnonymous,
                senderName = if (isAnonymous) "" else senderName,
                senderEmail = if (isAnonymous) "" else senderEmail,
                timestamp = System.currentTimeMillis(),
                sentiment = sentiment
            )

            val id = feedbackDao.insertFeedback(entity)
            Result.success(entity.copy(id = id).toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFeedback(id: Long) {
        feedbackDao.deleteFeedbackById(id)
    }

    fun getFeedbackCount(): Flow<Int> = feedbackDao.getFeedbackCount()

    private fun FeedbackEntity.toModel(): Feedback {
        return Feedback(
            id = id,
            message = message,
            isAnonymous = isAnonymous,
            senderName = senderName,
            senderEmail = senderEmail,
            timestamp = timestamp,
            sentiment = sentiment
        )
    }
}
