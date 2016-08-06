package com.aut.alij.mariobros.sprites.items;

import com.aut.alij.mariobros.MarioBros;
import com.aut.alij.mariobros.screens.PlayScreen;
import com.aut.alij.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ali J on 8/6/2016.
 */
public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(),getY(),16/ MarioBros.PPM,16/MarioBros.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void useItem(Mario mario);

    public void update(float dt){
        if (toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void destroy(){
        toDestroy = true;
    }

    public void reverseVelocity(boolean x,boolean y){
        if (x)
            velocity.x *= -1;
        if (y)
            velocity.y *= -1;
    }

    public void draw(Batch batch){
        if (!destroyed)
            super.draw(batch);
    }
}
