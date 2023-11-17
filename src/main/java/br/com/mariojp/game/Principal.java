package br.com.mariojp.game;
import java.awt.CardLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Principal extends JFrame {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Jogo gamePanel;

    public Principal() {
        initUI();
    }

    private void initUI() {
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        gamePanel = new Jogo(); 
        MenuPanel menuPanel = new MenuPanel(this, "/imagens/galaxia.jpg");
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        add(mainPanel);
        setResizable(false);
        pack();
        setTitle("Pit e suas aventuras espaciais");    
        setIconImage(loadIconImage("/imagens/icone.png"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

     private Image loadIconImage(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        return icon.getImage();
    }

    public void startGame() {
        cardLayout.show(mainPanel, "Game");
        gamePanel.startGame(); // Inicia o jogo
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame ex = new Principal();
            ex.setVisible(true);
        });
    }
}