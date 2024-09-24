package ru.kpfu.itis.gureva.mvi.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String
)
