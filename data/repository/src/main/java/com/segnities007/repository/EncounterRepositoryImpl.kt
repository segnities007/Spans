package com.segnities007.repository

import com.segnities007.model.Encounter
import com.segnities007.repository.EncounterRepository
import com.segnities007.repository.EncounterStats
import com.segnities007.remote.datasource.EncounterRemoteDataSource
import com.segnities007.db.dao.EncounterDao

class EncounterRepositoryImpl(
    private val remoteDataSource: EncounterRemoteDataSource,
    private val encounterDao: EncounterDao? = null
) : EncounterRepository {
    
    override suspend fun getEncounters(): Result<List<Encounter>> {
        return remoteDataSource.getEncounters()
    }
    
    override suspend fun getEncounterByUuid(uuid: String): Result<Encounter> {
        return remoteDataSource.getEncounterByUuid(uuid)
    }
    
    override suspend fun recordEncounter(
        uuid: String,
        rssi: Int,
        timestamp: Long
    ): Result<Encounter> {
        // TODO: BLE Service実装時にローカルDBへ記録
        // 現在は仮実装
        val encounter = Encounter(
            id = "temp-encounter-${timestamp}",
            userUuidA = "current-user-uuid", // TODO: AuthRepositoryから取得
            userUuidB = uuid,
            userANickname = null,
            userBNickname = null,
            userAAvatar = null,
            userBAvatar = null,
            lastEncounterAt = timestamp,
            encounterCount = 1,
            averageRssi = rssi,
            rssiHistory = listOf(rssi),
            createdAt = timestamp,
            updatedAt = timestamp
        )
        
        return Result.success(encounter)
    }
    
    override suspend fun syncEncounters(encounters: List<Encounter>): Result<Unit> {
        return remoteDataSource.syncEncounters(encounters)
    }
    
    override suspend fun getUnsyncedEncounters(): Result<List<Encounter>> {
        // TODO: Room実装時にローカルDBから未同期データを取得
        return Result.success(emptyList())
    }
    
    override suspend fun enrichEncountersWithUserInfo(
        encounters: List<Encounter>
    ): Result<List<Encounter>> {
        return remoteDataSource.enrichEncountersWithUserInfo(encounters)
    }
    
    override suspend fun deleteOldEncounters(olderThan: Long): Result<Int> {
        // TODO: Room実装時にローカルDBから古いデータを削除
        return Result.success(0)
    }
    
    override suspend fun getEncounterStats(): Result<EncounterStats> {
        return remoteDataSource.getEncounterStats()
    }
}
