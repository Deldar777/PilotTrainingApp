package nl.shekho.videoplayer.views.navigation

sealed class Screens(val route: String){
    object SplashScreen: Screens(route = "splash_screen")
    object Login: Screens(route = "login_screen")
    object Overview: Screens(route = "overview_screen")
    object Session: Screens(route = "session_screen")

    fun withArgs(vararg args: Int): String {
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}
