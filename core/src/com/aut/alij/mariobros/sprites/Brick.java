package com.aut.alij.mariobros.sprites;

import com.aut.alij.mariobros.MarioBros;
import com.aut.alij.mariobros.scenes.HUD;
import com.aut.alij.mariobros.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

/**
 * Created by Ali J on 8/2/2016.
 */
public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (mario.isBig()){
            Gdx.app.log("Brick", "Collision");
            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            HUD.addScore(200);
            MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }else MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }
}
