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
            TodoListEvent.CreateTodo -> createTodo()
            is TodoListEvent.Edit -> editTodo(event.todo)
        }
    }

    private fun editTodo(todo: Todo) {
        val currentList = _uiState.value.todos
        val updatedList =
            currentList.map {
                if (it.id == todo.id) todo else it
            }
        _uiState.value = TodoListState(updatedList)
    }

    private fun removeTodo(id: Int) {
        val currentList = _uiState.value.todos
        val updatedList = currentList.filter { it.id != id }
        _uiState.value = TodoListState(updatedList)
    }

    private fun createTodo() {
        val currentList = _uiState.value.todos
        val newId = currentList.maxOfOrNull(Todo::id)?.plus(1) ?: 0
        val newTodo =
            Todo(
                id = newId,
                title = "",
                description = "",
                isEditing = true,
            )
        _uiState.value = TodoListState(currentList + newTodo)
    }
}
