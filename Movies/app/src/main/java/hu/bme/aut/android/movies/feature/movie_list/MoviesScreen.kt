package hu.bme.aut.android.movies.feature.movie_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.movies.R
import hu.bme.aut.android.movies.feature.movie_load.LoadingScreen
import hu.bme.aut.android.movies.ui.common.MovieAppBar
import hu.bme.aut.android.movies.ui.model.toUiText
import hu.bme.aut.android.movies.R.string as StringResources

@ExperimentalMaterial3Api
@Composable
fun MoviesScreen(
    onListItemClick: (String) -> Unit,
    onFabClick: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: MoviesViewModel = viewModel(factory = MoviesViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MovieAppBar(
                title = stringResource(id = StringResources.text_your_movie_list),
                actions = {
                    IconButton(onClick = {
                        viewModel.signOut()
                        onSignOut()
                    }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                    }

                }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                LargeFloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }


    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    color = if (!state.isLoading && !state.isError) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                LoadingScreen()
            } else if (state.isError) {
                Text(
                    text = state.error?.toUiText()?.asString(context)
                        ?: stringResource(id = R.string.some_error_message)
                )
            } else {
                if (state.movies.isEmpty()) {
                    Text(text = stringResource(id = R.string.text_empty_movie_list))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        items(state.movies.size) { i ->
                            ListItem(
                                headlineText = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = state.movies[i].genre.imageResId),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(
                                                    end = 8.dp,
                                                    top = 8.dp,
                                                    bottom = 8.dp
                                                ),
                                        )

                                        Text(text = state.movies[i].title)
                                    }
                                },
                                modifier = Modifier.clickable(onClick = {
                                    onListItemClick(
                                        state.movies[i].id
                                    )
                                })
                            )
                            if (i != state.movies.lastIndex) {
                                Divider(
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}