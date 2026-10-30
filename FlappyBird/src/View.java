import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class View extends JPanel {

    int width = 360;
    int height = 640;

    private Logic logic;
    private JLabel scoreLabel;

    private BufferedImage backgroundImage;
    private BufferedImage baseImage;
    private BufferedImage gameOverImage;
    private BufferedImage[] digitImages = new BufferedImage[10];

    public View(Logic logic) {
        this.logic = logic;

        backgroundImage = Assets.scale(Assets.load("assets/background-day.png"), width, height);
        baseImage = Assets.scale(Assets.load("assets/base.png"), 420, 140);
        gameOverImage = Assets.scale(Assets.load("assets/gameover.png"), 240, 53);
        for (int i = 0; i < 10; i++) {
            digitImages[i] = Assets.scale(Assets.load("assets/numbers/" + i + ".png"), Assets.SCALE);
        }

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(logic);

        // JLabel skor: isinya gambar angka dari aset
        setLayout(null);
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setBounds(0, 50, width, 45);
        add(scoreLabel);
        updateScore(0);
    }

    // menyusun gambar angka sesuai skor lalu menampilkannya di JLabel
    public void updateScore(int score) {
        String text = String.valueOf(score);
        int gap = 2;
        int w = -gap;
        for (int i = 0; i < text.length(); i++) {
            w += digitImages[text.charAt(i) - '0'].getWidth() + gap;
        }
        BufferedImage img = new BufferedImage(w, digitImages[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        int x = 0;
        for (int i = 0; i < text.length(); i++) {
            BufferedImage d = digitImages[text.charAt(i) - '0'];
            g2.drawImage(d, x, 0, null);
            x += d.getWidth() + gap;
        }
        g2.dispose();
        scoreLabel.setIcon(new ImageIcon(img));
    }

    private void drawOutlined(Graphics2D g2, String text, Font font, int centerX, int y) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int x = centerX - fm.stringWidth(text) / 2;
        g2.setColor(new Color(84, 56, 71));
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                g2.drawString(text, x + dx, y + dy);
            }
        }
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. background
        g2.drawImage(backgroundImage, 0, 0, null);

        // 2. pipa
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g2.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // 3. tanah (di atas pipa)
        g2.drawImage(baseImage, 0, logic.groundY, null);

        // 4. burung: sayap mengepak (up - mid - down - mid), miring mengikuti kecepatan
        Player player = logic.getPlayer();
        if (player != null) {
            int[] sequence = {1, 0, 1, 2};
            int frame = logic.isGameOver() ? 1 : sequence[(int) (System.currentTimeMillis() / 120 % 4)];
            Image birdImage = logic.getBirdFrames()[frame];

            int cx = player.getPosX() + player.getWidth() / 2;
            int cy = player.getPosY() + player.getHeight() / 2;
            double degree = Math.max(-30, Math.min(90, player.getVelocityY() * 4));
            AffineTransform old = g2.getTransform();
            g2.rotate(Math.toRadians(degree), cx, cy);
            g2.drawImage(birdImage, player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
            g2.setTransform(old);
        }

        // 5. Game Over
        if (logic.isGameOver()) {
            g2.drawImage(gameOverImage, (width - gameOverImage.getWidth()) / 2, 210, null);
            drawOutlined(g2, "Tekan R untuk restart", new Font("Arial", Font.BOLD, 16), width / 2, 300);
        }
    }
}
