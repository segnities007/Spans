package com.segnities007.navigation.hub

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.segnities007.post.PostScreen
import com.segnities007.timeline.TimelineScreen
import com.segnities007.search.SearchScreen
import com.segnities007.profile.ProfileScreen
import com.segnities007.settings.SettingsScreen

/**
 * ハブ(メインアプリ)のナビゲーショングラフ
 * 
 * Type-Safe Navigationを使用して、メインアプリの画面遷移を管理します。
 * 
 * @param modifier Modifier
 * @param navController ナビゲーションコントローラー
 * @param startDestination 初期表示画面(デフォルト: Plaza)
 * @param onAppNavigate アプリ全体のナビゲーションコールバック (ログアウト時など)
 */
@Composable
fun HubNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: HubNavRoute = HubNavRoute.Plaza,
    onAppNavigate: () -> Unit = {},
) {

    Scaffold{ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
        ) {
            composable<HubNavRoute.Plaza> {
                PostScreen(
                    onHubNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable<HubNavRoute.Timeline> {
                TimelineScreen(
                    onHubNavigate = { route ->
                        when (route) {
                            is HubNavRoute.Plaza -> navController.popBackStack()
                            else -> navController.navigate(route)
                        }
                    }
                )
            }

            composable<HubNavRoute.Search> {
                SearchScreen(
                    onHubNavigate = { route ->
                        when (route) {
                            is HubNavRoute.Plaza -> navController.popBackStack()
                            else -> navController.navigate(route)
                        }
                    }
                )
            }

            composable<HubNavRoute.Profile> {
                ProfileScreen(
                    onHubNavigate = { route ->
                        when (route) {
                            is HubNavRoute.Plaza -> navController.popBackStack()
                            else -> navController.navigate(route)
                        }
                    }
                )
            }

            composable<HubNavRoute.Settings> {
                SettingsScreen(
                    onHubNavigate = { route ->
                        when (route) {
                            is HubNavRoute.Plaza -> navController.popBackStack()
                            else -> navController.navigate(route)
                        }
                    },
                    onLogout = onAppNavigate
                )
            }
        }
    }
}