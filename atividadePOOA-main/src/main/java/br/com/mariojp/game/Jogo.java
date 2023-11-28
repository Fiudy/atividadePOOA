package br.com.mariojp.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Jogo extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private Timer timer;
    private Nave nave;
    private int score = 0;
    private ArrayList<Inimigo> inimigos = new ArrayList<Inimigo>();
    private Random random = new Random();
    private boolean endgame = false;
    private final int DELAY = 10;
    private final int B_WIDTH = 800;
    private boolean isPaused = false;
    private final int B_HEIGHT = 600;
    private JButton returnButton; // Botão de retorno
    private JLabel mensagemLabel;
    private Timer timer2;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private int timeElapsed;

    public Jogo() {
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    isPaused = !isPaused; // Inverte o estado de pausa quando ESC é pressionado
                    repaint(); // Força uma atualização da tela
                }
            }
        });
        

        returnButton = new JButton("Retornar");
        returnButton.setBounds(10, 60, 100, 30);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGame();
                startGame(); 
                returnButton.setVisible(false); 
                nave.y = 90;
                nave.x = 10;
                
  
            }
        });
        returnButton.setVisible(false);
        add(returnButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            stopGame();
            updateNave();
            updateMissiles();
            updateInimigo();
            checkCollisions();
            repaint();
        }
    }

    public void initGame() {
        if (timerLabel != null) {
            remove(timerLabel);
        }
        if (timer2 != null) {
            timer2.stop();
        }

        if (scoreLabel != null) {
            remove(scoreLabel);
        }

        timerLabel = new JLabel();
        timerLabel.setBounds(15, 20, 200, 30);
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel);

        score = 0;
        scoreLabel = new JLabel();
        scoreLabel.setText("Pontos: " + score);
        add(scoreLabel);
        scoreLabel.setBounds(15, 5, 200, 30); // Ajuste a posição e o tamanho conforme necessário
        scoreLabel.setForeground(Color.WHITE);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setLayout(null); // Desativar o layout para posicionar livremente os componentes
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setDoubleBuffered(true);


        nave = Nave.getInstance(40, 60, B_WIDTH);

        mensagemLabel = new JLabel();
        mensagemLabel.setBounds(300, 250, 200, 30);
        mensagemLabel.setForeground(Color.WHITE);
        add(mensagemLabel);

        timer = new Timer(DELAY, this);
        timer.start();
        timeElapsed = 0;
        timerLabel = new JLabel();
        timerLabel.setBounds(15, 20, 200, 30);
        timerLabel.setForeground(Color.WHITE);
        add(timerLabel);

        // Timer para atualizar o tempo decorrido
        timer2 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!endgame) {
                    if (!isPaused) {
                        timeElapsed++; 
                    }
                }
                timerLabel.setText("Tempo: " + timeElapsed + " segundos");
            }
        });
        timer2.start();
    }

    public void startGame() {
        inimigos.clear();
        score = 0;
        endgame = false;
        isPaused = false;
        initGame();
        requestFocusInWindow();
        timeElapsed = 0;
        timer2.restart();
    }

    // Método para atualizar a pontuação
    public void updateScore() {
        scoreLabel.setText("Pontos: " + score);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        isPaused = false;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, B_HEIGHT / 2);
        returnButton.setVisible(true); // Torna o botão visível ao final do jogo
       
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!endgame) {
            desenhar(g);
            if (isPaused) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("Jogo pausado")) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString("Pausado", x, y);
            }
        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void desenhar(Graphics g) {
        g.drawImage(nave.getImage(), nave.getX(), nave.getY(), this);

        for (Missil m : nave.getMissiles()) {
            if (m.isVisible()) {
                g.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
        }
        for (Inimigo i : inimigos) {
            if (i.isVisible()) {
                g.drawImage(i.getImage(), i.getX(), i.getY(), this);
            }
        }

    }

    private void updateMissiles() {
        ArrayList<?> ms = nave.getMissiles();
        for (int i = 0; i < ms.size(); i++) {
            Missil m = (Missil) ms.get(i);
            if (m.isVisible()) {
                m.move();
            } else {
                ms.remove(i);
            }
        }
    }

    public void checkCollisions() {
        Rectangle r3 = nave.getBounds();
        for (Inimigo alien : inimigos) {
            Rectangle r2 = alien.getBounds();
            if (r3.intersects(r2)) {
                nave.setVisible(false);
                alien.setVisible(false);
                endgame = true;
            }
        }
        ArrayList<Missil> ms = nave.getMissiles();
        for (Missil m : ms) {
            Rectangle r1 = m.getBounds();
            for (Inimigo alien : inimigos) {
                Rectangle r2 = alien.getBounds();
                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    alien.setVisible(false);
                    score++;
                    updateScore();
                    if (score > 20) {
                        endgame = true;
                        exibirMensagemParabens();
                    }
                }
            }
        }
    }


    private void exibirMensagemParabens() {
        mensagemLabel.setText("    Parabéns! Você ganhou!");
    }

    private void updateInimigo() {
        while (inimigos.size() < 5) {
            inimigos.add(new Inimigo(B_WIDTH, random.nextInt(B_HEIGHT - 20) + 10));
        }

        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo inimigo = inimigos.get(i);
            if (inimigo.isVisible()) {
                inimigo.move();
            } else {
                inimigos.remove(inimigo);
            }
        }

    }

    private void stopGame() {
        if (endgame) {
            timer.stop();
        }
    }

    private void updateNave() {
        nave.move();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            nave.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            nave.keyPressed(e);
        }
    }
}