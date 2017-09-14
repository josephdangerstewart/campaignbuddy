package com.campaignbuddy.state.meta;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.main.Prefs;

import java.util.ArrayList;

/**
 * Created by josephstewart on 7/2/17.
 */
public class GameStateManager {

    private ArrayList<State> states;
    private int curState;
    private Prefs prefs;
    private AppPool appPool;

    public GameStateManager(Prefs prefs, AppPool pool) {
        states = new ArrayList<State>();
        curState = 0;
        this.prefs = prefs;
        this.appPool = pool;
    }

    public void addState(State state) {
        states.add(state);
    }

    public void setState(int state) {
        if (state >= 0 && state < states.size()) {
            curState = state;
            states.get(curState).init();
        }
    }

    public AppPool getPool() {
        return appPool;
    }

    public void init() {
        states.get(curState).init();
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public void draw(SpriteBatch batch) {
        states.get(curState).draw(batch);
    }

    public void update(int x, int y) {
        states.get(curState).update(x,y);
    }

    public void clickDown(int x, int y, int button) {
        states.get(curState).touchDown(x,y,button);
    }

    public void clickUp(int x, int y, int button) {
        states.get(curState).touchUp(x,y,button);
    }

    public void clickDragged(int x, int y) {
        states.get(curState).touchDragged(x,y);
    }

    public void keyDown(int keyCode) {
        states.get(curState).keyDown(keyCode);
    }

    public void keyUp(int keyCode) {
        states.get(curState).keyUp(keyCode);
    }

    public void keyTyped(char key) {
        states.get(curState).keyTyped(key);
    }

    public void mouseMoved(int x, int y) {
        states.get(curState).mouseMoved(x,y);
    }

    public void scrolled(int amount) {
        states.get(curState).mouseScrolled(amount);
    }

    public void dispose() {
        for (int i = 0; i < states.size(); i ++) {
            states.get(i).dispose();
        }
    }

}
