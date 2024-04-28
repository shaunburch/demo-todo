package com.example.demo.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme

@Composable
fun TodoListItem(todo: Todo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Column {
            Text(todo.title)
            Text(todo.description)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DemoTheme {
        TodoListItem(
            Todo(
                id = 0,
                title = "Title",
                description = "Description",
            )
        )
    }
}