package com.robolancers.lancerscoutkotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robolancers.lancerscoutkotlin.room.daos.TeamDao
import com.robolancers.lancerscoutkotlin.room.daos.TemplateDao
import com.robolancers.lancerscoutkotlin.room.entities.Template

@Database(entities = [Template::class], version = 3, exportSchema = false)
abstract class TemplateDatabase  : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
    abstract fun teamDao(): TeamDao

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