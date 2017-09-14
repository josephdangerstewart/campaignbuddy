package com.campaignbuddy.resources.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.campaignbuddy.resources.containers.InteractableList;
import com.campaignbuddy.resources.meta.InteractiveDrawable;

import java.util.concurrent.Callable;

@SuppressWarnings( "rawtypes")
public class ClickableImage extends InteractiveDrawable {
	private TextureAtlas atlas;
	private String name;
	
	private boolean clickDown;
	private boolean hovering;
	
	private Sprite img;
	
	private Callable onClick;
	
	public ClickableImage(String state, String name, int x, int y, Callable onClick) {
		
		atlas = new TextureAtlas(Gdx.files.internal("images/"+state+"/spritesheets/"+name+".txt"));
		
		img = new Sprite(atlas.findRegion(name));
		img.setPosition(x, y);
		
		clickDown = false;
		hovering = false;
		this.onClick = onClick;
		this.name = name;
	}
	
	public ClickableImage(String state, String name, int x, int y, Callable onClick, InteractableList list) {
		
		atlas = new TextureAtlas(Gdx.files.internal("images/"+state+"/spritesheets/"+name+".txt"));
		
		img = new Sprite(atlas.findRegion(name));
		img.setPosition(x, y);
		
		clickDown = false;
		this.onClick = onClick;
		this.name = name;
		list.add(this);
	}

	@Override
	public void draw(SpriteBatch batch) {
		img.draw(batch);
	}

	@Override
	public boolean update(int x, int y) {
		if (!clickDown && contains(img,x,y) && !hovering) {
			img.setRegion(atlas.findRegion(name+"-hover"));
			hovering = true;
			return true;
		} else if (hovering && !contains(img,x,y)) {
			img.setRegion(atlas.findRegion(name));
			hovering = false;
			return false;
		} else if (hovering) {
			return true;
		}
		return false;
	}

	@Override
	public void cancelUpdate() {
		img.setRegion(atlas.findRegion(name));
		hovering = false;
	}

	@Override
	public boolean clickDown(int x, int y, int button) {
		if (contains(img,x,y))
		{
			clickDown = true;
			img.setRegion(atlas.findRegion(name+"-down"));
			return true;
		}
		return false;
	}

	@Override
	public boolean clickUp(int x, int y, int button) {
		if (clickDown && contains(img,x,y))
		{
			try {
				if (!hovering)
					img.setRegion(atlas.findRegion(name));
				else
					img.setRegion(atlas.findRegion(name+"-hover"));
				clickDown = false;
				if (onClick != null)
					onClick.call();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		clickDown = false;
		if (!hovering)
			img.setRegion(atlas.findRegion(name));
		else
			img.setRegion(atlas.findRegion(name+"-hover"));
		return false;
	}

	@Override
	public void dispose() {
		img.getTexture().dispose();
		atlas.dispose();
	}

	@Override
	public boolean dragged(int x, int y) {
		return false;
	}

	@Override
	public int getWidth() {
		return (int) img.getWidth();
	}

	@Override
	public int getHeight() {
		return (int) img.getHeight();
	}

	@Override
	public int getX() {
		return (int) img.getX();
	}

	@Override
	public int getY() {
		return (int) img.getY();
	}

	@Override
	public void setX(float x) {
		img.setX(x);
	}

	@Override
	public void setY(float y) {
		img.setY(y);
	}
}
