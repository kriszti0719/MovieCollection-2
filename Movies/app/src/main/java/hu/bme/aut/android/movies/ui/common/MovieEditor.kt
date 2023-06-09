package hu.bme.aut.android.movies.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.movies.R
import hu.bme.aut.android.movies.domain.model.Genre
import hu.bme.aut.android.movies.ui.model.GenreUi
import hu.bme.aut.android.movies.ui.model.asGenreUi


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MovieEditor(
    titleValue: String,
    titleOnValueChange: (String) -> Unit,
    descriptionValue: String,
    descriptionOnValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    genres: List<GenreUi> = Genre.genres.map { it.asGenreUi() },
    selectedGenre: GenreUi,
    onGenreSelected: (GenreUi) -> Unit,
    yearValue: Int,
    yearOnValueChange: (Int) -> Unit,
    lengthValue: Int,
    lengthOnValueChange: (Int) -> Unit,
    enabled: Boolean = true,
) {
    val fraction = 0.95f

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background( color = MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        if (enabled) {
            NormalTextField(
                value = titleValue,
                label = stringResource(id = R.string.textfield_label_title),
                onValueChange = titleOnValueChange,
                singleLine = true,
                onDone = { keyboardController?.hide()  },
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .padding(top = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        GenreDropDown(
            genres = genres,
            selectedGenre = selectedGenre,
            onGenreSelected = onGenreSelected,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(fraction),
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = yearValue.toString(),
            onValueChange = {
                val intValue = it.toIntOrNull() ?: 0
                yearOnValueChange(intValue)
            },
            label = {
                val labelText = stringResource(id = R.string.textfield_label_year)
                Text(text = labelText, color = MaterialTheme.colorScheme.onBackground)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth(fraction)
                .padding(top = 5.dp),
            enabled = enabled,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = if (enabled) lengthValue.toString() else {
                val hour = lengthValue / 60
                val min = lengthValue % 60
                buildString {
                    if (hour > 0) {
                        append(hour)
                        append("h ")
                    }
                    if (min > 0) {
                        append(min)
                        append("m")
                    }
                }
            },
            onValueChange = {
                val intValue = it.toIntOrNull() ?: 0
                lengthOnValueChange(intValue)
            },
            label = {
                val labelText = if (enabled) {
                    stringResource(id = R.string.textfield_label_length_min)
                } else {
                    stringResource(id = R.string.textfield_label_length)
                }
                Text(text = labelText, color = MaterialTheme.colorScheme.onBackground)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth(fraction)
                .padding(top = 5.dp),
            enabled = enabled,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background
            ),
        )

        Spacer(modifier = Modifier.height(5.dp))
        NormalTextField(
            value = descriptionValue,
            label = stringResource(id = R.string.textfield_label_description),
            onValueChange = descriptionOnValueChange,
            singleLine = false,
            onDone = { keyboardController?.hide() },
            modifier = Modifier
                .weight(10f)
                .fillMaxWidth(fraction)
                .padding(bottom = 5.dp)
            ,
            enabled = enabled
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun MovieEditor_Preview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(1895) }
    var length by remember { mutableStateOf(120) }


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

    Box(Modifier.fillMaxSize()) {
        MovieEditor(
            titleValue = title,
            titleOnValueChange = { title = it },
            descriptionValue = description,
            descriptionOnValueChange = { description = it },
            genres = genres,
            selectedGenre = selectedGenre,
            onGenreSelected = { selectedGenre = it },
            yearValue = year,
            yearOnValueChange = {year = it},
            lengthValue = length,
            lengthOnValueChange = {length = it}
        )
    }
}
