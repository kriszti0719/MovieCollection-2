package hu.bme.aut.android.movies.feature.movie_load

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import hu.bme.aut.android.movies.R

@Composable
fun LoadingScreen() {
    // Lottie-t szerettem volna, de nem működik az import :(
    Image(
        painter = painterResource(R.drawable.loading),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

/*@Composable
fun LoadingScreen() {
    val lottieCompositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.movie_loading))
    val composition by lottieCompositionResult
    val progress by animateLottieCompositionAsState(composition = composition)
    if (composition != null) {
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            composition = composition!!,
            progress = progress
        )
    }
}*/
