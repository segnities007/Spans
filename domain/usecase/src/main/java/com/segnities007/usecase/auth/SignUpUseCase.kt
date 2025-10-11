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
        if (nickname.isBlank()) {
            return Result.failure(
                IllegalArgumentException("ニックネームを入力してください")
            )
        }

        if (nickname.length < User.MIN_NICKNAME_LENGTH) {
            return Result.failure(
                IllegalArgumentException("ニックネームは${User.MIN_NICKNAME_LENGTH}文字以上で入力してください")
            )
        }

        if (nickname.length > User.MAX_NICKNAME_LENGTH) {
            return Result.failure(
                IllegalArgumentException("ニックネームは${User.MAX_NICKNAME_LENGTH}文字以内で入力してください")
            )
        }

        // 早期リターン: 自己紹介検証
        if (bio != null && bio.length > User.MAX_BIO_LENGTH) {
            return Result.failure(
                IllegalArgumentException("自己紹介は${User.MAX_BIO_LENGTH}文字以内で入力してください")
            )
        }

        return authRepository.signUp(
            nickname = nickname,
            bio = bio,
            avatarData = avatarData
        )
    }
}
