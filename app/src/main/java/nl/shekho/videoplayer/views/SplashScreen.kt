package nl.shekho.videoplayer.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.views.navigation.Screens

@Composable
fun SplashScreen(
    navController: NavController,
    accessViewModel: AccessViewModel
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navController.popBackStack()

        if (accessViewModel.loggedIn) {
            accessViewModel.decodeJWT()
            navController.navigate(Screens.Overview.route)
        }else{
            navController.navigate(Screens.Login.route)
        }
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(customDarkGray)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.vref_logo),
            contentDescription = "",
            modifier = Modifier
                .size(600.dp)
                .alpha(alpha = alpha)
        )
    }
}