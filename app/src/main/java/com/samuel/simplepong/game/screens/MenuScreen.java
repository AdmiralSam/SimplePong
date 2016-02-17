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
    private Callback2<Integer, Point> onTouch;

    public MenuScreen(ContentManager content) {
        super(content);
        progress = 0.0f;
        onTouch = new Callback2<Integer, Point>() {
            @Override
            public void callback(Integer pointerID, Point location) {
                if (menuState == MenuState.Idle && getSingleButtonRectangle().contains(location)
                        ) {
                    menuState = MenuState.OnClick;
                } else if (menuState == MenuState.Idle && getQuickButtonRectangle().contains(location)
                        ) {
                    menuState = MenuState.OnClick;
                } else if (menuState == MenuState.Idle && getInviteButtonRectangle().contains(location)
                        ) {
                    menuState = MenuState.OnClick;
                }
            }
        };
    }

    @Override
    public void loadContent() {
        menuTexture = content.loadTexture("Textures/testTexture.png");
    }

    @Override
    public void unloadContent() {
        super.unloadContent();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        //spriteBatch.drawTexture(menuTexture, new Rectangle(0, 0, 1000, 500), new Rectangle(0, 0, 1920, 1080));
        spriteBatch.drawTexture(menuTexture, new Rectangle(0, 0, 300, 100), getSingleButtonRectangle());
        spriteBatch.drawTexture(menuTexture, new Rectangle(512, 0, 300, 100), getQuickButtonRectangle());
        spriteBatch.drawTexture(menuTexture, new Rectangle(0, 512, 300, 100), getInviteButtonRectangle());
        //spriteBatch.drawTexture(menuTexture, new Rectangle(512, 512, 300, 100), getButtonRectangle());

        spriteBatch.end();
    }

    @Override
    public void update(float deltaTime) {
        switch (menuState) {
//            case TransitionIn:
//                if (progress < 1.0f) {
//                    progress += deltaTime * 0.5f;
//                } else {
//                    progress = 1.0f;
//                    menuState = MenuState.Idle;
//                }
//                break;
//            case TransitionOut:
//                if (progress > 0.0f) {
//                    progress -= deltaTime * 0.5f;
//                } else {
//                    progress = 0.0f;
//                    menuState = MenuState.Idle;
//                    MainGLRenderer.messageCenter.broadcast("Switch Screens", "Game Screen");
//                    //the same as:
//                    //drag the code here and perform "switchScreens(it is a MainGLRender Method!)"
//                    //then continue running code below
//                }
//                break;
            case OnClick:
                //MainGLRenderer.messageCenter.broadcast("Switch Screens", "Game Screen");

                MainGLRenderer.messageCenter.broadcast("Sign In");
                menuState = MenuState.Idle;
                break;
        }
    }

    @Override
    public void start() {
        super.start();
        menuState = MenuState.Idle;
        messageCenter.addListener("Touch Down", onTouch);
    }

    @Override
    public void reset() {
        super.reset();
        messageCenter.removeListener(onTouch);
    }

    private Rectangle getSingleButtonRectangle() {
        return new Rectangle(1920 / 2 - 150, 1080 / 2 - 50 - 200, 300, 100);
    }

    private Rectangle getQuickButtonRectangle() {
        return new Rectangle(1920 / 2 - 150, 1080 / 2 - 50, 300, 100);
    }

    private Rectangle getInviteButtonRectangle() {
        return new Rectangle(1920 / 2 - 150, 1080 / 2 - 50 + 200, 300, 100);
    }

    //private enum MenuState {TransitionIn, Idle, TransitionOut}
    private enum MenuState {
        TransitionIn, Idle, OnClick
    }
}
