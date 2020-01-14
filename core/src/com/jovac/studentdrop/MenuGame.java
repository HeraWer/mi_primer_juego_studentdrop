package com.jovac.studentdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MenuGame implements Screen {

    MainGame game;

    private OrthographicCamera camera;

    private BitmapFont font;
    private Texture wallpaperMenuGame;

    public MenuGame(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {

         /*
            Ponemos una camara en el juego donde le damos un amcho y un alto
         */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        font = new BitmapFont(Gdx.files.internal("ravie.fnt"), Gdx.files.internal("ravie.png"), false);

        wallpaperMenuGame = new Texture(Gdx.files.internal("fondoMainMenu.png"));
    }

    @Override
    public void render(float delta) {

        /*
            Ponemos el color de fondo
         */
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        /*
            Updateamos la camara
         */
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        /*
            Escribimos en el juego con una fuente y el fondo de pantalla
         */

        game.batch.draw(wallpaperMenuGame, 0, -100);
        font.draw(game.batch, "Benvingut al recull gotes!! ", 50, 150);
        font.draw(game.batch, "Toca la pantalla per continuar!", 50, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MyStudentDrop(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
