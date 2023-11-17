package br.com.mariojp.game;

import javax.swing.*;
import java.awt.*;

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

    private void initMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        startButton = new JButton("Start");
        exitButton = new JButton("Exit");

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