package hu.bme.aut.android.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hu.bme.aut.android.movies.navigation.NavGraph
import hu.bme.aut.android.movies.ui.theme.MovieTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheme() {
                NavGraph()
            }
        }
    }
}