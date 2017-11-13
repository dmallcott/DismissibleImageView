package com.dmallcott.dismissibleimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class DismissibleImageView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private String finalUrl;
    private boolean blurOnLoading = true; // Defaults to true

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

    public void setFinalUrl(@NonNull final String url) {
        this.finalUrl = url;
    }

    public void blurOnLoading(final boolean blurOnLoading) {
        this.blurOnLoading = blurOnLoading;
    }

    /**
     * @deprecated Please override {@link #onClick(View)}} instead, otherwise you lose the full screen functionality.
     */
    @Deprecated
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        // TODO : Idk what I was thinking. I'll chane the fragment to an activity.
        if (getContext() instanceof AppCompatActivity) {

            FullScreenImageFragment fragment;

            if (TextUtils.isEmpty(finalUrl)) {
                fragment = new FullScreenImageFragment.Builder(getDrawingCache(true))
                        .withLoadingBlur(blurOnLoading).build();
            } else {
                fragment = new FullScreenImageFragment.Builder(finalUrl)
                        .withLoadingBitmap(getDrawingCache(true))
                        .withLoadingBlur(blurOnLoading).build();
            }

            fragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager());
        }
    }
}
