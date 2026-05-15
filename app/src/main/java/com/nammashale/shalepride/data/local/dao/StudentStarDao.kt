package com.nammashale.shalepride.data.local.dao

import androidx.room.*
import com.nammashale.shalepride.data.local.entity.StudentStarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentStarDao {

    @Query("SELECT * FROM student_stars ORDER BY timestamp DESC")
    fun getAllStudentStars(): Flow<List<StudentStarEntity>>

    @Query("SELECT * FROM student_stars WHERE className = :className ORDER BY timestamp DESC")
    fun getStudentStarsByClass(className: String): Flow<List<StudentStarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentStar(studentStar: StudentStarEntity): Long

    @Update
    suspend fun updateStudentStar(studentStar: StudentStarEntity)

    @Delete
    suspend fun deleteStudentStar(studentStar: StudentStarEntity)

    @Query("DELETE FROM student_stars WHERE id = :id")
    suspend fun deleteStudentStarById(id: Long)

    @Query("SELECT COUNT(*) FROM student_stars")
    fun getStudentStarCount(): Flow<Int>
}
