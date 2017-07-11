package mazeproject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @author Andrew.E
 */
public class MyMaze {
  
    // S for starting postion
    // F for finish
    // X for wall
    // . for open space
  
  private static final String FILENAME = "input_file";
  //map 
  LinkedList<LinkedList<Character>> map = new LinkedList<>();
   java.awt.Point start = new java.awt.Point();
   
    public static void main(String[] args) {
      new MyMaze();
    }
    
    public void initMaze(int x,int y){
        for(int i = 0; i < y; i ++){
          LinkedList<Character> tmp = new LinkedList<>();
          for (int j = 0; j < x; j++) {
            tmp.add('.');
          }
          map.add(tmp);
        }
      }
    
    /**
     * Clone linked list of characters
     * @param n - linke list to clone
     * @return a clone of the linked list passed by params 
     */
    public  LinkedList<LinkedList<Character>> clone(LinkedList<LinkedList<Character>> n){
      LinkedList<LinkedList<Character>> copy = new LinkedList<>();
      int y = n.get(0).size();
      int x = n.size();
      for (int i = 0; i < x; i++) {
        LinkedList<Character> tmp = new LinkedList<>();
        for (int j = 0; j < y; j++) {
          tmp.add(n.get(i).get(j));
        }
        copy.add(tmp);
      }
      return copy;
    }
    //test
    int counter = 0;
    public boolean step(int x, int y){
      counter ++;
      // dead end
      if(x < 0 || y < 0 || x >= map.size() || y >= map.get(0).size()){
        printMaze(map);
        return false;
      }
      //if are ok, finish
      if(map.get(x).get(y) == 'F'){
        map.get(x).remove(y);
        map.get(x).add(y,'f'); // to see 
        return true;
      }
      //if is a wall or are visited square
      if (map.get(x).get(y) == 'X' || map.get(x).get(y) == 'V') {
			return false;
		}
      // maze step
      map.get(x).set(y,'V');
      boolean result = false;
      //east
      result = step(x,y+1);
      if(result){return true;}
      //north
      result = step(x+1,y);
      if(result){return true;}
      //west 
      result = step(x,y-1);
      if(result){return true;}
      //south
      result = step(x-1,y);
      if(result){return true;}
      //dead end
      map.get(x).remove(y);
      map.get(x).add(y,'.');
      //go back
      return false;
    }
    
    
    /**
     * solve the maze
     * @param x - init.
     * @param y - end.
     */
    public void solveMaze(int x, int y) {
      if(step(x,y)){
        map.get(y).remove(x);
        map.get(y).add(x,'S');
        printMaze(map);
      }
    }
    public int[] findInit(){
      int[] res = {0,0};
      for (int i = 0; i < map.size(); i++) {
        for (int j = 0; j < map.get(i).size(); j++) {
          if(map.get(i).get(j) == 'S'){
            res[0] = i;
            res[1] = j;
          }
        }
      }
      return res;
    }
    
    public void printMaze(LinkedList<LinkedList<Character>> map){
      System.out.println("");
      for(LinkedList<Character> cols: map){
        for(Character sq : cols){
          System.out.print(sq);
        }
        System.out.println("");
      }
    }
    
    
    public void printPreMaze(){
      for(LinkedList<Character> cols: map){
        for(Character sq : cols){
          System.out.print(sq);
        }
        System.out.println("");
      }

      try {
        PrintWriter fileOutput = new PrintWriter("output_file.txt", "UTF-8");
        char[][] wall = { 
          {'X','X','X'},
          {'X','X','X'},
          {'X','X','X'}
        };
        char[][] init = { 
          {'.','.','.'},
          {'.','S','.'},
          {'.','.','.'}
        };
        char[][] end = { 
          {'.','.','.'},
          {'.','F','.'},
          {'.','.','.'}
        };
        char[][] open = { 
          {'.','.','.'},
          {'.','.','.'},
          {'.','.','.'}
        };
        char[][] visited = { 
          {'.','.','.'},
          {'.','V','.'},
          {'.','.','.'}
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < map.size(); i++) {
          for (int k = 0; k < 3; k++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
            
              switch(map.get(i).get(j)){
                case '.':
                  sb.append(open[k]);
                  break;
                case 'S':
                  sb.append(init[k]);
                  break;
                case 'F':
                  sb.append(end[k]);
                  break;
                case 'X':
                  sb.append(wall[k]);
                  break;
                case 'V':
                  sb.append(visited[k]);
                  break;
              }
            }
            sb.append("\n");
          }
        }
        fileOutput.write(sb.toString());
        fileOutput.close();
      } catch (FileNotFoundException ex) {
          System.out.println("File Not Found: "+ex);
      } catch (UnsupportedEncodingException ex) {
          System.out.println("Unsupported Encoding: "+ex);
      }

    }
    /**
     * map must be started
     * @param x 
     * @param y 
     */
    public void startPos(int x,int y){
      map.get(y).remove(x);
      map.get(y).add(x, 'S');
    }
    public void endPos(int x,int y){
      map.get(y).remove(x);
      map.get(y).add(x, 'F');
    }
    public void fillWallsByLine(String[] line){
      for(String pos: line){
        if(pos.matches("(.*)[0-9]")){
          pos = pos.replace("(", "");
          pos = pos.replace(")", "");
          int x = Integer.parseInt(pos.split(",")[0].trim());
          int y = Integer.parseInt(pos.split(",")[1].trim());
          map.get(y).remove(x);
          map.get(y).add(x, 'X');
        } 
      }
    }
    public MyMaze(){
      try {
      FileReader input = new FileReader("input_file.txt");
      BufferedReader br = new BufferedReader(input);
      String line;
      
      // to take the 3 first lines
      int lineCounter = 1;
      while ((line = br.readLine())!=null) {
        line = line.replace(".","");
        if(lineCounter == 1){
          String[] init = line.split(",");
          int x = Integer.parseInt(init[0].trim());
          int y = Integer.parseInt(init[1].trim());
          initMaze(x,y);
        }
        if(lineCounter == 2){
          line = line.replace("(","");
          line = line.replace(")","");
          String[] init = line.split(",");
          int x = Integer.parseInt(init[0].trim());
          int y = Integer.parseInt(init[1].trim());
          startPos(x,y);
        }
        if(lineCounter == 3){
          line = line.replace("(","");
          line = line.replace(")","");
          String[] init = line.split(",");
          int x = Integer.parseInt(init[0].trim());
          int y = Integer.parseInt(init[1].trim());
          endPos(x,y);
        }
        if(lineCounter >3 ){
          fillWallsByLine(line.split("[)]{1}[,]{1}"));
        }
        
        lineCounter++;
      }
      input.close();
      printPreMaze();
      }
      catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException ex) {
        Logger.getLogger(MyMaze.class.getName()).log(Level.SEVERE, null, ex);
      }
      // path finding
      int[] init = findInit();
      int x = init[0];
      int y = init[1];
      solveMaze(0,0);
      printPreMaze();
      
    }

}