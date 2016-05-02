/**
 * Plays Othello/Reversi against computer with a alpha-beta search.
 *
 * @author  T.Sergeant
 * @version For AI
 *
*/

public class OthelloGame
{
	private Board board;                    // the playing surface
	private final char [] TOKEN= {'X','O'}; // list of tokens
	private OthelloPlayer [] player;        // array of (2) players
	private ColinOthelloPlayer [] population;	// array of population members
	private ColinOthelloPlayer1 [] standard; //Standard to play randomly generated boards against.
	private boolean verbose;                // determines amount of output
	private int start;                      // who starts?
	int maxPopulation = 5000;
	int standardSize = 2;

	/**
	 * Creates a new game betwen the specified player types.
	 *
	 * @param p1type type of player 1
	 * @param p2type type of player 2
	 * @param verbose generates lots of output if true
	 * @param start who should start (-1 for random, 0, or 1)
	 *
	 * <p>See createPlayer() for documention about player types.</p>
	 */
	public OthelloGame(int p1type, int p2type, boolean verbose, int start)
	{
		board= new Board();
		player= new OthelloPlayer[2];
		population = new ColinOthelloPlayer[maxPopulation];
		standard = new ColinOthelloPlayer1[standardSize];
		this.verbose= verbose;
		player[0]= createPlayer(p1type,TOKEN[0]);
		player[1]= createPlayer(p2type,TOKEN[1]);
		
		//Fill standard up
		standard[0] = new ColinOthelloPlayer1(board, TOKEN[0], 5, 15, 4);
		standard[1] = new ColinOthelloPlayer1(board, TOKEN[0], 9, 4, 12);
		//Starting population of 50
		for(int i=0;i<maxPopulation;i++)
			population[i] = (ColinOthelloPlayer) createPlayer(p2type, TOKEN[1]);
		this.start= start;
	}


	/**
	 * Create a new game with specified types of players, verbose output, and
	 * random start.
	 *
	 * @param p1type type of player 1
	 * @param p2type type of player 2
	 */
	public OthelloGame(int p1type, int p2type)
	{
		this(p1type,p2type,true,-1);
	}


	/**
	 * Create a new game with human vs human, verbose
	 * output, and random start.
	 */
	public OthelloGame()
	{
		this(0,0,true,-1);
	}


	/**
	 * Create new player of the specified type using the specified token.
	 *
	 * @param type the type of player to create
	 * @param token the token the player will be using
	 * @return OthelloPlayer object of given type (null if type is not valid)
	 *
	 * <p>Types are as follows:</p>
	 * <ul>
	 * <li>0 = human</li>
	 * </ul>
	 */
	private OthelloPlayer createPlayer(int type, char token)
	{
		if (type==0)
			return new HumanOthelloPlayer(board,token);
		if(type==1)
			return new ColinOthelloPlayer(board,token);
		if(type==2)
			return new ColinOthelloPlayer1(board,token, 0, 0, 0);
		return null;
	}
	
	/**
	 * Method used to sort our population by their wins to determine the strongest SBE.
	 */
	public void sortByWins(){
		ColinOthelloPlayer temp;
		int k;
		int n = population.length;
		for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (population[i].win < population[k].win) {
					temp = population[i];
					population[i] = population[k];
					population[k] = temp;
				}
			}
		}
	}
	
	/**
	 * Method used to display the top 10 members our population and their weights
	 */
	public void displayTopTen(){
		for(int i=0; i<10; i++)
			System.out.println("Wins:" + population[i].win + ". Stability Weight: " + population[i].stabilityWeight
			+ " Mobility Weight: "+ population[i].mobilityWeight+ " Num Pieces Weight: " + population[i].numPiecesWeight);
	}


	/**
	 * Play a game of Othello/Reversi.
	 */
	public void play(int computer1, int computer2)
	{
		int turn;
		player[0] = standard[computer1];
		player[1] = population[computer2];

		// we want to remember who started for reportin purposes
		// if start was -1 it is a random start
		if (start==-1) {
			turn= (int) (Math.random()*2.0);
			start= turn;
		}
		else
			turn= start;

		do {
			if (verbose) {
				board.display();
				System.out.println("It is "+TOKEN[turn]+"'s move ... ("+board.countPossibleMoves(TOKEN[turn],false)+" moves available)");
			}
			player[turn].makeMove();
			turn= (turn+1)%2;
		} while (!board.gameOver());
		if (verbose) board.display();

		// we show this part whether verbose output is specified or not
		//System.out.println(player[0]+" vs. "+player[1]);
		//System.out.println(player[start]+" started");
		//board.showResults();
		
		//Increment wins and losses after a finished game.
		if(player[0].whoWon(player[0].token) == player[0].token){
			player[0].win++;;
			player[1].lose++;
		}
		else{
			player[0].lose++;
			player[1].win++;
		}
	}
}
