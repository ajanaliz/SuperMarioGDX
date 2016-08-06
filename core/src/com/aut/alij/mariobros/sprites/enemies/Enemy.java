package com.aut.alij.mariobros.sprites.enemies;

import com.aut.alij.mariobros.screens.PlayScreen;
import com.aut.alij.mariobros.sprites.Mario;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ali J on 8/3/2016.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body body;
    protected Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        body.setActive(false);
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x *= -1;
        if (y)
            velocity.y *= -1;
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead(Mario mario);

    public abstract void onEnemyHit(Enemy enemy);

    public abstract void update(float dt);

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public PlayScreen getScreen() {
        return screen;
    }

    public void setScreen(PlayScreen screen) {
        this.screen = screen;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
