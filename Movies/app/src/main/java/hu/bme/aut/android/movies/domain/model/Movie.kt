package hu.bme.aut.android.movies.domain.model

data class Movie(
    val id: String = "",
    val title: String = "",
    val year: Int = 1895,
    val length: Int = 120,
    val genre: Genre = Genre.ACTION,
    val description: String = "",
)