package com.example.lex.todolist;

import java.util.ArrayList;

/**
 * Created by lex on 3/13/2017.
 */

public class TodoList {

    private String title;
    private Boolean completed;
    private ArrayList<TodoItem> todoList;

    public TodoList(String Title, Boolean Completed){
        title = Title;
        completed = Completed;
    }

    public void setTitle(String Title){
        title = Title;
    }

    public void setCompleted(Boolean Completed){
        completed = Completed;
    }

    public void addTodoItem(TodoItem todoItem) {
        todoList.add(todoItem);
    }

    public void deleteTodoItem(TodoItem todoItem) {
        todoList.remove(todoItem);
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public ArrayList<TodoItem> getTodoList() {
        return todoList;
    }
}
