package com.segnities007.navigation

import kotlinx.serialization.Serializable

// 認証フロー
@Serializable
sealed interface AuthRoute {
    @Serializable
    data object SignIn : AuthRoute
    
    @Serializable
    data object SignUp : AuthRoute
}

// ハブ（メインアプリフロー）
@Serializable
sealed interface HubRoute {
    @Serializable
    data object Plaza : HubRoute
    
    @Serializable
    data object Timeline : HubRoute
    
    @Serializable
    data object Search : HubRoute
    
    @Serializable
    data object MyProfile : HubRoute
    
    @Serializable
    data object Settings : HubRoute
}

// 投稿関連
@Serializable
sealed interface PostRoute {
    @Serializable
    data object CreatePost : PostRoute
    
    @Serializable
    data class PostDetail(val postId: String) : PostRoute
    
    @Serializable
    data class EditPost(val postId: String) : PostRoute
}

// プロフィール関連
@Serializable
sealed interface ProfileRoute {
    @Serializable
    data class UserProfile(val userUuid: String) : ProfileRoute
}

// すべてのルートをまとめる型
@Serializable
sealed interface Route {
    @Serializable
    data class Auth(val route: AuthRoute) : Route
    
    @Serializable
    data class Hub(val route: HubRoute) : Route
    
    @Serializable
    data class Post(val route: PostRoute) : Route
    
    @Serializable
    data class Profile(val route: ProfileRoute) : Route
}
