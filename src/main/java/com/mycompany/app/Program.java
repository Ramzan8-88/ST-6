package com.mycompany.app;

// Реализация игры "Крестики-нолики" (3x3)
// Минимаксный алгоритм

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

enum State { PLAYING, OWIN, XWIN, DRAW }


class Player {
  public char symbol;
  public int move;
  public boolean selected;
  public boolean win;
}

class Game {
    public State state;
    public Player player1, player2;
    public Player cplayer; // текущий игрок
    public int nmove;  // последний шаг сделанный действующим игроком 
    public char symbol;
    public static final int INF = 100;
    public char[] board;


    public Game() {
      player1=new Player();
      player2=new Player();
      player1.symbol='X';
      player2.symbol='O';
      state=State.PLAYING; 
      board=new char[9];   // текущая доска в игре  
      for(int i=0;i<9;i++)
        board[i]=' ';
    }

    private static boolean line(char[] b, char s, int i, int j, int k) {
        return b[i] == s && b[j] == s && b[k] == s;
    }

    private static boolean hasWon(char[] b, char s) {
        return line(b, s, 0, 1, 2) || line(b, s, 3, 4, 5) || line(b, s, 6, 7, 8)
                || line(b, s, 0, 3, 6) || line(b, s, 1, 4, 7) || line(b, s, 2, 5, 8)
                || line(b, s, 0, 4, 8) || line(b, s, 2, 4, 6);
    }

    /** Итог позиции: победа X, победа O, ничья или игра продолжается (не зависит от {@link #symbol}). */
    public State checkState(char[] board) {
        if (hasWon(board, 'X')) {
            return State.XWIN;
        }
        if (hasWon(board, 'O')) {
            return State.OWIN;
        }
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                return State.PLAYING;
            }
        }
        return State.DRAW;
    }
     // сгенерировать возможные ходы
   void generateMoves(char[] board, ArrayList<Integer> move_list) {
    for (int i = 0; i < 9; i++) 
        if (board[i] == ' ') 
            move_list.add(i);
   }

   // оценка позиции
   int evaluatePosition(char[] board, Player player)  
   {
    State state=checkState(board);
    if ((state == State.XWIN || state == State.OWIN || state == State.DRAW)) 
    {
        if ((state == State.XWIN && player.symbol == 'X') || (state == State.OWIN && player.symbol == 'O')) 
            return +Game.INF;
        else if ((state == State.XWIN && player.symbol == 'O') || (state == State.OWIN && player.symbol == 'X')) 
            return -Game.INF;
        else if (state == State.DRAW) 
            return 0;
    }
    return -1;
   }

   int MiniMax(char[] board, Player player) {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMoves = new int[9];
        int tieCount = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] != ' ') {
                continue;
            }
            board[i] = player.symbol;
            symbol = player.symbol;
            int val = minimaxRec(board, false, player);
            board[i] = ' ';
            System.out.printf("%nminimax: %3d(%d) ", i + 1, val);
            if (val > bestVal) {
                bestVal = val;
                tieCount = 0;
                bestMoves[tieCount++] = i + 1;
            } else if (val == bestVal) {
                bestMoves[tieCount++] = i + 1;
            }
        }
        int pick = bestMoves[0];
        for (int t = 1; t < tieCount; t++) {
            if (bestMoves[t] < pick) {
                pick = bestMoves[t];
            }
        }
        System.out.printf("%nminimax best: %3d(%d) ", pick, bestVal);
        return pick;
    }

    /** Оценка позиции для игрока {@code ai} после хода соперника: максимизирует {@code ai}, затем минимизирует. */
    private int minimaxRec(char[] board, boolean maximizing, Player ai) {
        int leaf = evaluatePosition(board, ai);
        if (leaf != -1) {
            return leaf;
        }
        char placed = maximizing ? ai.symbol : (ai.symbol == 'X' ? 'O' : 'X');
        if (maximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] != ' ') {
                    continue;
                }
                board[i] = placed;
                int v = minimaxRec(board, false, ai);
                board[i] = ' ';
                if (v > best) {
                    best = v;
                }
            }
            return best;
        }
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < 9; i++) {
            if (board[i] != ' ') {
                continue;
            }
            board[i] = placed;
            int v = minimaxRec(board, true, ai);
            board[i] = ' ';
            if (v < best) {
                best = v;
            }
        }
        return best;
    }
}

public class Program {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("Крестики-нолики");
        frame.add(new TicTacToePanel(new GridLayout(3, 3)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(5, 5, 520, 520);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class TicTacToeCell extends JButton {
    private int num;
    private int row;
    private int col;
    private char marker;

    public TicTacToeCell(int num,int x,int y) {
        this.num=num;
        row=y;
        col=x;
        marker=' ';
        setText(Character.toString(marker));
        setFont(new Font("Arial", Font.PLAIN, 40));
    }
    public void setMarker(String m) {
        marker=m.charAt(0);
        setText(m);
        setEnabled(false);
    }
    public char getMarker() {
        return marker;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public int getNum() {
        return num;
    }

}

class Utility {

    private Utility() {
    }

  public static void print(char[] board) {
    System.out.println();
        for(int j=0;j<9;j++)
          System.out.print(board[j]+"-");
        System.out.println();
  }
  public static void print(int[] board) {
    System.out.println();
        for(int j=0;j<9;j++)
          System.out.print(board[j]+"-");
        System.out.println();
  }  
  public static void print(ArrayList<Integer> moves) {
    System.out.println();
        for(int j=0;j<moves.size();j++)
          System.out.print(moves.get(j)+"-");
        System.out.println();
  }  
}

class TicTacToePanel extends JPanel implements ActionListener {

   private Game game;

   private void createCell(int num,int x,int y) {
       cells[num]=new TicTacToeCell(num,x,y);
       cells[num].addActionListener(this);
       add(cells[num]);

   }

   private TicTacToeCell[] cells = new TicTacToeCell[9];
   TicTacToePanel(GridLayout layout) {
       super(layout);
       createCell(0,0,0);
       createCell(1,1,0);
       createCell(2,2,0);
       createCell(3,0,1);
       createCell(4,1,1);
       createCell(5,2,1);
       createCell(6,0,2);
       createCell(7,1,2); 
       createCell(8,2,2);
       game=new Game();
       game.cplayer=game.player1;
   }

   public void actionPerformed(ActionEvent ae) {
      game.player1.move = -1;
      game.player2.move = -1;
      //System.out.println(game.cplayer.symbol);
      //System.out.println(((TicTacToeCell)(ae.getSource())).getNum());


      int i=0;
      for(TicTacToeCell jb: cells) {
         if(ae.getSource()==jb) {
            jb.setMarker(Character.toString(game.cplayer.symbol));
         }
         game.board[i++]=jb.getMarker();
      }
      if(game.cplayer==game.player1) {

         game.player2.move = game.MiniMax(game.board, game.player2);
         game.nmove = game.player2.move;
         game.symbol = game.player2.symbol;
         game.cplayer = game.player2;
         if(game.player2.move>0)
            cells[game.player2.move-1].doClick();
       }
       else
       {
         game.nmove = game.player1.move;
         game.symbol = game.player1.symbol;
         game.cplayer = game.player1;
       }

      game.state=game.checkState(game.board);


      if (game.state == State.XWIN) {
        Window w = SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(w, "Выиграли крестики", "Результат", JOptionPane.INFORMATION_MESSAGE);
        if (w != null) {
          w.dispose();
        }
      } else if (game.state == State.OWIN) {
        Window w = SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(w, "Выиграли нолики", "Результат", JOptionPane.INFORMATION_MESSAGE);
        if (w != null) {
          w.dispose();
        }
      } else if (game.state == State.DRAW) {
        Window w = SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(w, "Ничья", "Результат", JOptionPane.INFORMATION_MESSAGE);
        if (w != null) {
          w.dispose();
        }
      } 




   }
}

