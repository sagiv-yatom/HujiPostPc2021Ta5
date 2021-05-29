package exercise.android.reemh.todo_items;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface TodoItemsDataBase extends Serializable {

  /** Get a copy of the current items list */
  ArrayList<TodoItem> getCurrentItems();

  /**
   * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
   */
  void addNewInProgressItem(String description);

  /** mark the @param item as DONE */
  void markItemDone(TodoItem item);

  /** mark the @param item as IN-PROGRESS */
  void markItemInProgress(TodoItem item);

  /** change the @itemId item's description to the given @param newDescription */
  void editItem(String itemId, String newDescription);

  /** delete the @param item */
  void deleteItem(TodoItem item);

  /** set the holder with @param items */
  void setAllItems(ArrayList<TodoItem> items);

  /** return the first item with the @param description */
  TodoItem getItemByDesc(String description);

  /** return an item by the @param id */
  @Nullable TodoItem getById(String id);

  /** get LiveData object */
  LiveData<List<TodoItem>> getItemsLiveData();

}
