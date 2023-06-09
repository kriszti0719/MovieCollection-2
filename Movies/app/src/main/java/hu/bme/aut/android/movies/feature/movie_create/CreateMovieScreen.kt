package hu.bme.aut.android.movies.feature.movie_create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import hu.bme.aut.android.movies.R
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.movies.ui.common.MovieAppBar
import hu.bme.aut.android.movies.ui.common.MovieEditor
import hu.bme.aut.android.movies.util.UiEvent
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun CreateMovieScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateMovieViewModel = viewModel(factory = CreateMovieViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    //var showDialog by remember { mutableStateOf(false) }
    val hostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is UiEvent.Success -> {
                    onNavigateBack()
                }
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
            MovieAppBar(
                title = stringResource(id = R.string.app_bar_title_add_movie),
                onNavigateBack = onNavigateBack,
                actions = { }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(8.dp)
            ){
                LargeFloatingActionButton(
                    onClick = { viewModel.onEvent(CreateMovieEvent.SaveMovie) },
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
            MovieEditor(
                titleValue = state.movie.title,
                titleOnValueChange = { viewModel.onEvent(CreateMovieEvent.ChangeTitle(it)) },
                descriptionValue = state.movie.description,
                descriptionOnValueChange = { viewModel.onEvent(CreateMovieEvent.ChangeDescription(it)) },
                selectedGenre = state.movie.genre,
                onGenreSelected = { viewModel.onEvent(CreateMovieEvent.SelectGenre(it)) },
                yearValue = state.movie.year,
                yearOnValueChange = { viewModel.onEvent(CreateMovieEvent.ChangeYear(it))},
                lengthValue = state.movie.length,
                lengthOnValueChange = { viewModel.onEvent(CreateMovieEvent.ChangeLength(it))},
                modifier = Modifier
            )
        }
    }
}