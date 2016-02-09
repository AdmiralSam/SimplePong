package com.samuel.simplepong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Samuel on 2/7/2016.
 */
public class ContentManager implements Disposable {
    private final HashMap<String, Disposable> content;
    private final Context context;

    public ContentManager(Context context) {
        this.context = context;
        content = new HashMap<>();
    }

    public ShaderProgram loadShader(String filename) {
        if (!content.containsKey(filename) || !(content.get(filename) instanceof ShaderProgram)) {
            String vertexShaderSource = getFileAsString(filename + ".vsf");
            String fragmentShaderSource = getFileAsString(filename + ".fsf");
            if (vertexShaderSource.length() != 0 && fragmentShaderSource.length() != 0) {
                content.put(filename, new ShaderProgram(vertexShaderSource, fragmentShaderSource));
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
        return (ShaderProgram) content.get(filename);
    }

    public Texture loadTexture(String filename) {
        if (!content.containsKey(filename) || !(content.get(filename) instanceof Texture)) {
            content.put(filename, new Texture(getBitmap(filename)));
        }
        return (Texture)content.get(filename);
    }

    public void dispose() {
        for (Disposable disposable : content.values()) {
            disposable.dispose();
        }
    }

    private Bitmap getBitmap(String filename) {
        InputStream fileStream;
        Bitmap bitmap = null;
        try {
            fileStream = context.getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(fileStream);
        } catch(IOException e) {
            Log.e("Content", "File \"" + filename + "\" does not exist");
        }
        return bitmap;
    }

    private String getFileAsString(String filename) {
        BufferedReader fileReader = null;
        StringBuilder fileStringBuilder = new StringBuilder();
        try {
            fileReader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            String line = fileReader.readLine();
            while (line != null) {
                fileStringBuilder.append(line);
                fileStringBuilder.append("\n");
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
                }
            }
        }
        return fileStringBuilder.toString();
    }
}
