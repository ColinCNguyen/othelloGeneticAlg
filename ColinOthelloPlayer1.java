

public class ColinOthelloPlayer1 extends OthelloPlayer {
	int gamePly;
	int bestRow;
	int bestCol;
	Move bestMove;

	public ColinOthelloPlayer1(Board board, char token) {
		super(board, token);
		gamePly=0;
	}
	public Move makeMove(){
		alphaBetaPruning('O', 5, board, -10000, 10000);
		bestMove = new Move(bestRow,bestCol,'O');
		System.out.println(bestRow + " row " + bestCol + " col ");
		board.makeMove(bestMove);
		return bestMove;
	}
	public int alphaBetaPruning(char token, int ply, Board b, int alpha, int beta){
		int bestScore;
		int currentScore;
		Move moveMade;
		if(ply > gamePly)
			gamePly = ply;
		if(ply==0 || board.gameOver())
			return evaluate('O');
		if(token=='O'){
			bestScore = -1000;
			for(int i=0;i<8;i++){
				for(int j=0; j<8; j++){
					if(board.canMove(token,i,j)){
						moveMade=board.makeMove(token,i,j);
						currentScore = alphaBetaPruning('X', ply-1, b, alpha, beta);
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
					if(board.canMove(token,i,j)){
						moveMade=board.makeMove(token,i,j);
						currentScore = alphaBetaPruning('O', ply-1, b, alpha, beta);
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
						good++;
						if(i==0 && j==0){
							count=1;
							good+=5;
							while(count<8 && board.grid[0][count] == token){
								count++;
								good+=2;
							}
						}
						if(i==0 && j==7){
							count=1;
							good+=5;
							while(count<8 && board.grid[count][7]==token){
								count++;
								good+=2;
							}
						}
						if(i==7 && j==0){
							good+=5;
							count=6;
							while(count>=0 && board.grid[count][0]==token){
								count--;
								good+=2;
							}
						}
						if(i==7 && j==7){
							good+=5;
							count=6;
							while(count>=0 && board.grid[7][count]==token){
								count--;
								good+=2;
							}
						}
					}
					else
						bad+=3;
				}
			}
		}
		return good-bad;
	}

}

