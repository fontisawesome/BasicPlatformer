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
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.Time;
import java.util.ArrayList;

public class ISPTesting extends ApplicationAdapter {
	private World world;
	private Box2DDebugRenderer debug;
	private OrthographicCamera camera;
	private Body slime;
	private Body ground;
	private Fixture ballFix;
	private float maximumVelocity;
	private float maximumVelocityY;
	private long lastBallNano;
	private Body ball;
	private Fixture slimeFix;
	private ArrayList<Body> coins;
	private Terrain t;
	private boolean touchingGround;
	private Fixture groundFix;
	private int touchingFaces = 0;
	@Override
	public void resize(int width, int height){
		camera.viewportHeight = height/40;
		camera.viewportWidth = width/40;
		camera.update();
	}
	public void spawnCoin(){

	}
	public void spawnBall(){
		float x = (float)(Math.random());
		float size = (float)(Math.random()*2+0.5);
		BodyDef ballDef = new BodyDef();
		ballDef.type = BodyDef.BodyType.DynamicBody;
		ballDef.position.set(slime.getPosition().x+x,6);
		CircleShape circle = new CircleShape();
		circle.setRadius(size);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 10;
		fixtureDef.friction = 0.33f;
		fixtureDef.restitution = 0.1f;
		ball = world.createBody(ballDef);
		ballFix = ball.createFixture(fixtureDef);
		ball.applyLinearImpulse(-1,-15,ball.getPosition().x, ball.getPosition().y, true);
	}
	@Override
	public void create () {
		maximumVelocity = 10;
		maximumVelocityY = 5;
		t = new Terrain();
		System.out.println(t.uheights[0]);
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

		//Shape creations
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f,0.5f);
		ChainShape groundShape = new ChainShape();
		int count = 0;

        for (int i = 0; i < 40; i+=2) {
			if (i != 0 && t.uheights[i/2] == t.uheights[i/2-1]){
				count++;
			}
        }
		Vector2 [] vertices = new Vector2[40-count];
        int index = 0;
		for (int i = 0; i < 40; i+=2) {
			if (index != 0 && t.uheights[i/2] == t.uheights[i/2-1]){
				vertices[index] = new Vector2(i/2-9, (float)t.uheights[i/2]-5);
				index++;
			}
			else {
				vertices[index] = new Vector2(i / 2 - 10, (float) t.uheights[i / 2]-5);
				vertices[index+1] = new Vector2(i/2-9, (float)t.uheights[i/2]-5);
				index+=2;
			}
			System.out.println(index);

		}
		for (int i = 0; i < vertices.length; i++){
			System.out.println(vertices[i].x + " " + vertices[i].y);
		}
        groundShape.createChain(vertices);

		//listener
		world.setContactListener(new ListenerClass(){
				@Override
				public void endContact(Contact contact) {
					if (contact.getFixtureA().equals(groundFix) && contact.getFixtureB().equals(slimeFix)){
						touchingFaces--;
					}
				}

				@Override
				public void beginContact(Contact contact) {
					if (contact.getFixtureA().equals(groundFix) && contact.getFixtureB().equals(slimeFix)){
						touchingFaces++;
					}
				}
		});

		//fixture creations
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0f;
		slime = world.createBody(slimeDef);
		slimeFix = slime.createFixture(fixtureDef);
		FixtureDef groundFixDef = new FixtureDef();
		groundFixDef.shape = groundShape;
		groundFixDef.friction = 0.2f;
		groundFixDef.restitution = 0.3f;
		ground = world.createBody(groundDef);
		groundFix = ground.createFixture(groundFixDef);
		shape.dispose();
		groundShape.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		debug.render(world, camera.combined);
		touchingGround = touchingFaces > 0 && touchingFaces<=2;
		if (Gdx.input.isKeyPressed(Input.Keys.D) && slime.getLinearVelocity().x < maximumVelocity) {
		    if (!touchingGround){
                if (slime.getLinearVelocity().x < (maximumVelocity/3))
                    slime.applyLinearImpulse(0.5f, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
		    else {
                slime.applyLinearImpulse(1, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)&& slime.getLinearVelocity().x > -1*maximumVelocity) {
            if (!touchingGround){
                if (slime.getLinearVelocity().x > -1*(maximumVelocity/3))
                    slime.applyLinearImpulse(-0.5f, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
            else {
                slime.applyLinearImpulse(-1, 0, slime.getPosition().x, slime.getPosition().y, true);
            }
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (touchingGround && slime.getLinearVelocity().y < maximumVelocityY){
				slime.applyLinearImpulse(0,3, slime.getPosition().x, slime.getPosition().y, true);
			}
		}

//		if (TimeUtils.nanoTime()-lastBallNano > 2000000000){
//			if (ball != null)
//				world.destroyBody(ball);
//			spawnBall();
//			lastBallNano = TimeUtils.nanoTime();
//		}
		if (slime.getPosition().y < -10){
			hide();
		}
		System.out.println(touchingFaces);
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
