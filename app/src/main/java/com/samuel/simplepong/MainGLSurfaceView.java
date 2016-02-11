package com.samuel.simplepong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Samuel on 2/2/2016.
 */
public class MainGLSurfaceView extends GLSurfaceView {
    private final MainGLRenderer renderer;

    public MainGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new MainGLRenderer(context);
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        renderer.onTouchEvent(event);
        return true;
    }
}
