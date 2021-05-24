package exercise.android.reemh.todo_items;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.viewHolder> {

    Context context;
    TodoItemsHolderImpl todoItems;

    public TodoItemsAdapter(Context c, TodoItemsHolderImpl items) {
        context = c;
        todoItems = items;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_todo_item, parent, false);
        return new viewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TodoItem item = todoItems.getCurrentItems().get(position);
        holder.checkBox.setText(item.itemText);
        holder.checkBox.setChecked(item.state == State.DONE);

        if (item.state == State.DONE) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.state == State.IN_PROGRESS) {
                    item.state = State.DONE;
                    todoItems.markItemDone(item);
                }
                else {
                    item.state = State.IN_PROGRESS;
                    todoItems.markItemInProgress(item);
                }
                notifyDataSetChanged();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoItems.deleteItem(item);
                notifyDataSetChanged();
            }
        });

    }

    /**
     * Return the size of the dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return todoItems.getCurrentItems().size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        Button deleteButton;
        boolean isDone;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todoItemCheckBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

//        @Override
//        public void setChecked(boolean checked) {
//            if (v instanceof ImageView){
//                mListener.onTomato((ImageView)v);
//            } else {
//                mListener.onPotato(v);
//            }
//        }
    }
}
