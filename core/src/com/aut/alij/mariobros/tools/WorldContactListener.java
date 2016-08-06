package com.aut.alij.mariobros.tools;

import com.aut.alij.mariobros.MarioBros;
import com.aut.alij.mariobros.items.Item;
import com.aut.alij.mariobros.sprites.Enemy;
import com.aut.alij.mariobros.sprites.InteractiveTileObject;
import com.aut.alij.mariobros.sprites.Mario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Ali J on 8/3/2016.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;



        switch (collisionDef){
            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
                if (fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit();
                else ((Mario) fixB.getUserData()).hit();
                break;
            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).useItem((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).useItem((Mario) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
