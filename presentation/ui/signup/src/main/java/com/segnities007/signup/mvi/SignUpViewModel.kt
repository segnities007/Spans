package com.segnities007.signup.mvi

import androidx.lifecycle.viewModelScope
import com.segnities007.model.exception.DomainException
import com.segnities007.mvi.BaseViewModel
import com.segnities007.usecase.auth.SignUpUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpUiState, SignUpIntent, SignUpEffect>(
    initialState = SignUpUiState.Wait()
) {

    override fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.NicknameChanged,
            is SignUpIntent.BioChanged,
            is SignUpIntent.AvatarSelected,
            SignUpIntent.AvatarRemoved,
            SignUpIntent.RetryClicked -> {
                updateState(intent)
            }

            SignUpIntent.SignUpClicked -> handleSignUpClicked()
            SignUpIntent.NavigateToSignIn -> handleNavigateToSignIn()
        }
    }

    private fun handleSignUpClicked() {
        viewModelScope.launch {
            val originalState = uiState.value

            if (originalState !is SignUpUiState.Wait && originalState !is SignUpUiState.Failed) {
                return@launch
            }

            // Reducerを経由して送信状態に遷移
            updateState(SignUpIntent.SignUpClicked)

            val submittingState = uiState.value as? SignUpUiState.Wait ?: return@launch

            if (!submittingState.isSubmitting) {
                return@launch
            }

            // TODO: avatarUriをByteArrayに変換する処理を実装
            val avatarData: ByteArray? = null

            val result = signUpUseCase(
                nickname = submittingState.nickname,
                bio = submittingState.bio,
                avatarData = avatarData
            )

            if (result.isSuccess) {
                // 成功状態への更新はReducerでは行わず、直接設定
                // （READMEの例に従い、UseCaseの結果は直接状態に反映）
                val user = result.getOrNull() ?: return@launch
                setState(SignUpUiState.Success(user = user))
                sendEffect(SignUpEffect.NavigateToPlaza)
                sendEffect(SignUpEffect.ShowSuccess("サインアップに成功しました"))
            } else {
                // Result型からエラー情報を取得
                val throwable = result.exceptionOrNull()
                
                // エラーメッセージとフィールドエラーを抽出
                val errorMessage = throwable?.message ?: "不明なエラーが発生しました"
                val (nicknameError, bioError) = extractFieldErrors(throwable)

                setState(
                    SignUpUiState.Failed(
                        nickname = submittingState.nickname,
                        bio = submittingState.bio,
                        avatarUri = submittingState.avatarUri,
                        nicknameError = nicknameError,
                        bioError = bioError
                    )
                )
                sendEffect(SignUpEffect.ShowError(errorMessage))
            }
        }
    }

    private fun handleNavigateToSignIn() {
        sendEffect(SignUpEffect.NavigateToSignIn)
    }

    private fun extractFieldErrors(throwable: Throwable?): Pair<String?, String?> {
        return when (throwable) {
            is DomainException.ValidationError -> {
                when (throwable.field) {
                    "nickname" -> throwable.message to null
                    "bio" -> null to throwable.message
                    else -> null to null
                }
            }
            is IllegalArgumentException -> {
                val message = throwable.message ?: return null to null
                when {
                    message.contains("ニックネーム") -> message to null
                    message.contains("自己紹介") -> null to message
                    else -> null to null
                }
            }
            else -> null to null
        }
    }

    /**
     * Reducerを使用して状態を更新
     */
    private fun updateState(intent: SignUpIntent) {
        setState(SignUpReducer.reduce(uiState.value, intent))
    }
}
