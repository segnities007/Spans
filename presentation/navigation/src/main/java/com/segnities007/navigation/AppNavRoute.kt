package com.segnities007.navigation

import kotlinx.serialization.Serializable
import com.segnities007.navigation.hub.HubNavRoute
import com.segnities007.navigation.auth.AuthNavRoute

// すべてのルートをまとめる型
@Serializable
sealed interface AppNavRoute {
    @Serializable
    data class Auth(val route: AuthNavRoute? = null) : AppNavRoute

    @Serializable
    data class Hub(val route: HubNavRoute? = null) : AppNavRoute
}
