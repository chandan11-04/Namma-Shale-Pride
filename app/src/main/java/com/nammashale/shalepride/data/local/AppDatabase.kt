package com.nammashale.shalepride.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nammashale.shalepride.data.local.dao.*
import com.nammashale.shalepride.data.local.entity.*
import com.nammashale.shalepride.util.Constants

@Database(
    entities = [
        MealEntity::class,
        PostEntity::class,
        FeedbackEntity::class,
        StudentStarEntity::class,
        FacilityEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao
    abstract fun postDao(): PostDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun studentStarDao(): StudentStarDao
    abstract fun facilityDao(): FacilityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
