package hu.bme.aut.android.movies.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.movies.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    onDone: (KeyboardActionScope.() -> Unit)?
) {
    val shape = RoundedCornerShape(5.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label, color = MaterialTheme.colorScheme.onBackground)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.clip(shape),
        singleLine = singleLine,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = shape
    )
}