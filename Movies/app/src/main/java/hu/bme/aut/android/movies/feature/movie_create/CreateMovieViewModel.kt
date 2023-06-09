package hu.bme.aut.android.movies.feature.movie_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.movies.MovieApplication
import hu.bme.aut.android.movies.R
import hu.bme.aut.android.movies.data.movies.MovieService
import hu.bme.aut.android.movies.ui.model.*
import hu.bme.aut.android.movies.util.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CreateMovieViewModel(
    private val movieService: MovieService
) : ViewModel() {

    private val _state = MutableStateFlow(CreateMovieState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateMovieEvent) {
        when(event) {
            is CreateMovieEvent.ChangeTitle -> {
                val newValue = event.text
                _state.update { it.copy(
                    movie = it.movie.copy(title = newValue)
                ) }
            }
            is CreateMovieEvent.ChangeYear -> {
                val newValue = event.int
                _state.update { it.copy(
                    movie = it.movie.copy(year = newValue)
                ) }
            }
            is CreateMovieEvent.ChangeLength -> {
                val newValue = event.int
                _state.update { it.copy(
                    movie = it.movie.copy(length = newValue)
                ) }
            }
            is CreateMovieEvent.SelectGenre -> {
                val newValue = event.genre
                _state.update { it.copy(
                    movie = it.movie.copy(genre = newValue)
                ) }
            }
            is CreateMovieEvent.ChangeDescription -> {
                val newValue = event.text
                _state.update { it.copy(
                    movie = it.movie.copy(description = newValue)
                ) }
            }

            CreateMovieEvent.SaveMovie -> {
                onSave()
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            try {
                if (isMovieDataValid()) {
                    CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                        movieService.saveMovie(state.value.movie.asMovie())
                    }
                    _uiEvent.send(UiEvent.Success)
                } else {
                    val movie = state.value.movie
                    if(movie.title == ""){
                        _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.title_error_message)))
                    }
                    if (movie.year < 1880 || movie.year > 2100) {
                        _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.year_error_message)))
                    }
                    if((movie.length < 1) || (movie.length > (6 * 60))){
                        _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.length_error_message)))
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val movieService = MovieApplication.movieService
                CreateMovieViewModel(
                    movieService = movieService,
                )
            }
        }
    }

    private fun isMovieDataValid(): Boolean {
        val movie = state.value.movie

        // Perform your validation checks here
        if(movie.title==""){
            return false
        }
        if (movie.year < 1880 || movie.year > 2100) {
            return false
        }
        if(movie.length < 1 || movie.length > (6 * 60)){
            return false
        }

        return true
    }
}

data class CreateMovieState(
    val movie: MovieUi = MovieUi()
)

/** modellezzük a szerkesztés során bekövetkezhető egyes eseményeket */
sealed class CreateMovieEvent {
    data class ChangeTitle(val text: String): CreateMovieEvent()
    data class ChangeYear(val int: Int): CreateMovieEvent()
    data class ChangeLength(val int: Int): CreateMovieEvent()
    data class ChangeDescription(val text: String): CreateMovieEvent()
    data class SelectGenre(val genre: GenreUi): CreateMovieEvent()
    object SaveMovie: CreateMovieEvent()
}