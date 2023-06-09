package hu.bme.aut.android.movies.feature.movie_check

import androidx.lifecycle.*
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckMovieViewModel(
    private val savedState: SavedStateHandle,
    private val movieService: MovieService,
) : ViewModel() {

    private val _state = MutableStateFlow(CheckMovieState())
    val state: StateFlow<CheckMovieState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CheckMovieEvent) {
        when(event) {
            CheckMovieEvent.EditingMovie -> {
                _state.update { it.copy(
                    isEditingMovie = true
                ) }
            }
            CheckMovieEvent.StopEditingMovie -> {
                _state.update { it.copy(
                    isEditingMovie = false
                ) }
            }
            is CheckMovieEvent.ChangeTitle -> {
                val newValue = event.text
                _state.update { it.copy(
                    movie = it.movie?.copy(title = newValue)
                ) }
            }
            is CheckMovieEvent.ChangeDescription -> {
                val newValue = event.text
                _state.update { it.copy(
                    movie = it.movie?.copy(description = newValue)
                ) }
            }
            is CheckMovieEvent.SelectGenre -> {
                val newValue = event.genre
                _state.update { it.copy(
                    movie = it.movie?.copy(genre = newValue)
                ) }
            }
            is CheckMovieEvent.ChangeYear -> {
                val newValue = event.int
                _state.update { it.copy(
                    movie = it.movie?.copy(year = newValue)
                ) }
            }
            is CheckMovieEvent.ChangeLength -> {
                val newValue = event.int
                _state.update { it.copy(
                    movie = it.movie?.copy(length = newValue)
                ) }
            }
            CheckMovieEvent.DeleteMovie -> {
                onDelete()
            }
            CheckMovieEvent.UpdateMovie -> {
                onUpdate()
            }
        }
    }

    init {
        load()
    }

    private fun load() {
        val movieId = checkNotNull<String>(savedState["id"])
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMovie = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val movie = movieService.getMovie(movieId)!!.asMovieUi()
                    _state.update { it.copy(isLoadingMovie = false, movie = movie) }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun onUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isMovieDataValid()) {
                    CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                        movieService.updateMovie(state.value.movie!!.asMovie())
                    }
                    _uiEvent.send(UiEvent.Success)
                } else {
                    val movie = state.value.movie
                    if (movie != null) {
                        if(movie.title == ""){
                            _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.title_error_message)))
                        }
                    }
                    if (movie != null) {
                        if (movie.year < 1880 || movie.year > 2100) {
                            _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.year_error_message)))
                        }
                    }
                    if (movie != null) {
                        if((movie.length < 1) || (movie.length > (6 * 60))){
                            _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.length_error_message)))
                        }
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun isMovieDataValid(): Boolean {
        val movie = state.value.movie ?: return false
        // Perform your validation checks here
        if(movie.title == ""){
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

    private fun onDelete() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    movieService.deleteMovie(state.value.movie!!.id)
                }
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val movieService = MovieApplication.movieService
                val savedState = createSavedStateHandle()
                CheckMovieViewModel(
                    movieService = movieService,
                    savedState = savedState
                )
            }
        }
    }
}

data class CheckMovieState(
    val movie: MovieUi? = null,
    val isLoadingMovie: Boolean = false,
    val isEditingMovie: Boolean = false,
    val error: Throwable? = null
)

sealed class CheckMovieEvent {
    object EditingMovie: CheckMovieEvent()
    object StopEditingMovie: CheckMovieEvent()
    data class ChangeTitle(val text: String): CheckMovieEvent()
    data class ChangeDescription(val text: String): CheckMovieEvent()
    data class ChangeYear(val int: Int): CheckMovieEvent()
    data class ChangeLength(val int: Int): CheckMovieEvent()
    data class SelectGenre(val genre: GenreUi): CheckMovieEvent()
    object DeleteMovie: CheckMovieEvent()
    object UpdateMovie: CheckMovieEvent()
}