package br.com.mariojp.game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

public class Nave extends Sprite {

    private static Nave instance; 
    private int dx;
    private int dy;

    private long lastFire;
    private static final long FIRE_COOLDOWN = 500;

    private int alcance;

    private ArrayList<Missil> missiles;

    private Nave(int x, int y, int alcance) {
        super(x, y);
        this.alcance = alcance;
        initNave();
    }

    private void initNave() {
        missiles = new ArrayList<Missil>();
        carregarImagem("/imagens/nave.png");
        getImageDimensions();
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public Image getImage() {
        return image;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            atira();
        }
        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
        }
    }

    private void atira() {
        long now = System.currentTimeMillis();

        // Se o tempo entre o último tiro e o atual for maior que o tempo de recarga, não atira
        if (now - lastFire >= FIRE_COOLDOWN) {
            EfeitoSonoroDisparo();
            missiles.add(new Missil(x + width, y + height / 2, alcance));
            lastFire = now; 
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    public ArrayList<Missil> getMissiles() {
        return missiles;
    }

    public static synchronized Nave getInstance(int x, int y, int alcance) {
        if (instance == null) {
            instance = new Nave(x, y, alcance);
        }
        return instance;
    }

    public void setX(int i) {
    }

    public void ajustarVelocidade(int i) {
    }

    // Efeito sonoro do disparo
    private void EfeitoSonoroDisparo() {
        try {
            File audioFile = new File("src\\main\\resources\\imagens\\disparo.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
