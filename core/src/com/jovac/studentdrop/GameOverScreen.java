package com.jovac.studentdrop;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameOverScreen implements Screen {

    MainGame game;

    private OrthographicCamera camera;

    private BitmapFont font;
    private Texture wallpaperGameOver, spriteGameOverStart;
    private Sprite photoGameOverRestart;

    public GameOverScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        /*
            Cargamos la camara del juego y le damos unas dimensiones
         */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        /*
            Cargamos todas las fuentes, texturas, sprites que necesitamos.
         */
        font = new BitmapFont(Gdx.files.internal("ravie.fnt"), Gdx.files.internal("ravie.png"), false);
        wallpaperGameOver = new Texture(Gdx.files.internal("wallpaperGameOver.png"));
        spriteGameOverStart = new Texture(Gdx.files.internal("GameOverRestart.png"));
        photoGameOverRestart = new Sprite(spriteGameOverStart);
        photoGameOverRestart.setSize(80, 80);
        photoGameOverRestart.setPosition(500, 200);
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

        /*
            Voy a empezar a printar cosas en el bach
         */
        game.batch.begin();

        /*
            Printo  las texturas y fuentes en el batch
         */
        game.batch.draw(wallpaperGameOver, 0, -400);
        font.draw(game.batch, "Has perdut... ", 50, 450);
        font.draw(game.batch, "Tornar a començar?", 350, 350);
        photoGameOverRestart.draw(game.batch);

        /*
            Voy a dejar de printar cosas en el batch
         */
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 tmp=new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
            camera.unproject(tmp);
            Rectangle textureBounds= new Rectangle(photoGameOverRestart.getX(),photoGameOverRestart.getY(),photoGameOverRestart.getWidth(),photoGameOverRestart.getHeight());
            if(textureBounds.contains(tmp.x,tmp.y)) {
                game.setScreen(new MyStudentDrop(game));
            }
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
