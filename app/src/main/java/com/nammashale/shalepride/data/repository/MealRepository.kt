package com.nammashale.shalepride.data.repository

import com.nammashale.shalepride.data.local.dao.MealDao
import com.nammashale.shalepride.data.local.entity.MealEntity
import com.nammashale.shalepride.data.model.Meal
import com.nammashale.shalepride.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepository(private val mealDao: MealDao) {

    fun getAllMeals(): Flow<List<Meal>> {
        return mealDao.getAllMeals().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getTodayMeal(): Flow<Meal?> {
        val today = DateUtils.getTodayDateString()
        return mealDao.getMealByDateFlow(today).map { it?.toModel() }
    }

    suspend fun isMealUploadedToday(): Boolean {
        val today = DateUtils.getTodayDateString()
        return mealDao.getMealByDate(today) != null
    }

    suspend fun uploadMeal(menu: String, imageUrl: String, uploadedBy: String): Result<Meal> {
        return try {
            val today = DateUtils.getTodayDateString()

            // Check one-per-day rule
            val existingMeal = mealDao.getMealByDate(today)
            if (existingMeal != null) {
                return Result.failure(Exception("Today's meal already uploaded"))
            }

            val entity = MealEntity(
                date = today,
                menu = menu,
                imageUrl = imageUrl,
                uploadedBy = uploadedBy,
                timestamp = System.currentTimeMillis()
            )

            val id = mealDao.insertMeal(entity)
            Result.success(entity.copy(id = id).toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMeal(id: Long) {
        mealDao.deleteMealById(id)
    }

    fun getMealCount(): Flow<Int> = mealDao.getMealCount()

    private fun MealEntity.toModel(): Meal {
        return Meal(
            id = id,
            date = date,
            menu = menu,
            imageUrl = imageUrl,
            uploadedBy = uploadedBy,
            timestamp = timestamp
        )
    }
}
