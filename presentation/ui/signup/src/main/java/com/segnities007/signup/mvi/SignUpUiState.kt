package com.segnities007.signup.mvi

import com.segnities007.model.User
import com.segnities007.mvi.UiState

sealed interface SignUpUiState : UiState {
    /**
     * 初期状態
     * ユーザーが入力を開始する前の状態
     */
    data object Initial : SignUpUiState

    /**
     * 入力中の状態
     * 
     * @property nickname 入力されたニックネーム
     * @property bio 入力された自己紹介（オプション）
     * @property avatarUri 選択されたアバター画像のURI（オプション）
     * @property nicknameError ニックネームのバリデーションエラー
     * @property bioError 自己紹介のバリデーションエラー
     * @property isFormValid フォームが有効かどうか
     */
    data class Editing(
        val nickname: String = "",
        val bio: String = "",
        val avatarUri: String? = null,
        val nicknameError: String? = null,
        val bioError: String? = null,
        val isFormValid: Boolean = false
    ) : SignUpUiState

    /**
     * サインアップ処理中の状態
     * 
     * @property nickname 入力されたニックネーム
     * @property bio 入力された自己紹介
     * @property avatarUri 選択されたアバター画像のURI
     */
    data class Loading(
        val nickname: String,
        val bio: String,
        val avatarUri: String?
    ) : SignUpUiState

    /**
     * サインアップ成功状態
     * 
     * @property user 作成されたユーザー情報
     */
    data class Success(
        val user: User
    ) : SignUpUiState

    /**
     * エラー状態
     * 
     * @property message エラーメッセージ
     * @property nickname 入力されていたニックネーム（再入力用）
     * @property bio 入力されていた自己紹介（再入力用）
     * @property avatarUri 選択されていたアバター画像のURI（再入力用）
     */
    data class Error(
        val message: String,
        val nickname: String = "",
        val bio: String = "",
        val avatarUri: String? = null
    ) : SignUpUiState
}
