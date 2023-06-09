package hu.bme.aut.android.movies.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.android.movies.MovieApplication
import hu.bme.aut.android.movies.feature.movie_auth.login.LoginScreen
import hu.bme.aut.android.movies.feature.movie_auth.register.RegisterScreen
import hu.bme.aut.android.movies.feature.movie_check.CheckMovieScreen
import hu.bme.aut.android.movies.feature.movie_create.CreateMovieScreen
import hu.bme.aut.android.movies.feature.movie_list.MoviesScreen
import hu.bme.aut.android.movies.feature.movie_load.LoadingScreen
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    val authService = MovieApplication.authService

    val coroutineScope = MainScope()

    LaunchedEffect(authService) {
        if (authService.hasUser) {
            // User is already authenticated, navigate to loading screen
            navController.navigate(Screen.Loading.route) {
                popUpTo(Screen.Login.route) {
                    inclusive = true
                }
            }

            // Simulate a delay before navigating to the MoviesScreen
            coroutineScope.launch {
                delay(1500) // Adjust the duration as needed
                navController.navigate(Screen.Movies.route) {
                    popUpTo(Screen.Loading.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Screen.Loading.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }

                    // Simulate a delay before navigating to the MoviesScreen
                    coroutineScope.launch {
                        delay(1500) // Adjust the duration as needed
                        navController.navigate(Screen.Movies.route) {
                            popUpTo(Screen.Loading.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Login.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Login.route)
                },
                onSuccess = {
                    navController.navigate(Screen.Movies.route)
                }
            )
        }
        composable(Screen.Loading.route) {
            LoadingScreen()
        }
        composable(Screen.Movies.route) {
            MoviesScreen(
                onListItemClick = {
                    navController.navigate(Screen.CheckMovie.passId(it))
                },
                onFabClick = {
                    navController.navigate(Screen.CreateMovie.route)
                },
                onSignOut = {
                    navController.popBackStack(
                        route = Screen.Login.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.CreateMovie.route) {
            CreateMovieScreen(onNavigateBack = {
                navController.popBackStack(
                    route = Screen.Movies.route,
                    inclusive = true
                )
                navController.navigate(Screen.Movies.route)
            })
        }
        composable(
            route = Screen.CheckMovie.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {
            CheckMovieScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Movies.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Movies.route)
                }
            )
        }
    }
}