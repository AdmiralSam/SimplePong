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
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Samuel on 2/2/2016.
 */
public class MainGLRenderer implements GLSurfaceView.Renderer {
    //Test
    private int[] positionBufferID;
    private int[] colorBufferID;
    private Context context;
    private ContentManager contentManager;
    private ShaderProgram basicShader;
    //Test

    public MainGLRenderer(Context context) {
        this.context = context;
        contentManager = new ContentManager(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.5f, 1.0f, 1.0f, 1.0f);
        positionBufferID = new int[1];
        glGenBuffers(1, positionBufferID, 0);
        glBindBuffer(GL_ARRAY_BUFFER, positionBufferID[0]);
        FloatBuffer positionBuffer = FloatBuffer.allocate(12);
        positionBuffer.put(new float[]{0.0f, 0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.0f, 1.0f});
        positionBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER, 48, positionBuffer, GL_STATIC_DRAW);
        colorBufferID = new int[1];
        glGenBuffers(1, colorBufferID, 0);
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferID[0]);
        FloatBuffer colorBuffer = FloatBuffer.allocate(12);
        colorBuffer.put(new float[]{0.0f, 0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.0f, 1.0f});
        colorBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER, 48, colorBuffer, GL_STATIC_DRAW);
        basicShader = contentManager.loadShader("Shaders/BasicShader");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        basicShader.begin();
        glBindBuffer(GL_ARRAY_BUFFER, positionBufferID[0]);
        glVertexAttribPointer(basicShader.getAttributeLocation("position"), 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferID[0]);
        glVertexAttribPointer(basicShader.getAttributeLocation("color"), 4, GL_FLOAT, false, 0, 0);
        float[] testMatrix = new float[16];
        Matrix.setIdentityM(testMatrix, 0);
        Matrix.rotateM(testMatrix, 0, 90, 0.0f, 0.0f, 1.0f);
        glUniformMatrix4fv(basicShader.getUniformLocation("matrix"), 1, false, testMatrix, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        basicShader.end();
    }
}