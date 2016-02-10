package com.samuel.simplepong;

import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_DYNAMIC_DRAW;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_INT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Samuel on 2/9/2016.
 */
public class SpriteBatch {
    private int virtualWidth;
    private int virtualHeight;
    private boolean active;
    private final int positionBufferID;
    private final int uvBufferID;
    private final int indexBufferID;
    private final ArrayList<Texture> textures;
    private final ArrayList<Rectangle> sourceRectangles;
    private final ArrayList<Rectangle> destinationRectangles;
    private final ShaderProgram spriteShader;

    public SpriteBatch(int virtualWidth, int virtualHeight, ShaderProgram spriteShader) {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        this.spriteShader = spriteShader;
        active = false;
        textures = new ArrayList<>();
        sourceRectangles = new ArrayList<>();
        destinationRectangles = new ArrayList<>();
        int[] bufferIDs = new int[3];
        glGenBuffers(3, bufferIDs, 0);
        positionBufferID = bufferIDs[0];
        uvBufferID = bufferIDs[1];
        indexBufferID = bufferIDs[2];
    }

    public void drawTexture(Texture texture, Rectangle destinationRectangle) {
        drawTexture(texture, new Rectangle(0, 0, texture.getWidth(), texture.getHeight()), destinationRectangle);
    }

    public void drawTexture(Texture texture, Rectangle sourceRectangle, Rectangle destinationRectangle) {
        textures.add(texture);
        sourceRectangles.add(sourceRectangle);
        destinationRectangles.add(destinationRectangle);
    }

    public void begin() {
        if (!active) {
            active = true;
            spriteShader.begin();
        } else {
            Log.e("SpriteBatch", "Sprite batch has already begun");
        }
    }

    public void end() {
        if (active) {
            int drawIndex = 0;
            glUniform1i(spriteShader.getUniformLocation("texture"), 0);
            while (drawIndex < textures.size()) {
                Texture currentTexture = textures.get(drawIndex);
                int batchCount = 1;
                for (int i = drawIndex + 1; i < textures.size(); i++) {
                    if (textures.get(i) == currentTexture) {
                        batchCount++;
                    } else {
                        break;
                    }
                }
                currentTexture.bindToUnit(GL_TEXTURE0);
                float[] positions = new float[16 * batchCount];
                float[] uvs = new float[8 * batchCount];
                short[] indices = new short[6 * batchCount];
                for (int i = 0; i < batchCount; i++) {
                    Rectangle destination = destinationRectangles.get(i + drawIndex);
                    Rectangle source = sourceRectangles.get(i + drawIndex);
                    positions[16 * i] = 2.0f * destination.x / virtualWidth - 1.0f;
                    positions[16 * i + 1] = 2.0f * (virtualHeight - destination.y) / virtualHeight - 1.0f;
                    positions[16 * i + 2] = 0.0f;
                    positions[16 * i + 3] = 1.0f;
                    positions[16 * i + 4] = 2.0f * destination.x / virtualWidth - 1.0f;
                    positions[16 * i + 5] = 2.0f * (virtualHeight - destination.y - destination.height) / virtualHeight - 1.0f;
                    positions[16 * i + 6] = 0.0f;
                    positions[16 * i + 7] = 1.0f;
                    positions[16 * i + 8] = 2.0f * (destination.x + destination.width) / virtualWidth - 1.0f;
                    positions[16 * i + 9] = 2.0f * (virtualHeight - destination.y - destination.height) / virtualHeight - 1.0f;
                    positions[16 * i + 10] = 0.0f;
                    positions[16 * i + 11] = 1.0f;
                    positions[16 * i + 12] = 2.0f * (destination.x + destination.width) / virtualWidth - 1.0f;
                    positions[16 * i + 13] = 2.0f * (virtualHeight - destination.y) / virtualHeight - 1.0f;
                    positions[16 * i + 14] = 0.0f;
                    positions[16 * i + 15] = 1.0f;
                    uvs[8 * i] = currentTexture.getUVX(source.x);
                    uvs[8 * i + 1] = currentTexture.getUVY(source.y);
                    uvs[8 * i + 2] = currentTexture.getUVX(source.x);
                    uvs[8 * i + 3] = currentTexture.getUVY(source.y + source.height);
                    uvs[8 * i + 4] = currentTexture.getUVX(source.x + source.width);
                    uvs[8 * i + 5] = currentTexture.getUVY(source.y + source.height);
                    uvs[8 * i + 6] = currentTexture.getUVX(source.x + source.width);
                    uvs[8 * i + 7] = currentTexture.getUVY(source.y);
                    indices[6 * i] = (short)(4 * i);
                    indices[6 * i + 1] = (short)(4 * i + 1);
                    indices[6 * i + 2] = (short)(4 * i + 2);
                    indices[6 * i + 3] = (short)(4 * i + 2);
                    indices[6 * i + 4] = (short)(4 * i + 3);
                    indices[6 * i + 5] = (short)(4 * i);
                }
                glBindBuffer(GL_ARRAY_BUFFER, positionBufferID);
                glBufferData(GL_ARRAY_BUFFER, 4 * positions.length, FloatBuffer.wrap(positions), GL_DYNAMIC_DRAW);
                glVertexAttribPointer(spriteShader.getAttributeLocation("position"), 4, GL_FLOAT, false, 0, 0);
                glBindBuffer(GL_ARRAY_BUFFER, uvBufferID);
                glBufferData(GL_ARRAY_BUFFER, 4 * uvs.length, FloatBuffer.wrap(uvs), GL_DYNAMIC_DRAW);
                glVertexAttribPointer(spriteShader.getAttributeLocation("uv"), 2, GL_FLOAT, false, 0, 0);
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, 2 * indices.length, ShortBuffer.wrap(indices), GL_DYNAMIC_DRAW);
                glDrawElements(GL_TRIANGLES, 6 * batchCount, GL_UNSIGNED_SHORT, 0);
                drawIndex += batchCount;
            }
            spriteShader.end();
            textures.clear();
            sourceRectangles.clear();
            destinationRectangles.clear();
            active = false;
        } else {
            Log.e("SpriteBatch", "Sprite batch has not begun yet");
        }
    }
}
