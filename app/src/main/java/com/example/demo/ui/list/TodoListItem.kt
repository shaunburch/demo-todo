package com.example.demo.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo.data.Priority
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme
import java.time.OffsetDateTime
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
                .focusGroup()
                .clickable {
                    onEdit(todo.copy(isEditing = true))
                },
    ) {
        val color = if (todo.isFlagged) Color(0xFFFFF59D) else Color.Transparent
        Column(modifier = Modifier.background(color)) {
            if (todo.isEditing) {
                EditableTodo(todo, onEdit, focusManager)
            } else {
                ReadOnlyTodo(todo)
            }
        }
    }
}

@Composable
private fun ReadOnlyTodo(todo: Todo) {
    Text(todo.title)
    Text(todo.description)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
        if (todo.priority != Priority.NONE) Text("Priority: ${todo.priority}")
        val formatter = remember { DateTimeFormatter.ofPattern("M/d/yy hh:mm a") }
        todo.dueAt?.let { Text("Due: ${it.format(formatter)}") }
    }
}

@Composable
private fun EditableTodo(
    todo: Todo,
    onEdit: (Todo) -> Unit,
    focusManager: FocusManager,
) {
    val focusNext = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) }
    TextField(
        modifier =
            Modifier
                .fillMaxWidth(),
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
    TextField(
        modifier =
            Modifier
                .fillMaxWidth(),
        placeholder = { Text("Due") },
        value = todo.dueAt?.toString() ?: "",
        onValueChange = {
            onEdit(todo.copy(dueAt = OffsetDateTime.parse(it)))
        },
        singleLine = true,
        keyboardActions = focusNext,
    )
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
