package com.segnities007.usecase.user

import com.segnities007.model.User
import com.segnities007.repository.UserRepository

class UpdateProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        nickname: String? = null,
        bio: String? = null,
        avatarUrl: String? = null
    ): Result<User> {
        // 早期リターン: ニックネーム検証
        if (nickname != null) {
            User.validateNickname(nickname)?.let { errorMessage ->
                return Result.failure(IllegalArgumentException(errorMessage))
            }
        }

        // 早期リターン: 自己紹介検証
        if (bio != null) {
            User.validateBio(bio)?.let { errorMessage ->
                return Result.failure(IllegalArgumentException(errorMessage))
            }
        }

        return userRepository.updateProfile(
            nickname = nickname,
            bio = bio,
            avatarUrl = avatarUrl
        )
    }
}
