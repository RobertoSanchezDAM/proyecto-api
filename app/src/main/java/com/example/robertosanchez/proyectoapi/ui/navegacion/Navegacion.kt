package com.example.robertosanchez.proyectoapi.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.ui.screens.buscarScreen.BuscarScreen
import com.example.robertosanchez.proyectoapi.ui.screens.contrasenaOlvScreen.ContrasenaOlvScreen
import com.example.robertosanchez.proyectoapi.ui.screens.detailScreen.DetailScreen
import com.example.robertosanchez.proyectoapi.ui.screens.inicioScreen.InicioScreen
import com.example.robertosanchez.proyectoapi.ui.screens.listaScreen.ListaScreen
import com.example.robertosanchez.proyectoapi.ui.screens.listaScreen.ListaViewModel
import com.example.robertosanchez.proyectoapi.ui.screens.loginScreen.LoginScreen
import com.example.robertosanchez.proyectoapi.ui.screens.usuarioScreen.UsuarioScreen
import com.example.robertosanchez.proyectoapi.ui.screens.registroScreen.RegistroScreen

@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Inicio) {
        composable<Inicio> {
            InicioScreen(
                {
                    navController.navigate(Buscar)
                },
                {
                    navController.navigate(Login)
                }
            )
        }

        composable<Login> {
            LoginScreen(
                auth,
                { navController.navigate(Registro) },
                {
                    navController.navigate(Usuario) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                { navController.navigate(ContraseñaOlv) }
            )
        }

        composable<Registro> {
            RegistroScreen(
                auth
            ) { navController.popBackStack() }
        }

        composable<ContraseñaOlv> {
            ContrasenaOlvScreen(
                auth,
                { navController.navigate(Login) }
            )
        }

        composable<Usuario> {
            UsuarioScreen (
                auth,
                { navController.navigate(Login) }
            )
        }

        composable<Buscar> {
            BuscarScreen { cantante ->
                navController.navigate(Lista(cantante))
            }
        }

        composable<Lista> {backStackEntry ->
            val cantante = backStackEntry.arguments?.getString("cantante") ?: ""
            val viewModel = ListaViewModel(cantante)

            ListaScreen(cantante, viewModel,
                onBack = {navController.popBackStack() },
                navigateToDetail = { id, cantante ->
                navController.navigate(Detail(id, cantante))
            })
        }

        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            val id = detail.id
            val cantante = backStackEntry.arguments?.getString("cantante") ?: ""
            DetailScreen(id, cantante,
                    onBack = { navController.popBackStack() },
                    navigateToList = { navController.navigate(Lista(cantante)) }
            )
        }
    }
}