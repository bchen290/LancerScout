package com.robolancers.lancerscoutkotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PitTemplateEntity::class, MatchTemplateEntity::class], version = 1)
abstract class TemplateDatabase  : RoomDatabase() {
    abstract fun pitTemplateDao(): PitTemplateDao
    abstract fun matchTemplateDao(): MatchTemplateDao

    companion object {
        @Volatile
        private var INSTANCE: TemplateDatabase? = null

        fun getDatabase(context: Context): TemplateDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TemplateDatabase::class.java,
                    "template_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}