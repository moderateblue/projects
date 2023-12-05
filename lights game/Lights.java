import java.util.Scanner;
import java.lang.Math;

class Lights {
  int[][] lightsGrid = {{-1, 0, 0, 0, 0, 0, -1},
                        { 0, 0, 0, 0, 0, 0, 0},
                        { 0, 0, 0, 0, 0, 0, 0},
                        { 0, 0, 0, 0, 0, 0, 0},
                        { 0, 0, 0, 0, 0, 0, 0},
                        { 0, 0, 0, 0, 0, 0, 0},
                        {-1, 0, 0, 0, 0, 0, -1}};

  public void setup() {
    for (int i = 0; i < 50; i++) {
      int randx = 1 + (int)(Math.random() * 5);
      int randy = 1 + (int)(Math.random() * 5);
      toggle(randy, randx);
      //System.out.println("toggled " + randx + " " + randy);
    }
  }

  public boolean check() {
    int countZero = 0;
    int countOne = 0;
    for (int i = 0; i < lightsGrid.length; i++) {
      for (int j = 0; j < lightsGrid[i].length; j++) {
        if (lightsGrid[i][j] == 0) countZero++;
        else if (lightsGrid[i][j] == 1) countOne++;
      }
    }
    if (countZero == 45 || countOne == 45) return true;
    else return false;
  }

  public void flip(int y, int x) {
    if (lightsGrid[y][x] == 1) lightsGrid[y][x] = 0;
    else lightsGrid[y][x] = 1;
  }
  
  public boolean toggle(int x, int y) {
    if (x < 1 || x > 5) return false;
    if (y < 1 || y > 5) return false;
    flip(y, x-1);
    flip(y-1, x);
    flip(y, x);
    flip(y, x+1);
    flip(y+1, x);
    return true;
  }

  public String toString() {
    String grid = "";
    for (int i = 0; i < lightsGrid.length; i++) {
      for (int j = 0; j < lightsGrid[i].length; j++) {
        if (lightsGrid[i][j] == -1) grid += "  ";
        else if (lightsGrid[i][j] == 0) grid += "0 ";
        else grid += "1 ";
      }
      grid += "\n";
    }
    return grid;
  }

  public Lights() {
    Scanner in = new Scanner(System.in);
    setup();
    int moves = 0;
    while(!check()) {
      System.out.println(toString());
      System.out.println("input coords: ");
      int x = in.nextInt();
      int y = in.nextInt();
      if (!toggle(x, y)) {
        System.out.println("invalid coords");
        continue;
      }
      moves++;
    }
    System.out.println("Solved in " + moves + " moves!");
  }
}
