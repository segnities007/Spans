package com.segnities007.model

private const val MIN_NICKNAME_LENGTH = 2
private const val MAX_NICKNAME_LENGTH = 20
private const val MAX_BIO_LENGTH = 500


data class User(
    val uuid: String,
    val googleId: String,
    val nickname: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val totalEncounters: Int = 0,
    val achievements: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        /**
         * ニックネームのバリデーションを実行し、エラーメッセージを返す
         * 
         * @return エラーメッセージ（エラーがない場合はnull）
         */
        fun validateNickname(nickname: String): String? {
            return when {
                nickname.isBlank() -> "ニックネームを入力してください"
                nickname.length < MIN_NICKNAME_LENGTH ->
                    "ニックネームは${MIN_NICKNAME_LENGTH}文字以上で入力してください"
                nickname.length > MAX_NICKNAME_LENGTH ->
                    "ニックネームは${MAX_NICKNAME_LENGTH}文字以内で入力してください"
                else -> null
            }
        }
        
        /**
         * 自己紹介のバリデーションを実行し、エラーメッセージを返す
         * 
         * @return エラーメッセージ（エラーがない場合はnull）
         */
        fun validateBio(bio: String): String? {
            return when {
                bio.length > MAX_BIO_LENGTH ->
                    "自己紹介は${MAX_BIO_LENGTH}文字以内で入力してください"
                else -> null
            }
        }
        
        /**
         * Google IDトークンのバリデーションを実行し、エラーメッセージを返す
         * 
         * @return エラーメッセージ（エラーがない場合はnull）
         */
        fun validateIdToken(idToken: String): String? {
            return if (idToken.isBlank()) {
                "IDトークンが空です"
            } else {
                null
            }
        }
    }
}
