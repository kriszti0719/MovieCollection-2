package hu.bme.aut.android.movies.feature.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.movies.MovieApplication
import hu.bme.aut.android.movies.data.auth.AuthService
import hu.bme.aut.android.movies.data.movies.MovieService
import hu.bme.aut.android.movies.ui.model.MovieUi
import hu.bme.aut.android.movies.ui.model.asMovieUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val authService: AuthService,
    private val movieService: MovieService
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesState())
    val state = _state.asStateFlow()

    init {
        loadMovies()
    }
    private fun loadMovies() {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                movieService.movies.collect {
                    val movies = it.sortedBy { it.title }.map { it.asMovieUi() }
                    _state.update { it.copy(isLoading = false, movies = movies) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authService.signOut()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val authService = MovieApplication.authService
                val movieService = MovieApplication.movieService
                MoviesViewModel(
                    authService = authService,
                    movieService = movieService
                )
            }
        }
    }
}


data class MoviesState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val movies: List<MovieUi> = emptyList()
)