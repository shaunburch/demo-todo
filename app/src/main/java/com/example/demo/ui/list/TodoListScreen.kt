package com.example.demo.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.data.Todo
import com.example.demo.ui.theme.DemoTheme

@ExperimentalMaterial3Api
@Composable
fun TodoListScreen() {
    val vm: TodoListViewModel = viewModel()
    val state by vm.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    vm.onEvent(TodoListEvent.CreateTodo)
                    focusRequester.requestFocus()
                },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
        ) {
            if (state.todos.isEmpty()) {
                item {
                    Text("Nothing to do!")
                }
            } else {
                items(items = state.todos, key = Todo::id) { todo ->
                    TodoListItem(todo) { vm.onEvent(TodoListEvent.Edit(it)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val vm = TodoListViewModel()
    DemoTheme {
        TodoListScreen()
    }
}
