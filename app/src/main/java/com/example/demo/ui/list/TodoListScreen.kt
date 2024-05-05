package com.example.demo.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme

@ExperimentalMaterial3Api
@Composable
fun TodoListScreen() {
    val vm: TodoListViewModel = viewModel()
    val state by vm.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "To Do") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Menu",
                    )
                },
                actions = {
                    if (state.todos.any { it.isEditing }) {
                        IconButton(onClick = { vm.onEvent(TodoListEvent.StopEditing) }) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Done")
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { vm.onEvent(TodoListEvent.CreateTodo) },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        vm.onEvent(TodoListEvent.StopEditing)
                    }
                    .fillMaxHeight(),
            contentPadding = padding,
        ) {
            if (state.todos.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Nothing to do!")
                    }
                }
            } else {
                items(items = state.todos, key = Todo::id) { todo ->
                    TodoListItem(todo) { vm.onEvent(TodoListEvent.Edit(it)) }
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    DemoTheme {
        TodoListScreen()
    }
}
