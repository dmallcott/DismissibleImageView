package com.dmallcott.dismissibleimageview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.dmallcott.dismissibleimageview.blur_transformation.BlurTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class FullScreenImageFragment extends DialogFragment {

    private ImageView imageView;

    private View topBorderView;
    private View bottomBorderView;
    private View leftBorderView;
    private View rightBorderView;

    private static final String ARGUMENT_URL = "ARGUMENT_URL";
    private static final String ARGUMENT_LOADING_URL = "ARGUMENT_LOADING_URL";
    private static final String ARGUMENT_BITMAP = "ARGUMENT_BITMAP";
    private static final String ARGUMENT_LOADING_BITMAP = "ARGUMENT_LOADING_BITMAP";
    private static final String ARGUMENT_LOADING_BLUR = "ARGUMENT_LOADING_BLUR";

    // TODO : Handle rotation

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        final LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = layoutInflater.inflate(R.layout.fragment_full_screen_image, null);

        try {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        } catch (NullPointerException e) {
            // Do nothing
        }

        initialiseViews(view);

        handleArguments((savedInstanceState != null ) ? savedInstanceState : getArguments());

        dialog.setContentView(view);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        if (getDialog() != null) {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            outState.putParcelable(
                    ARGUMENT_BITMAP, Bitmap.createBitmap(((BitmapDrawable) drawable).getBitmap())
            );
        }
    }

    public void show(@NonNull final FragmentManager fragmentManager) {
        show(fragmentManager, "FullScreenImageFragment");
    }

    private void initialiseViews(@NonNull final View view) {
        imageView = (ImageView) view.findViewById(R.id.fragment_full_screen_imageView);
        topBorderView = view.findViewById(R.id.fragment_full_screen_top_border);
        bottomBorderView = view.findViewById(R.id.fragment_full_screen_bottom_border);
        leftBorderView = view.findViewById(R.id.fragment_full_screen_left_border);
        rightBorderView = view.findViewById(R.id.fragment_full_screen_right_border);

        // TODO - I know, this is really ugly. Keep in mind, it's an MVP.

        leftBorderView.setOnDragListener(new DismissibleOnDragListener(new DismissibleOnDragListener.OnDropListener() {
            @Override
            void onDragStarted() {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrop() {
                dismiss();
            }

            @Override
            void onDragEnded() {
                imageView.setVisibility(View.VISIBLE);
                view.setAlpha(1f);
            }

            @Override
            void onDragLocation(float x, float y) {
                view.setAlpha(x / leftBorderView.getWidth());
            }
        }));

        rightBorderView.setOnDragListener(new DismissibleOnDragListener(new DismissibleOnDragListener.OnDropListener() {
            @Override
            void onDragStarted() {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrop() {
                dismiss();
            }

            @Override
            void onDragEnded() {
                imageView.setVisibility(View.VISIBLE);
                view.setAlpha(1f);
            }

            @Override
            void onDragLocation(float x, float y) {
                view.setAlpha(1f - x / rightBorderView.getWidth());
            }
        }));

        topBorderView.setOnDragListener(new DismissibleOnDragListener(new DismissibleOnDragListener.OnDropListener() {
            @Override
            void onDragStarted() {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrop() {
                dismiss();
            }

            @Override
            void onDragEnded() {
                imageView.setVisibility(View.VISIBLE);
                view.setAlpha(1f);
            }

            @Override
            void onDragLocation(float x, float y) {
                view.setAlpha(y / topBorderView.getHeight());
            }
        }));

        bottomBorderView.setOnDragListener(new DismissibleOnDragListener(new DismissibleOnDragListener.OnDropListener() {
            @Override
            void onDragStarted() {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrop() {
                dismiss();
            }

            @Override
            void onDragEnded() {
                imageView.setVisibility(View.VISIBLE);
                view.setAlpha(1f);
            }

            @Override
            void onDragLocation(float x, float y) {
                view.setAlpha(1f - y / topBorderView.getHeight());
            }
        }));

        imageView.setAdjustViewBounds(true);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final Point offset = new Point((int) event.getX(), (int) event.getY());

                        final View.DragShadowBuilder shadowBuilder = new DismissibleDragShadowBuilder(imageView, offset);
                        imageView.startDrag(null, shadowBuilder, imageView, 0);
                        return true;
                }

                return false;
            }
        });
    }

    private void handleArguments(@NonNull final Bundle bundle) {
        final Bitmap bitmap = bundle.getParcelable(ARGUMENT_BITMAP);
        final Bitmap loadingBitmap = bundle.getParcelable(ARGUMENT_LOADING_BITMAP);
        final String url = bundle.getString(ARGUMENT_URL);
        final String loadingUrl = bundle.getString(ARGUMENT_LOADING_URL);
        final boolean loadingBlur = bundle.getBoolean(ARGUMENT_LOADING_BLUR);

        if (bitmap != null) {
            // If you have the final bitmap what's the point of loading behaviour?
            loadBitmap(bitmap);
        } else if (!TextUtils.isEmpty(url)) {
            if (loadingBitmap != null) {
                loadLoadingBitmap(loadingBitmap, url, loadingBlur);
            } else if (!TextUtils.isEmpty(loadingUrl)) {
                loadLoadingUrl(url, loadingUrl, loadingBlur);
            }
        }
    }

    private void loadLoadingBitmap(@NonNull final Bitmap bitmap, @NonNull final String url, final boolean loadingBlur) {
        if (loadingBlur) {
            final BlurTransformation transformation = new BlurTransformation(getContext());
            final Bitmap blurredBitmap = transformation.transform(bitmap);

            loadUrl(url, blurredBitmap);
        } else {
            loadBitmap(bitmap);
        }
    }

    private void loadLoadingUrl(@NonNull final String url, @NonNull final String loadingUrl, final boolean loadingBlur) {
        final List<Transformation> transformations = new ArrayList<>();

        if (loadingBlur) {
            transformations.add(new BlurTransformation(getContext()));
        }

        Picasso.with(getContext()).load(loadingUrl).transform(transformations).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                loadUrl(url, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void loadUrl(@NonNull final String url, @NonNull final Bitmap bitmap) {
        clearImageView();

        Picasso.with(getContext()).load(url)
                .placeholder(new BitmapDrawable(getResources(), bitmap))
                .into(imageView);
    }

    private void clearImageView() {
        imageView.setImageDrawable(null);
    }

    private void loadBitmap(@NonNull final Bitmap bitmap) {
        clearImageView();
        imageView.setImageBitmap(bitmap);
    }

    // todo document Bitmap > Url
    public static class Builder {

        private String url;
        private String loadingUrl;
        private Bitmap bitmap;
        private Bitmap loadingBitmap;
        private boolean loadingBlur = true;

        public Builder(@NonNull final String url) {
            this.url = url;
        }

        public Builder(@NonNull final Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Builder withLoadingUrl(@NonNull final String loadingUrl) {
            this.loadingUrl = loadingUrl;
            return this;
        }

        public Builder withLoadingBitmap(@NonNull final Bitmap loadingBitmap) {
            this.loadingBitmap = loadingBitmap;
            return this;
        }

        public Builder withLoadingBlur(final boolean loadingBlur) {
            this.loadingBlur = loadingBlur;
            return this;
        }

        public FullScreenImageFragment build() {
            final FullScreenImageFragment fragment = new FullScreenImageFragment();
            final Bundle bundle = new Bundle();
            if (this.url != null) {
                bundle.putString(ARGUMENT_URL, this.url);
            }
            if (this.loadingUrl!= null) {
                bundle.putString(ARGUMENT_LOADING_URL, this.loadingUrl);
            }
            if (this.bitmap != null) {
                bundle.putParcelable(ARGUMENT_BITMAP, this.bitmap);
            }
            if (this.loadingBitmap != null) {
                bundle.putParcelable(ARGUMENT_LOADING_BITMAP, this.loadingBitmap);
            }

            bundle.putBoolean(ARGUMENT_LOADING_BLUR, loadingBlur);

            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
