package com.campaignbuddy.resources.containers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.campaignbuddy.resources.components.NumberTextField;
import com.campaignbuddy.resources.components.TextField;
import com.campaignbuddy.resources.meta.InteractiveContainer;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

import java.util.ArrayList;

public class InteractableList extends InteractiveContainer {
	
	private ArrayList<InteractiveDrawable> list;
	private ArrayList<Sprite> sprites;

	private float x;
	private float y;

	public InteractableList() {
		list = new ArrayList<InteractiveDrawable>();
		sprites = new ArrayList<Sprite>();
		x = 0;
		y = 0;
	}

	public InteractableList(float x, float y) {
		list = new ArrayList<InteractiveDrawable>();
		sprites = new ArrayList<Sprite>();
		this.x = x;
		this.y = y;
	}

	public ArrayList<InteractiveDrawable> getList() {
	    return list;
    }

    public ArrayList<Sprite> getSprites() {
	    return sprites;
    }
	
	@Override
	public void draw(SpriteBatch batch) {
		for (int i = 0; i < sprites.size(); i++)
			sprites.get(i).draw(batch);
		for (int i = 0; i < list.size(); i++)
		    if (list.get(i).getVisible())
    			list.get(i).draw(batch);
	}

	@Override
	public boolean update(int x, int y) {
		boolean found = false;
	    for (int i = list.size()-1; i >= 0; i--) {
	        if (list.get(i).getVisible()) {
                if (!found && list.get(i).update(x, y)) {
                    found = true;
                } else if (found) {
                    list.get(i).cancelUpdate();
                }
            }
		}
		return found;
	}

	@Override
	public void cancelUpdate() {
		for (int i = list.size()-1; i >= 0; i--)
			list.get(i).cancelUpdate();
	}

	@Override
	public boolean clickDown(int x, int y, int button) {
		for (int i = list.size()-1; i >= 0; i--)
			if (list.get(i).getVisible() && list.get(i).clickDown(x, y, button)) return true;
        return false;
	}

	@Override
	public boolean clickUp(int x, int y, int button) {
		for (int i = list.size()-1; i >= 0; i--)
			if(list.get(i).getVisible() && list.get(i).clickUp(x, y, button)) return true;
		return false;
	}

	@Override
	public boolean keyDown(int keyCode) {
		for (int i = list.size()-1; i >= 0; i--) {
			if(list.get(i).getVisible() && list.get(i).keyDown(keyCode)) return true;
		}

		if (keyCode == Input.Keys.TAB) {
			for (int i = 0; i < list.size(); i ++) {
				System.out.println(list.get(i).getClass().getCanonicalName());
				if (list.get(i).getClass().getCanonicalName().equals("com.campaignbuddy.resources.components.TextField")) {
					((TextField)list.get(i)).click();
					System.out.println("TextField!!!");
					return true;
				} else if (list.get(i).getClass().getCanonicalName().equals("com.campaignbuddy.resources.components.NumberTextField")) {
					((NumberTextField)list.get(i)).click();
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean keyUp(int keyCode) {
		for (int i = list.size()-1; i >= 0; i--) {
			if(list.get(i).getVisible() && list.get(i).keyUp(keyCode)) return true;
		}
        return false;
	}

	@Override
	public boolean keyTyped(char key) {
		for (int i = list.size()-1; i >= 0; i--) {
			if(list.get(i).getVisible() && list.get(i).keyTyped(key)) return true;
		}
        return false;
	}

	@Override
	public boolean scrolled(int amount) {
		for (int i = list.size()-1; i >= 0; i--) {
			if(list.get(i).getVisible() && list.get(i).scrolled(amount)) return true;
		}
        return false;
	}

	public void setOriginCorner() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;

		for (int i = 0; i < list.size();i++) {
			if (list.get(i).getX() < minX) {
				minX = list.get(i).getX();
			}
			if (list.get(i).getY() < minY) {
				minY = list.get(i).getY();
			}
		}

		this.x = minX;
		this.y = minY;
	}

	@Override
	public int getWidth() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < list.size(); i++) {
			int width = list.get(i).getWidth();
			int x = list.get(i).getX();
			if (x < min) {
				min = x;
			}
			if (x+width > max) {
				max = x+width;
			}
		}

		return max-min;
	}

	@Override
	public int getHeight() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < list.size(); i++) {
			int height = list.get(i).getHeight();
			int y = list.get(i).getY();
			if (y < min) {
				min = y;
			}
			if (y+height > max) {
				max = y+height;
			}
		}

		return max-min;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < list.size(); i++)
			list.get(i).dispose();
		for (int i = 0; i < sprites.size(); i++)
			sprites.get(i).getTexture().dispose();
	}

	@Override
	public boolean dragged(int x, int y) {
		for (int i = list.size()-1; i >=0; i--) {
			if (list.get(i).dragged(x,y)) return true;
		}
		return false;
	}

	public int getX() {
		return (int)x;
	}

	public int getY() {
		return (int)y;
	}

	@Override
	public void setX(float x) {
		float offset = x-this.x;
		for (int i = 0; i < list.size(); i++)
			list.get(i).setX(list.get(i).getX()+offset);
		for (int i = 0; i < sprites.size(); i++)
			sprites.get(i).setX(sprites.get(i).getX()+offset);
		this.x = x;
	}

	@Override
	public void setY(float y) {
		float offset = y-this.y;
		for (int i = 0; i < list.size(); i++)
			list.get(i).setY(list.get(i).getY()+offset);
		for (int i = 0; i < sprites.size(); i++)
			sprites.get(i).setY(sprites.get(i).getY()+offset);
		this.y = y;
	}

	public void add(InteractiveDrawable... elements) {
		for (int i = 0; i < elements.length; i++) {
			add(elements[i]);
		}
	}

	public void add(InteractiveDrawable element) {
		if (element != null)
			list.add(element);
	}
	
	public void add(Sprite sprite) {
		sprites.add(sprite);
	}
	
}
