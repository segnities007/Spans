package com.segnities007.model

data class Like(
    val id: String,
    val userUuid: String,
    val postId: String,
    val createdAt: Long
) {
    fun isValid(): Boolean {
        return id.isNotBlank() &&
                userUuid.isNotBlank() &&
                postId.isNotBlank()
    }
}
