package com.campaignbuddy.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.main.Prefs;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.InteractiveDrawable;
import com.campaignbuddy.resources.components.ScrollBar;
import com.campaignbuddy.resources.containers.ScrollableWindow;
import com.campaignbuddy.state.meta.AppPool;

import java.util.ArrayList;

/**
 * Created by josephstewart on 8/19/17.
 */
public class Dashboard extends InteractiveDrawable {

    private final int X_BUFFER = 30;
    private final int INIT_X_BUFFER = 50;
    private final int Y_BUFFER = 40;

    private int listY;

    private int lastX;
    private int lastY;

    private ScrollBar scrollBar;

    private InteractableList list;

    private ScrollableWindow window;
    private Sidebar sidebar;
    private QuickActionBar qaBar;

    private AppPool appPool;
    private Prefs prefs;

    private ArrayList<InteractiveDrawable> logos;

    public Dashboard(Sidebar sidebar, QuickActionBar qaBar, AppPool appPool, Prefs prefs, OrthographicCamera camera, Viewport viewport) {
        list = new InteractableList();

        this.prefs = prefs;
        this.appPool = appPool;
        this.sidebar = sidebar;
        this.qaBar = qaBar;

        logos = appPool.getApps();
        System.out.println("logos.size() = " + logos.size());
        appPool.getSideBar("Party Manager");

        int width = CampaignBuddyMain.WIDTH - sidebar.getWidth();
        int height = CampaignBuddyMain.HEIGHT - qaBar.getHeight();

        lastX = sidebar.getX() + sidebar.getWidth();
        lastY = qaBar.getY() + qaBar.getHeight();

        int max=0;
        if (logos.size() > 0) {
            int rows = width / (logos.size() * (AppPool.WIDTH + X_BUFFER));
            int totalHeight = rows * Y_BUFFER;

            max = totalHeight - height;
        }

        if (max > 0) {
            window = new ScrollableWindow(sidebar.getX()+sidebar.getWidth(),qaBar.getY()+qaBar.getHeight(),width,height,camera,max,0,viewport);
        } else {
            window = new ScrollableWindow(sidebar.getX()+sidebar.getWidth(),qaBar.getY()+qaBar.getHeight(),width,height,camera,0,0,viewport);
        }

        listY = window.getList().getY();

        scrollBar = new ScrollBar("images/main/spritesheets/","scroll",window,CampaignBuddyMain.WIDTH-60,20);
        window.setScrollBar(scrollBar);

        int y = height - (Y_BUFFER + AppPool.HEIGHT);
        int x = INIT_X_BUFFER;
        for (int i = 0; i < logos.size(); i++) {
            window.add(logos.get(i),x,y);
            x += X_BUFFER + AppPool.WIDTH;
            if (x+AppPool.WIDTH > width) {
                x = INIT_X_BUFFER;
                y -= (Y_BUFFER + AppPool.HEIGHT);
            }
        }

        list.add(scrollBar);
        list.add(window);
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
    public boolean update(int x, int y) {

        int newX = sidebar.getX() + sidebar.getWidth();
        int newY = qaBar.getY() + qaBar.getHeight();

        if (newX != lastX) {
            setX(newX);
            lastX = newX;
        }

        if (newY != lastY) {
            setY(newY);
            lastY = newY;
        }

        return list.update(x,y);
    }

    @Override
    public void cancelUpdate() {
        list.cancelUpdate();
    }

    @Override
    public int getWidth() {
        return window.getWidth();
    }

    @Override
    public int getHeight() {
        return window.getHeight();
    }

    @Override
    public int getX() {
        return window.getX();
    }

    @Override
    public int getY() {
        return window.getY();
    }

    @Override
    public void setX(float x) {
        window.setX(x);
        window.setWidth(CampaignBuddyMain.WIDTH-x);

        int max = 0;
        if (logos.size() > 0) {
            int rows = window.getWidth() / (logos.size() * (AppPool.WIDTH + X_BUFFER));
            int totalHeight = rows * Y_BUFFER;

            max = totalHeight - window.getHeight();
        }

        if (max > 0) {
            window.setMax(max);
        } else {
            window.setMax(0);
        }
    }

    @Override
    public void setY(float y) {

        window.setY(y);
        window.setHeight(CampaignBuddyMain.HEIGHT-y);
        window.getList().setY(listY);

        int max = 0;
        if (logos.size() > 0) {
            int rows = window.getWidth() / (logos.size() * (AppPool.WIDTH + X_BUFFER));
            int totalHeight = rows * Y_BUFFER;

            max = totalHeight - window.getHeight();
        }

        if (max > 0) {
            window.setMax(max);
        } else {
            window.setMax(0);
        }
    }

    @Override
    public void dispose() {
        list.dispose();
    }
}
