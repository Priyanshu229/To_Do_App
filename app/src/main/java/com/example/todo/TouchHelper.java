package com.example.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.ToDOAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDOAdapter adapter;

    public TouchHelper(ToDOAdapter toDOAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = toDOAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setMessage("Are You Sure?").setTitle("Delete Task?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteTask(position);

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);

                }
            });

            builder.show();
        }else if(direction == ItemTouchHelper.LEFT){
            adapter.editTask(position);
        }




    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(R.drawable.ic_delete)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeLeftActionIcon(R.drawable.ic_edit)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext() , R.color.lightOrange))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
