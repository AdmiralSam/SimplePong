package com.samuel.simplepong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
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
        spriteBatch = new SpriteBatch(1920, 1080, basicShader);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.drawTexture(testTexture, new Rectangle(0, 0, 512, 512), new Rectangle(0, 0, 100, 100));
        spriteBatch.drawTexture(testTexture, new Rectangle(512, 0, 512, 512), new Rectangle(100, 100, 100, 100));
        spriteBatch.drawTexture(testTexture, new Rectangle(512, 512, 512, 512), new Rectangle(200, 200, 100, 100));
        spriteBatch.end();
    }
}
