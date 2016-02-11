package com.samuel.simplepong.game.screens;

import android.util.Log;

import com.samuel.simplepong.framework.core.ContentManager;
import com.samuel.simplepong.framework.core.Screen;
import com.samuel.simplepong.framework.graphics.SpriteBatch;

/**
 * Created by SamuelDong on 2/10/16.
 */
public class MenuScreen extends Screen{
    public MenuScreen(ContentManager content) {
        super(content);
    }

    @Override
    public void loadContent() {

    }

    @Override
    public void unloadContent() {
        super.unloadContent();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {

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
