package com.example.demo.ui.list

import com.example.demo.data.Todo

sealed class TodoListEvent {
    data class AddTodo(val todo: Todo) : TodoListEvent()
    data class RemoveTodo(val id: Int) : TodoListEvent()
}
