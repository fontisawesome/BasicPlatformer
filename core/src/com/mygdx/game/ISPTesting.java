package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.org.apache.xpath.internal.operations.Or;

public class ISPTesting extends ApplicationAdapter {
	private World world;
	private Box2DDebugRenderer debug;
	private OrthographicCamera camera;
	private Body slime;
	private Body ground;
	private float maximumVelocity;
	@Override
	public void resize(int width, int height){
		camera.viewportHeight = height/40;
		camera.viewportWidth = width/40;
		camera.update();
	}
	@Override
	public void create () {
		maximumVelocity = 10;
		world = new World(new Vector2(0, -10), true);
		debug = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/40, Gdx.graphics.getHeight()/40);

		//body creations
		BodyDef slimeDef = new BodyDef();
		slimeDef.type = BodyDef.BodyType.DynamicBody;
		slimeDef.position.set(0,1);
		BodyDef groundDef = new BodyDef();
		groundDef.type = BodyDef.BodyType.StaticBody;
		groundDef.position.set(0,0);

		//slime shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f,0.5f);
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[] {new Vector2(-500,0), new Vector2(500,0)});


		//fixture creations
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.33f;
		fixtureDef.restitution = 0f;
		slime = world.createBody(slimeDef);
		Fixture slimeFix = slime.createFixture(fixtureDef);
		FixtureDef groundFixDef = new FixtureDef();
		groundFixDef.shape = groundShape;
		groundFixDef.friction = 0.5f;
		groundFixDef.restitution = 0.3f;
		ground = world.createBody(groundDef);
		Fixture groundFix = ground.createFixture(groundFixDef);


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		debug.render(world, camera.combined);
		if (Gdx.input.isKeyPressed(Input.Keys.D) && slime.getLinearVelocity().x < maximumVelocity) {
		    if (Math.abs(slime.getPosition().y-0.5f) >= 0.5f){
                if (slime.getLinearVelocity().x < (maximumVelocity/2))
                    slime.applyLinearImpulse(0.5f, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
		    else {
                slime.applyLinearImpulse(1, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)&& slime.getLinearVelocity().x > -1*maximumVelocity) {
            if (Math.abs(slime.getPosition().y-0.5f) >= 0.5f){
                if (slime.getLinearVelocity().x > -1*(maximumVelocity/2))
                    slime.applyLinearImpulse(-0.5f, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
            else {
                slime.applyLinearImpulse(-1, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (Math.abs(slime.getPosition().y-0.5f) < 0.02){
				slime.applyLinearImpulse(0,10, slime.getPosition().x, slime.getPosition().y, true);
			}
		}
		System.out.println(slime.getPosition().x + " " + slime.getPosition().y);
		world.step(1/60f, 8, 3);
	}
	public void hide(){
		dispose();
	}
	@Override
	public void dispose () {
		world.dispose();
		debug.dispose();
	}
}
