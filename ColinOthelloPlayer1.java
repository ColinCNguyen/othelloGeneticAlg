import java.util.Random;

//This class is used to create standards for the ColinOthelloPlayer class to play against.
public class ColinOthelloPlayer1 extends OthelloPlayer {
	int gamePly;
	int bestRow;
	int bestCol;
	Random randomWeight;
	int mobilityWeight, stabilityWeight, numPiecesWeight;
	Move bestMove;

	public ColinOthelloPlayer1(Board board, char token, int mobility, int stability, int pieces) {
		super(board, token);
		randomWeight = new Random();
		gamePly=0;
		mobilityWeight = mobility;
		stabilityWeight = stability;
		numPiecesWeight = pieces;
	}
	public Move makeMove(){
		alphaBetaPruning(token, 2, board, -10000, 10000);
		bestMove = new Move(bestRow,bestCol, token);
		board.makeMove(bestMove);
		return bestMove;
	}
	public int alphaBetaPruning(char theToken, int ply, Board b, int alpha, int beta){
		int bestScore;
		int currentScore;
		Move moveMade;
		if(ply > gamePly)
			gamePly = ply;
		if(ply==0 || board.gameOver())
			return evaluate(token);
		if(theToken==token){
			bestScore = -1000;
			for(int i=0;i<8;i++){
				for(int j=0; j<8; j++){
					if(board.canMove(theToken,i,j)){
						moveMade=board.makeMove(theToken,i,j);
						currentScore = alphaBetaPruning(oppositeToken(theToken), ply-1, b, alpha, beta);
						board.unMakeMove(moveMade);
						if(currentScore > bestScore){
							bestScore = currentScore;
							if(ply == gamePly){
								bestRow = i;
								bestCol = j;
							}
						}
						if(bestScore > alpha)
							alpha = bestScore;
						if(alpha > beta)
							break;
					}
				}
			}
			return bestScore;
		}
		else{
			bestScore = 10000;
			for(int i=0;i<8;i++){
				for(int j=0; j<8; j++){
					if(board.canMove(theToken,i,j)){
						moveMade=board.makeMove(theToken,i,j);
						currentScore = alphaBetaPruning(oppositeToken(theToken), ply-1, b, alpha, beta);
						board.unMakeMove(moveMade);
						if(currentScore < bestScore){
							bestScore = currentScore;
							if(ply == gamePly){
								bestRow = i;
								bestCol = j;
							}
						}
						if(bestScore < beta)
							beta = bestScore;
						if(alpha > beta)
							break;
					}
				}
			}
			return bestScore;
		}
	}
	public int evaluate(char token){
		int good,bad,count;
		good=0;
		bad=0;
		for(int i=0; i<8; i++){
			for(int j=0;j<8;j++){
				if(board.grid[i][j] != ' '){
					if(board.grid[i][j]==token){
						good+=numPiecesWeight;
						if(i==0 && j==0){
							count=1;
							good+=2*stabilityWeight;
							while(count<8 && board.grid[0][count] == token){
								count++;
								good+=stabilityWeight;
							}
						}
						if(i==0 && j==7){
							count=1;
							good+=2*stabilityWeight;
							while(count<8 && board.grid[count][7]==token){
								count++;
								good+=stabilityWeight;
							}
						}
						if(i==7 && j==0){
							count=6;
							good+=2*stabilityWeight;
							while(count>=0 && board.grid[count][0]==token){
								count--;
								good+=stabilityWeight;
							}
						}
						if(i==7 && j==7){
							count=6;
							good+=2*stabilityWeight;
							while(count>=0 && board.grid[7][count]==token){
								count--;
								good+=stabilityWeight;
							}
						}
					}
					else
						bad+=3;
				}
			}
		}
		good+= (board.countPossibleMoves(token, false) * mobilityWeight);
		return good-bad;
	}

	public char oppositeToken(char token){
		if(token=='O')
			return 'X';
		else
			return 'O';
	}
}
