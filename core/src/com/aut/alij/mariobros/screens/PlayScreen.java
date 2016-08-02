package com.aut.alij.mariobros.screens;

import com.aut.alij.mariobros.MarioBros;
import com.aut.alij.mariobros.scenes.HUD;
import com.aut.alij.mariobros.sprites.Mario;
import com.aut.alij.mariobros.tools.B2WorldCreator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Ali J on 8/1/2016.
 */
public class PlayScreen implements Screen {

    private MarioBros game;
    private HUD hud;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Mario mario;

    public PlayScreen(MarioBros game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
        hud = new HUD(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
        mario = new Mario(world);
        box2DDebugRenderer = new Box2DDebugRenderer();
        new B2WorldCreator(world,map);
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            mario.getBody().applyLinearImpulse(new Vector2(0,4f),mario.getBody().getWorldCenter(),true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.getBody().getLinearVelocity().x <= 2)
            mario.getBody().applyLinearImpulse(new Vector2(0.1f,0),mario.getBody().getWorldCenter(),true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.getBody().getLinearVelocity().x >= -2)
            mario.getBody().applyLinearImpulse(new Vector2(-0.1f,0),mario.getBody().getWorldCenter(),true);

    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);
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
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
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
}
