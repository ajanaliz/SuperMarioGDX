package com.aut.alij.mariobros.sprites;

import com.aut.alij.mariobros.MarioBros;
import com.aut.alij.mariobros.screens.PlayScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Ali J on 8/3/2016.
 */
public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy, destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setToDestroy = false;
        destroyed = false;
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
    }

    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        }else if (!destroyed){
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    public void draw(Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
        head.set(vertices);
        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead() {
        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();
        setToDestroy(true);
    }

    public void setToDestroy(boolean setToDestroy) {
        this.setToDestroy = setToDestroy;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
