package com.segnities007.usecase.post

import com.segnities007.model.Post
import com.segnities007.model.exception.DomainException
import com.segnities007.repository.PostRepository

class LikePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): Result<Post> {
        // 早期リターン: 投稿ID検証
        Post.validatePostId(postId)?.let { errorMessage ->
            return Result.failure(DomainException.ValidationError("postId", errorMessage))
        }

        return postRepository.likePost(postId)
    }
}
