package com.samuel.simplepong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.samuel.simplepong.framework.core.Screen;
import com.samuel.simplepong.framework.messaging.Callback0;
import com.samuel.simplepong.framework.messaging.Callback1;
import com.samuel.simplepong.framework.core.ContentManager;
import com.samuel.simplepong.framework.messaging.MessageCenter;
import com.samuel.simplepong.framework.core.Rectangle;
import com.samuel.simplepong.framework.graphics.ShaderProgram;
import com.samuel.simplepong.framework.graphics.SpriteBatch;
import com.samuel.simplepong.framework.graphics.Texture;
import com.samuel.simplepong.game.screens.MenuScreen;

import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Samuel on 2/2/2016.
 */
public class MainGLRenderer implements GLSurfaceView.Renderer {
    public static MessageCenter messageCenter;
    private final ContentManager contentManager;
    private final SpriteBatch spriteBatch;
    private final ShaderProgram defaultShader;
    private final Context context;
    private Screen currentScreen;
    private final HashMap<String, Screen> screens;
    private long lastTime;

    public MainGLRenderer(Context context) {
        this.context = context;
        contentManager = new ContentManager(context);
        defaultShader = contentManager.loadShader("Shaders/2DShader");
        spriteBatch = new SpriteBatch(1920, 1080, defaultShader);
        screens = new HashMap<>();
        messageCenter = new MessageCenter();
        messageCenter.addListener("Switch Screens", new Callback1<String>(){
            @Override
            public void callback(String parameter1) {
                switchScreens(parameter1);
            }
        });
        initializeScreens();
        currentScreen = screens.get("Menu Screen");
        currentScreen.loadContent();
        currentScreen.start();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.5f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        lastTime = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (currentScreen != null) {
            currentScreen.draw(spriteBatch);
            currentScreen.update(getElapsedTime());
        }
    }

    private float getElapsedTime() {
        float elapsedTime = (float)((double)(System.nanoTime() - lastTime) / 1000000000.0);
        lastTime = System.nanoTime();
        return elapsedTime;
    }

    private void initializeScreens() {
        screens.put("Menu Screen", new MenuScreen(new ContentManager(context)));
    }

    private void switchScreens(String screenName) {
        if (screens.containsKey(screenName)) {
            Screen oldScreen = currentScreen;
            Screen newScreen = screens.get(screenName);
            newScreen.loadContent();
            newScreen.start();
            currentScreen = newScreen;
            oldScreen.unloadContent();
            oldScreen.reset();
        }
        else {
            Log.e("Main", "Screen " + screenName + " does not exist");
        }
    }
}
