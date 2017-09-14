package com.campaignbuddy.resources.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

/**
 * Created by josephstewart on 8/20/17.
 */
public class Text extends InteractiveDrawable {

    private GlyphLayout layout;
    private BitmapFont font;
    private String text;
    private int x;
    private int y;

    public Text(int x, int y, BitmapFont font, String text) {
        this.x = x;
        this.y = y;
        this.font = font;
        this.text = text;

        this.font.setColor(Color.BLACK);

        layout = new GlyphLayout();
    }

    public Text(int x, int y, String font, String text) {
        this.x = x;
        this.y = y;
        this.font = new BitmapFont(Gdx.files.internal(font));
        this.text = text;

        this.font.setColor(Color.BLACK);

        layout = new GlyphLayout();
    }

    public BitmapFont getFont() {
        return font;
    }

    public String getText() {return text;}

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(SpriteBatch batch) {
        font.draw(batch,text,x,y);
    }

    @Override
    public boolean update(int x, int y) {
        return false;
    }

    @Override
    public void cancelUpdate() {

    }

    @Override
    public int getWidth() {
        layout.setText(font,text);
        return (int) layout.width;
    }

    @Override
    public int getHeight() {
        layout.setText(font,text);
        return (int) layout.height;
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
    }

    @Override
    public void setY(float y) {
        this.y = (int)y;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
