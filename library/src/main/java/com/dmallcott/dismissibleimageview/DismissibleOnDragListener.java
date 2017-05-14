package com.dmallcott.dismissibleimageview;

import android.support.annotation.NonNull;
import android.view.DragEvent;
import android.view.View;

class DismissibleOnDragListener implements View.OnDragListener {

    abstract static class OnDropListener {
        abstract void onDragStarted();
        abstract void onDrop();
        abstract void onDragEnded();
        abstract void onDragLocation(float x, float y);
    }

    @NonNull
    private final OnDropListener onDropListener;

    DismissibleOnDragListener(@NonNull final OnDropListener onDropListener) {
        this.onDropListener = onDropListener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                onDropListener.onDragStarted();
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                onDropListener.onDragLocation(event.getX(), event.getY());
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                return true;

            case DragEvent.ACTION_DROP:
                onDropListener.onDrop();
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                onDropListener.onDragEnded();
                return true;

            default:
                break;
        }

        return false;
    }
}
