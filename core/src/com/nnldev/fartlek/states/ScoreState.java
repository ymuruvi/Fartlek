package com.nnldev.fartlek.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.nnldev.fartlek.Fartlek;
import com.nnldev.fartlek.essentials.Button;
import com.nnldev.fartlek.essentials.GameStateManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Nano on 1/20/2016.
 * Screen where all the scores were going to be displayed
 */
public class ScoreState extends State {
    private String scores;
    private Button exitBtn;
    private FreeTypeFontGenerator generator;
    private BitmapFont infoFont;

    /**
     * Creates a new scor view state
     *
     * @param gsm
     * @deprecated No longer needed, the info stat handles ths stuff now
     */
    protected ScoreState(GameStateManager gsm) {
        super(gsm);
        exitBtn = new Button("Buttons\\exitbtn.png", (float) (Fartlek.WIDTH - 30), (float) (Fartlek.HEIGHT - 30), true);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/vp.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter sParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        sParameter.size = 15;
        sParameter.color = Color.WHITE;
        FreeTypeFontGenerator.FreeTypeFontParameter dParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        dParameter.size = 30;
        dParameter.color = Color.WHITE;
        infoFont = generator.generateFont(sParameter);
        try {
            scores = "";
            FileReader fr = new FileReader("Extras&Logo\\scores.txt");
            BufferedReader br = new BufferedReader(fr);
            boolean eof = false;
            String txt;
            int num = 0;
            while (!eof) {
                num++;
                txt = br.readLine();
                if (txt == (null)) {
                    eof = true;
                } else {
                    scores += num + ":" + txt + "\n";
                }
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            if (exitBtn.contains(Fartlek.mousePos.x, Fartlek.mousePos.y)) {
                gsm.push(new MenuState(gsm));
                dispose();
            }
        }
    }

    /**
     * Th update method
     *
     * @param deltaTime The time since the previous update
     */
    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    /**
     * The render method where everything is drawn
     *
     * @param sb All the graphics that will be drawn
     */
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(exitBtn.getTexture(), exitBtn.getPosition().x, exitBtn.getPosition().y);
        infoFont.draw(sb, scores, 0, Fartlek.HEIGHT - 50);
        sb.end();
    }

    /**
     * The method where all useless objects are disposed of
     */
    @Override
    public void dispose() {
        exitBtn.dispose();
        infoFont.dispose();
    }
}
