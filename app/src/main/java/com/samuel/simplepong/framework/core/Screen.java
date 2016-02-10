package com.samuel.simplepong.framework.core;

import com.samuel.simplepong.framework.messaging.MessageCenter;

/**
 * Created by SamuelDong on 2/10/16.
 */
public abstract class Screen {
    public final MessageCenter messageCenter;
    protected final ContentManager content;
    protected ScreenState screenState;

    protected Screen(ContentManager content) {
        this.content = content;
        messageCenter = new MessageCenter();
        screenState = ScreenState.Inactive;
    }

    public abstract void loadContent();

    public abstract void unloadContent();

    public abstract void draw();

    public abstract void update(float deltaTime);

    public abstract void start();

    public abstract void reset();

    protected enum ScreenState {Inactive, Loading, Active, Unloading}
}
