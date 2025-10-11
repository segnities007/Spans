package com.segnities007.usecase.auth

import com.segnities007.model.User
import com.segnities007.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nickname: String,
        bio: String? = null,
        avatarData: ByteArray? = null
    ): Result<User> {
        // 早期リターン: ニックネーム検証
        User.validateNickname(nickname)?.let { errorMessage ->
            return Result.failure(IllegalArgumentException(errorMessage))
        }

        // 早期リターン: 自己紹介検証
        if (bio != null) {
            User.validateBio(bio)?.let { errorMessage ->
                return Result.failure(IllegalArgumentException(errorMessage))
            }
        }

        return authRepository.signUp(
            nickname = nickname,
            bio = bio,
            avatarData = avatarData
        )
    }
}
