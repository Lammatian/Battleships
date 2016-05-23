package Ships;
public abstract class Ship{
	
	/*
	 * all standard ships have width equal to 1, although a player will be able to create his own ships of different dimensions
	 */
	private int width;
	private int length;
	
	public Ship(int width, int length){
		this.width = width;
		this.length = length;
	}
	
	public Ship(int length){
		/*
		 * constructor for standard ships
		 */
		this.width = 1;
		this.length = length;
	}
	
	public void setWidth(int width){
		/*
		 * Add exceptions later
		 */
		this.width = width;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setLength(int length){
		/*
		 * Add exceptions later
		 */
		this.length = length;
	}
	
	public int getLength(){
		return length;
	}
	
	public abstract String toString();
}
