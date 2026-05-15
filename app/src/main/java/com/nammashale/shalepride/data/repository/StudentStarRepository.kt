package com.nammashale.shalepride.data.repository

import com.nammashale.shalepride.data.local.dao.StudentStarDao
import com.nammashale.shalepride.data.local.entity.StudentStarEntity
import com.nammashale.shalepride.data.model.StudentStar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudentStarRepository(private val studentStarDao: StudentStarDao) {

    fun getAllStudentStars(): Flow<List<StudentStar>> {
        return studentStarDao.getAllStudentStars().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getStudentStarsByClass(className: String): Flow<List<StudentStar>> {
        return studentStarDao.getStudentStarsByClass(className).map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun addStudentStar(
        name: String,
        className: String,
        achievement: String,
        photoUrl: String
    ): Result<StudentStar> {
        return try {
            val entity = StudentStarEntity(
                name = name,
                className = className,
                achievement = achievement,
                photoUrl = photoUrl,
                timestamp = System.currentTimeMillis()
            )
            val id = studentStarDao.insertStudentStar(entity)
            Result.success(entity.copy(id = id).toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteStudentStar(id: Long) {
        studentStarDao.deleteStudentStarById(id)
    }

    fun getStudentStarCount(): Flow<Int> = studentStarDao.getStudentStarCount()

    private fun StudentStarEntity.toModel(): StudentStar {
        return StudentStar(
            id = id,
            name = name,
            className = className,
            achievement = achievement,
            photoUrl = photoUrl,
            timestamp = timestamp
        )
    }
}
