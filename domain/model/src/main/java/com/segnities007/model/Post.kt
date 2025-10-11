package com.segnities007.model

data class Post(
    val id: String,
    val authorUuid: String,
    val authorNickname: String,
    val authorAvatar: String? = null,
    val content: String,
    val mediaUrl: String? = null,
    val mediaType: MediaType? = null,
    val thumbnailUrl: String? = null,
    val likeCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val MIN_CONTENT_LENGTH = 1
        const val MAX_CONTENT_LENGTH = 256
        const val MAX_IMAGE_SIZE_MB = 10
        const val MAX_VIDEO_SIZE_MB = 50
        const val MAX_VIDEO_DURATION_SECONDS = 60
        
        /**
         * 投稿内容のバリデーションを実行し、エラーメッセージを返す
         * 
         * @return エラーメッセージ（エラーがない場合はnull）
         */
        fun validateContent(content: String): String? {
            return when {
                content.isBlank() -> "投稿内容を入力してください"
                content.length < MIN_CONTENT_LENGTH ->
                    "投稿内容は${MIN_CONTENT_LENGTH}文字以上で入力してください"
                content.length > MAX_CONTENT_LENGTH ->
                    "投稿内容は${MAX_CONTENT_LENGTH}文字以内で入力してください"
                else -> null
            }
        }
        
        /**
         * 投稿IDのバリデーションを実行し、エラーメッセージを返す
         * 
         * @return エラーメッセージ（エラーがない場合はnull）
         */
        fun validatePostId(postId: String): String? {
            return if (postId.isBlank()) {
                "投稿IDが無効です"
            } else {
                null
            }
        }
    }

    enum class MediaType {
        IMAGE,
        VIDEO
    }

    fun hasMedia(): Boolean {
        return mediaUrl != null && mediaType != null
    }

    fun isImagePost(): Boolean {
        return mediaType == MediaType.IMAGE
    }

    fun isVideoPost(): Boolean {
        return mediaType == MediaType.VIDEO
    }

    fun toggleLike(): Post {
        return if (isLikedByMe) {
            copy(
                isLikedByMe = false,
                likeCount = (likeCount - 1).coerceAtLeast(0)
            )
        } else {
            copy(
                isLikedByMe = true,
                likeCount = likeCount + 1
            )
        }
    }
}
