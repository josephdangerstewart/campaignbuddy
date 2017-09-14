package com.campaignbuddy.state.meta;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.resources.components.Background;
import com.campaignbuddy.resources.containers.InteractableList;

/**
 * Created by josephstewart on 7/2/17.
 */
public abstract class State {

    protected InteractableList list;
    protected Background background;
    public static GameStateManager gsm;
    public static OrthographicCamera camera;
    public static Viewport viewport;

    public State(String backgroundName) throws IllegalArgumentException {
        list = new InteractableList();
        background = new Background(backgroundName);
        list.add(background);
    }

    public void draw(SpriteBatch batch) {
        list.draw(batch);
    }

    public void update(int x, int y) {
        list.update(x,y);
    }

    public void init() {}

    public  void keyDown(int keycode) {list.keyDown(keycode);}

    public void keyUp(int keycode) {list.keyUp(keycode);}
    public void keyTyped(char key) {list.keyTyped(key);}
    public void touchDown(int x, int y, int button) {list.clickDown(x,y,button);}
    public void touchUp(int x, int y, int button) {list.clickUp(x,y,button);}
    public void touchDragged(int x, int y) {list.dragged(x,y);}
    public void mouseMoved(int x, int y) {}
    public void mouseScrolled(int ammount) {list.scrolled(ammount);}

    public void dispose() {
        list.dispose();
    }

}
