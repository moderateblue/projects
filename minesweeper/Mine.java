import java.util.Random;
import java.util.Scanner;

public class Mine {
  private int[][] mineBoard;
  private String[][] displayMineBoard;
  private int mineCount;
  private boolean[][] mineChecked;
  private boolean gameOn = true;
  private int[] gameParam = new int[3];
  private boolean firstTurn = true;

  public Mine() {
    startInput();
  }

  public void gameOn() {
    while (getGameOn()) {
      printBoard();
      // System.out.println(getMineCount());
      // System.out.println(getUnrevealedCount());
      revealInput();
      System.out.println();
      if (win()) {
        setGameOn(false);
        printBoard();
        System.out.println();
        System.out.println("You win");
      }
    }
  }

  public void startInput() {
    Scanner game = new Scanner(System.in);
    System.out.print(
        "input the size of the board as the height width and number of mines ex. (9 9 10) -> 9x9 board with 10 mines: ");
    String startGame = game.nextLine();
    String[] params = startGame.split(" ");

    if (startGame.equals("description")) {
      System.out.println();
      System.out.println("This is a minesweeper project I decided to do for computer science class.\nIt has features such as error handling, customizable board, and a guaranteed no mines in the 3x3 area of your first coordinates.\nIt's just regular old minesweeper.");
      System.out.println();
      startInput();
      return;
    }

    try {
      gameParam[0] = Integer.parseInt(params[0]);
      gameParam[1] = Integer.parseInt(params[1]);
      gameParam[2] = Integer.parseInt(params[2]);
    } catch (Exception e) {
      System.out.println();
      System.out.println("Bruh O_o");
      System.out.println("Board must be 5x5 or more and 24x30 or less and mines must be 50% or less of the board");
      System.out.println();
      startInput();
      return;
    }

    if (gameParam[0] <= 24 && gameParam[0] >= 5 && gameParam[1] >= 5 && gameParam[1] <= 30 && gameParam[2] <= gameParam[0] * gameParam[1] / 2 && gameParam[2] >= 0) {
      System.out.println();
      initialBoard(gameParam[0], gameParam[1]);
      return;
    } else {
      System.out.println();
      System.out.println("Board must be 5x5 or more and 24x30 or less and mines must be 50% or less of the board");
      System.out.println();
      startInput();
    }
  }

  public void initialBoard(int length, int width) {
    mineBoard = new int[length][width];
    displayMineBoard = new String[length][width];
    mineChecked = new boolean[length][width];

    for (int row = 0; row < displayMineBoard.length; row++) {
      for (int col = 0; col < displayMineBoard[row].length; col++) {
        displayMineBoard[row][col] = "#";
      }
    }
    printBoard();
    revealInput();
  }

  // setting up the mines
  public void boardSetup(int length, int width, int amountOfMines, int r, int c) {
    Random rand = new Random();
    mineCount = 0;
    boolean within = false;

    // assign number to each element (mine = -1)
    while (mineCount < amountOfMines) {
      int randRow = rand.nextInt(length);
      int randCol = rand.nextInt(width);
      // if random index does not have a mine, add a mine
      if (mineBoard[randRow][randCol] != -1) {
        for (int initRow = r - 1; initRow <= r + 1; initRow++) {
          for (int initCol = c - 1; initCol <= c + 1; initCol++) {
            if (randRow == initRow && randCol == initCol)
              within = true;
          }
        }
        if (within) {
          within = false;
          continue;
        }
        mineBoard[randRow][randCol] = -1;
        // add numbers to show how many mines are in the area
        for (int row = randRow - 1; row <= randRow + 1; row++) {
          for (int col = randCol - 1; col <= randCol + 1; col++) {
            if (row == randRow && col == randCol)
              continue;
            if (row < 0 || row > mineBoard.length - 1 || col < 0 || col > mineBoard[row].length - 1)
              continue;
            if (mineBoard[row][col] != -1)
              mineBoard[row][col] += 1;
          }
        }
        mineCount++;
      }
    }
  }

  // prints the board with everything revealed
  public String toString() {
    String displayMines = "";
    for (int row = 0; row < mineBoard.length; row++) {
      for (int col = 0; col < mineBoard[row].length; col++) {
        if (mineBoard[row][col] != -1) {
          displayMines += mineBoard[row][col] + "  ";
        } else {
          displayMines += mineBoard[row][col] + " ";
        }
      }
      displayMines += "\n";
    }
    return displayMines;
  }

  // prints the game board
  public void printBoard() {
    System.out.print("    ");
    for (int x = 1; x < displayMineBoard[1].length + 1; x++)
      if (x < 10)
        System.out.print(x + "  ");
      else if (x == 30)
        System.out.print(x);
      else
        System.out.print(x + " ");
    System.out.println();
    System.out.println();

    for (int row = 0; row < displayMineBoard.length; row++) {
      if (row + 1 < 10)
        System.out.print(row + 1 + "   ");
      else
        System.out.print(row + 1 + "  ");
      for (int col = 0; col < displayMineBoard[row].length; col++) {
        System.out.print(displayMineBoard[row][col] + "  ");
      }
      System.out.println();
    }
  }

  // get user input for coordinates to the element
  public void revealInput() {
    System.out.println();
    Scanner coords = new Scanner(System.in);
    System.out.print("input field coordinates (row column) to reveal square (ex. 5 5), type f to flag (ex. f 5 5) and u to unflag (ex. u 5 5): ");
    String userCoords = coords.nextLine();

    int[] inputCoords = new int[2];

    if (userCoords.startsWith("f")) {
      try {
        int firstCoord = userCoords.indexOf(" ", 1);
        int secondCoord = userCoords.indexOf(" ", firstCoord + 1);

        inputCoords[0] = Integer.parseInt(userCoords.substring(firstCoord + 1, secondCoord)) - 1;

        inputCoords[1] = Integer.parseInt(userCoords.substring(secondCoord + 1)) - 1;

        if (inputCoords[0] > mineBoard.length - 1 || inputCoords[0] < 0 || inputCoords[1] > mineBoard[0].length - 1 || inputCoords[1] < 0)
          throw new Exception("Coordinates must be on the board");

      } catch (Exception e) {
        revealInput();
        return;
      }

      if (displayMineBoard[inputCoords[0]][inputCoords[1]] == "#")
        displayMineBoard[inputCoords[0]][inputCoords[1]] = "F";

    }

    else if (userCoords.startsWith("u")) {
      try {
        int firstCoord = userCoords.indexOf(" ", 1);
        int secondCoord = userCoords.indexOf(" ", firstCoord + 1);

        inputCoords[0] = Integer.parseInt(userCoords.substring(firstCoord + 1, secondCoord)) - 1;

        inputCoords[1] = Integer.parseInt(userCoords.substring(secondCoord + 1)) - 1;

        if (inputCoords[0] > mineBoard.length - 1 || inputCoords[0] < 0 || inputCoords[1] > mineBoard[0].length - 1 || inputCoords[1] < 0)
          throw new Exception("Coordinates must be on the board");

      } catch (Exception e) {
        revealInput();
        return;
      }

      if (displayMineBoard[inputCoords[0]][inputCoords[1]] == "F")
        displayMineBoard[inputCoords[0]][inputCoords[1]] = "#";

    }

    else {
      // turn the coordinates string into an array
      try {
        inputCoords[0] = Integer.parseInt(userCoords.substring(0, userCoords.indexOf(" "))) - 1;
        inputCoords[1] = Integer.parseInt(userCoords.substring(userCoords.indexOf(" ") + 1)) - 1;

        if (inputCoords[0] > mineBoard.length - 1 || inputCoords[0] < 0 || inputCoords[1] > mineBoard[0].length - 1 || inputCoords[1] < 0)
          throw new Exception("Coordinates must be on the board");

      } catch (Exception e) {
        revealInput();
        return;
      }

      if (displayMineBoard[inputCoords[0]][inputCoords[1]] == "F") {
        System.out.println();
        System.out.println("You cannot reveal a flagged square, unflag it first");
        revealInput();
        return;
      }

      if (firstTurn) {
        boardSetup(gameParam[0], gameParam[1], gameParam[2], inputCoords[0], inputCoords[1]);
        showNeighboringSquares(inputCoords[0], inputCoords[1]);
        firstTurn = false;
      }

      else if (mineBoard[inputCoords[0]][inputCoords[1]] == -1) {
        gameOn = false;
        for (int row = 0; row < mineBoard.length; row++)
          for (int col = 0; col < mineBoard[row].length; col++) {
            if (mineBoard[row][col] != -1 && displayMineBoard[row][col] == "F")
              displayMineBoard[row][col] = "X";
            else if (mineBoard[row][col] == -1)
              displayMineBoard[row][col] = "@";
            else
              displayMineBoard[row][col] = " ";
          }
        System.out.println();
        printBoard();
        System.out.println();
        System.out.println("You stepped on a mine and lost");
        setGameOn(false);
      } else
        showNeighboringSquares(inputCoords[0], inputCoords[1]);
    }
  }

  // get if game is active
  public boolean getGameOn() {
    return gameOn;
  }

  // set game activity
  public void setGameOn(boolean bool) {
    gameOn = bool;
  }

  // test if F or # is a -1
  public boolean win() {
    for (int i = 0; i < displayMineBoard.length; i++) {
      for (int j = 0; j < displayMineBoard[i].length; j++) {
        if (displayMineBoard[i][j].equals("F") || displayMineBoard[i][j].equals("#")) {
          if (mineBoard[i][j] == -1)
            continue;
          else
            return false;
        } else if (displayMineBoard[i][j].equals("@")) {
          return false;
        }
      }
    }
    return true;
  }

  // use user-inputted coordinates to reveal elements
  public void showNeighboringSquares(int r, int c) {
    if (mineBoard[r][c] == -1) {
      displayMineBoard[r][c] = "#";
      mineChecked[r][c] = true;
    } else if (mineBoard[r][c] != 0) {
      displayMineBoard[r][c] = String.valueOf(mineBoard[r][c]);
      mineChecked[r][c] = true;
    } else {
      if (mineBoard[r][c] == 0) {
        displayMineBoard[r][c] = " ";
        mineChecked[r][c] = true;
      }
      for (int row = r - 1; row < r + 2; row++) {
        for (int col = c - 1; col < c + 2; col++) {
          if (row == r && col == c)
            continue;
          if (row < 0 || row > mineBoard.length - 1 || col < 0 || col > mineBoard[row].length - 1)
            continue;
          if (mineChecked[row][col] == false) {
            mineChecked[row][col] = true;
            displayMineBoard[row][col] = " ";
            showNeighboringSquares(row, col);
          }
        }
      }
    }
  }
}
