package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.meta.Pallate;

/**
 * Created by josephstewart on 7/30/17.
 */
public class Button extends InteractiveDrawable {

    private Sprite image;
    private Event event;
    private boolean hovering;
    private boolean mouseDown;

    public Button(int x, int y, String path, Event event) {
        image = new Sprite(new Texture(path));
        image.setPosition(x,y);
        this.event = event;
    }

    public Button(int x, int y, Sprite sprite, Event event) {
        image = sprite;
        image.setPosition(x,y);
        this.event = event;
    }

    public Sprite getSprite() {
        return image;
    }

    @Override
    public void draw(SpriteBatch batch) {
        image.draw(batch);
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        if (contains(image,x,y)) {
            System.out.println("image.getTexture().getHeight() = " + image.getTexture().getHeight());
            System.out.println("image.getTexture().getWidth() = " + image.getTexture().getWidth());
            mouseDown = true;
            image.setColor(Color.WHITE.cpy().lerp(Pallate.DOWN, .5f));
            return true;
        }
        return false;
    }

    public void click() {
        event.onCall();
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        if (mouseDown && contains(image,x,y)) {
            mouseDown = false;
            event.onCall();
            image.setColor(Color.WHITE.cpy().lerp(Pallate.HOVER, .5f));
            return true;
        }
        mouseDown = false;
        return false;
    }

    @Override
    public boolean update(int x, int y) {
        boolean c = contains(image,x,y);
        if (c && !hovering) {
            hovering = true;
            image.setColor(Color.WHITE.cpy().lerp(Pallate.HOVER, .5f));
        } else if (!c && hovering) {
            hovering = false;
            image.setColor(Pallate.NONE);
        } else if (!c) {
            cancelUpdate();
        }
        return false;
    }

    @Override
    public void cancelUpdate() {
        hovering = false;
        image.setColor(Pallate.NONE);
    }

    public void setSize(int width, int height) {
        image.setSize(width,height);
    }

    @Override
    public int getWidth() {
        return (int)image.getWidth();
    }

    @Override
    public int getHeight() {
        return (int)image.getHeight();
    }

    @Override
    public int getX() {
        return (int) image.getX();
    }

    @Override
    public int getY() {
        return (int) image.getY();
    }

    @Override
    public void setX(float x) {
        image.setX(x);
    }

    @Override
    public void setY(float y) {
        image.setY(y);
    }

    @Override
    public void dispose() {
        image.getTexture().dispose();
    }
}
