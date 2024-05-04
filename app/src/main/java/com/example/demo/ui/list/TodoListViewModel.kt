package com.example.demo.ui.list

import androidx.lifecycle.ViewModel
import com.example.demo.data.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TodoListState(emptyList()))
    val uiState: StateFlow<TodoListState> = _uiState.asStateFlow()

    fun onEvent(event: TodoListEvent) {
        when (event) {
            TodoListEvent.CreateTodo -> TODO()
        }
    }

    private fun removeTodo(id: Int) {
        val currentList = _uiState.value.todos
        val updatedList = currentList.filter { it.id != id }
        _uiState.value = TodoListState(updatedList)
    }

    private fun addTodo(todo: Todo) {
        TODO("Not yet implemented")
    }
}