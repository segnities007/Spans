package com.segnities007.usecase.user

import com.segnities007.model.User
import com.segnities007.model.exception.DomainException
import com.segnities007.repository.UserRepository

class UpdateProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        nickname: String?,
        bio: String?,
        avatarUrl: String?
    ): Result<User> {
        // 早期リターン: ニックネームのバリデーション
        nickname?.let {
            User.validateNickname(it)?.let { errorMessage ->
                return Result.failure(DomainException.ValidationError("nickname", errorMessage))
            }
        }

        // 早期リターン: 自己紹介のバリデーション
        bio?.let {
            User.validateBio(it)?.let { errorMessage ->
                return Result.failure(DomainException.ValidationError("bio", errorMessage))
            }
        }

        return userRepository.updateProfile(nickname, bio, avatarUrl)
    }
}
