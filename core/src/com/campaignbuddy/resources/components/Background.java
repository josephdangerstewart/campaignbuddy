package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 7/2/17.
 */
public class Background extends InteractiveDrawable {

    private Sprite img;

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setX(float x) {

    }

    @Override
    public void setY(float y) {

    }

    public Background(String name) {
        img = new Sprite(new Texture(name));
        img.setX(0);
        img.setY(0);
    }

    @Override
    public void draw(SpriteBatch batch) {
        img.draw(batch);
    }

    @Override
    public boolean update(int x, int y) {
        return false;
    }

    @Override
    public void cancelUpdate() {}

    @Override
    public boolean clickUp(int x, int y, int button) {
        return false;
    }

    @Override
    public boolean keyUp(int keyCode) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        img.getTexture().dispose();
    }


}
