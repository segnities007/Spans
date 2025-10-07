package com.segnities007.remote.datasource.mock

import com.segnities007.model.Encounter
import com.segnities007.remote.datasource.EncounterRemoteDataSource
import com.segnities007.repository.EncounterStats
import kotlinx.coroutines.delay
import java.time.Instant

/**
 * EncounterRemoteDataSourceのモック実装
 * 
 * Supabase実装前のテスト用
 */
class MockEncounterRemoteDataSource : EncounterRemoteDataSource {
    
    private val mockEncounters = mutableListOf<Encounter>()
    
    init {
        // サンプルデータ
        val now = Instant.now().toEpochMilli()
        mockEncounters.add(
            Encounter(
                id = "mock-encounter-1",
                userUuidA = "current-user-uuid",
                userUuidB = "mock-user-1",
                userANickname = "MyUser",
                userBNickname = "Taro",
                userAAvatar = null,
                userBAvatar = null,
                lastEncounterAt = Instant.now().minusSeconds(3600).toEpochMilli(),
                encounterCount = 5,
                averageRssi = -65,
                rssiHistory = listOf(-60, -65, -70),
                createdAt = now - 7200000,
                updatedAt = Instant.now().minusSeconds(3600).toEpochMilli()
            )
        )
        mockEncounters.add(
            Encounter(
                id = "mock-encounter-2",
                userUuidA = "current-user-uuid",
                userUuidB = "mock-user-2",
                userANickname = "MyUser",
                userBNickname = "Hanako",
                userAAvatar = null,
                userBAvatar = null,
                lastEncounterAt = Instant.now().minusSeconds(7200).toEpochMilli(),
                encounterCount = 3,
                averageRssi = -75,
                rssiHistory = listOf(-70, -75, -80),
                createdAt = now - 14400000,
                updatedAt = Instant.now().minusSeconds(7200).toEpochMilli()
            )
        )
    }
    
    override suspend fun syncEncounters(encounters: List<Encounter>): Result<Unit> {
        delay(800) // バッチ同期の遅延をシミュレート
        
        // 既存のすれ違い情報を更新、新規は追加
        encounters.forEach { newEncounter ->
            val index = mockEncounters.indexOfFirst { it.id == newEncounter.id }
            if (index != -1) {
                mockEncounters[index] = newEncounter
            } else {
                mockEncounters.add(newEncounter)
            }
        }
        
        return Result.success(Unit)
    }
    
    override suspend fun getEncounters(): Result<List<Encounter>> {
        delay(500)
        
        return Result.success(mockEncounters.toList())
    }
    
    override suspend fun getEncounterByUuid(uuid: String): Result<Encounter> {
        delay(300)
        
        val encounter = mockEncounters.find { it.userUuidB == uuid || it.userUuidA == uuid }
        return if (encounter != null) {
            Result.success(encounter)
        } else {
            Result.failure(NoSuchElementException("すれ違い情報が見つかりません"))
        }
    }
    
    override suspend fun enrichEncountersWithUserInfo(
        encounters: List<Encounter>
    ): Result<List<Encounter>> {
        delay(600)
        
        // モックではUserRemoteDataSourceにアクセスできないので、そのまま返す
        // 実際の実装では、各UUIDに対してユーザー情報を取得してマージ
        return Result.success(encounters)
    }
    
    override suspend fun getEncounterStats(): Result<EncounterStats> {
        delay(400)
        
        val totalEncounters = mockEncounters.size
        val last7DaysEncounters = mockEncounters.count {
            val sevenDaysAgo = Instant.now().minusSeconds(7 * 24 * 3600).toEpochMilli()
            it.lastEncounterAt >= sevenDaysAgo
        }
        val totalInteractions = mockEncounters.sumOf { it.encounterCount }
        
        // ユニークユーザー数（重複を除外）
        val uniqueUserUuids = mockEncounters.flatMap { listOf(it.userUuidA, it.userUuidB) }.toSet()
        val uniqueUsers = uniqueUserUuids.size
        
        val stats = EncounterStats(
            totalEncounters = totalEncounters,
            uniqueUsers = uniqueUsers,
            todayEncounters = last7DaysEncounters, // 今日のすれ違い（簡略化）
            thisWeekEncounters = last7DaysEncounters
        )
        
        return Result.success(stats)
    }
}
