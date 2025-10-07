package com.segnities007.db.util

object CacheConfig {
    const val POST_CACHE_DURATION_MS = 30 * 60 * 1000L // 30分
    const val ENCOUNTER_RETENTION_DURATION_MS = 24 * 60 * 60 * 1000L // 24時間
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_CACHE_SIZE = 500
    
    fun isExpired(cachedAt: Long, durationMs: Long = POST_CACHE_DURATION_MS): Boolean {
        return System.currentTimeMillis() - cachedAt > durationMs
    }
    
    fun getExpiryTime(durationMs: Long = POST_CACHE_DURATION_MS): Long {
        return System.currentTimeMillis() - durationMs
    }
}
