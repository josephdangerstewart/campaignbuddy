package com.campaignbuddy.resources.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.campaignbuddy.resources.meta.Event;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 7/25/17.
 */
public class TextButton extends InteractiveDrawable {

    private boolean hovering = false;
    private boolean clickDown = false;

    public final int WIDTH_BUFFER = 10;
    public int HEIGHT_BUFFER = 20;

    private BitmapFont font;
    private GlyphLayout layout;

    private Button button;

    private int x;
    private int y;

    private String text;
    private String file;
    private Event onCall;

    public TextButton(int x, int y, String text, String file, String font, Event onCall) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.onCall = onCall;
        this.file = file;

        button = new Button(x,y,file,onCall);

        this.font = new BitmapFont(Gdx.files.internal(font));
        this.font.setColor(Color.BLACK);
        layout = new GlyphLayout();
        layout.setText(this.font,text);
        int width = (int)(layout.width + WIDTH_BUFFER*2);
        int height = (int)(layout.height + HEIGHT_BUFFER*2);
        button.getSprite().setSize(width,height);
    }

    @Override
    public void draw(SpriteBatch batch) {
        button.draw(batch);
        layout.setText(font,text);
        font.draw(batch,text,x+WIDTH_BUFFER,y+HEIGHT_BUFFER+layout.height);
    }

    @Override
    public boolean update(int x, int y) {
        button.update(x,y);
        return false;
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
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        this.x = (int)x;
        button.setX(x);
    }

    public void setHeightBuffer(int heightBuffer) {
        button.getSprite().setSize(button.getWidth(),button.getHeight()-(HEIGHT_BUFFER-heightBuffer));
        this.HEIGHT_BUFFER = heightBuffer;
    }

    @Override
    public void setY(float y) {
        this.y = (int)y;
        button.setY(y);
    }

    @Override
    public void dispose() {
        button.dispose();
    }
}
