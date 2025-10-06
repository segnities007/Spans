package com.segnities007.usecase.post

import com.segnities007.model.Post
import com.segnities007.repository.PostRepository

class CreatePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        content: String,
        mediaData: ByteArray? = null,
        mediaType: Post.MediaType? = null
    ): Result<Post> {
        // 早期リターン: コンテンツ検証
        if (content.isBlank()) {
            return Result.failure(
                IllegalArgumentException("投稿内容を入力してください")
            )
        }

        if (content.length < Post.MIN_CONTENT_LENGTH) {
            return Result.failure(
                IllegalArgumentException("投稿内容は${Post.MIN_CONTENT_LENGTH}文字以上で入力してください")
            )
        }

        if (content.length > Post.MAX_CONTENT_LENGTH) {
            return Result.failure(
                IllegalArgumentException("投稿内容は${Post.MAX_CONTENT_LENGTH}文字以内で入力してください")
            )
        }

        // 早期リターン: メディアデータとタイプの整合性チェック
        if (mediaData != null && mediaType == null) {
            return Result.failure(
                IllegalArgumentException("メディアタイプを指定してください")
            )
        }

        if (mediaData == null && mediaType != null) {
            return Result.failure(
                IllegalArgumentException("メディアデータが必要です")
            )
        }

        return postRepository.createPost(
            content = content.trim(),
            mediaData = mediaData,
            mediaType = mediaType
        )
    }
}
