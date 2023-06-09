package hu.bme.aut.android.movies.data.movies.firebase

import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.movies.domain.model.Genre
import hu.bme.aut.android.movies.domain.model.Movie

/** az adatot reprezentáló osztály  */
data class FirebaseMovie (
    @DocumentId val id: String = "",
    val title: String = "",
    val year: Int = 1895,
    val length: Int = 120,
    val genre: Genre = Genre.ACTION,
    val description: String = ""
)

fun FirebaseMovie.asMovie() = Movie(
    id = id,
    title = title,
    year = year,
    length = length,
    genre = genre,
    description = description,
)

fun Movie.asFireBaseMovie() = FirebaseMovie(
    id = id,
    title = title,
    year = year,
    length = length,
    genre = genre,
    description = description
)