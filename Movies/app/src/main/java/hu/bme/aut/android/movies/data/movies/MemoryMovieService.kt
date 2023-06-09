package hu.bme.aut.android.movies.data.movies

import hu.bme.aut.android.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemoryMovieService : MovieService {
    private val movieStateFlow = MutableStateFlow(emptyList<Movie>())

    override val movies: Flow<List<Movie>>
        get() = movieStateFlow.asStateFlow()

    override suspend fun getMovie(id: String): Movie? {
        return movieStateFlow.value.firstOrNull { it.id == id }
    }

    override suspend fun saveMovie(movie: Movie) {
        val movieList = movieStateFlow.value.toMutableList()
        movieList.removeIf { it.id == movie.id }
        movieList.add(movie.copy(id = movie.hashCode().toString()))
        movieStateFlow.value = movieList.toList()
    }

    override suspend fun updateMovie(movie: Movie) {
        movieStateFlow.value = movieStateFlow.value.map {
            if (it.id == movie.id) movie else it
        }
    }

    override suspend fun deleteMovie(id: String) {
        val movieList = movieStateFlow.value.toMutableList()
        movieList.removeIf { it.id == id }
        movieStateFlow.value = movieList.toList()
    }
}