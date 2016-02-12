package com.samuel.simplepong.game.systems;

import android.view.MotionEvent;

import com.samuel.simplepong.framework.core.Point;
import com.samuel.simplepong.framework.messaging.MessageCenter;

import java.util.HashMap;

/**
 * Created by Samuel on 2/10/2016.
 */
public class TouchSystem {
    private final HashMap<Integer, Point> pointers;

    public TouchSystem() {
        pointers = new HashMap<>();
    }

    public void onTouchEvent(MotionEvent event, MessageCenter messageCenter) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int newPointerIndex = event.getActionIndex();
                int newPointerID = event.getPointerId(newPointerIndex);
                float newX = event.getX(newPointerIndex);
                float newY = event.getY(newPointerIndex);
                pointers.put(newPointerID, new Point(newX, newY));
                messageCenter.broadcast("Touch Down", newPointerID, pointers.get(newPointerID));
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerIndex = event.getActionIndex();
                int pointerID = event.getPointerId(pointerIndex);
                messageCenter.broadcast("Touch Up", pointerID, pointers.get(pointerID));
                pointers.remove(pointerID);
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                for (Integer pointerID : pointers.keySet()) {
                    int pointerIndex = event.findPointerIndex(pointerID);
                    float newX = event.getX(pointerIndex);
                    float newY = event.getY(pointerIndex);
                    pointers.get(pointerID).x = newX;
                    pointers.get(pointerID).y = newY;
                    messageCenter.broadcast("Touch Moved", pointerID, pointers.get(pointerID));
                }
            }
        }
    }

    public boolean isDown(int pointerID) {
        return pointers.containsKey(pointerID);
    }

    public Point getLocation(int pointerID) {
        if (isDown(pointerID)) {
            return pointers.get(pointerID);
        } else {
            return new Point(-1.0f, -1.0f);
        }
    }
}
