package com.dmallcott.dismissibleimageview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

public class DismissibleImageView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private static final String FRAGMENT_TAG = "FS_TAG";

    public DismissibleImageView(Context context) {
        this(context, null);
    }

    public DismissibleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DismissibleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnClickListener(this);
        setDrawingCacheEnabled(true); // todo remember to clear it on set image
        setAdjustViewBounds(true);
    }


    @Override
    public void onClick(View v) {
        // TODO : find a workaround to this
        if (getContext() instanceof AppCompatActivity) {
            final FullScreenImageFragment fragment = FullScreenImageFragment.newInstance(getDrawingCache());
            fragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), FRAGMENT_TAG);
        }

    }
}
