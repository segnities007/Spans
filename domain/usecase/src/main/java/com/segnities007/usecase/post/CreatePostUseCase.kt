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
        Post.validateContent(content)?.let { errorMessage ->
            return Result.failure(IllegalArgumentException(errorMessage))
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
