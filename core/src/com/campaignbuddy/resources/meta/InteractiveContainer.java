package com.campaignbuddy.resources.meta;

import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 8/21/17.
 */
public abstract class InteractiveContainer extends InteractiveDrawable {

    public abstract boolean clickDown(int x, int y, int button);
    public abstract boolean clickUp(int x, int y, int button);
    public abstract boolean dragged(int x, int y);
    public abstract boolean keyDown(int keyCode);
    public abstract boolean keyUp(int keyCode);
    public abstract boolean keyTyped(char key);
    public abstract boolean scrolled(int amount);

}
