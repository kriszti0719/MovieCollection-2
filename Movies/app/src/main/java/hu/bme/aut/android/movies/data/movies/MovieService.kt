package hu.bme.aut.android.movies.data.movies

import hu.bme.aut.android.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieService {
    val movies: Flow<List<Movie>>

    suspend fun getMovie(id: String): Movie?

    suspend fun saveMovie(movie: Movie)

    suspend fun updateMovie(movie: Movie)

    suspend fun deleteMovie(id: String)
}