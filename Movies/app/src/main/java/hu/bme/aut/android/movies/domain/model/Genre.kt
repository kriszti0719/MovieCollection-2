package hu.bme.aut.android.movies.domain.model

enum class Genre {
    ACTION,
    COMEDY,
    DRAMA,
    FAMILY,
    FANTASY,
    HORROR,
    ROMANCE,
    SCI_FI;

    companion object {
        val genres = listOf(ACTION, COMEDY, DRAMA, FAMILY, FANTASY, HORROR, ROMANCE, SCI_FI)
    }
}