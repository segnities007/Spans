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

        return createWaitState(
            nickname = trimmedNickname,
            bio = snapshot.second,
            avatarUri = snapshot.third
        )
    }

    private fun reduceBioChanged(
        state: SignUpUiState,
        intent: SignUpIntent.BioChanged
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        val snapshot = state.formSnapshot() ?: return state

        val trimmedBio = intent.bio.trim()

        return createWaitState(
            nickname = snapshot.first,
            bio = trimmedBio,
            avatarUri = snapshot.third
        )
    }

    private fun reduceAvatarSelected(
        state: SignUpUiState,
        intent: SignUpIntent.AvatarSelected
    ): SignUpUiState {
        // 早期リターン: 編集状態以外は無視
        val snapshot = state.formSnapshot() ?: return state

        return createWaitState(
            nickname = snapshot.first,
            bio = snapshot.second,
            avatarUri = intent.uri
        )
    }

    private fun reduceAvatarRemoved(
        state: SignUpUiState,
        intent: SignUpIntent.AvatarRemoved
    ): SignUpUiState {
        // AvatarSelectedと同じロジック（uri = null）
        val snapshot = state.formSnapshot() ?: return state

        return createWaitState(
            nickname = snapshot.first,
            bio = snapshot.second,
            avatarUri = null
        )
    }
    
    /**
     * バリデーション付きのWait状態を生成
     */
    private fun createWaitState(
        nickname: String,
        bio: String,
        avatarUri: String?,
        isSubmitting: Boolean = false
    ): SignUpUiState.Wait {
        return SignUpUiState.Wait(
            nickname = nickname,
            bio = bio,
            avatarUri = avatarUri,
            nicknameError = User.validateNickname(nickname),
            bioError = User.validateBio(bio),
            isSubmitting = isSubmitting
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
                val waitState = createWaitState(
                    nickname = state.nickname,
                    bio = state.bio,
                    avatarUri = state.avatarUri
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

        return createWaitState(
            nickname = state.nickname,
            bio = state.bio,
            avatarUri = state.avatarUri
        )
    }
}

private fun SignUpUiState.formSnapshot(): Triple<String, String, String?>? {
    return when (this) {
        is SignUpUiState.Wait -> Triple(nickname, bio, avatarUri)
        is SignUpUiState.Failed -> Triple(nickname, bio, avatarUri)
        else -> null
    }
}
