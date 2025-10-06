package com.segnities007.navigation.hub

import androidx.compose.material3.Scaffold
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

/**
 * ハブ(メインアプリ)のナビゲーショングラフ
 * 
 * Type-Safe Navigationを使用して、メインアプリの画面遷移を管理します。
 * 
 * @param modifier Modifier
 * @param navController ナビゲーションコントローラー
 * @param startDestination 初期表示画面(デフォルト: Plaza)
 */
@Composable
fun HubNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: HubNavRoute = HubNavRoute.Plaza,
) {

    Scaffold{ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable<HubNavRoute.Plaza> {
                // TODO: hub:ui:PlazaScreenをここで呼び出す
            }

            composable<HubNavRoute.Timeline> {
                // TODO: hub:ui:TimelineScreenをここで呼び出す
            }

            composable<HubNavRoute.Search> {
                // TODO: hub:ui:SearchScreenをここで呼び出す
            }

            composable<HubNavRoute.Profile> {
                // TODO: hub:ui:ProfileScreenをここで呼び出す
            }

            composable<HubNavRoute.Settings> {
                // TODO: hub:ui:SettingsScreenをここで呼び出す
            }
        }
    }
}