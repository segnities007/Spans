package com.segnities007.usecase.post

import com.segnities007.model.Post
import com.segnities007.model.exception.DomainException
import com.segnities007.repository.PostRepository

class CreatePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        content: String,
        mediaData: ByteArray?,
        mediaType: Post.MediaType?
    ): Result<Post> {
        // 早期リターン: 投稿内容のバリデーション
        Post.validateContent(content)?.let { errorMessage ->
            return Result.failure(DomainException.ValidationError("content", errorMessage))
        }

        return postRepository.createPost(content, mediaData, mediaType)
    }
}
