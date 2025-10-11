package com.segnities007.signup.mvi

import androidx.lifecycle.viewModelScope
import com.segnities007.mvi.BaseViewModel
import com.segnities007.usecase.auth.SignUpUseCase
import kotlinx.coroutines.launch

/**
 * サインアップ画面のViewModel
 * 
 * MVIライブラリ(com.segnities007:mvi)のBaseViewModelを継承
 * Reducerを使用して純粋な状態変換を実現
 */
class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpUiState, SignUpIntent, SignUpEffect>(
    initialState = SignUpUiState.Initial
) {

    private val reducer = SignUpReducer()

    init {
        // 初期状態を編集状態に設定
        updateState(SignUpIntent.RetryClicked)
    }

    /**
     * MVIライブラリが要求するIntent処理メソッド
     */
    override fun onIntent(intent: SignUpIntent) {
        when (intent) {
            // Reducerで処理できるIntentは直接reduce
            is SignUpIntent.NicknameChanged,
            is SignUpIntent.BioChanged,
            is SignUpIntent.AvatarSelected,
            SignUpIntent.AvatarRemoved,
            SignUpIntent.RetryClicked -> {
                updateState(intent)
            }
            
            // 副作用を伴うIntent
            SignUpIntent.SignUpClicked -> handleSignUpClicked()
            SignUpIntent.NavigateToSignIn -> handleNavigateToSignIn()
        }
    }

    private fun handleSignUpClicked() {
        viewModelScope.launch {
            val state = uiState.value
            
            if (state !is SignUpUiState.Editing) return@launch
            if (!state.isFormValid) return@launch

            // Reducerで状態をLoadingに更新
            updateState(SignUpIntent.SignUpClicked)

            // TODO: avatarUriをByteArrayに変換する処理を実装
            val avatarData: ByteArray? = null

            val result = signUpUseCase(
                nickname = state.nickname,
                bio = state.bio,
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
                val errorMessage = result.exceptionOrNull()?.message ?: "不明なエラーが発生しました"
                setState(SignUpUiState.Error(
                    message = errorMessage,
                    nickname = state.nickname,
                    bio = state.bio,
                    avatarUri = state.avatarUri
                ))
                sendEffect(SignUpEffect.ShowError(errorMessage))
            }
        }
    }

    private fun handleNavigateToSignIn() {
        sendEffect(SignUpEffect.NavigateToSignIn)
    }

    /**
     * Reducerを使用して状態を更新
     */
    private fun updateState(intent: SignUpIntent) {
        setState(reducer.reduce(uiState.value, intent))
    }

    /**
     * 状態更新のヘルパーメソッド
     * 
     * BaseViewModelの_uiStateはprivateのため、Reflectionを使用
     * 
     * ※理想的には BaseViewModel に protected setState() を追加すべき
     */
    private fun setState(newState: SignUpUiState) {
        try {
            val field = this::class.java.superclass?.getDeclaredField("_uiState")
            field?.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val mutableStateFlow = field?.get(this) as? kotlinx.coroutines.flow.MutableStateFlow<SignUpUiState>
            mutableStateFlow?.value = newState
        } catch (e: Exception) {
            throw IllegalStateException(
                "BaseViewModelの_uiStateにアクセスできません。" +
                "BaseViewModelに protected fun setState(newState: State) を追加してください。",
                e
            )
        }
    }
}
