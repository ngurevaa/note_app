package ru.kpfu.itis.gureva.mvi.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.kpfu.itis.gureva.mvi.data.database.entity.GroupEntity

@Dao
interface GroupDao {
    @Query("select * from groups")
    fun getAll(): List<GroupEntity>
}