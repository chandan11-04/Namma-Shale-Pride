package com.nammashale.shalepride.data.repository

import com.nammashale.shalepride.data.local.dao.FacilityDao
import com.nammashale.shalepride.data.local.entity.FacilityEntity
import com.nammashale.shalepride.data.model.Facility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FacilityRepository(private val facilityDao: FacilityDao) {

    fun getAllFacilities(): Flow<List<Facility>> {
        return facilityDao.getAllFacilities().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getFacilitiesByCategory(category: String): Flow<List<Facility>> {
        return facilityDao.getFacilitiesByCategory(category).map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun addFacility(
        name: String,
        description: String,
        imageUrl: String,
        category: String
    ): Result<Facility> {
        return try {
            val entity = FacilityEntity(
                name = name,
                description = description,
                imageUrl = imageUrl,
                category = category
            )
            val id = facilityDao.insertFacility(entity)
            Result.success(entity.copy(id = id).toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFacility(id: Long) {
        facilityDao.deleteFacilityById(id)
    }

    private fun FacilityEntity.toModel(): Facility {
        return Facility(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            category = category
        )
    }
}
