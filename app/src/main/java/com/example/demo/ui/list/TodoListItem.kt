package com.example.demo.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme

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
        Column {
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
}

@Composable
private fun EditableTodo(
    todo: Todo,
    onEdit: (Todo) -> Unit,
    focusManager: FocusManager,
) {
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
        keyboardActions =
        KeyboardActions {
            focusManager.moveFocus(FocusDirection.Next)
        },
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
        keyboardActions =
        KeyboardActions {
            focusManager.clearFocus()
            onEdit(todo.copy(isEditing = false))
        },
    )
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
