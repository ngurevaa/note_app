package ru.kpfu.itis.gureva.mvi.data.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.mvi.data.database.dao.GroupDao
import ru.kpfu.itis.gureva.mvi.data.database.entity.GroupEntity
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao
) {
    suspend fun getAll(): List<GroupEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext groupDao.getAll()
        }
    }
}
