package mazeproject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
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

  //char [][] map; 
  LinkedList<LinkedList<Character>> map = new LinkedList<>();
   java.awt.Point start = new java.awt.Point();
   
    public static void main(String[] args) {
        new MyMaze();
        
    }
    public void initMaze(int x,int y){
      //map = new char[y][x];
      /*for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[0].length; j++) {
          map[i][j] = '.';
        }*/
        for(int i = 0; i < y; i ++){
          LinkedList<Character> tmp = new LinkedList<Character>();
          for (int j = 0; j < x; j++) {
            tmp.add('.');
          }
          map.add(tmp);
        }
      }
    
    
    public void printPreMaze(){
      /*for(char[] col: map){
        for(char sq : col){
          System.out.print(sq);
        }
        System.out.println("");
      }*/
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
        char[][] white = { 
          {'.','.','.'},
          {'.','.','.'},
          {'.','.','.'}
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < map.size(); i++) {
          for (int k = 0; k < 3; k++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
            
              switch(map.get(i).get(j)){
                case '.':
                  sb.append(white[k]);
                  break;
                case 's':
                  sb.append(init[k]);
                  break;
                case 'f':
                  sb.append(end[k]);
                  break;
                case 'X':
                  sb.append(wall[k]);
                  break;
                  
              }
            }
            sb.append("\n");
          }
          
         // fileOutput.println();
        }
        fileOutput.write(sb.toString());
        fileOutput.close();
      } catch (FileNotFoundException ex) {
        Logger.getLogger(MyMaze.class.getName()).log(Level.SEVERE, null, ex);
      } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(MyMaze.class.getName()).log(Level.SEVERE, null, ex);
      }

    }
    /**
     * map must be started
     * @param x 
     * @param y 
     */
    public void startPos(int x,int y){
      //map[y][x] = 's';
      map.get(y).remove(x);
      map.get(y).add(x, 's');
    }
    public void endPos(int x,int y){
      //map[y][x] = 'f';
      map.get(y).remove(x);
      map.get(y).add(x, 'f');
    }
    public void fillWallsByLine(String[] line){
      for(String pos: line){
        if(pos.matches("(.*)[0-9]")){
          pos = pos.replace("(", "");
          pos = pos.replace(")", "");
          int x = Integer.parseInt(pos.split(",")[0].trim());
          int y = Integer.parseInt(pos.split(",")[1].trim());
          //map[y][x] = 'X';
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
        String[] a = {""};
      
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
      
    }

}