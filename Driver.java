/**
 * Driver for the game.
 */

public class Driver
{
  public static void main(String [] args)
  {
  		OthelloGame game= new OthelloGame(2,1,false,0);
  		
  		//Plays every member of population against each other.
  		for(int i=0; i<game.standardSize;i++){
  			for(int j=i+1;j<game.maxPopulation;j++){
  				game.play(i,j);

  			}
  		}
  		game.sortByWins();
  		game.displayTopTen();
  }
}
