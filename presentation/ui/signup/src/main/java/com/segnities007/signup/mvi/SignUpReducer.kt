package com.segnities007.signup.mvi

import com.segnities007.model.User
import com.segnities007.mvi.Reducer

object SignUpReducer : Reducer<SignUpUiState, SignUpIntent> {

    override fun reduce(state: SignUpUiState, intent: SignUpIntent): SignUpUiState {
        return when (intent) {
            is SignUpIntent.NicknameChanged -> reduceNicknameChanged(state, intent)
            is SignUpIntent.BioChanged -> reduceBioChanged(state, intent)
            is SignUpIntent.AvatarSelected -> reduceAvatarSelected(state, intent)
            is SignUpIntent.AvatarRemoved -> reduceAvatarRemoved(state, intent)
            is SignUpIntent.SignUpClicked -> reduceSignUpStarted(state, intent)
            is SignUpIntent.NavigateToSignIn -> state // 状態変更なし
            is SignUpIntent.RetryClicked -> reduceRetry(state, intent)
        }
    }

    private fun reduceNicknameChanged(
        state: SignUpUiState,
        intent: SignUpIntent.NicknameChanged
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        val snapshot = state.formSnapshot() ?: return state

        val trimmedNickname = intent.nickname.trim()

        return SignUpUiState.Wait(
            nickname = trimmedNickname,
            bio = snapshot.second,
            avatarUri = snapshot.third,
            nicknameError = validateNickname(trimmedNickname),
            bioError = validateBio(snapshot.second),
            isSubmitting = false
        )
    }

    private fun reduceBioChanged(
        state: SignUpUiState,
        intent: SignUpIntent.BioChanged
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        val snapshot = state.formSnapshot() ?: return state

        val trimmedBio = intent.bio.trim()

        return SignUpUiState.Wait(
            nickname = snapshot.first,
            bio = trimmedBio,
            avatarUri = snapshot.third,
            nicknameError = validateNickname(snapshot.first),
            bioError = validateBio(trimmedBio),
            isSubmitting = false
        )
    }

    private fun reduceAvatarSelected(
        state: SignUpUiState,
        intent: SignUpIntent.AvatarSelected
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        val snapshot = state.formSnapshot() ?: return state

        return SignUpUiState.Wait(
            nickname = snapshot.first,
            bio = snapshot.second,
            avatarUri = intent.uri,
            nicknameError = validateNickname(snapshot.first),
            bioError = validateBio(snapshot.second),
            isSubmitting = false
        )
    }

    private fun reduceAvatarRemoved(
        state: SignUpUiState,
        intent: SignUpIntent.AvatarRemoved
    ): SignUpUiState {
        // AvatarSelectedと同じロジック（uri = null）
        val snapshot = state.formSnapshot() ?: return state

        return SignUpUiState.Wait(
            nickname = snapshot.first,
            bio = snapshot.second,
            avatarUri = null,
            nicknameError = validateNickname(snapshot.first),
            bioError = validateBio(snapshot.second),
            isSubmitting = false
        )
    }

    private fun reduceSignUpStarted(
        state: SignUpUiState,
        intent: SignUpIntent.SignUpClicked
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        return when (state) {
            is SignUpUiState.Wait -> {
                if (!state.canSubmit) return state
                state.copy(isSubmitting = true)
            }

            is SignUpUiState.Failed -> {
                val waitState = SignUpUiState.Wait(
                    nickname = state.nickname,
                    bio = state.bio,
                    avatarUri = state.avatarUri,
                    nicknameError = validateNickname(state.nickname),
                    bioError = validateBio(state.bio),
                    isSubmitting = false
                )

                if (!waitState.canSubmit) return waitState
                waitState.copy(isSubmitting = true)
            }

            else -> state
        }
    }

    private fun reduceRetry(
        state: SignUpUiState,
        intent: SignUpIntent.RetryClicked
    ): SignUpUiState {
        // 早期リターン: エラー状態以外は無視
        if (state !is SignUpUiState.Failed) return state

        return SignUpUiState.Wait(
            nickname = state.nickname,
            bio = state.bio,
            avatarUri = state.avatarUri,
            nicknameError = validateNickname(state.nickname),
            bioError = validateBio(state.bio),
            isSubmitting = false
        )
    }
}

private fun validateNickname(nickname: String): String? {
    return when {
        nickname.isBlank() -> "ニックネームを入力してください"
        nickname.length < User.Companion.MIN_NICKNAME_LENGTH ->
            "ニックネームは${User.Companion.MIN_NICKNAME_LENGTH}文字以上で入力してください"
        nickname.length > User.Companion.MAX_NICKNAME_LENGTH ->
            "ニックネームは${User.Companion.MAX_NICKNAME_LENGTH}文字以内で入力してください"
        else -> null
    }
}

private fun validateBio(bio: String): String? {
    return when {
        bio.length > User.Companion.MAX_BIO_LENGTH ->
            "自己紹介は${User.Companion.MAX_BIO_LENGTH}文字以内で入力してください"
        else -> null
    }
}

private fun SignUpUiState.formSnapshot(): Triple<String, String, String?>? {
    return when (this) {
        is SignUpUiState.Wait -> Triple(nickname, bio, avatarUri)
        is SignUpUiState.Failed -> Triple(nickname, bio, avatarUri)
        else -> null
    }
}
