package com.grig.danish.data

import com.grig.danish.data.local.NounDao
import com.grig.danish.data.model.Noun
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanishRepository @Inject constructor(
    private val danishService: DanishService,
    private val nounDao: NounDao
) {

    fun getAllNouns(): Flow<List<Noun>> = nounDao.getAll()

    fun getNounsByFolder(folder: String): Flow<List<Noun>> = nounDao.getByFolder(folder)

    suspend fun refreshNouns(): Result<Unit> {
        return danishService.getAllNouns().map { nouns ->
            nounDao.insertAll(nouns)
            Timber.d("Refreshed ${nouns.size} nouns from API")
        }
    }
}
