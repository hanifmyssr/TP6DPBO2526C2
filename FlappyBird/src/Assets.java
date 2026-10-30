import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

// Helper kecil untuk memuat dan menskalakan gambar (pixel art tetap tajam)
public class Assets {
    // aset asli dibuat untuk 288x512, game ini 360x640 -> skala 1.25
    public static final double SCALE = 1.25;

    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(Assets.class.getResource(path));
        } catch (Exception e) {
            throw new RuntimeException("Gagal load gambar: " + path, e);
        }
    }

    public static BufferedImage scale(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    public static BufferedImage scale(BufferedImage src, double factor) {
        return scale(src, (int) Math.round(src.getWidth() * factor), (int) Math.round(src.getHeight() * factor));
    }

    public static BufferedImage flipVertical(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, 0, h, w, 0, 0, 0, w, h, null);
        g.dispose();
        return out;
    }
}
