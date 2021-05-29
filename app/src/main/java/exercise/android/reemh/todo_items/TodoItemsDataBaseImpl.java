package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase, Serializable {

  ArrayList<TodoItem> inProgressItems;
  ArrayList<TodoItem> doneItems;
  ArrayList<TodoItem> allItems;
  private static SharedPreferences sp;
  private static MutableLiveData<List<TodoItem>> itemsLiveDataMutable;
  public static LiveData<List<TodoItem>> itemsLiveDataPublic;

  public TodoItemsDataBaseImpl(Context context) {
    this.inProgressItems = new ArrayList<>();
    this.doneItems = new ArrayList<>();
    this.allItems = new ArrayList<>();
    sp = context.getSharedPreferences("local_db_items", Context.MODE_PRIVATE);
    itemsLiveDataMutable = new MutableLiveData<>();
    itemsLiveDataPublic = itemsLiveDataMutable;
    initializeFromSp();
  }

  private void addInProgressItemInOrder(TodoItem item) {
    for (TodoItem currItem: inProgressItems) {
      if (item.creationTimeInNanoSec > currItem.creationTimeInNanoSec) {
        int i = inProgressItems.indexOf(currItem);
        inProgressItems.add(i, item);
        break;
      }
    }

    if (!inProgressItems.contains(item)) {
      inProgressItems.add(item);
    }
  }

  private @Nullable TodoItem stringToItem(String string) {
    if (string == null) return null;
    try {
      String[] split = string.split("#");

      String id = split[0];
      String text = split[1];
      State state = (split[2].equals("DONE")) ? State.DONE : State.IN_PROGRESS;
      long creationTimeInNanoSec = Long.parseLong(split[3]);
      long creationTimeInMilliSec = Long.parseLong(split[4]);
      long lastModifiedTime = Long.parseLong(split[5]);
      TodoItem item = new TodoItem(id, text, state);
      item.setCreationTimeInNanoSec(creationTimeInNanoSec);
      item.setCreationTimeInMilliSec(creationTimeInMilliSec);
      item.setLastModifiedTime(lastModifiedTime);
      return item;
    } catch (Exception e) {
      System.out.println("exception");
      return null;
    }
  }

  private void initializeFromSp() {
    Set<String> keys = sp.getAll().keySet();
    for (String key : keys) {
      String itemSavedAsString = sp.getString(key, null);
      TodoItem item = stringToItem(itemSavedAsString);
      if (item != null) {
        if (item.getState() == State.DONE) {
          doneItems.add(item);
        }
        else {
          this.addInProgressItemInOrder(item);
        }
      }
    }
    itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));
  }

  public @Nullable TodoItem getById(String id) {
    if (id == null) return null;
    for (TodoItem item : this.getCurrentItems()) {
      if (item.getId().equals(id)) {
        return item;
      }
    }
    return null;
  }

  @Override
  public ArrayList<TodoItem> getCurrentItems() {
    allItems.clear();
    allItems.addAll(inProgressItems);
    allItems.addAll(doneItems);
    return allItems;
  }

  @Override
  public void addNewInProgressItem(String description) {
    String newId = UUID.randomUUID().toString();
    TodoItem newItem = new TodoItem(newId, description, State.IN_PROGRESS);
    inProgressItems.add(0, newItem);
    itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));

    SharedPreferences.Editor editor = sp.edit();
    editor.putString(newId, newItem.serialize());
    editor.apply();
  }

  public void editItem(String itemId, String newDescription) {
    TodoItem oldItem = getById(itemId);
    if (oldItem == null) return;
    TodoItem newItem = new TodoItem(itemId, newDescription, oldItem.getState());
    newItem.setCreationTimeInNanoSec(oldItem.creationTimeInNanoSec);
    newItem.setCreationTimeInMilliSec(oldItem.creationTimeInMilliSec);
    newItem.setLastModifiedTime(oldItem.lastModifiedTime);

    if (oldItem.getState() == State.DONE) {
      doneItems.remove(oldItem);
      doneItems.add(newItem);
    }
    else {
      inProgressItems.remove(oldItem);
      this.addInProgressItemInOrder(newItem);
    }
    itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));

    SharedPreferences.Editor editor = sp.edit();
    editor.putString(newItem.getId(), newItem.serialize());
    editor.apply();
  }

  @Override
  public void markItemDone(TodoItem item) {
    item.setState(State.DONE);

    if (inProgressItems.contains(item)) {
      inProgressItems.remove(item);
      doneItems.add(item);

      itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));

      SharedPreferences.Editor editor = sp.edit();
      editor.putString(item.getId(), item.serialize());
      editor.apply();
    }
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    item.setState(State.IN_PROGRESS);
    doneItems.remove(item);
    this.addInProgressItemInOrder(item);

    itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));

    SharedPreferences.Editor editor = sp.edit();
    editor.putString(item.getId(), item.serialize());
    editor.apply();
  }

  @Override
  public void deleteItem(TodoItem item) {
    if (inProgressItems.contains(item)) {
      inProgressItems.remove(item);
    }
    else doneItems.remove(item);

    itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));

    SharedPreferences.Editor editor = sp.edit();
    editor.remove(item.getId());
    editor.apply();
  }

  @Override
  public void setAllItems(ArrayList<TodoItem> items) {
    SharedPreferences.Editor editor = sp.edit();
    for (TodoItem item : items) {
      if (item.state == State.DONE) {
        doneItems.add(item);
      }
      else {
        this.addInProgressItemInOrder(item);
      }
      editor.putString(item.getId(), item.serialize());
      editor.apply();
    }
    itemsLiveDataMutable.setValue(new ArrayList<>(this.getCurrentItems()));
  }

  public TodoItem getItemByDesc(String description) {
    for(TodoItem item : inProgressItems) {
      if (item.itemText.equals(description)) {
        return item;
      }
    }

    for(TodoItem item : doneItems) {
      if (item.itemText.equals(description)) {
        return item;
      }
    }

    return null;
  }

  public LiveData<List<TodoItem>> getItemsLiveData() {
    return itemsLiveDataPublic;
  }

}
