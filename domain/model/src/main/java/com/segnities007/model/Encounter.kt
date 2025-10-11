package com.segnities007.model

data class Encounter(
    val id: String,
    val userUuidA: String,
    val userUuidB: String,
    val userANickname: String? = null,
    val userBNickname: String? = null,
    val userAAvatar: String? = null,
    val userBAvatar: String? = null,
    val lastEncounterAt: Long,
    val encounterCount: Int = 1,
    val averageRssi: Int? = null,
    val rssiHistory: List<Int> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val MAX_RSSI_HISTORY_SIZE = 10
        const val MIN_DETECTION_RANGE_METERS = 10
        const val RSSI_HIGH_QUALITY = -60 // 近距離
        const val RSSI_MEDIUM_QUALITY = -75 // 中距離
        const val RSSI_LOW_QUALITY = -90 // 遠距離（検出閾値）
        const val DUPLICATE_DETECTION_THRESHOLD_MILLIS = 5 * 60 * 1000L // 5分
    }

    enum class EncounterQuality {
        HIGH,
        MEDIUM,
        LOW,
        UNKNOWN
    }

    fun getQuality(): EncounterQuality {
        return when {
            averageRssi == null -> EncounterQuality.UNKNOWN
            averageRssi >= RSSI_HIGH_QUALITY -> EncounterQuality.HIGH
            averageRssi >= RSSI_MEDIUM_QUALITY -> EncounterQuality.MEDIUM
            averageRssi >= RSSI_LOW_QUALITY -> EncounterQuality.LOW
            else -> EncounterQuality.UNKNOWN
        }
    }

    fun isEncounterWith(uuid: String): Boolean {
        return userUuidA == uuid || userUuidB == uuid
    }

    fun getPartnerUuid(myUuid: String): String? {
        return when (myUuid) {
            userUuidA -> userUuidB
            userUuidB -> userUuidA
            else -> null
        }
    }

    fun getPartnerNickname(myUuid: String): String? {
        return when (myUuid) {
            userUuidA -> userBNickname
            userUuidB -> userANickname
            else -> null
        }
    }

    fun getPartnerAvatar(myUuid: String): String? {
        return when (myUuid) {
            userUuidA -> userBAvatar
            userUuidB -> userAAvatar
            else -> null
        }
    }

    fun addRssi(rssi: Int): Encounter {
        val newHistory = (rssiHistory + rssi).takeLast(MAX_RSSI_HISTORY_SIZE)
        val newAverage = newHistory.average().toInt()
        return copy(
            averageRssi = newAverage,
            rssiHistory = newHistory
        )
    }

    fun incrementCount(timestamp: Long): Encounter {
        return copy(
            encounterCount = encounterCount + 1,
            lastEncounterAt = timestamp,
            updatedAt = timestamp
        )
    }

    fun isDuplicateDetection(currentTimestamp: Long): Boolean {
        return (currentTimestamp - lastEncounterAt) < DUPLICATE_DETECTION_THRESHOLD_MILLIS
    }

    fun isValid(): Boolean {
        return id.isNotBlank() &&
                userUuidA.isNotBlank() &&
                userUuidB.isNotBlank() &&
                userUuidA < userUuidB && // 辞書順の制約
                encounterCount > 0
    }
}
