package ru.kpfu.itis.gureva.mvi.data.database.entity

import androidx.room.Entity

@Entity(tableName = "tasks")
data class TaskEntity(
    val id: Int?,
    val name: String
)
