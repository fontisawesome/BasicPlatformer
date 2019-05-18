package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slime {
    int slimeX;
    int slimeY;
    int stateOfSlime;
    int speedOfSlime;
    int acceleration;
    double bounciness;
    Texture slime;
    public Slime(int sX, int sY, int state, int speed, int accel, Texture img, double b){
        slimeX = sX;
        slimeY = sY;
        stateOfSlime = state;
        speedOfSlime = speed;
        acceleration = accel;
        slime = img;
        bounciness = b;
    }

}
