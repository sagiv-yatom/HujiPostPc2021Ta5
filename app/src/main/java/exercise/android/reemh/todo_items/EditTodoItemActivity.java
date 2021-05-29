package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EditTodoItemActivity extends AppCompatActivity {

    public TodoItemsDataBase dataBase = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo_item);

        EditText itemEditDescription = findViewById(R.id.itemEditDescription);
        Button itemStatus = findViewById(R.id.stateButton);
        TextView creationTime = findViewById(R.id.creationTime);
        TextView lastModifiedTime = findViewById(R.id.modifyTime);

        if (dataBase == null) {
            dataBase = TodoItemsApplication.getInstance().getDataBase();
        }

        Intent intent = getIntent();

        if (intent.hasExtra("item_id")) {
            String itemId = intent.getStringExtra("item_id");
            TodoItem item = dataBase.getById(itemId);

            if (item != null) {
                itemEditDescription.setText(item.itemText);
                creationTime.setText(getCreationTime(item));
                lastModifiedTime.setText(getLastModifiedTime(item));

                if (item.getState() == State.DONE) {
                    itemStatus.setText("Done");
                }
                else itemStatus.setText("In Progress");
            }

            itemStatus.setOnClickListener(v -> {
                if (item.getState() == State.DONE) {
                    dataBase.markItemInProgress(item);
                    itemStatus.setText("In Progress");
                }
                else {
                    dataBase.markItemDone(item);
                    itemStatus.setText("Done");
                }

                item.setLastModifiedTime(System.currentTimeMillis());
            });

            itemEditDescription.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    dataBase.editItem(itemId, itemEditDescription.getText().toString());

                    item.setLastModifiedTime(System.currentTimeMillis());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Button stateButton = findViewById(R.id.stateButton);

        outState.putString("state", stateButton.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String state = savedInstanceState.getString("state");
        Button stateButton = findViewById(R.id.stateButton);
        stateButton.setText(state);
    }

    private String getCreationTime(TodoItem item) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(item.creationTimeInMilliSec);
    }

    private String getLastModifiedTime(TodoItem item) {
        Date currentDateTime = new Date(System.currentTimeMillis());
        Date modifyDateTime = new Date(item.getLastModifiedTime());

        long diffInMillis = Math.abs(modifyDateTime.getTime() - currentDateTime.getTime());
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter= new SimpleDateFormat("HH:mm");

        String currentDate = dateFormatter.format(currentDateTime);
        String currentTime  = timeFormatter.format(currentDateTime);
        String modifyDate = dateFormatter.format(modifyDateTime);
        String modifyTime = timeFormatter.format(modifyDateTime);

        if (diffInMinutes < 60) {
            return diffInMinutes + " minutes ago";
        }
        else if (currentDate.equals(modifyDate)) {
            return "today at " + modifyTime;
        }
        else {
            return currentDate + " at " + currentTime;
        }
    }
}