package hu.bme.aut.android.movies.ui.model

import androidx.compose.ui.graphics.Color
import hu.bme.aut.android.movies.R
import hu.bme.aut.android.movies.domain.model.Genre

enum class GenreUi(
    val title: Int,
    val color: Color,
    val imageResId: Int,
) {
    Action(
        title =  R.string.genre_title_action,
        color = Color(0xFFCF8B46),
        imageResId = R.drawable.action
    ),
    Comedy(
        title = R.string.genre_title_comedy,
        color = Color(0xFFFFFF8D),
        imageResId = R.drawable.comedy
    ),
    Drama(
        title = R.string.genre_title_drama,
        color = Color(0xFFF0A5A5),
        imageResId = R.drawable.drama
    ),
    Fantasy(
        title = R.string.genre_title_fantasy,
        color = Color(0xFF6DC7EB),
        imageResId = R.drawable.fantasy
    ),
    Horror(
        title = R.string.genre_title_horror,
        color = Color(0xFF2E2C2C),
        imageResId = R.drawable.horror
    ),
    Family(
        title = R.string.genre_title_family,
        color = Color(0xFF85E285),
        imageResId = R.drawable.family
    ),
    Romance(
        title = R.string.genre_title_romance,
        color = Color(0xFFC22F2F),
        imageResId = R.drawable.romance
    ),
    SciFi(
        title = R.string.genre_title_sci_fi,
        color = Color(0xFF23609C),
        imageResId = R.drawable.sci_fi
    )
}

fun GenreUi.asGenre(): Genre {
    return when(this) {
        GenreUi.Action -> Genre.ACTION
        GenreUi.Comedy -> Genre.COMEDY
        GenreUi.Drama -> Genre.DRAMA
        GenreUi.Family -> Genre.FAMILY
        GenreUi.Fantasy -> Genre.FANTASY
        GenreUi.Horror -> Genre.HORROR
        GenreUi.Romance -> Genre.ROMANCE
        GenreUi.SciFi -> Genre.SCI_FI
    }
}

fun Genre.asGenreUi(): GenreUi {
    return when(this) {
        Genre.ACTION -> GenreUi.Action
        Genre.COMEDY -> GenreUi.Comedy
        Genre.DRAMA -> GenreUi.Drama
        Genre.FAMILY -> GenreUi.Family
        Genre.FANTASY -> GenreUi.Fantasy
        Genre.HORROR -> GenreUi.Horror
        Genre.ROMANCE -> GenreUi.Romance
        Genre.SCI_FI -> GenreUi.SciFi
    }
}