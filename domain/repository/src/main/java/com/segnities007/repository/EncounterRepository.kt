package com.segnities007.repository

import com.segnities007.model.Encounter

interface EncounterRepository {

    suspend fun getEncounters(): Result<List<Encounter>>

    suspend fun getEncounterByUuid(uuid: String): Result<Encounter>

    suspend fun recordEncounter(
        uuid: String,
        rssi: Int,
        timestamp: Long
    ): Result<Encounter>

    suspend fun syncEncounters(encounters: List<Encounter>): Result<Unit>

    suspend fun getUnsyncedEncounters(): Result<List<Encounter>>

    suspend fun enrichEncountersWithUserInfo(
        encounters: List<Encounter>
    ): Result<List<Encounter>>

    suspend fun deleteOldEncounters(olderThan: Long): Result<Int>

    suspend fun getEncounterStats(): Result<EncounterStats>
}

data class EncounterStats(
    val totalEncounters: Int,
    val uniqueUsers: Int,
    val todayEncounters: Int,
    val thisWeekEncounters: Int
)
