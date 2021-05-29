package exercise.android.reemh.todo_items;

import android.annotation.SuppressLint;
import android.os.Build;

import java.text.SimpleDateFormat;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Date;

enum State {
    IN_PROGRESS,
    DONE
}

public class TodoItem implements Serializable {

    String itemId;
    String itemText;
    State state;
    long creationTimeInNanoSec;
    long creationTimeInMilliSec;
    long lastModifiedTime;

    public TodoItem(String id, String text, State state) {
        this.itemId = id;
        this.itemText = text;
        this.state = state;
        this.creationTimeInNanoSec = System.nanoTime();
        this.creationTimeInMilliSec = System.currentTimeMillis();
        this.lastModifiedTime = System.currentTimeMillis();
    }

    public String getId() {
        return this.itemId;
    }

    public State getState() {
        return this.state;
    }

    public long getLastModifiedTime() { return this.lastModifiedTime; }

    public void setState(State state) {
        this.state = state;
    }

    public void setCreationTimeInNanoSec(long time) { this.creationTimeInNanoSec = time; }

    public void setCreationTimeInMilliSec(long time) { this.creationTimeInMilliSec = time; }

    public void setLastModifiedTime(long time) {
        this.lastModifiedTime = time;
    }

    public void setDescription(String description) { this.itemText = description; }

    public String serialize() {
        return itemId + "#" + itemText + "#" + state + "#" + creationTimeInNanoSec +
                "#" + creationTimeInMilliSec + "#" + lastModifiedTime;
    }
}