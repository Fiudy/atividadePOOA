package br.com.mariojp.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {
    private JButton startButton;
    private JButton exitButton;
    private Principal principal;
    private Image backgroundImage;

    public MenuPanel(Principal principal, String imagePath) {
        this.principal = principal;
        initBackground(imagePath);
        initMenu();
    }

    private void initBackground(String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public void showMenu() {
        initMenu();
    }

    private void initMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        startButton = new JButton("Começar");
        exitButton = new JButton("Sair");

        startButton.setPreferredSize(new Dimension(200, 70));
        exitButton.setPreferredSize(new Dimension(200, 70));

        // Adiciona hover nos botões
        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                startButton.setBackground(new Color(153, 255, 153)); // Verde suave
            }

            public void mouseExited(MouseEvent evt) {
                startButton.setBackground(UIManager.getColor("control"));
            }
        });

        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                exitButton.setBackground(new Color(255, 102, 102)); // Vermelho suave
            }

            public void mouseExited(MouseEvent evt) {
                exitButton.setBackground(UIManager.getColor("control"));
            }
        });

        startButton.addActionListener(e -> principal.startGame());
        exitButton.addActionListener(e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(startButton, gbc);

        gbc.gridy = 1;
        add(exitButton, gbc);
    }
}