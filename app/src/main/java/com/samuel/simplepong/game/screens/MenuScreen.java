package com.samuel.simplepong.game.screens;

import com.samuel.simplepong.MainGLRenderer;
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
    private MenuState menuState;
    private Texture menuTexture;
    private float progress;
    public MenuScreen(ContentManager content) {
        super(content);
        progress = 0.0f;
    }

    @Override
    public void loadContent() {
        menuTexture = content.loadTexture("Textures/simplePong.png");
        messageCenter.addListener("Touch Down", new Callback2<Integer, Point>() {
            @Override
            public void callback(Integer pointerID, Point location) {
                if (menuState == MenuState.Idle && getButtonRectangle().contains(location)) {
                    menuState = MenuState.TransitionOut;
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
        spriteBatch.drawTexture(menuTexture, new Rectangle(0, 0, 1000, 500), new Rectangle(0, 0, 1920, 1080));
        spriteBatch.drawTexture(menuTexture, new Rectangle(0, 500, 300, 100), getButtonRectangle());
        spriteBatch.end();
    }

    @Override
    public void update(float deltaTime) {
        switch (menuState) {
            case TransitionIn:
                if (progress < 1.0f) {
                    progress += deltaTime * 0.5f;
                } else {
                    progress = 1.0f;
                    menuState = MenuState.Idle;
                }
                break;
            case TransitionOut:
                if (progress > 0.0f) {
                    progress -= deltaTime * 0.5f;
                } else {
                    progress = 0.0f;
                    menuState = MenuState.Idle;
                    MainGLRenderer.messageCenter.broadcast("Switch Screens", "Game Screen");
                }
                break;
        }
    }

    @Override
    public void start() {
        super.start();
        menuState = MenuState.TransitionIn;
    }

    @Override
    public void reset() {
        super.reset();
    }

    private Rectangle getButtonRectangle() {
        float x = -150.0f + 1110.0f * progress;
        float y = 648;
        return new Rectangle((int) x - 150, (int) y - 50, 300, 100);
    }

    private enum MenuState {TransitionIn, Idle, TransitionOut}
}
