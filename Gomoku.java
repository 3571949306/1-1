/**
 * 五子棋核心逻辑 - Java实现
 * 编译: javac Gomoku.java
 * 运行: java Gomoku
 */
public class Gomoku {
    private static final int BOARD_SIZE = 15;
    private static final int EMPTY = 0;
    private static final int BLACK = 1;  // 玩家
    private static final int WHITE = 2;  // AI
    
    private int[][] board;
    private int currentPlayer;
    
    public Gomoku() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = BLACK;
    }
    
    // 下棋
    public boolean makeMove(int row, int col, int player) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        if (board[row][col] != EMPTY) {
            return false;
        }
        board[row][col] = player;
        return true;
    }
    
    // 检查是否获胜
    public boolean checkWin(int row, int col) {
        int player = board[row][col];
        return checkDirection(row, col, 1, 0, player) ||  // 水平
               checkDirection(row, col, 0, 1, player) ||  // 垂直
               checkDirection(row, col, 1, 1, player) ||  // 对角线
               checkDirection(row, col, 1, -1, player);   // 反对角线
    }
    
    private boolean checkDirection(int row, int col, int dRow, int dCol, int player) {
        int count = 1;
        
        // 正方向
        for (int i = 1; i < 5; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        
        // 反方向
        for (int i = 1; i < 5; i++) {
            int r = row - i * dRow;
            int c = col - i * dCol;
            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        
        return count >= 5;
    }
    
    // AI计算最佳落子位置
    public int[] getAIMove() {
        int[] bestMove = {-1, -1};
        int bestScore = Integer.MIN_VALUE;
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    int score = evaluatePosition(row, col, WHITE) + evaluatePosition(row, col, BLACK);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    private int evaluatePosition(int row, int col, int player) {
        int score = 0;
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        
        for (int[] dir : directions) {
            int count = countLine(row, col, dir[0], dir[1], player);
            int open = countOpenEnds(row, col, dir[0], dir[1], player);
            score += getScoreForLine(count, open);
        }
        
        return score;
    }
    
    private int countLine(int row, int col, int dRow, int dCol, int player) {
        int count = 1;
        
        // 正方向
        for (int i = 1; i < 5; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        
        // 反方向
        for (int i = 1; i < 5; i++) {
            int r = row - i * dRow;
            int c = col - i * dCol;
            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
        
        return count;
    }
    
    private int countOpenEnds(int row, int col, int dRow, int dCol, int player) {
        int open = 0;
        
        // 正方向
        int r = row + 5 * dRow;
        int c = col + 5 * dCol;
        if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == EMPTY) {
            open++;
        }
        
        // 反方向
        r = row - 5 * dRow;
        c = col - 5 * dCol;
        if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == EMPTY) {
            open++;
        }
        
        return open;
    }
    
    private int getScoreForLine(int count, int open) {
        if (count >= 5) return 100000;
        if (count == 4 && open == 2) return 10000;
        if (count == 4 && open == 1) return 1000;
        if (count == 3 && open == 2) return 1000;
        if (count == 3 && open == 1) return 100;
        if (count == 2 && open == 2) return 100;
        if (count == 2 && open == 1) return 10;
        return count;
    }
    
    // 获取棋盘状态
    public int[][] getBoard() {
        return board;
    }
    
    // 打印棋盘到控制台（用于调试）
    public void printBoard() {
        System.out.print("   ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
        }
        System.out.println();
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    System.out.print("+ ");
                } else if (board[i][j] == BLACK) {
                    System.out.print("● ");
                } else {
                    System.out.print("○ ");
                }
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        Gomoku game = new Gomoku();
        System.out.println("五子棋游戏 - 人机对战");
        System.out.println("你执黑子(●)，AI执白子(○)");
        System.out.println("输入行列(如: 7 7)下棋，输入q退出");
        game.printBoard();
    }
}