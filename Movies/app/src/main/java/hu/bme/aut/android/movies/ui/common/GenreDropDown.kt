package hu.bme.aut.android.movies.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.movies.ui.model.GenreUi

@ExperimentalMaterial3Api
@Composable
fun GenreDropDown(
    genres: List<GenreUi>,
    selectedGenre: GenreUi,
    onGenreSelected: (GenreUi) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    val shape = RoundedCornerShape(5.dp)
    //val color = Color(0xecf0f1)

    Surface(
        modifier = modifier
            .clip(shape = shape)
            .width(TextFieldDefaults.MinWidth)
            .background(MaterialTheme.colorScheme.background)
            .height(TextFieldDefaults.MinHeight)
            .clickable(enabled = enabled) { expanded = true },
        shape = shape
    ) {
        Row(
            modifier = modifier
                .width(TextFieldDefaults.MinWidth)
                .height(TextFieldDefaults.MinHeight)
                .clip(shape = shape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = selectedGenre.imageResId),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .weight(weight = 8f),
                text = stringResource(id = selectedGenre.title),
                style = MaterialTheme.typography.labelMedium
            )
            IconButton(
                modifier = Modifier
                    .rotate(degrees = angle)
                    .weight(weight = 1.5f),
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            DropdownMenu(
                modifier = modifier
                    .width(TextFieldDefaults.MinWidth)
                    .background(MaterialTheme.colorScheme.background),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genres.forEach { genre ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = genre.title),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            expanded = false
                            onGenreSelected(genre)
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = genre.imageResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                            )
                        }
                    )
                }
            }
        }
    }


}

@ExperimentalMaterial3Api
@Composable
@Preview
fun GenreDropdown_Preview() {
    val genres = listOf(
        GenreUi.Action,
        GenreUi.Comedy,
        GenreUi.Drama,
        GenreUi.Family,
        GenreUi.Fantasy,
        GenreUi.Horror,
        GenreUi.Romance,
        GenreUi.SciFi,

    )
    var selectedGenre by remember { mutableStateOf(genres[0]) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GenreDropDown(
            genres = genres,
            selectedGenre = selectedGenre,
            onGenreSelected = {
                selectedGenre = it
            }
        )

    }
}