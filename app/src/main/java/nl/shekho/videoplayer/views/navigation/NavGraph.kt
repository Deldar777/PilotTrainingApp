package nl.shekho.videoplayer.views.navigation

import android.content.Context
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
    sessionViewModel: SessionViewModel,
    context: Context,
//    videoPlayerModel: VideoPlayerViewModel
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
            route = Screens.SessionScreen.route
        ){
            SessionView(
                sessionViewModel = sessionViewModel,
                navController = navController,
                accessViewModel = accessViewModel,
                context = context,
            )
        }

        composable(
            route = Screens.ReviewScreen.route
        ){
            ReviewView(
                sessionViewModel = sessionViewModel,
                navController = navController,
                accessViewModel = accessViewModel,
                context = context
            )
        }

        composable(
            route = Screens.OverviewScreen.route
        ){
            OverviewView(
                sessionViewModel = sessionViewModel,
                navController = navController,
                accessViewModel = accessViewModel,
                context = context
            )
        }

        composable(
            route = Screens.LoginScreen.route
        ){
            LoginView(
                accessViewModel = accessViewModel,
                context = context,
                navController = navController
            )
        }

    }
}