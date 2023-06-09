package hu.bme.aut.android.movies.feature.movie_check

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import hu.bme.aut.android.movies.R
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.movies.feature.movie_load.LoadingScreen
import hu.bme.aut.android.movies.ui.common.MovieAppBar
import hu.bme.aut.android.movies.ui.common.MovieEditor
import hu.bme.aut.android.movies.ui.model.MovieUi
import hu.bme.aut.android.movies.util.UiEvent
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun CheckMovieScreen(
    onNavigateBack: () -> Unit,
    viewModel: CheckMovieViewModel = viewModel(factory = CheckMovieViewModel.Factory)
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    //var showDialog by remember { mutableStateOf(false) }
    val hostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> { onNavigateBack() }
                is UiEvent.Failure -> {
                    scope.launch {
                        hostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            if (!state.isLoadingMovie) {
                MovieAppBar(
                    title = if (state.isEditingMovie) {
                        stringResource(id = R.string.app_bar_title_edit_movie)
                    } else state.movie?.title ?: "Movie",
                    onNavigateBack = onNavigateBack,
                    actions = {
                        IconButton(
                            onClick = {
                                if (state.isEditingMovie) {
                                    viewModel.onEvent(CheckMovieEvent.StopEditingMovie)
                                } else {
                                    viewModel.onEvent(CheckMovieEvent.EditingMovie)
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CheckMovieEvent.DeleteMovie)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (state.isEditingMovie) {
                LargeFloatingActionButton(
                    onClick = {
                        viewModel.onEvent(CheckMovieEvent.UpdateMovie)
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            if (state.isLoadingMovie) {
                LoadingScreen()
            } else {
                val movie = state.movie ?: MovieUi()
                MovieEditor(
                    titleValue = movie.title,
                    titleOnValueChange = { viewModel.onEvent(CheckMovieEvent.ChangeTitle(it)) },
                    descriptionValue = movie.description,
                    descriptionOnValueChange = { viewModel.onEvent(CheckMovieEvent.ChangeDescription(it)) },
                    selectedGenre = movie.genre,
                    onGenreSelected = { viewModel.onEvent(CheckMovieEvent.SelectGenre(it)) },
                    yearValue = movie.year,
                    yearOnValueChange = { viewModel.onEvent(CheckMovieEvent.ChangeYear(it)) },
                    lengthValue = movie.length,
                    lengthOnValueChange = { viewModel.onEvent(CheckMovieEvent.ChangeLength(it)) },
                    modifier = Modifier,
                    enabled = state.isEditingMovie
                )
            }
        }
    }
}