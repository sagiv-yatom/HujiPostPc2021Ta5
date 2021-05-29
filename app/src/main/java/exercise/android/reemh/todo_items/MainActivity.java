package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public TodoItemsDataBase dataBase = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null || !savedInstanceState.containsKey("dataBase")) {
      dataBase = TodoItemsApplication.getInstance().getDataBase();
    }
    else {
      dataBase = (TodoItemsDataBaseImpl) savedInstanceState.getSerializable("dataBase");
    }

    RecyclerView recyclerView = findViewById(R.id.recyclerTodoItemsList);
    FloatingActionButton buttonCreateTodoItem = findViewById(R.id.buttonCreateTodoItem);
    EditText editTextInsertTask = findViewById(R.id.editTextInsertTask);

    LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    recyclerView.setLayoutManager(layout);

    TodoItemsAdapter adapter = new TodoItemsAdapter(this, (TodoItemsDataBaseImpl) dataBase);
    recyclerView.setAdapter(adapter);

    // for every time todoItems has changed, the method onChanged will be running
    dataBase.getItemsLiveData().observe(this, new Observer<List<TodoItem>>() {
      @Override
      public void onChanged(List<TodoItem> todoItems) {
        adapter.notifyDataSetChanged();
      }
    });

    buttonCreateTodoItem.setOnClickListener(v -> {
      String itemText = editTextInsertTask.getText().toString();

      // check if editTextInsertTask is not empty
      if (!itemText.matches("")) {
        dataBase.addNewInProgressItem(itemText);
        adapter.notifyDataSetChanged();
        editTextInsertTask.setText("");
      }
    });
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);

    EditText editTextInsertTask = findViewById(R.id.editTextInsertTask);

    outState.putString("edit text content", editTextInsertTask.getText().toString());
    outState.putSerializable("dataBase", this.dataBase);
  }
}

/*

SPECS:

- the screen starts out empty (no items shown, edit-text input should be empty)
- every time the user taps the "add TODO item" button:
    * if the edit-text is empty (no input), nothing happens
    * if there is input:
        - a new TodoItem (checkbox not checked) will be created and added to the items list
        - the new TodoItem will be shown as the first item in the Recycler view
        - the edit-text input will be erased
- the "TodoItems" list is shown in the screen
  * every operation that creates/edits/deletes a TodoItem should immediately be shown in the UI
  * the order of the TodoItems in the UI is:
    - all IN-PROGRESS items are shown first. items are sorted by creation time,
      where the last-created item is the first item in the list
    - all DONE items are shown afterwards, no particular sort is needed (but try to think about what's the best UX for the user)
  * every item shows a checkbox and a description. you can decide to show other data as well (creation time, etc)
  * DONE items should show the checkbox as checked, and the description with a strike-through text
  * IN-PROGRESS items should show the checkbox as not checked, and the description text normal
  * upon click on the checkbox, flip the TodoItem's state (if was DONE will be IN-PROGRESS, and vice versa)
  * add a functionality to remove a TodoItem. either by a button, long-click or any other UX as you want
- when a screen rotation happens (user flips the screen):
  * the UI should still show the same list of TodoItems
  * the edit-text should store the same user-input (don't erase input upon screen change)

Remarks:
- you should use the `holder` field of the activity
- you will need to create a class extending from RecyclerView.Adapter and use it in this activity
- notice that you have the "row_todo_item.xml" file and you can use it in the adapter
- you should add tests to make sure your activity works as expected. take a look at file `MainActivityTest.java`



(optional, for advanced students:
- save the TodoItems list to file, so the list will still be in the same state even when app is killed and re-launched
)

*/
