package com.example.lex.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class TODOActivity extends AppCompatActivity {

    // state the listview and the edittext
    ListView TODOList;
    ListView DONEList;
    EditText add_categorie;

    // state  the watchlist
    String parentlist;

    // make the database and the adapters
    private DBManager dbManager;
    TodoCursorAdapter todoCursorAdapter;
    DoneCursorAdapter doneCursorAdapter;    //CHANGE THIS TO DONECURSORADAPTER!
    Cursor todocursor;
    Cursor donecursor;

    // state this activity
    TODOActivity main = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // get the categorie from the intent
        Intent intent = getIntent();
        Bundle categorie = intent.getExtras();
        parentlist = categorie.getString("parent");

        // get the database
        dbManager = new DBManager(this);
        dbManager.open();

        // make the to do adapter
        todocursor = dbManager.fetchtodo(parentlist);
        todoCursorAdapter = new TodoCursorAdapter(this, todocursor);

        // make the done adapter
        donecursor = dbManager.fetchdone(parentlist);
        doneCursorAdapter = new DoneCursorAdapter(this, donecursor);

        // sate the add bar
        add_categorie = (EditText) findViewById(R.id.add_categorie);
        assert add_categorie != null;

        // check for a savedinstancestate
        if (savedInstanceState != null) {
            // set the text to the saved state
            String savedsearch = savedInstanceState.getString("add_categorie");
            add_categorie.setText(savedsearch);
        }

        // make a listview of the todolist
        TODOList = (ListView) findViewById(R.id.TODOList);
        assert TODOList != null;
        TODOList.setAdapter(todoCursorAdapter);

        // make a listview of the donelist
        DONEList = (ListView) findViewById(R.id.Done_TODOList);
        assert DONEList != null;
        DONEList.setAdapter(doneCursorAdapter);

        // set an onlongclick listener for every to do item
        TODOList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // set the to do done
                Cursor Parent = (Cursor) parent.getAdapter().getItem(position);
                String todo = Parent.getString(todocursor.getColumnIndexOrThrow("todoitem"));
                dbManager.done(parentlist, todo);

                // notify the to do array adapter
                todocursor = dbManager.fetchtodo(parentlist);
                todoCursorAdapter.changeCursor(todocursor);
                todoCursorAdapter.notifyDataSetChanged();

                // notify the done array adapter
                donecursor = dbManager.fetchdone(parentlist);
                doneCursorAdapter.changeCursor(donecursor);
                doneCursorAdapter.notifyDataSetChanged();

                return true;
            }
        });

        // set an onlongclick listener for every done item
        DONEList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // delete the todo
                dbManager.deletetodo(id);

                // notify the done array adapter
                donecursor = dbManager.fetchdone(parentlist);
                doneCursorAdapter.changeCursor(donecursor);
                doneCursorAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    // add a to do
    public void add_todo_button(View view) {
        String todo = add_categorie.getText().toString();

        // check if the to do already exists
        if (dbManager.checktodo(parentlist, todo)) {
            dbManager.inserttodo(parentlist, todo);
        }
        else {
            Toast.makeText(main, "Todo already exists", Toast.LENGTH_SHORT).show();
        }

        // clear the edittext
        add_categorie.getText().clear();

        // notify the array adapter
        todocursor = dbManager.fetchtodo(parentlist);
        todoCursorAdapter.changeCursor(todocursor);
        todoCursorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // save the edittext
        outState.putString("add_categorie", add_categorie.getText().toString());

        super.onSaveInstanceState(outState);
    }
}