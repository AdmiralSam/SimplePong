package com.samuel.simplepong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Samuel on 2/2/2016.
 */
public class MainGLRenderer implements GLSurfaceView.Renderer {
    //Test
    int basicShaderID;
    int[] positionBufferID;
    int[] colorBufferID;
    private Context context;
    //Test

    public MainGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.5f, 1.0f, 1.0f, 1.0f);
        basicShaderID = loadShaderProgram("Shaders/BasicShader");
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
        glEnableVertexAttribArray(glGetAttribLocation(basicShaderID, "position"));
        glEnableVertexAttribArray(glGetAttribLocation(basicShaderID, "color"));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(basicShaderID);
        glBindBuffer(GL_ARRAY_BUFFER, positionBufferID[0]);
        glVertexAttribPointer(glGetAttribLocation(basicShaderID, "position"), 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferID[0]);
        glVertexAttribPointer(glGetAttribLocation(basicShaderID, "color"), 4, GL_FLOAT, false, 0, 0);
        float[] testMatrix = new float[16];
        Matrix.setIdentityM(testMatrix, 0);
        Matrix.rotateM(testMatrix, 0, 90, 0.0f, 0.0f, 1.0f);
        glUniformMatrix4fv(glGetUniformLocation(basicShaderID, "matrix"), 1, false, testMatrix, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    //Test
    private int loadShader(String filename, int shaderType) {
        int shader = glCreateShader(shaderType);
        StringBuilder shaderSourceBuilder = new StringBuilder();
        BufferedReader shaderReader = null;
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<String> uniforms = new ArrayList<>();
        try {
            shaderReader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename + (shaderType == GL_VERTEX_SHADER ? ".vsf" : ".fsf"))));
            String line = shaderReader.readLine();
            while (line != null) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("attribute")) {
                    String name = tokens[tokens.length - 1];
                    attributes.add(name.substring(0, name.length() - 1));
                }
                if (tokens[0].equals("uniform")) {
                    String name = tokens[tokens.length - 1];
                    uniforms.add(name.substring(0, name.length() - 1));
                }
                shaderSourceBuilder.append(line + "\n");
                line = shaderReader.readLine();
            }
        } catch (IOException e) {
            return -1;
        } finally {
            if (shaderReader != null) {
                try {
                    shaderReader.close();
                } catch (IOException e) {
                    return -1;
                }
            }
        }
        glShaderSource(shader, shaderSourceBuilder.toString());
        glCompileShader(shader);
        for(String attribute : attributes) {
            Log.i("Attribute", attribute);
        }
        for(String uniform : uniforms) {
            Log.i("Uniform", uniform);
        }
        return shader;
    }

    private int loadShaderProgram(String filename) {
        int shaderProgramID = glCreateProgram();
        int vertexShader = loadShader(filename, GL_VERTEX_SHADER);
        int fragmentShader = loadShader(filename, GL_FRAGMENT_SHADER);
        glAttachShader(shaderProgramID, vertexShader);
        glAttachShader(shaderProgramID, fragmentShader);
        glLinkProgram(shaderProgramID);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        return shaderProgramID;
    }
    //Test
}