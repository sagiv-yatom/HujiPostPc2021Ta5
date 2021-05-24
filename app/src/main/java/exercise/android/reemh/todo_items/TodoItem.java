package exercise.android.reemh.todo_items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

enum State {
    IN_PROGRESS,
    DONE
}

public class TodoItem implements Serializable {
  // TODO: edit this class as you want

    String itemText;
    State state;
    long creationTime;

    public TodoItem(String text, State s) {
        itemText = text;
        state = s;
        creationTime = System.nanoTime();
    }
}
