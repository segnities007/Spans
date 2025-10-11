package com.segnities007.usecase.post

import com.segnities007.repository.PostRepository

class DeletePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): Result<Unit> {
        // 早期リターン: 投稿ID検証
        if (postId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("投稿IDが無効です")
            )
        }

        return postRepository.deletePost(postId)
    }
}
