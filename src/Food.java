public class Food extends Snake implements GameConstants{
	
	private Snake snake = new Snake() ;
	private int foodX  ;
	private int foodY  ;
	private int randomPosition = 25  ;

	public int getFoodX() {
		return foodX;
	}
	
	public int getFoodY() {
		return foodY;
	}
	
	public void createFood() {
		
		int location = (int)(Math.random() * randomPosition) ;
		foodX = location*PIXELSIZE ;
		
		location = (int)(Math.random() * randomPosition) ;
		foodY = location*PIXELSIZE ;
		
		if((foodX==snake.getSnakeX(0)) && (foodX==snake.getSnakeY(0))) {
			createFood();
		}	
	}
}
