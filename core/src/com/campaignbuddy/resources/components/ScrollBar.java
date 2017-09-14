package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.containers.ScrollableWindow;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 7/4/17.
 */
public class ScrollBar extends InteractiveDrawable {

    private int buffer;

    TextureAtlas atlas;

    InteractableList list;

    private Image scrollTrack;
    private Image scrollBox;

    private ScrollableWindow window;

    private int minY;
    private int maxY;
    private int y;

    @Override
    public int getWidth() {
        return scrollBox.getWidth();
    }

    @Override
    public int getHeight() {
        return scrollTrack.getHeight();
    }

    private boolean clickdown;

    public ScrollBar(String src, String name, ScrollableWindow window, int x, int y) {
        atlas = new TextureAtlas(src+name+".txt");

        scrollBox = new Image(new Sprite(atlas.findRegion(name+"-box")));
        scrollBox.setPosition(x,y);

        scrollTrack = new Image(new Sprite(atlas.findRegion(name+"-track")));
        scrollTrack.setPosition(x+scrollBox.getWidth()/2-scrollTrack.getWidth()/2,y);

        this.y = y;
        minY = y;
        maxY = (scrollTrack.getHeight()+y)-scrollBox.getHeight();
        System.out.println("MAXY: " + maxY);

        this.window = window;

        list = new InteractableList(x,y);
        list.add(scrollTrack);
        list.add(scrollBox);

        buffer = scrollBox.getWidth()/2+10;
    }

    public void setHeight(float height) {
        scrollTrack.getSprite().setSize(scrollTrack.getWidth(),height);
        maxY = (scrollTrack.getHeight()+y)-scrollBox.getHeight();
    }

    @Override
    public void setX(float x) {
        list.setX(x);
    }

    @Override
    public void setY(float y) {
        float change = y-list.getY();
        list.setY(y);
        maxY += change;
        minY += change;
    }

    @Override
    public int getX() {
        return list.getX();
    }

    @Override
    public int getY() {
        return list.getY();
    }

    public void setPercent(double percent) {
        int yPos = (maxY-(int)(((double)(maxY-minY)*percent)+(double)minY))+minY;
        scrollBox.setY(yPos);
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        if (contains(scrollBox.getSprite(),x,y)) {
            System.out.println("CLICK DOWN SCROLL BAR");
            clickdown = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        if (clickdown) {
            clickdown = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean dragged(int x, int y) {
        y-=buffer;
        if (clickdown && y > minY && y < maxY) {
            double percent = 1- (double)((y-minY))/(double)(maxY-minY);
            window.setScroll(percent);
            scrollBox.setY(y);
            return true;
        } else if (clickdown && y < minY) {
            window.setScroll(1);
            scrollBox.setY(minY);
        } else if (clickdown && y > maxY) {
            window.setScroll(0);
            scrollBox.setY(maxY);
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        list.draw(batch);
    }

    @Override
    public boolean update(int x, int y) {
        list.update(x,y);
        return false;
    }

    @Override
    public void cancelUpdate() {
        list.cancelUpdate();
    }

    @Override
    public void dispose() {
        list.dispose();
    }
}
