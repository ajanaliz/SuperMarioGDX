package com.aut.alij.mariobros.screens;

import com.aut.alij.mariobros.MarioBros;
import com.aut.alij.mariobros.items.Item;
import com.aut.alij.mariobros.items.ItemDef;
import com.aut.alij.mariobros.items.Mushroom;
import com.aut.alij.mariobros.scenes.HUD;
import com.aut.alij.mariobros.sprites.Enemy;
import com.aut.alij.mariobros.sprites.Mario;
import com.aut.alij.mariobros.tools.B2WorldCreator;
import com.aut.alij.mariobros.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ali J on 8/1/2016.
 */
public class PlayScreen implements Screen {

    private MarioBros game;
    private HUD hud;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Mario mario;
    private Music music;
    private B2WorldCreator creator;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(MarioBros game) {
        this.game = game;
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
        hud = new HUD(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
        mario = new Mario(this);
        world.setContactListener(new WorldContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public void spawnItem(ItemDef itemDef) {
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();
            if (itemDef.type == Mushroom.class) {
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public void handleInput(float dt) {
        if (mario.getCurrentState() != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                mario.getBody().applyLinearImpulse(new Vector2(0, 4f), mario.getBody().getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.getBody().getLinearVelocity().x <= 2)
                mario.getBody().applyLinearImpulse(new Vector2(0.1f, 0), mario.getBody().getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.getBody().getLinearVelocity().x >= -2)
                mario.getBody().applyLinearImpulse(new Vector2(-0.1f, 0), mario.getBody().getWorldCenter(), true);
        }

    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);
        mario.update(dt);
        for (Enemy enemy : creator.getGoombas()) {
            enemy.update(dt);
            if (enemy.getX() < mario.getX() + 224 / MarioBros.PPM)
                enemy.getBody().setActive(true);
        }
        for (Item item : items) {
            item.update(dt);
        }
        hud.update(dt);
        if (mario.getCurrentState() != Mario.State.DEAD)
            gameCam.position.x = mario.getBody().getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        box2DDebugRenderer.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        mario.draw(game.batch);
        for (Enemy enemy : creator.getGoombas())
            enemy.draw(game.batch);
        for (Item item : items) {
            item.draw(game.batch);
        }
        game.batch.end();
        hud.stage.draw();
        if (gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        if (mario.getCurrentState() == Mario.State.DEAD && mario.getStateTimer() > 3)
            return true;
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        hud.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }
}
