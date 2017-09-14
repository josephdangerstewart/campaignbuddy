package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 7/3/17.
 */
public class Image extends InteractiveDrawable {

    private Sprite img;

    public Image(Sprite sprite) {
        this.img = sprite;
    }

    public Image(String path) {
        img = new Sprite(new Texture(path));
    }

    public Sprite getSprite() {
        return img;
    }

    public boolean contains(int x, int y) {
        return contains(img,x,y);
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
    public void setPosition(float x, float y) {
        img.setPosition(x,y);
    }

    @Override
    public int getWidth() {
        return (int) img.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) img.getHeight();
    }

    @Override
    public void setX(float x) {
        img.setPosition(x,img.getY());
    }

    @Override
    public void setY(float y) {
        img.setPosition(img.getX(),y);
    }

    @Override
    public int getX() {
        return (int)img.getX();
    }

    @Override
    public int getY() {
        return (int)img.getY();
    }

    @Override
    public void dispose() {
        img.getTexture().dispose();
    }
}
