package com.robolancers.lancerscoutkotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robolancers.lancerscoutkotlin.room.daos.MatchTemplateDao
import com.robolancers.lancerscoutkotlin.room.daos.PitTemplateDao
import com.robolancers.lancerscoutkotlin.room.entities.MatchTemplate
import com.robolancers.lancerscoutkotlin.room.entities.PitTemplate

@Database(entities = [PitTemplate::class, MatchTemplate::class], version = 1, exportSchema = false)
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
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}