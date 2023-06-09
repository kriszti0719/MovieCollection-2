package hu.bme.aut.android.movies.util

import hu.bme.aut.android.movies.ui.model.UiText

sealed class UiEvent {
    object Success: UiEvent()
    data class Failure(val message: UiText): UiEvent()
}