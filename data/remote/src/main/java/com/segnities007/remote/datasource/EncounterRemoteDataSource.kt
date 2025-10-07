package com.segnities007.remote.datasource

import com.segnities007.model.Encounter
import com.segnities007.repository.EncounterStats

/**
 * すれ違い関連のリモートデータソースインターフェース
 * 
 * Supabase PostgREST APIとの通信を担当
 */
interface EncounterRemoteDataSource {
    
    /**
     * すれ違い情報をサーバーに同期
     */
    suspend fun syncEncounters(encounters: List<Encounter>): Result<Unit>
    
    /**
     * サーバーからすれ違い情報を取得
     */
    suspend fun getEncounters(): Result<List<Encounter>>
    
    /**
     * 特定のユーザーとのすれ違い情報を取得
     */
    suspend fun getEncounterByUuid(uuid: String): Result<Encounter>
    
    /**
     * ユーザー情報ですれ違い情報を補完
     */
    suspend fun enrichEncountersWithUserInfo(
        encounters: List<Encounter>
    ): Result<List<Encounter>>
    
    /**
     * すれ違い統計を取得
     */
    suspend fun getEncounterStats(): Result<EncounterStats>
}
