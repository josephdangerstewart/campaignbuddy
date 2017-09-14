package com.campaignbuddy.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.main.CampaignBuddyMain;
import com.campaignbuddy.resources.components.Button;
import com.campaignbuddy.resources.components.Image;
import com.campaignbuddy.resources.components.ScrollBar;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.containers.ScrollableWindow;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveContainer;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

import java.util.ArrayList;


/**
 * Created by josephstewart on 7/30/17.
 */
public class Sidebar extends InteractiveContainer {

    private BitmapFont font;
    private GlyphLayout layout;

    private static final int speed = 10;
    private static final int Y_BUFFER = 20;
    private static final int X_BUFFER = 10;
    private static final int SPACING = 15;

    private Image header;
    private String headerText;

    private ScrollBar scrollBar;
    private ScrollableWindow body;
    private Button unhide;

    private InteractableList list;

    private boolean hidden = false;

    public Sidebar(OrthographicCamera camera, Viewport viewport) {
        list = new InteractableList();

        body = new ScrollableWindow("images/main/sidebar-body.png",0,0,camera,0,0,viewport);
        scrollBar = new ScrollBar("images/main/spritesheets/","scroll",body,body.getWidth()-20,10);
        scrollBar.setHeight(body.getHeight()-20);
        body.setScrollBar(scrollBar);

        header = new Image(new Sprite(new Texture("images/main/sidebar-header.png")));

        header.getSprite().setSize(body.getWidth(),CampaignBuddyMain.HEIGHT-body.getHeight());

        body.setYBuffer(5);
        body.setPosition(0,0);
        header.setPosition(0,body.getHeight());

        font = new BitmapFont(Gdx.files.internal("fonts/header-big.fnt"));
        layout = new GlyphLayout();

        unhide = new Button(0, 0, "images/main/sidebar-unhide.png", new Event() {
            @Override
            public void onCall() {
                unhide.setVisible(false);
                hidden = false;
            }
        });
        unhide.setVisible(false);
        unhide.setY(CampaignBuddyMain.HEIGHT-unhide.getHeight());

        headerText = "Campaign Buddy";

        list.add(unhide);
        list.add(body);
        list.add(header);
        list.add(scrollBar);
    }

    public void add(ArrayList<InteractiveDrawable> drawables) {

        if (drawables.size() > 0) {
            int y = body.getHeight() - Y_BUFFER - drawables.get(0).getHeight();

            body.clear();

            for (int i = 0; i < drawables.size(); i++) {
                body.add(drawables.get(i), X_BUFFER, y);
                y -= (drawables.get(i).getHeight() + SPACING);
            }

            y -= SPACING;
            if (y < 0) {
                body.setMax(y * -1);
            } else {
                body.setMax(0);
            }
        } else {
            body.clear();
            body.setMax(0);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        list.draw(batch);
        layout.setText(font,headerText);
        font.draw(batch,layout,header.getWidth()/2-layout.width/2+body.getX(),header.getHeight()/2+layout.height/2+header.getY());
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    @Override
    public boolean update(int x, int y) {

        if (hidden && (body.getX() + body.getWidth()) > 0) {
            body.setX(body.getX()-speed);
            header.setX(body.getX());
            scrollBar.setX(body.getWidth()-20+body.getX());
        } else if (hidden) {
            body.setX((body.getWidth() * -1)-3);
            header.setX(body.getX());
            scrollBar.setX(body.getWidth()-20+body.getX());
            unhide.setVisible(true);
        } else if (!hidden && body.getX() < 0) {
            body.setX(body.getX()+speed);
            header.setX(body.getX());
            scrollBar.setX(body.getWidth()-20+body.getX());
        } else if (!hidden) {
            body.setX(0);
            header.setX(0);
            scrollBar.setX(body.getWidth()-20+body.getX());
        }

        list.update(x,y);

        return false;
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        if (contains(header.getSprite(),x,y)) {
            hidden = true;
        }
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
    public void cancelUpdate() {
        list.cancelUpdate();
    }

    @Override
    public int getWidth() {
        return body.getWidth();
    }

    @Override
    public int getHeight() {
        return (int)(body.getHeight() + header.getHeight());
    }

    @Override
    public int getX() {
        return body.getX();
    }

    @Override
    public int getY() {
        return body.getY();
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
