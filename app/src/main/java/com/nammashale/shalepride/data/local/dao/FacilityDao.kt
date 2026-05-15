package com.nammashale.shalepride.data.local.dao

import androidx.room.*
import com.nammashale.shalepride.data.local.entity.FacilityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FacilityDao {

    @Query("SELECT * FROM facilities ORDER BY category ASC")
    fun getAllFacilities(): Flow<List<FacilityEntity>>

    @Query("SELECT * FROM facilities WHERE category = :category ORDER BY name ASC")
    fun getFacilitiesByCategory(category: String): Flow<List<FacilityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFacility(facility: FacilityEntity): Long

    @Update
    suspend fun updateFacility(facility: FacilityEntity)

    @Delete
    suspend fun deleteFacility(facility: FacilityEntity)

    @Query("DELETE FROM facilities WHERE id = :id")
    suspend fun deleteFacilityById(id: Long)
}
