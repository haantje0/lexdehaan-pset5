package com.example.lex.todolist;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.sql.Time;

/**
 * Created by lex on 3/7/2017.
 */

public class TodoItem {

    private String title;
    private Boolean completed;

    public TodoItem(String Title, Boolean Completed){
        title = Title;
        completed = Completed;
    }

    public void setTitle(String Title){
        title = Title;
    }

    public void setCompleted(Boolean Completed){
        completed = Completed;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
