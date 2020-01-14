package com.jovac.studentdrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyStudentDrop implements Screen {

	MainGame game;

	private Texture dropImage;
	private Texture bucketImage;

	private Sound dropSound, gameOver;
	private Music rainMusic;
	private BitmapFont font;

	private OrthographicCamera camera;

	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;

	private int countSpeed;
	private double speed = 200;
	static int countPoints;
	private int lifes = 0;



	public MyStudentDrop(MainGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		/*
			Cargamos los assets que hemos guardado como una nueva textura.
		*/
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		/*
			Cargamos los sonidos y la musica.
		*/
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		gameOver = Gdx.audio.newSound(Gdx.files.internal("gameOver.mp3"));

		/*
			Cargamos el archivo que contiene la fuente, es una imagen y el formato de la fuente
		 */
		font = new BitmapFont(Gdx.files.internal("ravie.fnt"), Gdx.files.internal("ravie.png"), false);
		countPoints = 0;

		/*
		   Seteamos la musica del rain, al ponre setLooping hacemos que se reproduzca continuamente
		   Luego le damos play para que comience.
		*/
		rainMusic.setLooping(true);
		rainMusic.play();

		/*
			Creo una camara para la orientación, y le pongo false para que me salga el cubo abajo
			si no la orientacion me cambiaria y saldria el cubo arriba.
		*/
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);


		/*
			Hago un SpritBatch para poder poner los objetos en este caso el cubo donde quiero que este.
		 */
		game.batch = new SpriteBatch();

		/*
			Creo un rectangulo de la foto del cubo le pongo posicion donde debe estar
			y le pongo una dimesion.
		 */
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();

		/*
			Le añade un rectanculo a la gota
		 */
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render (float delta) {

		/*
			Color de fondo del juego
		 */
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/*
			Pongo la camara.
		 */
		camera.update();

		/*
			Renderizo los sprits de la camara y el sprite del Bucket con su Bucket x, y
		 */
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		font.draw(game.batch, "Points: " + countPoints, 0, 480);
		font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 625, 480);
		game.batch.end();



		/*
			Este metodo mueve el cubo cuando presionas sobre la pantalla con el raton.
		 */
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		/*
			Metodos para mover el bucket con las flechas de direcion
		 */
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		/*
			Limites para que el cubo no se salga de la pantalla x
		 */
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		/*
			Tiempo hasta que aparece una nueva gota y llama otra vez al metodo despues de ese tiempo.
		 */
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		/*
			For para que la gota se mueva hacia abajo y si llega la parte de abajo se borra
		 */
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= speed * Gdx.graphics.getDeltaTime();
			/*
				Si se cae fuera se borra, es decir el final de la camara de y
			 */
			if(raindrop.y + 64 < 0){
				gameOver.play();
				iter.remove();
				game.setScreen(new GameOverScreen(game));
			}

			if(lifes > 0){

			}

			/*
				Si la gota cae dentro del cubo hace el sonido y se borra
				 mira que si la gota esta mas baja que la altura del cubo no cogera la gota
			 */
			if(raindrop.overlaps(bucket) && raindrop.y >= 70) {
				dropSound.play();
				countSpeed = countSpeed + 1;
				countPoints = countPoints  + 1;
				iter.remove();
			}

			if(countSpeed == 10){
				speed = speed * 1.5;
				countSpeed = 0;
			}
		}

		/*
			Aqui es para que se vea la gota definida en el metodo private spawnRaindrop
		 */
		game.batch.begin();
		for(Rectangle raindrop: raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		game.batch.dispose();
	}

	/*
		Este metodo hace que la gota aparezca de forma aletaoria en un punto x de juego.
	 */
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
}
