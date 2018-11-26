package no.hiof.andrekar.badhabits;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import no.hiof.andrekar.badhabits.MyAdapter;

public class rec_SwipeDelete extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
    private ColorDrawable background = new ColorDrawable();

    public rec_SwipeDelete(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((MyAdapter.ViewHolder) viewHolder).itemView;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MyAdapter.ViewHolder) viewHolder).itemView;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MyAdapter.ViewHolder) viewHolder).itemView;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MyAdapter.ViewHolder) viewHolder).itemView;
        int itemHeight = foregroundView.getBottom() - foregroundView.getTop();
        boolean isCancelled = dX == 0f && !isCurrentlyActive;

        //Based on kotlin example from https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6

        if (isCancelled) {
            //clearCanvas(c, foregroundView.right + dX, foregroundView.top.toFloat(), foregroundView.right.toFloat(), foregroundView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        //Set delete icon
        Drawable deleteIcon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_delete);
        int iconWidth = deleteIcon.getIntrinsicWidth();
        int iconHeight = deleteIcon.getIntrinsicHeight();

        //Set margins and placement for delete icon
        int deleteIconTop = foregroundView.getTop() + (itemHeight - iconHeight) / 2;
        int deleteIconMargin = (itemHeight - iconHeight) / 2;
        int deleteIconLeft = (foregroundView.getRight() - deleteIconMargin - iconWidth);
        int deleteIconRight = foregroundView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + iconHeight;

        //Draw background and icon
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
        background.setColor(Color.parseColor("#f44336"));
        background.setBounds(foregroundView.getRight() +(int) dX, foregroundView.getTop(), foregroundView.getRight(), foregroundView.getBottom() );
        background.draw(c);

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteIcon.setTint(Color.parseColor("#ffffff"));
        deleteIcon.draw(c);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, new Paint());
    }
}