package com.segnities007.model.exception

/**
 * ドメイン層で使用するカスタム例外
 * 
 * アプリケーション固有のエラーを型安全に扱うための例外階層
 * AGENTS.md準拠: Line 701-706
 */
sealed class DomainException : Exception() {
    
    /**
     * ネットワークエラー
     * 
     * API通信やネットワーク接続に関するエラー
     */
    data class NetworkError(override val message: String) : DomainException()
    
    /**
     * 認証エラー
     * 
     * Google Sign-In失敗、セッション切れなど認証に関するエラー
     */
    data class AuthError(override val message: String) : DomainException()
    
    /**
     * BLE関連エラー
     * 
     * Bluetooth無効、権限不足、スキャン失敗などBLE検出に関するエラー
     */
    data class BleError(override val message: String) : DomainException()
    
    /**
     * バリデーションエラー
     * 
     * ユーザー入力の検証エラー
     * 
     * @param field エラーが発生したフィールド名（例: "nickname", "content"）
     * @param message エラーメッセージ
     */
    data class ValidationError(
        val field: String,
        override val message: String
    ) : DomainException()
    
    /**
     * すれ違いが存在しない
     * 
     * まだ誰ともすれ違っていない場合に発生
     */
    object NoEncountersException : DomainException() {
        override val message: String = "No encounters found"
    }
}
