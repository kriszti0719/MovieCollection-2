package hu.bme.aut.android.movies.navigation

sealed class Screen(val route: String) {
    object Movies: Screen("movies")
    object CreateMovie: Screen("create")
    object CheckMovie: Screen("check/{id}") {
        fun passId(id: String) = "check/$id"
    }
    object Loading: Screen("loading")
    object Login: Screen("login")
    object Register: Screen("register")
}