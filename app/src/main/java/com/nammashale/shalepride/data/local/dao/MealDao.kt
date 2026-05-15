package com.nammashale.shalepride.data.local.dao

import androidx.room.*
import com.nammashale.shalepride.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meals ORDER BY timestamp DESC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE date = :date LIMIT 1")
    suspend fun getMealByDate(date: String): MealEntity?

    @Query("SELECT * FROM meals WHERE date = :date LIMIT 1")
    fun getMealByDateFlow(date: String): Flow<MealEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteMealById(id: Long)

    @Query("SELECT COUNT(*) FROM meals")
    fun getMealCount(): Flow<Int>
}
