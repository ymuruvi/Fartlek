package com.nnldev.fartlek.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.nnldev.fartlek.Fartlek;
import com.nnldev.fartlek.essentials.Animation;

import java.util.ArrayList;

/**
 * Nano, Nick
 */
public class Runner {
    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private Rectangle[] rectangle;//hitboxes
    private Animation playerAnimation;
    private int health;
    private float horizontalDeceleration = 0.5f;
    private float horizontalSpeed;
    private final int RUNNER_Y = 160;
    private final float ANIM_CYCLE_TIME = 0.25f;
    private Sound moveSound;
    private float soundTimer;
    private boolean soundPlayable;

    public Runner() {
        velocity = new Vector3(0, 0, 0);
        soundTimer = 0;
        soundPlayable = false;
        moveSound = Gdx.audio.newSound(Gdx.files.internal("Sounds\\movesound1.ogg"));
        health = 100;
        horizontalSpeed = 9;
    }

    /**
     * Makes a new runner
     *
     * @param path       The path for the runner's pic
     * @param animFrames The number of frames in the picture t oallow for animation of
     *                   the runner
     */
    public Runner(String path, int animFrames,Rectangle[] rectangles) {
        this();
        texture = new Texture(path);
        position = new Vector3(((Fartlek.WIDTH / 2) - ((texture.getWidth() / animFrames) / 2)), RUNNER_Y, 0);
        rectangle = rectangles;//rectangles are made in the playstate
        //rectangle = new Rectangle(position.x, position.y, texture.getWidth() / animFrames, texture.getHeight());
        playerAnimation = new Animation(new TextureRegion(texture), animFrames, ANIM_CYCLE_TIME);
    }

    /**
     * Plays the move sound
     */
    private void playMoveSound() {
        if (soundPlayable) {
            moveSound.stop();
            moveSound.play(0.1f);
            soundPlayable = false;
        }

    }

    /**
     * Updates the runner
     *
     * @param dt The change in time since the last time the runner's position
     *           was updated
     */
    public void update(float dt) {
        soundTimer += dt;
        if (soundTimer >= 0.3f) {
            soundTimer = 0;
            soundPlayable = true;
        }
        playerAnimation.update(dt);
        if (velocity.x > 0) {
            velocity.x -= horizontalDeceleration;
        } else if (velocity.x < 0) {
            velocity.x += horizontalDeceleration;
        } else {
            velocity.x = 0;
        }
        position.x += velocity.x;
        position.y += velocity.y;
        //changes all rectangles locations
        rectangle[0].y = position.y+22;//22 is about a third of the runner size
        rectangle[0].x = position.x;
        rectangle[1].y = position.y;
        rectangle[1].x = position.x+22;
        if (position.x < 0)
            position.x = 0;
            //problem here, hitbox keeps moving
        if (position.x + rectangle[0].getWidth() > Fartlek.WIDTH)
            position.x = Fartlek.WIDTH - rectangle[0].getWidth();
    }

    /**
     * Moves the character left
     */
    public void left() {
        playMoveSound();
        velocity.x = -horizontalSpeed;
    }

    /**
     * Moves the character right
     */
    public void right() {
        playMoveSound();
        velocity.x = horizontalSpeed;
    }

    /**
     * Returns the rectangle hitbox of the character
     *
     * @return
     */
    public Rectangle[] getRectangle() {
        return rectangle;
    }

    /**
     * Sets the rectangle hitbox of the character
     *
     * @param rectangle
     */
    public void setRectangle(Rectangle rectangle[]) {
        this.rectangle = rectangle;
    }

    /**
     * Gets the position of the character
     *
     * @return
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Sets the position of the runner
     *
     * @param position The vector3 position of where the runner will be drawn
     */
    public void setPosition(Vector3 position) {
        this.position = position;
        for(Rectangle rect: rectangle){
            rect.setPosition(position.x, position.y);
        }
    }

    /**
     * Returns the current texture for the player.
     *
     * @return
     */
    public TextureRegion getTexture() {
        return playerAnimation.getFrame();
    }

    /**
     * Sets the texture for the animations to somethign new
     *
     * @param texture
     */
    public void setTexture(Texture texture, int animFrames) {
        setPlayerAnimation(new Animation(new TextureRegion(texture), animFrames, ANIM_CYCLE_TIME));
    }

    /**
     * Gets the player animation
     *
     * @return
     */
    public Animation getPlayerAnimation() {
        return playerAnimation;
    }

    /**
     * Sets the player animation
     *
     * @param playerAnimation
     */
    public void setPlayerAnimation(Animation playerAnimation) {
        this.playerAnimation = playerAnimation;
    }

    /**
     * Adds a new bullet to the bullet timer
     */
    public void shoot() {
    }

    public void dispose() {
        texture.dispose();
        moveSound.dispose();
    }
}
