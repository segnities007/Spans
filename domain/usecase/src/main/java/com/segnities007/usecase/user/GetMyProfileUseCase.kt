package com.segnities007.usecase.user

import com.segnities007.model.User
import com.segnities007.repository.UserRepository

class GetMyProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return userRepository.getMyProfile()
    }
}
