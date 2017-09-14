package com.campaignbuddy.resources.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.meta.SwitchEvent;

/**
 * Created by josephstewart on 8/21/17.
 */
public class Switch extends InteractiveDrawable {

    private Button button;
    private String name;
    private TextureAtlas atlas;

    private SwitchEvent onSwitch;

    private boolean selected;

    public Switch(int x, int y, String src, String name, boolean state) {
        atlas = new TextureAtlas(src + name + ".txt");

        this.name = name;

        selected = state;
        Sprite s = null;
        if (selected)
            s = new Sprite(atlas.findRegion(name + "-selected"));
        else
            s = new Sprite(atlas.findRegion(name+"-unselected"));

        button = new Button(x, y, s, new Event() {
            @Override
            public void onCall() {
                selected = !selected;
                if (onSwitch != null) {
                    onSwitch.onSwitch(selected);
                }
                setSprite();
            }
        });
    }

    public void setOnSwitch(SwitchEvent event) {
        this.onSwitch = event;
    }

    private void setSprite() {
        if (selected)
            button.getSprite().setRegion(atlas.findRegion(name + "-selected"));
        else
            button.getSprite().setRegion(atlas.findRegion(name+"-unselected"));
    }

    public void setState(boolean selected) {
        this.selected = selected;
        setSprite();
    }

    @Override
    public void draw(SpriteBatch batch) {
        button.draw(batch);
    }

    @Override
    public boolean update(int x, int y) {
        return button.update(x,y);
    }

    @Override
    public void cancelUpdate() {
        button.cancelUpdate();
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        return this.button.clickDown(x,y,button);
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        return this.button.clickUp(x,y,button);
    }

    @Override
    public int getWidth() {
        return button.getWidth();
    }

    @Override
    public int getHeight() {
        return button.getHeight();
    }

    @Override
    public int getX() {
        return button.getX();
    }

    @Override
    public int getY() {
        return button.getY();
    }

    @Override
    public void setX(float x) {
        button.setX(x);
    }

    @Override
    public void setY(float y) {
        button.setY(y);
    }

    @Override
    public void dispose() {
        button.dispose();
    }
}
