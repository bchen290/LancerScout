package com.robolancers.lancerscoutkotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robolancers.lancerscoutkotlin.room.daos.ScoutDataDao
import com.robolancers.lancerscoutkotlin.room.daos.TeamDao
import com.robolancers.lancerscoutkotlin.room.daos.TemplateDao
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.entities.Template

@Database(entities = [Template::class, Team::class, ScoutData::class], version = 1, exportSchema = false)
abstract class LancerDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
    abstract fun teamDao(): TeamDao
    abstract fun scoutDataDao(): ScoutDataDao

    companion object {
        @Volatile
        private var INSTANCE: LancerDatabase? = null

        fun getDatabase(context: Context): LancerDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LancerDatabase::class.java,
                    "template_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}