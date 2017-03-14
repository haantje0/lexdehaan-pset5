package com.example.lex.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    // state the listview and the edittext
    ListView CategorieList;
    EditText add_todo;

    // make the database and adapter
    private DBManager dbManager;
    TodoListCursorAdapter cursorAdapter;
    Cursor cursor;

    // state this activity
    MainActivity main = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the database
        dbManager = new DBManager(this);
        dbManager.open();

        // get the right adapter
        cursor = dbManager.fetchcategorie();
        cursorAdapter = new TodoListCursorAdapter(this, cursor);

        // sate the add bar
        add_todo = (EditText) findViewById(R.id.add_categorie);
        assert add_todo != null;

        // check for a savedinstancestate
        if (savedInstanceState != null) {
            // set the text to the saved state
            String savedsearch = savedInstanceState.getString("add_categorie");
            add_todo.setText(savedsearch);
        }

        // make a listview of the watchlist
        CategorieList = (ListView) findViewById(R.id.CategorieList);
        assert CategorieList != null;
        CategorieList.setAdapter(cursorAdapter);

        // set an onclick listener for every list item
        CategorieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(main, TODOActivity.class);

                // give the categorie in the intent
                Cursor Parent = (Cursor) parent.getAdapter().getItem(position);
                String parentname = Parent.getString(cursor.getColumnIndexOrThrow("list"));
                intent.putExtra("parent", parentname);
                startActivity(intent);
            }
        });

        // set an onlongclick listener for every list item
        CategorieList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // delete the categorie
                Cursor Parent = (Cursor) parent.getAdapter().getItem(position);
                String listname = Parent.getString(cursor.getColumnIndexOrThrow("list"));
                dbManager.deletecategorie(listname);

                // notify the array adapter
                cursor = dbManager.fetchcategorie();
                cursorAdapter.changeCursor(cursor);
                cursorAdapter.notifyDataSetChanged();

                return true;
        }});
    }

    // add a categorie
    public void add_categorie_button(View view) {
        String categorie = add_todo.getText().toString();

        // check if the categorie already exists
        if (dbManager.checkcategories(categorie)) {
            dbManager.insertcatagorie(categorie);
        }
        else {
            Toast.makeText(main, "Categorie already exists", Toast.LENGTH_SHORT).show();
        }

        // clear the edittext
        add_todo.getText().clear();

        // notify the array adapter
        cursor = dbManager.fetchcategorie();
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // save the edittext
        outState.putString("add_categorie", add_todo.getText().toString());

        super.onSaveInstanceState(outState);
    }
}
