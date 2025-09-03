// App.kt
package com.arkanzi.today

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arkanzi.today.data.db.AppDatabase
import com.arkanzi.today.model.CalendarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    object DatabaseProvider {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase {
            val appContext = context.applicationContext
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "today_database"
            )
                .fallbackToDestructiveMigration(true)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed on first creation
                        seedDefaults(appContext)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Idempotent seed on every open to cover restores, edge cases
                        seedDefaults(appContext)
                    }
                })
                .build()
        }

        private fun seedDefaults(context: Context) {
            // Offload to IO
            CoroutineScope(Dispatchers.IO).launch {
                val db = getDatabase(context)
                // Ensure the default "Personal" calendar type exists
                db.calendarTypeDao().insertIgnore(CalendarType(name = "Personal"))
            }
        }
    }
}
