package nl.shekho.videoplayer.views.navigation

sealed class Screens(val route: String){
    object SplashScreen: Screens(route = "splash_screen")
    object StartScreen: Screens(route = "start_screen")
    object SessionScreen: Screens(route = "session_screen")
    object ReviewScreen: Screens(route = "review_screen")
    object OverviewScreen: Screens(route = "overview_screen")
    object LoginScreen: Screens(route = "login_screen")

    fun withArgs(vararg args: Int): String {
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}
