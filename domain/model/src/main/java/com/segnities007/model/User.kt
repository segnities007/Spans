package com.segnities007.model

data class User(
    val uuid: String,
    val googleId: String,
    val nickname: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val totalEncounters: Int = 0,
    val achievements: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val MIN_NICKNAME_LENGTH = 2
        const val MAX_NICKNAME_LENGTH = 20
        const val MAX_BIO_LENGTH = 500
    }

    fun isValidNickname(): Boolean {
        return nickname.length in MIN_NICKNAME_LENGTH..MAX_NICKNAME_LENGTH
    }

    fun isValidBio(): Boolean {
        return bio == null || bio.length <= MAX_BIO_LENGTH
    }

    fun isValid(): Boolean {
        return uuid.isNotBlank() &&
                googleId.isNotBlank() &&
                isValidNickname() &&
                isValidBio()
    }
}
