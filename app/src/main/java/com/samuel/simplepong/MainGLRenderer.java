package com.samuel.simplepong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.samuel.simplepong.framework.messaging.Callback0;
import com.samuel.simplepong.framework.messaging.Callback1;
import com.samuel.simplepong.framework.core.ContentManager;
import com.samuel.simplepong.framework.messaging.MessageCenter;
import com.samuel.simplepong.framework.core.Rectangle;
import com.samuel.simplepong.framework.graphics.ShaderProgram;
import com.samuel.simplepong.framework.graphics.SpriteBatch;
import com.samuel.simplepong.framework.graphics.Texture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Samuel on 2/2/2016.
 */
public class MainGLRenderer implements GLSurfaceView.Renderer {
    //Test
    private Context context;
    private ContentManager contentManager;
    private ShaderProgram basicShader;
    private Texture testTexture;
    private SpriteBatch spriteBatch;
    private MessageCenter testCenter;
    //Test

    public MainGLRenderer(Context context) {
        this.context = context;
        contentManager = new ContentManager(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.5f, 1.0f, 1.0f, 1.0f);
        basicShader = contentManager.loadShader("Shaders/2DShader");
        testTexture = contentManager.loadTexture("Textures/testTexture.png");
        spriteBatch = new SpriteBatch(1920, 1080);
        testCenter = new MessageCenter();
        testCenter.addListener("Test", new Callback0() {
            @Override
            public void callback() {
                Log.e("Test", "0");
            }
        });
        testCenter.addListener("Test", new Callback1<String>() {

            @Override
            public void callback(String parameter1) {
                Log.e("Test", parameter1);
            }
        });
        Callback1<Integer> ses = new Callback1<Integer>() {
            @Override
            public void callback(Integer parameter1) {
                Log.e("Test", "-" + parameter1);
            }
        };
        testCenter.addListener("Test", ses);
        testCenter.broadcast("Test");
        testCenter.broadcast("Test", "Hi");
        testCenter.broadcast("Test", 5);
        testCenter.removeListener(ses);
        testCenter.broadcast("Test", "Hi");
        testCenter.broadcast("Test", 5);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        spriteBatch.begin(basicShader);
        spriteBatch.drawTexture(testTexture, new Rectangle(0, 0, 512, 512), new Rectangle(0, 0, 100, 100));
        spriteBatch.drawTexture(testTexture, new Rectangle(512, 0, 512, 512), new Rectangle(100, 100, 100, 100));
        spriteBatch.drawTexture(testTexture, new Rectangle(512, 512, 512, 512), new Rectangle(200, 200, 100, 100));
        spriteBatch.end();
    }
}
