package com.example.demo.ui.list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo.data.Priority
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TodoListItem(
    todo: Todo,
    onEdit: (Todo) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onEdit(todo.copy(isEditing = true))
                },
    ) {
        // yellow with 50% opacity
        val color = if (todo.isFlagged) Color.Yellow.copy(alpha = 0.3f) else Color.Transparent
        Column(modifier = Modifier.background(color)) {
            if (todo.isEditing) {
                EditableTodo(todo, onEdit, focusManager)
            } else {
                ReadOnlyTodo(todo)
            }
        }
    }
}

const val FORMAT_DUE_AT = "M/d/yy hh:mm a"

@Composable
private fun ReadOnlyTodo(todo: Todo) {
    Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(todo.title, style = MaterialTheme.typography.headlineSmall)
        Text(todo.description, style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        ) {
            when (todo.priority) {
                Priority.NONE -> null
                Priority.LOW -> "•" to MaterialTheme.colorScheme.onSurface
                Priority.MEDIUM -> "••" to Color(0xFFFFA500)
                Priority.HIGH -> "•••" to Color.Red
            }?.let { (text, color) ->
                Text(
                    text,
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
                )
            }
            val formatter = remember { DateTimeFormatter.ofPattern(FORMAT_DUE_AT) }
            todo.dueAt?.let { Text("Due: ${it.format(formatter)}") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditableTodo(
    todo: Todo,
    onEdit: (Todo) -> Unit,
    focusManager: FocusManager,
) {
    val focusRequester = remember { FocusRequester() }
    var showDatePicker by remember { mutableStateOf(false) }
    val focusNext = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        modifier =
            Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        placeholder = { Text("Title") },
        value = todo.title,
        onValueChange = {
            onEdit(todo.copy(title = it))
        },
        singleLine = true,
        keyboardActions = focusNext,
    )
    TextField(
        modifier =
            Modifier
                .fillMaxWidth(),
        placeholder = { Text("Description") },
        value = todo.description,
        onValueChange = {
            onEdit(todo.copy(description = it))
        },
        singleLine = true,
        keyboardActions = focusNext,
    )
    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = todo.dueAt?.toInstant()?.toEpochMilli(),
                initialDisplayMode = DisplayMode.Input,
            )
        DatePicker(datePickerState)
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            // Done and Cancel buttons
            TextButton(
                onClick = {
                    showDatePicker = false
                },
            ) {
                Text(text = "Cancel")
            }
            TextButton(
                onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let {
                        onEdit(
                            todo.copy(
                                dueAt =
                                    OffsetDateTime.ofInstant(
                                        Instant.ofEpochMilli(it),
                                        ZoneId.systemDefault(),
                                    ),
                            ),
                        )
                    }
                },
            ) {
                Text(text = "Done")
            }
        }
    } else {
        TextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) showDatePicker = true
                    },
            readOnly = true,
            placeholder = { Text("Due") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Due") },
            value = todo.dueAt?.format(DateTimeFormatter.ofPattern(FORMAT_DUE_AT)) ?: "",
            onValueChange = {
                onEdit(todo.copy(dueAt = OffsetDateTime.parse(it)))
            },
            singleLine = true,
            keyboardActions = focusNext,
        )
    }
    HorizontalDivider()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        // Flag toggle
        IconButton(
            onClick = {
                onEdit(todo.copy(isFlagged = !todo.isFlagged))
            },
        ) {
            Icon(
                imageVector = if (todo.isFlagged) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Flag",
            )
        }
        Text("Priority: ")
        Priority.entries.forEach { priority ->
            val backgroundColor =
                if (todo.priority == priority) MaterialTheme.colorScheme.primary else Color.Transparent
            val textColor =
                if (todo.priority == priority) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            Text(
                text = priority.toString(),
                color = textColor,
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .clickable {
                            onEdit(todo.copy(priority = priority))
                        },
            )
        }
    }
}

@Preview(showBackground = true, name = "To Do")
@Preview(showBackground = true, name = "To Do (Night)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    DemoTheme {
        TodoListItem(
            Todo(
                id = 0,
                title = "Title",
                description = "Description",
                isFlagged = true,
                priority = Priority.HIGH,
                dueAt = OffsetDateTime.now(),
            ),
        )
    }
}

@Preview(showBackground = true, name = "To Do – Edit Mode")
@Preview(showBackground = true, name = "To Do – Edit Mode (Night)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewEdit() {
    DemoTheme {
        TodoListItem(
            Todo(
                id = 0,
                title = "Title",
                description = "Description",
                isEditing = true,
            ),
        )
    }
}
