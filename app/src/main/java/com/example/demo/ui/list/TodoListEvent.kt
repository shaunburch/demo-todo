package com.example.demo.ui.list

import com.example.demo.data.Todo

sealed class TodoListEvent {
    data object CreateTodo : TodoListEvent()
}
