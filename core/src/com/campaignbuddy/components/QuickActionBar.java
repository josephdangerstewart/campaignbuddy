package com.campaignbuddy.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.Button;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.containers.ScrollableWindow;
import com.campaignbuddy.resources.containers.Window;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveContainer;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.state.meta.AppPool;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/4/17.
 */
public class QuickActionBar extends InteractiveContainer {

    private final int X_BUFFER = 20;
    private final int INIT_X_BUFFER = 10;

    private final static int speed = 10;

    private InteractableList list;

    private int lastWidth;

    private ScrollableWindow bar;
    private Sidebar sidebar;
    private Button unhide;
    private Button hide;

    private AppPool pool;

    private boolean hidden;

    public QuickActionBar(OrthographicCamera camera, Viewport viewport, Sidebar sidebar, AppPool pool) {
        this.pool = pool;
        bar = new ScrollableWindow("images/main/qa-bar.png",0,0,camera,0,0,viewport);
        bar.setScrollOnY(false);
        this.sidebar = sidebar;

        lastWidth = (sidebar.getWidth()+sidebar.getX());

        list = new InteractableList();

        bar.setX(sidebar.getWidth());
        bar.setWidth(CampaignBuddyMain.WIDTH-lastWidth);

        hide = new Button(bar.getX()+10, bar.getY()+bar.getHeight()+5, "images/main/qa-hide.png", new Event() {
            @Override
            public void onCall() {
                hidden = true;
                unhide.setVisible(true);
                hide.setVisible(false);
            }
        });

        ArrayList<InteractiveDrawable> components = pool.getQuickActionBar();

        int x = INIT_X_BUFFER;
        for (int i = 0; i < components.size(); i ++) {
            bar.add(components.get(i),x,bar.getHeight()/2-components.get(i).getHeight()/2);
            x += components.get(i).getWidth() + X_BUFFER;
        }

        System.out.println("Size: " + bar.getList().getList().size());

        unhide = new Button(0,0,"images/main/qa-unhide.png", new Event() {
            public void onCall() {
                unhide();
            }
        });
        unhide.setX(lastWidth);
        unhide.setVisible(false);

        list.add(unhide);
        list.add(hide);
        list.add(bar);

    }

    public void init() {
        bar.clear();
        ArrayList<InteractiveDrawable> components = pool.getQuickActionBar();

        int x = INIT_X_BUFFER;
        for (int i = 0; i < components.size(); i ++) {
            bar.add(components.get(i),x,bar.getHeight()/2-components.get(i).getHeight()/2);
            x += components.get(i).getWidth() + X_BUFFER;
        }

        int width = bar.getList().getWidth()+INIT_X_BUFFER;
        System.out.println(width);
        if (width - bar.getWidth() > 0) {
            bar.setMax(width-bar.getWidth()+INIT_X_BUFFER);
        } else {
            bar.setMax(0);
        }
    }

    private void unhide() {
        hidden = false;
        unhide.setVisible(false);
        hide.setVisible(true);
    }

    @Override
    public void draw(SpriteBatch batch) {
        list.draw(batch);
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        return list.clickDown(x,y,button);
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        return list.clickUp(x,y,button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return list.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return list.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char key) {
        return list.keyTyped(key);
    }

    @Override
    public boolean dragged(int x, int y) {
        return list.dragged(x,y);
    }

    @Override
    public boolean scrolled(int amount) {
        return list.scrolled(amount);
    }

    @Override
    public boolean update(int x, int y) {

        if (lastWidth != (sidebar.getWidth()+sidebar.getX())) {
            lastWidth = (sidebar.getWidth()+sidebar.getX());
            bar.setX(lastWidth);
            bar.setWidth(CampaignBuddyMain.WIDTH-lastWidth);
            unhide.setX(lastWidth);
        }


        if (hidden && bar.getY()+bar.getHeight() > 0) {
            bar.setY(bar.getY()-speed);
        } else if (hidden) {
            bar.setY(bar.getHeight()*(-1));
        } else if (!hidden && bar.getY() < 0) {
            bar.setY(bar.getY()+speed);
        } else if (!hidden) {
            bar.setY(0);
        }

        hide.setX(bar.getX()+10);
        hide.setY(bar.getY()+bar.getHeight()+10);


        list.update(x,y);

        return false;
    }

    @Override
    public void cancelUpdate() {
        list.cancelUpdate();
    }

    @Override
    public int getWidth() {
        return bar.getWidth();
    }

    @Override
    public int getHeight() {
        return bar.getHeight();
    }

    @Override
    public int getX() {
        return bar.getX();
    }

    @Override
    public int getY() {
        return bar.getY();
    }

    @Override
    public void setX(float x) {

    }

    @Override
    public void setY(float y) {

    }

    @Override
    public void dispose() {
        list.dispose();
    }
}
