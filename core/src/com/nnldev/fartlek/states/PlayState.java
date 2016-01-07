/**
 * @author Nano
 * In game Play State for the Fartlek game.
 */
package com.nnldev.fartlek.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.nnldev.fartlek.Fartlek;
import com.nnldev.fartlek.essentials.Button;
import com.nnldev.fartlek.essentials.GameStateManager;
import com.nnldev.fartlek.essentials.TouchSector;
import com.nnldev.fartlek.sprites.Scene;
import com.nnldev.fartlek.sprites.Box;
import com.nnldev.fartlek.sprites.Obstacle;
import com.nnldev.fartlek.sprites.Runner;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayState extends State {
	private Button exitBtn;
	private Runner runner;
	private TouchSector bottomLeft;
	private TouchSector bottomRight;
	private TouchSector bottomMiddle;
	private Music music;
	private ArrayList<Scene> sceneTiles;
	private ArrayList<Obstacle[]> obstacleSet;
	private float obstacleTime, maxObstacleTime = 2.0f;
	private Box emptyBox;
	private String boxTextureName;
	private String tileTextureName;
	private boolean DONE;
	private int tileWidth;
	private int tileHeight;

	/**
	 * Creates a new game state
	 *
	 * @param gsm
	 *            The game state manager which is controlling this state
	 */
	public PlayState(GameStateManager gsm) {
		super(gsm);
		DONE = false;
		emptyBox = new Box("Items\\emptybox.png", 0, 0, 0);
		boxTextureName = "Items\\box.png";
		tileTextureName = "Scene\\bckg1.png";
		exitBtn = new Button("Buttons\\exitbtn.png", (float) (Fartlek.WIDTH - 30), (float) (Fartlek.HEIGHT - 30), true);
		runner = new Runner("Characters\\ship1Anim.png", 3);
		bottomLeft = new TouchSector(0, 0, Fartlek.WIDTH / 3, Fartlek.HEIGHT / 2);
		bottomRight = new TouchSector((2 * Fartlek.WIDTH) / 3, 0, Fartlek.WIDTH / 3, Fartlek.HEIGHT / 2);
		bottomMiddle = new TouchSector(Fartlek.WIDTH / 3, 0, Fartlek.WIDTH / 3, Fartlek.HEIGHT / 2);
		tileWidth = new Texture(tileTextureName).getWidth();
		tileHeight = new Texture(tileTextureName).getHeight();
		sceneTiles = new ArrayList<Scene>();
		for (int i = 0; i < 3; i++) {
			sceneTiles.add(i, new Scene(tileTextureName, 0, i * Fartlek.HEIGHT);
		}
		//creates obstacles (all boxes, for now), similar to creation of scene tiles -L
		/*
		obstacleSet = new ArrayList<Obstacle[]>();
		obstacleSet.add(new Obstacle[Obstacle.OBS_PER_ROW]);
		for (int i = 0; i < obstacleSet.get(0).length; i++) {
			(obstacleSet.get(obstacleSet.size() - 1)[i] = new Box(//boxTextureName, (Fartlek.WIDTH/Obstacle.OBS_PER_ROW) * i, Fartlek.HEIGHT, 100);
		}
		*/
		startMusic("music1.mp3");
	}

	/**
	 * Makes a new row of tiles
	 */
	public void newSceneTile() {
		sceneTiles.add(new Scene(tileTextureName, 0, i * Fartlek.HEIGHT);
	}

	/**
	 * Starts playing a song
	 *
	 * @param song
	 *            The name of the song to play
	 */
	public void startMusic(String song) {
		music = Gdx.audio.newMusic(Gdx.files.internal("Music\\song1.mp3"));
		music.setLooping(false);
		music.setVolume(0.1f);
		if (Fartlek.soundEnabled)
			music.play();
	}

	/**
	 * Handles user input
	 */
	@Override
	protected void handleInput() {
		// If you touched the screen
		if (Gdx.input.justTouched() || Gdx.input.isTouched()) {
			// If the x,y position of the click is in the exit button
			if (exitBtn.getRectangle().contains(Fartlek.mousePos.x, Fartlek.mousePos.y)) {
				gsm.push(new MenuState(gsm));
				DONE = true;
				dispose();
			}
			// If the x,y position of the click is in the bottom left
			if (bottomLeft.getRectangle().contains(Fartlek.mousePos.x, Fartlek.mousePos.y)) {
				runner.left();
			}
			// If the x,y position of the click is in the bottom right
			if (bottomRight.getRectangle().contains(Fartlek.mousePos.x, Fartlek.mousePos.y)) {
				runner.right();
			}
			// If the x,y position of the click is in the bottom middle
			if (bottomMiddle.getRectangle().contains(Fartlek.mousePos.x, Fartlek.mousePos.y)
					&& Gdx.input.justTouched()) {
				runner.shoot();
			}
		}
	}

	/**
	 * Updates the play state and all the information
	 *
	 * @param dt
	 *            The game state manager which organizes which states will be
	 *            shown
	 */
	@Override
	public void update(float dt) {//dt is delta time
		handleInput();
		if (!DONE) {
			runner.update(dt);
			// Loops through all the tiles and updates their positions
			for (int i = 0; i < sceneTiles.size(), i++) {
				sceneTiles.get(i).update();
			}
			if (Scene.remainingTiles < 3) {
				newSceneTile();
			}
			// If the tile goes below 0 then it will dispose of it to
			// avoid memory leaks and save space
			if ((sceneTiles.get(0).getPosition().y + sceneTiles.get(0).getRectangle().height) < 0) {
				// Removes the oldest one
				sceneTiles.remove(0);
			}
			obstacleTime += dt;
			if (obstacleTime >= maxObstacleTime) {
				obstacleTime = 0;
			}
		}
	}

	/**
	 * Renders the graphics to the screen
	 *
	 * @param sb
	 *            The sprite batch which is all the stuff that's going to be
	 *            drawn to the screen.
	 */
	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(Fartlek.cam.combined);
		sb.begin();
		for (Scene[] tileArray : sceneTiles) {
			for (Scene tile : tileArray) {
				sb.draw(tile.getTexture(), tile.getPosition().x, tile.getPosition().y);
			}
		}
		sb.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);
		sb.draw(exitBtn.getTexture(), exitBtn.getPosition().x, exitBtn.getPosition().y);
		sb.end();
	}

	/**
	 * Disposes of objects to avoid memory leaks
	 */
	@Override
	public void dispose() {
		exitBtn.dispose();
		runner.dispose();
		music.dispose();
		for (Scene[] sceneArray : sceneTiles) {
			for (Scene scene : sceneArray)
				scene.dispose();
		}
		for (Obstacle[] obstacleArray : obstacleSet) {
			for (Obstacle ob : obstacleArray)
				Obstacle.dispose();
		}
		sceneTiles.clear();
		obstacleSet.clear();
	}
}
