package exercise.android.reemh.todo_items;

import android.app.Application;

import java.io.Serializable;

public class TodoItemsApplication extends Application implements Serializable {

    private TodoItemsDataBase dataBase;

    public TodoItemsDataBase getDataBase() {
        return dataBase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = new TodoItemsDataBaseImpl(this);
    }

    private static TodoItemsApplication instance = null;

    public static TodoItemsApplication getInstance() {
        return instance;
    }
}
