package com.segnities007.usecase.auth

import com.segnities007.model.User
import com.segnities007.model.exception.DomainException
import com.segnities007.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nickname: String,
        bio: String,
        avatarData: ByteArray?
    ): Result<User> {
        // 早期リターン: ニックネームのバリデーション
        User.validateNickname(nickname)?.let { errorMessage ->
            return Result.failure(DomainException.ValidationError("nickname", errorMessage))
        }

        // 早期リターン: 自己紹介のバリデーション
        User.validateBio(bio)?.let { errorMessage ->
            return Result.failure(DomainException.ValidationError("bio", errorMessage))
        }

        return authRepository.signUp(nickname, bio, avatarData)
    }
}
