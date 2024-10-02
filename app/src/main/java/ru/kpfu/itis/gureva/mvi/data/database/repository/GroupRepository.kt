package ru.kpfu.itis.gureva.mvi.data.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.mvi.R
import ru.kpfu.itis.gureva.mvi.data.database.dao.GroupDao
import ru.kpfu.itis.gureva.mvi.data.database.entity.GroupEntity
import ru.kpfu.itis.gureva.mvi.util.ResourceManager
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val resourceManager: ResourceManager
) {
    suspend fun getAll(): List<GroupEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext groupDao.getAll()
        }
    }

    suspend fun init() {
        withContext(Dispatchers.IO) {
            groupDao.add(GroupEntity(1, resourceManager.getString(R.string.today)))
            groupDao.add(GroupEntity(2, resourceManager.getString(R.string.all)))
        }
    }

    suspend fun getByName(name: String): GroupEntity? {
        return withContext(Dispatchers.IO) {
            return@withContext groupDao.getByName(name)
        }
    }

    suspend fun add(name: String) {
        withContext(Dispatchers.IO) {
            groupDao.add(GroupEntity(null, name))
        }
    }
}
