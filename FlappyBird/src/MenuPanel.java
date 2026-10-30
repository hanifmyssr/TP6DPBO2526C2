import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {
    private BufferedImage backgroundImage, baseImage, logoImage, birdImage;

    public MenuPanel(Runnable onStart) {
        setPreferredSize(new Dimension(360, 640));
        setLayout(null);

        backgroundImage = Assets.scale(Assets.load("assets/background-day.png"), 360, 640);
        baseImage = Assets.scale(Assets.load("assets/base.png"), 420, 140);
        // logo "FlappyBird" = bagian atas message.png (baris 0-47)
        logoImage = Assets.scale(Assets.load("assets/message.png").getSubimage(0, 0, 184, 48), Assets.SCALE);
        birdImage = Assets.scale(Assets.load("assets/yellowbird-midflap.png"), 43, 30);

        PixelButton startButton = new PixelButton("Mulai Main", new Color(100, 185, 40));
        PixelButton exitButton = new PixelButton("Keluar", new Color(232, 106, 36));
        startButton.setBounds(100, 330, 160, 46);
        exitButton.setBounds(100, 395, 160, 46);
        startButton.addActionListener(e -> onStart.run());
        exitButton.addActionListener(e -> System.exit(0));
        add(startButton);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
        g.drawImage(baseImage, 0, 500, null);
        g.drawImage(logoImage, (360 - logoImage.getWidth()) / 2, 150, null);
        g.drawImage(birdImage, (360 - birdImage.getWidth()) / 2, 235, null);
    }

    // tombol bergaya pixel (warna & border mengikuti gaya Flappy Bird)
    private static class PixelButton extends JButton {
        private final Color fill;

        PixelButton(String text, Color fill) {
            super(text);
            this.fill = fill;
            setFont(new Font("Arial", Font.BOLD, 20));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int push = getModel().isPressed() ? 2 : 0;

            g2.setColor(new Color(84, 56, 71));
            g2.fillRoundRect(0, 0, w, h, 12, 12);
            g2.setColor(getModel().isPressed() ? fill.darker() : fill);
            g2.fillRoundRect(3, 3, w - 6, h - 6, 9, 9);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (w - fm.stringWidth(getText())) / 2;
            int y = (h + fm.getAscent() - fm.getDescent()) / 2 + push;
            g2.setColor(new Color(84, 56, 71));
            g2.drawString(getText(), x + 2, y + 2);
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }
}
