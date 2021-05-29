package exercise.android.reemh.todo_items;

import org.junit.Assert;
import org.junit.Test;

public class TodoItemsHolderImplTest {
  @Test
  public void when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_adding2TodoItems_then_callingListShouldHaveTheseItems(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");

    // verify
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_addingTodoItem_then_itemShouldBeInTopOfTheList(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    TodoItem item = holderUnderTest.getItemByDesc("do shopping 2");

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().indexOf(item));
  }

  @Test
  public void when_changingItemStateToDone_then_itemShouldBeAfterInProgressItems(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    holderUnderTest.addNewInProgressItem("do shopping 3");
    TodoItem item = holderUnderTest.getItemByDesc("do shopping 2");
    holderUnderTest.markItemDone(item);

    // verify
    Assert.assertEquals(2, holderUnderTest.getCurrentItems().indexOf(item));
  }

  @Test
  public void when_changingItemStateToInProgress_then_itemShouldReturnToItsOriginalIndex(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    holderUnderTest.addNewInProgressItem("do shopping 3");
    TodoItem item = holderUnderTest.getItemByDesc("do shopping 2");
    holderUnderTest.markItemDone(item);
    holderUnderTest.markItemInProgress(item);

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item));
  }

  @Test
  public void when_changingAllItemsStateToInProgress_then_itemsShouldReturnToTheirOriginalIndexes(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    TodoItem item1 = holderUnderTest.getItemByDesc("do shopping 1");
    TodoItem item2 = holderUnderTest.getItemByDesc("do shopping 2");
    holderUnderTest.markItemDone(item1);
    holderUnderTest.markItemDone(item2);
    holderUnderTest.markItemInProgress(item1);
    holderUnderTest.markItemInProgress(item2);

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().indexOf(item2));
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item1));
  }

  public void when_deletingTodoItem_then_itemShouldBeDeletedSuccessfully(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    TodoItem item = holderUnderTest.getItemByDesc("do shopping");
    holderUnderTest.deleteItem(item);

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_addingTodoItemAndMarkItAsDone_then_itemShouldBeAfterItemInProgress(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    TodoItem item = holderUnderTest.getItemByDesc("do shopping 2");
    holderUnderTest.markItemDone(item);

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item));
  }

  @Test
  public void checkSequenceOfActions1(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    holderUnderTest.addNewInProgressItem("do shopping 3");
    TodoItem item1 = holderUnderTest.getItemByDesc("do shopping 1");
    TodoItem item2 = holderUnderTest.getItemByDesc("do shopping 2");
    TodoItem item3 = holderUnderTest.getItemByDesc("do shopping 3");
    holderUnderTest.markItemDone(item2);
    holderUnderTest.deleteItem(item1);

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().indexOf(item3));
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item2));
  }

  @Test
  public void checkSequenceOfActions2(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    holderUnderTest.addNewInProgressItem("do shopping 3");
    TodoItem item1 = holderUnderTest.getItemByDesc("do shopping 1");
    TodoItem item2 = holderUnderTest.getItemByDesc("do shopping 2");
    TodoItem item3 = holderUnderTest.getItemByDesc("do shopping 3");
    holderUnderTest.markItemDone(item2);
    holderUnderTest.markItemDone(item1);
    holderUnderTest.markItemDone(item3);
    holderUnderTest.markItemInProgress(item1);
    holderUnderTest.addNewInProgressItem("do shopping 4");
    TodoItem item4 = holderUnderTest.getItemByDesc("do shopping 4");

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().indexOf(item4));
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item1));
  }

  @Test
  public void checkSequenceOfActions3(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    TodoItem item1 = holderUnderTest.getItemByDesc("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    holderUnderTest.addNewInProgressItem("do shopping 3");
    TodoItem item2 = holderUnderTest.getItemByDesc("do shopping 2");
    TodoItem item3 = holderUnderTest.getItemByDesc("do shopping 3");
    holderUnderTest.markItemDone(item3);
    holderUnderTest.deleteItem(item2);
    holderUnderTest.markItemInProgress(item1);

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().indexOf(item1));
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item3));
  }

  @Test
  public void checkSequenceOfActions4(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    holderUnderTest.addNewInProgressItem("do shopping 3");
    TodoItem item1 = holderUnderTest.getItemByDesc("do shopping 1");
    TodoItem item2 = holderUnderTest.getItemByDesc("do shopping 2");
    TodoItem item3 = holderUnderTest.getItemByDesc("do shopping 3");
    holderUnderTest.deleteItem(item3);
    holderUnderTest.deleteItem(item1);
    holderUnderTest.deleteItem(item2);
    holderUnderTest.addNewInProgressItem("do shopping 1");
    holderUnderTest.addNewInProgressItem("do shopping 2");
    item1 = holderUnderTest.getItemByDesc("do shopping 1");
    item2 = holderUnderTest.getItemByDesc("do shopping 2");
    holderUnderTest.markItemDone(item2);

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().indexOf(item1));
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().indexOf(item2));
  }
}