package hu.bme.aut.android.movies.ui.model

import hu.bme.aut.android.movies.domain.model.Movie

data class MovieUi(
    val id: String = "",
    val title: String = "",
    val year: Int = 2020,
    val length: Int = 120,
    val genre: GenreUi = GenreUi.Action,
    val description: String = ""
)

fun Movie.asMovieUi(): MovieUi = MovieUi(
    id = id,
    title = title,
    year = year,
    length = length,
    genre = genre.asGenreUi(),
    description = description
)

fun MovieUi.asMovie(): Movie = Movie(
    id = id,
    title = title,
    year = year,
    length = length,
    genre = genre.asGenre(),
    description = description
)