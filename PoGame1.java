import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PoGame1 extends Application {

    private static final int TILE_SIZE = 30;
    private static final int ROWS = 20;
    private static final int COLS = 20;
    private static final int WIDTH = COLS * TILE_SIZE;
    private static final int HEIGHT = ROWS * TILE_SIZE;
    private static final int TIME_LIMIT_SECONDS = 20;

    private char[][] mapData = new char[ROWS][COLS];
    private Group root;

    private Rectangle playerView;
    private int playerX = 1;
    private int playerY = 1;

    private Rectangle goalView;
    private int goalFrameCount = 0;

    private boolean hasKey = false;
    private Rectangle keyView;
    private Rectangle doorView;

    private List<Enemy> enemies = new ArrayList<>();

    private boolean isGameOver = false;
    private Text messageText;
    private Text timerText;
    private long startTime = 0;

    private String[] rawMap = {
            "BBBBBBBBBBBBBBBBBBBB",
            "BS          B      B",
            "BBB B BBBBB B BBBB B",
            "B   B     B      B B",
            "B BBBBBBB B B BB B B",
            "B       B   B  B   B",
            "BBBBBBB BBBBBB B BBB",
            "B     B    B   B   B",
            "B BBB BBBB B BBBBB B",
            "B B      B B     B B",
            "B B BBBB B B BBB B B",
            "B B    B   B   B B B",
            "B BBBB BBBBBBB B B B",
            "B            B   B B",
            "BBBBBBBBBBBB B BBB B",
            "B      B     B  K   B",
            "B BBBB B BBBBBBBBB B",
            "B    B      D      G",
            "BBBBBBBBBBBBBBBBBBBB",
            "BBBBBBBBBBBBBBBBBBBB"
    };

    @Override
    public void start(Stage primaryStage) {
        root = new Group();

        createMap();
        createPlayer();

        enemies.add(new Enemy(1 * TILE_SIZE, 3 * TILE_SIZE, 2.0));
        enemies.add(new Enemy(8 * TILE_SIZE, 5 * TILE_SIZE, 3.0));
        enemies.add(new Enemy(2 * TILE_SIZE, 13 * TILE_SIZE, 4.5));
        enemies.add(new Enemy(10 * TILE_SIZE, 15 * TILE_SIZE, 2.5));

        timerText = new Text(10, 25, "Time: " + TIME_LIMIT_SECONDS);
        timerText.setFont(new Font("Consolas", 20));
        timerText.setFill(Color.WHITE);
        root.getChildren().add(timerText);

        messageText = new Text(WIDTH / 2 - 150, HEIGHT / 2, "");
        messageText.setFont(new Font("Impact", 50));
        messageText.setFill(Color.RED);
        root.getChildren().add(messageText);

        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.rgb(30, 30, 30));

        scene.setOnKeyPressed(e -> {
            if (isGameOver) return;
            KeyCode key = e.getCode();
            switch (key) {
                case UP -> movePlayer(0, -1);
                case DOWN -> movePlayer(0, 1);
                case LEFT -> movePlayer(-1, 0);
                case RIGHT -> movePlayer(1, 0);
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isGameOver) return;
                if (startTime == 0) startTime = now;
                update(now);
            }
        };
        timer.start();

        primaryStage.setTitle("PoGame - Key & Door");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void createMap() {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                char c = rawMap[y].charAt(x);
                mapData[y][x] = c;

                if (c == 'B') {
                    Rectangle wall = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    wall.setFill(Color.rgb(100, 100, 100));
                    wall.setStroke(Color.rgb(50, 50, 50));
                    root.getChildren().add(wall);
                }
                else if (c == 'S') {
                    playerX = x;
                    playerY = y;
                }
                else if (c == 'G') {
                    goalView = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    goalView.setFill(Color.GOLD);
                    root.getChildren().add(goalView);
                }
                else if (c == 'K') {
                    keyView = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    keyView.setFill(Color.LIGHTGREEN);
                    root.getChildren().add(keyView);
                }
                else if (c == 'D') {
                    doorView = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    doorView.setFill(Color.SADDLEBROWN);
                    root.getChildren().add(doorView);
                }
            }
        }
    }

    private void createPlayer() {
        playerView = new Rectangle(playerX * TILE_SIZE, playerY * TILE_SIZE, TILE_SIZE - 4, TILE_SIZE - 4);
        playerView.setFill(Color.CYAN);
        playerView.setStroke(Color.WHITE);
        playerView.setTranslateX(2);
        playerView.setTranslateY(2);
        root.getChildren().add(playerView);
    }

    private void movePlayer(int dx, int dy) {
        int nextX = playerX + dx;
        int nextY = playerY + dy;

        if (nextX < 0 || nextX >= COLS || nextY < 0 || nextY >= ROWS) return;

        if (mapData[nextY][nextX] == 'D' && !hasKey) {
            return;
        }

        if (mapData[nextY][nextX] != 'B') {
            playerX = nextX;
            playerY = nextY;
            playerView.setX(playerX * TILE_SIZE);
            playerView.setY(playerY * TILE_SIZE);
        }


        if (keyView != null &&
                playerView.getBoundsInParent().intersects(keyView.getBoundsInParent())) {
            hasKey = true;
            root.getChildren().remove(keyView);
            keyView = null;
        }

        if (doorView != null &&
                playerView.getBoundsInParent().intersects(doorView.getBoundsInParent()) &&
                hasKey) {
            root.getChildren().remove(doorView);
            doorView = null;
        }

        if (mapData[playerY][playerX] == 'G') {
            endGame(true, "YOU WIN!");
        }
    }

    private void update(long now) {
        long elapsedSeconds = (now - startTime) / 1_000_000_000;
        long remainingTime = TIME_LIMIT_SECONDS - elapsedSeconds;

        timerText.setText("Time: " + remainingTime);
        if (remainingTime <= 10) timerText.setFill(Color.ORANGE);

        if (remainingTime <= 0) {
            endGame(false, "TIME UP!");
            return;
        }

        goalFrameCount++;
        if (goalFrameCount % 20 == 0) {
            goalView.setFill(goalView.getFill() == Color.GOLD ? Color.WHITE : Color.GOLD);
        }

        for (Enemy enemy : enemies) {
            enemy.move();
            if (playerView.getBoundsInParent().intersects(enemy.view.getBoundsInParent())) {
                endGame(false, "CAUGHT!");
            }
        }
    }

    private void endGame(boolean win, String msg) {
        isGameOver = true;
        messageText.setText(msg);
        messageText.setFill(win ? Color.LIMEGREEN : Color.RED);
        messageText.toFront();
    }

    class Enemy {
        Circle view;
        double x, y;
        double vx;

        public Enemy(double startX, double startY, double speed) {
            x = startX + TILE_SIZE / 2.0;
            y = startY + TILE_SIZE / 2.0;
            vx = speed;

            view = new Circle(TILE_SIZE / 2.0 - 4, Color.RED);
            view.setCenterX(x);
            view.setCenterY(y);
            root.getChildren().add(view);
        }

        public void move() {
            x += vx;
            int gridX = (int) ((x + (vx > 0 ? TILE_SIZE / 2 : -TILE_SIZE / 2)) / TILE_SIZE);
            int gridY = (int) (y / TILE_SIZE);

            if (gridX < 0 || gridX >= COLS || mapData[gridY][gridX] == 'B') {
                vx = -vx;
            }

            view.setCenterX(x);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
