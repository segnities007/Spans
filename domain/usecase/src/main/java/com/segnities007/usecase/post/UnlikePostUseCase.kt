package com.segnities007.usecase.post

import com.segnities007.model.Post
import com.segnities007.repository.PostRepository

class UnlikePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): Result<Post> {
        // 早期リターン: 投稿ID検証
        if (postId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("投稿IDが無効です")
            )
        }

        return postRepository.unlikePost(postId)
    }
}
