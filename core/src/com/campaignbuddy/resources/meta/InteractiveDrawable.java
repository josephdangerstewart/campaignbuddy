package com.campaignbuddy.resources.meta;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class InteractiveDrawable {

	private boolean visible = true;
	private boolean alwaysUpdate = false;
	private boolean alwaysShowing = false;

	public static final boolean SHOW_DEFAULTS = true;
	public static final boolean SHOW_DEFAULT_LOOPS = false;

    //loop methods
	public abstract void draw(SpriteBatch batch);
	public abstract boolean update(int x, int y);
	public abstract void cancelUpdate();

	//input event methods
	public boolean clickDown(int x, int y, int button) {
	    if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] CLICK DOWN DEFAULT");
	    return false;
	}
	public boolean clickUp(int x, int y, int button) {
        if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] CLICK UP DEFAULT");
        return false; }
    public boolean dragged(int x, int y) {
        if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] DRAGGED DEFAULT (" + x + "," + y + ")");
        return false; }
    public boolean keyDown(int keyCode) {
        if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] KEY DOWN DEFAULT");
        return false; }
    public boolean keyUp(int keyCode) {
        if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] KEY UP DEFAULT");
        return false; }
    public boolean keyTyped(char key) {
        if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] KEY TYPED DEFAULT");
        return false;}
    public boolean scrolled(int amount) {
        if (SHOW_DEFAULTS) System.out.println("[DEBUGGING] SCROLLED DEFAULT");
        return false; }

	//getter functions
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getX();
	public abstract int getY();
	public boolean getVisible() {return visible;}

	//setter functions
	public abstract void setX(float x);
	public abstract void setY(float y);
	public void setPosition(float x, float y) {
	    setX(x);
	    setY(y);
    }
    public void setVisible(boolean visible) {this.visible = visible;}

    public void setAlwaysUpdate(boolean alwaysUpdate) {
	    this.alwaysUpdate = alwaysUpdate;
    }

    public boolean getAlwaysUpdate() {
	    return alwaysUpdate;
    }

    public void setAlwaysShowing(boolean alwaysShowing) {this.alwaysShowing=alwaysShowing;}

    public boolean getAlwaysShowing() {return alwaysShowing;}

    protected boolean contains(Sprite img, int x, int y) {
        if (img.getBoundingRectangle().contains((float)x, (float)y)) {

            int spriteLocalX = (int) (x-img.getX());
            int spriteLocalY = (int) (y-img.getY());

            int textureLocalX = img.getRegionX() + spriteLocalX;
            int textureLocalY = ((int)img.getHeight()+img.getRegionY())-(img.getRegionY() + spriteLocalY);

			textureLocalX = (int)map(textureLocalX,0,(int)img.getWidth(),0,img.getRegionWidth());
			textureLocalY = (int)map(textureLocalY,0,(int)img.getHeight(),0,(int)img.getRegionHeight());

            TextureData data = img.getTexture().getTextureData();
            if (!data.isPrepared())
                data.prepare();
            Pixmap map = data.consumePixmap();
            Color pix = new Color(map.getPixel(textureLocalX, textureLocalY));
            if (pix.a != 0) {
                return true;
            }
        }
        return false;
    }

	private long map(long x, long in_min, long in_max, long out_min, long out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	//dispose method
	public abstract void dispose();
	
}
