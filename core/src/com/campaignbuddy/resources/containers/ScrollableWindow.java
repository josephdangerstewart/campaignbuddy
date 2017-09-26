package com.campaignbuddy.resources.containers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.campaignbuddy.resources.components.ScrollBar;

/**
 * Created by josephstewart on 7/3/17.
 */
public class ScrollableWindow extends Window {

    private int max;
    private int min;
    private int scrollAmount = 0;

    private int scrollFactor = 4;

    private boolean scrollOnY = true;

    private Vector3 mousePos;
    private OrthographicCamera camera;
    private Viewport viewport;

    private ScrollBar scrollBar = null;

    public ScrollableWindow(int x, int y, int width, int height, OrthographicCamera camera, int max, int min, Viewport viewport) {
        super(x,y,width,height,camera,viewport);

        this.max = max;
        this.min = min;
        this.camera = camera;
        mousePos = new Vector3();
    }

    public ScrollableWindow(String background, int x, int y, OrthographicCamera camera, int max, int min, Viewport viewport) {
        super(background, x, y, camera,viewport);

        this.max = max;
        this.min = min;

        this.viewport = viewport;
        this.camera = camera;
        mousePos = new Vector3();
    }

    public ScrollableWindow(Sprite background, int x, int y, OrthographicCamera camera, int max, int min, Viewport viewport) {
        super(background, x, y, camera, viewport);

        this.max = max;
        this.min = min;

        this.viewport = viewport;
        this.camera = camera;
        mousePos = new Vector3();
    }

    @Override
    public void clear() {
        setScroll(0);
        if (scrollBar != null) {
            scrollBar.setPercent(0);
        }
        super.clear();
    }

    public void setScrollBar(ScrollBar scrollBar) {
        this.scrollBar = scrollBar;
        if (max != 0) {
            scrollBar.setPercent(scrollAmount / (max - min));
            scrollBar.setVisible(true);
        } else {
            scrollBar.setPercent(0);
            scrollBar.setVisible(false);
        }
    }

    public void setScrollOnY(boolean scroll) {
        scrollOnY = scroll;
    }

    public void setScrollFactor(int scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    public void setScroll(double percent) {
        if (max != 0) {
            scrollAmount = (int) ((max - min) * percent);
            System.out.println(scrollAmount);
            if (scrollOnY) {
                setOffsetY(scrollAmount);
            } else {
                setOffsetX(scrollAmount);
            }
        }
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    @Override
    public boolean scrolled(int amount) {
        if (max != 0) {
            mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mousePos = camera.unproject(mousePos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

            int x = (int) mousePos.x;
            int y = (int) mousePos.y;

            if (contains(getSprite(), x, y)) {
                if (scrollOnY && (scrollAmount + amount * scrollFactor) >= min && (scrollAmount + amount * scrollFactor) <= max) {
                    move(0, amount * scrollFactor);
                    scrollAmount += amount * scrollFactor;
                    System.out.println(scrollAmount);
                    if (scrollBar != null) {
                        scrollBar.setPercent((double) scrollAmount / (double) (max - min));
                    }
                    return true;
                } else if (!scrollOnY && (scrollAmount + amount * scrollFactor) >= min && (scrollAmount + amount * scrollFactor) <= max) {
                    move(amount * scrollFactor * (-1), 0);
                    scrollAmount += amount * scrollFactor;
                    System.out.println(scrollAmount);
                    if (scrollBar != null) {
                        scrollBar.setPercent((double) scrollAmount / (double) (max - min));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void setMax(int max) {
        if (scrollBar != null) {
            if (max == 0) {
                scrollBar.setVisible(false);
            } else {
                scrollBar.setVisible(true);
            }
        }
        this.max = max;
    }

}
