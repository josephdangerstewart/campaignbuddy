package com.campaignbuddy.resources.containers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.resources.meta.InteractiveContainer;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

import java.util.ArrayList;

/**
 * Created by josephstewart on 7/3/17.
 */
public class Window extends InteractiveContainer {

    private Sprite background;
    private InteractableList list;
    private InteractableList alwaysShowingList;

    private int x;
    private int y;

    private int width;
    private int height;

    private int offSetX;
    private int offSetY;

    private boolean transparent;

    private int yBuffer = 0;

    private OrthographicCamera camera;
    private Viewport viewport;

    public void clear() {
        alwaysShowingList.getList().clear();
        list.getList().clear();
    }

    public Sprite getBackground() {
        if (!transparent)
            return background;
        else
            return null;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Window(int x, int y, int width, int height, OrthographicCamera camera, Viewport viewport) {
        transparent = true;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.camera = camera;
        this.viewport = viewport;

        list = new InteractableList(x,y);
        alwaysShowingList = new InteractableList(x,y);
    }

    public Window(String background, int x, int y, OrthographicCamera camera, Viewport viewport) {
        transparent = false;
        this.background = new Sprite(new Texture(Gdx.files.internal(background)));
        this.background.setPosition(x,y);

        this.x = x;
        this.y = y;
        this.width = (int)this.background.getWidth();
        this.height = (int)this.background.getHeight();

        this.camera = camera;
        this.viewport = viewport;

        list = new InteractableList(x,y);
        alwaysShowingList = new InteractableList(x,y);
    }

    public Window(Sprite background, int x, int y, OrthographicCamera camera, Viewport viewport) {
        transparent = false;
        this.background = background;
        this.background.setPosition(x,y);

        this.x = x;
        this.y = y;
        this.width = (int)this.background.getWidth();
        this.height = (int)this.background.getHeight();

        this.camera = camera;
        this.viewport = viewport;

        list = new InteractableList(x,y);
        alwaysShowingList = new InteractableList(x,y);
    }

    public void setYBuffer(int buffer) {
        yBuffer = buffer;
    }

    public InteractableList getList() {
        InteractableList rList = new InteractableList(list.getX(),list.getY());

        ArrayList<InteractiveDrawable> drawables2 = list.getList();
        ArrayList<InteractiveDrawable> drawables1 = alwaysShowingList.getList();

        for (int i = 0; i < drawables1.size(); i ++) {
            rList.add(drawables1.get(i));
        }

        for (int i = 0; i < drawables2.size(); i++) {
            rList.add(drawables2.get(i));
        }

        return list;
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
        if (!transparent)
            background.setX(x);
        list.setX(offSetX + x);
        alwaysShowingList.setX(offSetX + x);
    }

    @Override
    public void setY(float y) {
        this.y = (int)y;
        if (!transparent)
            background.setY(y);
        list.setY(offSetY + y);
        alwaysShowingList.setY(offSetY+y);
    }

    @Override
    public boolean dragged(int x, int y) {
        if (alwaysShowingList.dragged(x,y)) {
            return true;
        }
        return list.dragged(x,y);
    }

    @Override
    public void draw(SpriteBatch batch) {

        if (!transparent)
            background.draw(batch);

        alwaysShowingList.draw(batch);

        Rectangle scissors = new Rectangle();
        Rectangle bounds = new Rectangle(x, y + yBuffer, width, height - 2 * yBuffer);
        ScissorStack.calculateScissors(camera,viewport.getScreenX(),viewport.getScreenY(),viewport.getScreenWidth(),viewport.getScreenHeight(), batch.getTransformMatrix(), bounds, scissors);
        batch.flush();
        ScissorStack.pushScissors(scissors);


        //DRAWING STARTS HERE

        list.draw(batch);

        //DRAWING ENDS HERE


        batch.flush();
        ScissorStack.popScissors();

    }

    public void setWidth(float width) {
        if (!transparent)
            background.setSize(width,height);
        this.width = (int)width;
    }

    public void setHeight(float height) {
        if (!transparent)
            background.setSize(width,height);
        this.height = (int)height;
    }

    @Override
    public boolean update(int x, int y) {
        Rectangle bounds = new Rectangle(this.x,this.y,width,height);
        ArrayList<InteractiveDrawable> drawables = list.getList();

        if (alwaysShowingList.update(x,y)) {
            list.cancelUpdate();
            return true;
        }

        ArrayList<InteractiveDrawable> list = this.list.getList();

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getAlwaysUpdate()) {
                list.get(i).update(x,y);
            }
        }

        if (bounds.contains(x,y)) {

            boolean found = false;
            for (int i = list.size() - 1; i >= 0; i--) {
                if (!found) {
                    found = list.get(i).update(x,y);
                } else {
                    list.get(i).cancelUpdate();
                }
            }

        }
        return false;
    }

    @Override
    public void cancelUpdate() {
        alwaysShowingList.cancelUpdate();
        list.cancelUpdate();
    }

    @Override
    public boolean clickUp(int x, int y, int button) {
        Rectangle bounds = new Rectangle(this.x,this.y,width,height);

        if (alwaysShowingList.clickUp(x,y,button)) return true;

        if (bounds.contains(x,y)) {
            return list.clickUp(x,y,button);
        }
        return false;
    }

    @Override
    public boolean clickDown(int x, int y, int button) {
        Rectangle bounds = new Rectangle(this.x, this.y, width, height);

        if (alwaysShowingList.clickDown(x,y,button)) return true;

        if (bounds.contains(x,y)) {
            return list.clickDown(x,y,button);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (alwaysShowingList.keyUp(keycode)) return true;
        return list.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (alwaysShowingList.keyDown(keycode)) return true;
        return list.keyDown(keycode);
    }

    @Override
    public boolean keyTyped(char key) {
        if (alwaysShowingList.keyTyped(key)) return true;
        return list.keyTyped(key);
    }

    @Override
    public boolean scrolled(int amount) {
        if (alwaysShowingList.scrolled(amount)) return true;
        return list.scrolled(amount);
    }

    public Sprite getSprite() {
        return background;
    }

    public void setOffset(int x, int y) {
        offSetX = x;
        offSetY = y;
        list.setX(list.getX() + offSetX);
        list.setY(list.getY() + offSetY);
        alwaysShowingList.setPosition(list.getX(),list.getY());
    }

    public void setOffsetX(int x) {
        offSetX = x;
        list.setX(this.x+offSetX);
        alwaysShowingList.setX(list.getX());
    }

    public void setOffsetY(int y) {
        offSetY = y;
        list.setY(this.y+offSetY);
        alwaysShowingList.setY(list.getY());
    }

    public void move(int changeX, int changeY) {
        offSetX += changeX;
        offSetY += changeY;
        list.setX(list.getX() + changeX);
        list.setY(list.getY() + changeY);
        alwaysShowingList.setX(list.getX());
        alwaysShowingList.setY(list.getY());
    }

    public boolean contains(int x, int y) {
        return contains(background,x,y);
    }

    public void add(InteractiveDrawable element, float x, float y) {
        element.setPosition(
                x + this.x,
                y + this.y
        );
        if (element.getAlwaysShowing())
            alwaysShowingList.add(element);
        else
            list.add(element);
    }

    @Override
    public void dispose() {
        list.dispose();
        alwaysShowingList.dispose();
        if (background != null)
            background.getTexture().dispose();
    }
}
