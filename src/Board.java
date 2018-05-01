import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import jaco.mp3.player.MP3Player;

public class Board extends JPanel implements GameConstants ,ActionListener {

	private static final long serialVersionUID = 1L;
	private Boolean inGame  = true ;
	private Food food = new Food() ;
	private Snake snake = new Snake() ;

	public Board() {

	setPreferredSize(new Dimension(GWIDTH, GHEIGHT));
	setSize(GWIDTH,GHEIGHT);
	setBackground(Color.BLACK);	
	gameLoop();
	setFocusable(true);
	bindEvents() ;
	playBackGround(true);
	}
	
	MP3Player mp3 = new MP3Player() ;
	MP3Player sound = new MP3Player() ;
	
	private void playSound() {
		sound = new MP3Player(Board.class.getResource(POP_SOUND)) ;
		sound.play();
	}
	
private void playBackGround(Boolean isPlaying) {
	
	if(isPlaying) {
	mp3 = new MP3Player(Board.class.getResource(BACKGROUND_SOUND));
	mp3.play();
	}
	else {
		mp3.stop();
	}
}

	private void bindEvents() {
			this.addKeyListener(new KeyAdapter() {
			
				public void keyPressed(KeyEvent e) {
				int key =  e.getKeyCode();
				if ((key == KeyEvent.VK_LEFT) && (!snake.isMovingRight())) {
		            snake.setMovingLeft(true);
		            snake.setMovingUp(false);
		            snake.setMovingDown(false);
		        }

		        if ((key == KeyEvent.VK_RIGHT) && (!snake.isMovingLeft())) {
		            snake.setMovingRight(true);
		            snake.setMovingUp(false);
		            snake.setMovingDown(false);
		        }

		        if ((key == KeyEvent.VK_UP) && (!snake.isMovingDown())) {
		            snake.setMovingUp(true);
		            snake.setMovingRight(false);
		            snake.setMovingLeft(false);
		        }

		        if ((key == KeyEvent.VK_DOWN) && (!snake.isMovingUp())) {
		            snake.setMovingDown(true);
		            snake.setMovingRight(false);
		            snake.setMovingLeft(false);
		        }

		        if ((key == KeyEvent.VK_ENTER) && (inGame == false)) {

		            inGame = true;
		            snake.setMovingDown(false);
		            snake.setMovingRight(false);
		            snake.setMovingLeft(false);
		            snake.setMovingUp(false);

		            initialiseGame();
		        }
			}
		});
	}
		
private Timer timer ;

@Override
public void actionPerformed(ActionEvent e) {
    if (inGame == true) {
    	
    	checkFoodCollisions();
        checkCollisions();
        snake.move();
        System.out.println(snake.getSnakeX(0) + " " + snake.getSnakeY(0) + " " + food.getFoodX() + ", " + food.getFoodY());
    }
    repaint();
}

public void gameLoop() {
	 initialiseGame() ;
	 snake.move(); 
	 timer = new Timer(DELAY,this);
	 timer.start();
}

@Override
public void paintComponent(Graphics g) {
	super.paintComponent(g);	
	draw(g) ;
}

void initialiseGame() {
	snake.setJoints(3);
	for(int i = 0 ;i <snake.getJoints() ; i++) {
		snake.setSnakeX(GWIDTH/2);
		snake.setSnakeY(GHEIGHT/2);
		}
 	 snake.setMovingRight(true);
	 food.createFood();
	
}

void checkFoodCollisions() {

	   if ((proximity(snake.getSnakeX(0), food.getFoodX(), 20)) && (proximity(snake.getSnakeY(0), food.getFoodY(), 20))) {

		   playSound();
		   System.out.println("intersection");
		   snake.setJoints(snake.getJoints() + 1);
           food.createFood();
    } 
}

void checkCollisions() {

    for (int i = snake.getJoints(); i > 0; i--) {

    	if ((i > 5) && (snake.getSnakeX(0) == snake.getSnakeX(i) && (snake.getSnakeY(0) == snake.getSnakeY(i)))) {
            inGame = false;
        }
    }

    if (snake.getSnakeY(0) >= GHEIGHT) {
        inGame = false;
    }

    if (snake.getSnakeY(0) < 0) {
        inGame = false;
    }

    if (snake.getSnakeX(0) >= GWIDTH) {
        inGame = false;
    }

    if (snake.getSnakeX(0) < 0) {
        inGame = false;
    }

    if (!inGame) {
        timer.stop();
    }
}

void draw(Graphics g) {
	if(inGame == true) {
		g.setColor(Color.GREEN);
//		g.fillRect(food.getFoodX(), food.getFoodY(), PIXELSIZE,PIXELSIZE);
		g.fillOval(food.getFoodX(), food.getFoodY(), PIXELSIZE,PIXELSIZE);
		for(int i= 0 ; i<snake.getJoints() ;i++ ) {
			if(i==0) {
				g.setColor(Color.red) ;
				g.fillOval(snake.getSnakeX(i), snake.getSnakeY(i),PIXELSIZE, PIXELSIZE);
			
				g.setColor(Color.BLACK);
				g.fillOval(snake.getSnakeX(i)+10, snake.getSnakeY(i)+10,PIXELSIZE/3, PIXELSIZE/3);
			}			
			else {
				g.setColor(Color.YELLOW);
				g.fillRect(snake.getSnakeX(i), snake.getSnakeY(i),PIXELSIZE, PIXELSIZE);
			}
		}
		Toolkit.getDefaultToolkit().sync();		
	}
	else {
	endGame(g) ;	
	}
}

void endGame(Graphics g) {

    String message = "Game over";

    g.setColor(Color.red);
    g.drawString(message,GWIDTH/2 ,GHEIGHT / 2);
    
    System.out.println("Game Ended");
    
    playBackGround(false);

}

private boolean proximity(int a, int b, int closeness) {
    return Math.abs((long) a - b) <= closeness;
}

}

