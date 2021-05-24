package exercise.android.reemh.todo_items;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: implement!
public class TodoItemsHolderImpl implements TodoItemsHolder, Serializable {

  ArrayList<TodoItem> inProgressItems;
  ArrayList<TodoItem> doneItems;
  ArrayList<TodoItem> allItems;

  public TodoItemsHolderImpl() {
    inProgressItems = new ArrayList<>();
    doneItems = new ArrayList<>();
    allItems = new ArrayList<>();
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
    TodoItem newItem = new TodoItem(description, State.IN_PROGRESS);
    inProgressItems.add(0, newItem);
    getCurrentItems();
  }

  @Override
  public void markItemDone(TodoItem item) {
    if (inProgressItems.contains(item)) {
      inProgressItems.remove(item);
      doneItems.add(item);
    }
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    doneItems.remove(item);

    for (TodoItem currItem: inProgressItems) {
      if (item.creationTime > currItem.creationTime) {
        int i = inProgressItems.indexOf(currItem);
        inProgressItems.add(i, item);
        break;
      }
    }

    if (!inProgressItems.contains(item)) {
      inProgressItems.add(item);
    }
  }

  @Override
  public void deleteItem(TodoItem item) {
    if (inProgressItems.contains(item)) {
      inProgressItems.remove(item);
    }
    else doneItems.remove(item);
  }

  @Override
  public void setAllItems(ArrayList<TodoItem> items) {
    for (TodoItem item : items) {
      if (item.state == State.DONE) {
        doneItems.add(item);
      }
      else {
        inProgressItems.add(item);
      }
    }
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

}
