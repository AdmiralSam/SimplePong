package com.samuel.simplepong.game.screens;

import android.util.Log;

import com.samuel.simplepong.framework.core.ContentManager;
import com.samuel.simplepong.framework.core.Point;
import com.samuel.simplepong.framework.core.Rectangle;
import com.samuel.simplepong.framework.core.Screen;
import com.samuel.simplepong.framework.graphics.SpriteBatch;
import com.samuel.simplepong.framework.graphics.Texture;
import com.samuel.simplepong.framework.messaging.Callback2;

/**
 * Created by SamuelDong on 2/10/16.
 */
public class MenuScreen extends Screen {
    private Texture uiTexture;
    private boolean[] used;
    private Point[] locations;
    private Rectangle[] sources;
    private int[] ids;

    public MenuScreen(ContentManager content) {
        super(content);
        used = new boolean[4];
        locations = new Point[4];
        ids = new int[4];
        for (int i = 0; i < 4; i++) {
            used[i] = false;
            locations[i] = new Point(0, 0);
        }
        sources = new Rectangle[4];
        sources[0] = new Rectangle(0, 0, 512, 512);
        sources[1] = new Rectangle(512, 0, 512, 512);
        sources[2] = new Rectangle(0, 512, 512, 512);
        sources[3] = new Rectangle(512, 512, 512, 512);
    }

    @Override
    public void loadContent() {
        uiTexture = content.loadTexture("Textures/testTexture.png");
        messageCenter.addListener("Touch Down", new Callback2<Integer, Point>() {
            @Override
            public void callback(Integer pointerID, Point location) {
                for (int i = 0; i < 4; i++) {
                    if (!used[i]) {
                        Log.e("W", "Added");
                        used[i] = true;
                        ids[i] = pointerID;
                        locations[i].x = location.x;
                        locations[i].y = location.y;
                        break;
                    }
                }
            }
        });
        messageCenter.addListener("Touch Up", new Callback2<Integer, Point>() {
            @Override
            public void callback(Integer pointerID, Point location) {
                for (int i = 0; i < 4; i++) {
                    if (used[i] && ids[i] == pointerID) {
                        Log.e("W", "Removed");
                        used[i] = false;
                        break;
                    }
                }
            }
        });
        messageCenter.addListener("Touch Moved", new Callback2<Integer, Point>() {
            @Override
            public void callback(Integer pointerID, Point location) {
                for (int i = 0; i < 4; i++) {
                    if (used[i] && ids[i] == pointerID) {
                        Log.e("W", "Moved");
                        locations[i].x = location.x;
                        locations[i].y = location.y;
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void unloadContent() {
        super.unloadContent();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        for (int i = 0; i < 4; i++) {
            if (used[i]) {
                spriteBatch.drawTexture(uiTexture, sources[i], new Rectangle((int) locations[i].x - 50, (int) locations[i].y - 50, 100, 100));
            }
        }
        spriteBatch.end();
    }

    @Override
    public void update(float deltaTime) {
        Log.d("Menu Screen", "Delta: " + deltaTime);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void reset() {
        super.reset();
    }
}
