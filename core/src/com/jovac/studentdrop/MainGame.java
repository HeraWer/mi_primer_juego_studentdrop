package com.jovac.studentdrop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {

    /*
        Para represetar texturas
     */
    public SpriteBatch batch;

    /*
        Para representar textos en pantalla
     */
    public BitmapFont font;

    @Override
    public void create() {

        batch = new SpriteBatch();
        // Usamos LibGDX arial font por defecto
        font = new BitmapFont();
        this.setScreen(new MenuGame(this));
    }

    @Override
    public void render() {
        super.render(); // ES IMPORTANTE
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
