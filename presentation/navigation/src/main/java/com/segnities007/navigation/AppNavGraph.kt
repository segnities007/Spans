package com.segnities007.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.segnities007.navigation.auth.AuthNavGraph
import com.segnities007.navigation.hub.HubNavGraph

/**
 * アプリ全体のナビゲーショングラフ
 * 
 * Type-Safe Navigationを使用して、すべての画面遷移を管理します。
 *
 * @param modifier Modifier
 * @param navController ナビゲーションコントローラー
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    var currentRoute by remember { mutableStateOf<AppNavRoute>(AppNavRoute.Auth()) }

    NavHost(
        navController = navController,
        startDestination = currentRoute,
        modifier = modifier
    ) {
        composable<AppNavRoute.Auth> {
            AuthNavGraph(
                onAppNavigate = {
                    // 認証完了時はHubへ遷移
                    navController.navigate(AppNavRoute.Hub()) {
                        popUpTo<AppNavRoute.Auth> { inclusive = true }
                    }
                }
            )
        }
        
        composable<AppNavRoute.Hub> {
            HubNavGraph(
                onAppNavigate = {
                    // ログアウト時は認証画面へ
                    navController.navigate(AppNavRoute.Auth()) {
                        popUpTo<AppNavRoute.Hub> { inclusive = true }
                    }
                }
            )
        }
    }
}