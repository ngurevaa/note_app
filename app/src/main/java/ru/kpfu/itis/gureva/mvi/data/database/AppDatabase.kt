package ru.kpfu.itis.gureva.mvi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.gureva.mvi.data.database.dao.GroupDao
import ru.kpfu.itis.gureva.mvi.data.database.entity.GroupEntity

@Database(entities = [GroupEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
}
