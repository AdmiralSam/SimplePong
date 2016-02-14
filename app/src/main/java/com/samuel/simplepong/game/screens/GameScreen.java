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
 * Created by John on 2016/2/13.
 */
public class GameScreen extends Screen {
    private GameState gameState;
    private Texture gameTexture;
    private float progress;
    private float move;
    private Callback2<Integer, Point> onTouch;

    public GameScreen(ContentManager content) {
        super(content);
        progress = 0.0f;
        move=1.0f;
        onTouch = new Callback2<Integer, Point>() {
            @Override
            public void callback(Integer pointerID, Point location) {
                if (gameState == GameState.Idle && getBackButtonRectangle().contains(location)) {
                    gameState = GameState.TransitionOut;
                }
                else if(gameState == GameState.Idle && getClickButtonRectangle().contains(location)){
                    gameState = GameState.OnClick;
                }
            }
        };
    }

    @Override
    public void loadContent() {
        gameTexture = content.loadTexture("Textures/testTexture.png");
    }

    @Override
    public void unloadContent() {
        super.unloadContent();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        //spriteBatch.drawTexture(gameTexture, new Rectangle(0, 0, 1000, 500), new Rectangle(0, 0, 1920, 1080));
        spriteBatch.drawTexture(gameTexture, new Rectangle(0, 0, 300, 100), getBackButtonRectangle());
        spriteBatch.drawTexture(gameTexture, new Rectangle(700, 700, 300, 100), getClickButtonRectangle());
        spriteBatch.end();
    }

    @Override
    public void update(float deltaTime) {
        switch (gameState) {
            case TransitionIn:
                if (progress < 1.0f) {
                    progress += deltaTime * 0.5f;
                } else {
                    progress = 1.0f;
                    gameState = GameState.Idle;
                }
                break;
            case TransitionOut:
                if (progress > 0.0f) {
                    progress -= deltaTime * 0.5f;
                } else {
                    progress = 0.0f;
                    gameState = GameState.Idle;
                    move=1.0f;
                    MainGLRenderer.messageCenter.broadcast("Switch Screens", "Menu Screen");
                    //the same as:
                    //drag the code here and perform "switchScreens(it is a MainGLRender Method!)"
                    //then continue running code below
                }
                break;
            case OnClick:
                if (move > 0.0f)
                {
                    move-=0.1f;
                    gameState=GameState.Idle;
                }
                else
                {
                    move=0.0f;
                    gameState=GameState.Idle;
                }
        }
    }

    @Override
    public void start() {
        super.start();
        gameState = GameState.TransitionIn;
        messageCenter.addListener("Touch Down", onTouch);
    }

    @Override
    public void reset() {
        super.reset();
        messageCenter.removeListener(onTouch);
    }

    private Rectangle getBackButtonRectangle() {
        float x = -150.0f + 1110.0f * progress;
        float y = 648+100;
        return new Rectangle((int) x - 150, (int) y - 50, 300, 100);
    }

    private Rectangle getClickButtonRectangle() {
        float x=(1920/2-150)*move;
        float y=1080/2-50;
        return new Rectangle((int)x,(int)y, 300, 100);
    }

    private enum GameState {TransitionIn, Idle, TransitionOut,OnClick}
}