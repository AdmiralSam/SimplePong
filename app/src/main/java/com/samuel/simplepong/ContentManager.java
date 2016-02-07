package com.samuel.simplepong;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Samuel on 2/7/2016.
 */
public class ContentManager implements Disposable {
    private HashMap<String, Disposable> content;
    private Context context;

    public ContentManager(Context context) {
        this.context = context;
        content = new HashMap<>();
    }

    public ShaderProgram loadShader(String filename) {
        if (content.containsKey(filename) && content.get(filename) instanceof ShaderProgram) {
            return (ShaderProgram) content.get(filename);
        } else {
            String vertexShaderSource = getFileAsString(filename + ".vsf");
            String fragmentShaderSource = getFileAsString(filename + ".fsf");
            if (vertexShaderSource.length() != 0 && fragmentShaderSource.length() != 0) {
                return new ShaderProgram(vertexShaderSource, fragmentShaderSource);
            } else {
                if (vertexShaderSource.length() == 0) {
                    Log.e("Content", "Vertex shader file missing");
                }
                if (fragmentShaderSource.length() == 0) {
                    Log.e("Content", "Fragment shader file missing");
                }
                return null;
            }
        }
    }

    public void dispose() {
        for (Disposable disposable : content.values()) {
            disposable.dispose();
        }
    }

    private String getFileAsString(String filename) {
        BufferedReader fileReader = null;
        StringBuilder fileStringBuilder = new StringBuilder();
        try {
            fileReader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            String line = fileReader.readLine();
            while (line != null) {
                fileStringBuilder.append(line + "\n");
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            Log.e("Content", "File \"" + filename + "\" does not exist");
            return "";
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    Log.e("Content", "Failed to close buffered reader");
                    return "";
                }
            }
            return fileStringBuilder.toString();
        }
    }
}