package nl.shekho.videoplayer.views.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {

        composable(
            route = Screens.SplashScreen.route
        ){
            SplashScreen(
                navController = navController,
                accessViewModel = accessViewModel
            )
        }

        composable(
            route = Screens.StartView.route
        ){
            StartView(
                navController = navController,
                accessViewModel = accessViewModel,
                sessionViewModel = sessionViewModel
            )
        }

        composable(
            route = Screens.Session.route
        ){
            SessionView(
                sessionViewModel = sessionViewModel,
                navController = navController
            )
        }

    }
}