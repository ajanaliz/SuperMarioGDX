package com.aut.alij.mariobros.items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ali J on 8/6/2016.
 */
public class ItemDef {

    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position,Class<?> type){
        this.position = position;
        this.type = type;
    }
}
