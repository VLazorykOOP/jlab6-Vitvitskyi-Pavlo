import javax.swing.*;
import java.awt.*;

public class BallAnimation extends JPanel implements Runnable {
    private int ballX = 150; // початкова позиція кулі по осі X
    private int ballY = 150; // початкова позиція кулі по осі Y
    private int ballDiameter = 10; // початковий діаметр кулі
    private boolean growing = true; // стан кулі (зростає або зменшується)
    private int speed = 1; // швидкість руху

    public BallAnimation() {
        Thread animationThread = new Thread(this);
        animationThread.start(); // запускаємо потік анімації
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(ballX - ballDiameter / 2, ballY - ballDiameter / 2, ballDiameter, ballDiameter);
    }

    @Override
    public void run() {
        while (true) {
            // якщо куля росте
            if (growing) {
                ballDiameter += speed;
                if (ballDiameter >= 100) { // максимальний розмір кулі
                    growing = false; // починає зменшуватись
                }
            } else {
                ballDiameter -= speed;
                if (ballDiameter <= 10) { // мінімальний розмір кулі
                    growing = true; // починає збільшуватись
                }
            }

            // оновлюємо екран
            repaint();

            // Затримка для плавності анімації
            try {
                Thread.sleep(20); // 20 мілісекунд
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ball Animation");
        BallAnimation ballAnimation = new BallAnimation();
        frame.add(ballAnimation);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
