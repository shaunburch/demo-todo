package com.example.demo.ui.list

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme

@Composable
fun TodoListItem(
    todo: Todo,
    onEdit: (Todo) -> Unit = {},
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {
            if (todo.isEditing) {
                TextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .focusable(),
                    value = todo.title,
                    maxLines = 1,
                    placeholder = { Text("Title") },
                    onValueChange = {
                        onEdit(todo.copy(title = it))
                    },
                )
                TextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .focusable(),
                    value = todo.description,
                    placeholder = { Text("Description") },
                    onValueChange = {
                        onEdit(todo.copy(description = it))
                    },
                )
            } else {
                Text(todo.title)
                Text(todo.description)
            }
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
