package com.ocordero.android_part.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ocordero.android_part.view.screens.AltaCitaScreen
import com.ocordero.android_part.view.screens.EditarCitaScreen
import com.ocordero.android_part.view.screens.ListaCitasScreen

sealed class Screen(val route: String) {
    object Lista : Screen("lista_citas")
    object Alta : Screen("alta_cita")
    object Editar : Screen("editar_cita/{citaId}") {
        fun crearRuta(citaId: Int) = "editar_cita/$citaId"
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route
    ) {
        composable(Screen.Lista.route) {

                ListaCitasScreen(
                    onAgregarClick = {
                        navController.navigate(Screen.Alta.route)
                    },
                    onEditarClick = { citaId ->
                        navController.navigate(Screen.Editar.crearRuta(citaId))
                    }
                )
        }

        composable(Screen.Alta.route) {
            AltaCitaScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Editar.route,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            EditarCitaScreen(
                citaId = citaId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}