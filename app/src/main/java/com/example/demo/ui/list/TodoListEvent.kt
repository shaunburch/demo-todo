package com.example.demo.ui.list

import com.example.demo.data.Todo

sealed class TodoListEvent {
    data class Edit(val todo: Todo) : TodoListEvent()

    data object CreateTodo : TodoListEvent()
}
