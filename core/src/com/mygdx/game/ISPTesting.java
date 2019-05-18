package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ISPTesting extends ApplicationAdapter {
	SpriteBatch batch;
	Texture grass;
	Texture slime;
	int slimeX;
	int slimeY;
	int stateOfSlime;
	int speedOfSlime;
	int acceleration;
	int ground;
	int lastSlimeY;
	Terrain t;
	OrthographicCamera camera;
	boolean forward;
	@Override
	public void create () {
		t = new Terrain();
		ground = (int)t.uheights[0]+32;
		batch = new SpriteBatch();
		grass = new Texture("pixel.png");
		slime = new Texture("slime.png");
		slimeX = 0;
		slimeY = ground;
		stateOfSlime = 0;
		speedOfSlime = 0;
		acceleration = 2;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		forward = true;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0.5f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < 800; i+= 32) {
			for (int y = (int)t.uheights[i/32]; y >= 0; y-=32)
				batch.draw(grass, i, y);
		}
		batch.draw(slime, slimeX, slimeY,32,32);
		batch.end();
		double leftHeight = t.uheights[slimeX/32];
		double rightHeight = t.uheights[Math.min(slimeX/32 + 1, t.uheights.length-1)];
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			forward = false;
			if (slimeY > Math.max(leftHeight, rightHeight)+32 && stateOfSlime == 0){
				stateOfSlime = -1;
			}
			if (slimeY-32 >= leftHeight) {
				slimeX -= 200 * Gdx.graphics.getDeltaTime();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			forward = true;
			System.out.println("left: " + slimeY + " right: " + rightHeight);
			if (slimeY > Math.max(leftHeight, rightHeight)+32 && stateOfSlime == 0){
				stateOfSlime = -1;
			}
			if (slimeY-32 >= rightHeight) {
				slimeX += 200 * Gdx.graphics.getDeltaTime();
			}

		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			if (stateOfSlime == 0) {
				stateOfSlime = 1;
				speedOfSlime = 16;
			}
		}
		if (stateOfSlime == 1){
			slimeY += speedOfSlime;
			speedOfSlime-= acceleration;
			if (speedOfSlime <= 0){
				stateOfSlime = -1;
				speedOfSlime = 0;
			}
		}
		else if(stateOfSlime == -1){
			slimeY -= speedOfSlime;
			speedOfSlime += acceleration;
			if (forward) {
				if (slimeY <= (int) Math.max(leftHeight, rightHeight) + 32 && lastSlimeY > (int) Math.max(leftHeight, rightHeight) + 32) {
					stateOfSlime = 0;
					speedOfSlime = 0;
					slimeY = (int) Math.max(leftHeight, rightHeight) + 32;
				} else if (lastSlimeY <= (int) Math.max(leftHeight, rightHeight) + 32 && slimeY <= leftHeight + 32) {
					stateOfSlime = 0;
					speedOfSlime = 0;
					slimeY = (int) leftHeight + 32;
					slimeX = (slimeX / 32) * 32;
				}
			}
			else{
				if (slimeY <= (int) Math.max(leftHeight, rightHeight) + 32 && lastSlimeY > (int) Math.max(leftHeight, rightHeight) + 32) {
					stateOfSlime = 0;
					speedOfSlime = 0;
					slimeY = (int) Math.max(leftHeight, rightHeight) + 32;
				} else if (lastSlimeY <= (int) Math.max(leftHeight, rightHeight) + 32 && slimeY <= rightHeight + 32) {
					stateOfSlime = 0;
					speedOfSlime = 0;
					slimeY = (int) rightHeight + 32;
					slimeX = (slimeX / 32 + 1) * 32;
				}
			}
			lastSlimeY = slimeY;
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		grass.dispose();
		slime.dispose();
	}
}
