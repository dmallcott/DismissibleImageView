package com.dmallcott.dismissibleimageview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FullScreenImageFragment extends DialogFragment {

    private static final String ARGUMENT_BITMAP = "ARGUMENT_BITMAP";
    private static final String ARGUMENT_URL = "ARGUMENT_URL";

    private Bitmap bitmap;

    private ImageView imageView;
    private View topBorderView;
    private View bottomBorderView;
    private View leftBorderView;
    private View rightBorderView;

    protected static FullScreenImageFragment newInstance(@NonNull final Bitmap bitmap) {
        final FullScreenImageFragment fragment = new FullScreenImageFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_BITMAP, bitmap);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected static FullScreenImageFragment newInstance(@NonNull final String url) {
        final FullScreenImageFragment fragment = new FullScreenImageFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

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

        if (savedInstanceState != null) {
            bitmap = savedInstanceState.getParcelable(ARGUMENT_BITMAP);
            initialiseViews(view, bitmap);
        } else if (getArguments().containsKey(ARGUMENT_BITMAP) && getArguments().getParcelable(ARGUMENT_BITMAP) != null) {
            bitmap = getArguments().getParcelable(ARGUMENT_BITMAP);
            initialiseViews(view, bitmap);
        } else if (getArguments().containsKey(ARGUMENT_URL) && getArguments().getParcelable(ARGUMENT_URL) != null) {
            Picasso.with(getContext()).load(getArguments().getString(ARGUMENT_URL)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bm, Picasso.LoadedFrom from) {
                    bitmap = bm;
                    initialiseViews(view, bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

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

        outState.putParcelable(ARGUMENT_BITMAP, Bitmap.createBitmap(bitmap));
    }

    private void initialiseViews(@NonNull final View view, @NonNull final Bitmap bitmap) {
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
        imageView.setImageBitmap(bitmap);

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
}
