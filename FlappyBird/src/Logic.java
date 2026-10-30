import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;
    int groundY = 500;          // permukaan tanah (base.png)

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 43;
    int playerHeight = 30;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 65;
    int pipeHeight = 400;

    Timer gameloop;
    Timer pipesCooldown;
    double gravity = 0.4;        // makin kecil = jatuh makin pelan
    double jumpVelocity = -6.5;  // makin kecil (lebih negatif) = lompat makin tinggi
    double playerY;              // posisi Y presisi (desimal) agar gerakan halus
    int pipeVelocityX = -2;

    boolean gameOver = false;
    int score = 0;

    View view;
    Image[] birdFrames;         // 0 = upflap, 1 = midflap, 2 = downflap
    Player player;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    SoundPlayer wingSound, pointSound, hitSound, dieSound, swooshSound;

    public Logic() {
        birdFrames = new Image[] {
            Assets.scale(Assets.load("assets/yellowbird-upflap.png"), playerWidth, playerHeight),
            Assets.scale(Assets.load("assets/yellowbird-midflap.png"), playerWidth, playerHeight),
            Assets.scale(Assets.load("assets/yellowbird-downflap.png"), playerWidth, playerHeight)
        };
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdFrames[1]);
        playerY = playerStartPosY;
        player.setVelocityY(jumpVelocity);

        // pipa atas = pipa bawah yang dibalik vertikal
        java.awt.image.BufferedImage pipeImage = Assets.scale(Assets.load("assets/pipe-green.png"), pipeWidth, pipeHeight);
        lowerPipeImage = pipeImage;
        upperPipeImage = Assets.flipVertical(pipeImage);
        pipes = new ArrayList<Pipe>();

        wingSound = new SoundPlayer("assets/wing.wav");
        pointSound = new SoundPlayer("assets/point.wav");
        hitSound = new SoundPlayer("assets/hit.wav");
        dieSound = new SoundPlayer("assets/die.wav");
        swooshSound = new SoundPlayer("assets/swoosh.wav");
        swooshSound.play();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();

        gameloop = new Timer(1000/60, this);
        gameloop.start();
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {return pipes;}

    public Image[] getBirdFrames() {return birdFrames;}

    public boolean isGameOver() {return gameOver;}

    public void placePipes(){
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    private boolean collision(Player p, Pipe pipe) {
        return p.getPosX() < pipe.getPosX() + pipe.getWidth()
            && p.getPosX() + p.getWidth() > pipe.getPosX()
            && p.getPosY() < pipe.getPosY() + pipe.getHeight()
            && p.getPosY() + p.getHeight() > pipe.getPosY();
    }

    private void endGame(SoundPlayer sound) {
        if (gameOver) return;
        gameOver = true;
        sound.play();
        pipesCooldown.stop();
    }

    public void restart() {
        player.setPosX(playerStartPosX);
        playerY = playerStartPosY;
        player.setPosY(playerStartPosY);
        player.setVelocityY(jumpVelocity);
        pipes.clear();
        score = 0;
        gameOver = false;
        view.updateScore(0);
        swooshSound.play();
        pipesCooldown.restart();
    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        playerY = Math.max(playerY + player.getVelocityY(), 0);
        player.setPosY((int) Math.round(playerY));

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // menabrak pipa
            if (collision(player, pipe)) {
                endGame(hitSound);
            }

            // melewati pipa: dihitung dari pipa atas saja (+1 per pasang pipa)
            if (!pipe.getPassed() && pipe.getImage() == upperPipeImage
                    && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score++;
                view.updateScore(score);
                pointSound.play();
            }
        }

        // buang pipa yang sudah keluar layar
        pipes.removeIf(p -> p.getPosX() + p.getWidth() < 0);

        // jatuh ke tanah
        if (playerY + player.getHeight() >= groundY) {
            playerY = groundY - player.getHeight();
            player.setPosY((int) playerY);
            endGame(dieSound);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (!gameOver) {
            move();
        }
        if(view != null){
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver){
            player.setVelocityY(jumpVelocity);
            wingSound.play();
        }
        if(e.getKeyCode() == KeyEvent.VK_R && gameOver){
            restart();
        }
    }
    public void keyReleased(KeyEvent e){}
}
