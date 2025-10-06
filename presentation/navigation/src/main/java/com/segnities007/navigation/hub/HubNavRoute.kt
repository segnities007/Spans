package com.segnities007.navigation.hub

import com.segnities007.navigation.NavRoute
import kotlinx.serialization.Serializable

// ハブ（メインアプリフロー）
@Serializable
sealed interface HubNavRoute : NavRoute {
    @Serializable
    data object Plaza : HubNavRoute

    @Serializable
    data object Timeline : HubNavRoute

    @Serializable
    data object Search : HubNavRoute

    @Serializable
    data object Profile : HubNavRoute

    @Serializable
    data object Settings : HubNavRoute
}
