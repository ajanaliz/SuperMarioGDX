package com.aut.alij.mariobros.sprites;

import com.aut.alij.mariobros.MarioBros;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Ali J on 8/2/2016.
 */
public class Mario extends Sprite{
    private World world;
    private Body body;

    public Mario(World world){
        this.world = world;
        defineMario();
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32/ MarioBros.PPM,32/ MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/ MarioBros.PPM);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
