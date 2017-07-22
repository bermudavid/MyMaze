package mazeproject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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
    // O for backtracking
  
  private static final String FILENAME = "input_file";
  //map 
  LinkedList<LinkedList<Character>> map = new LinkedList<>();
  Node ini;
  Node end;
  
  
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
    /**
     * mark with character
     * @param x  
     * @param y  
     * @return 
     */

    void mark(int x, int y, Character v){
      map.get(x).remove(y);
      map.get(x).add(y,v);
      printMaze(map);
      try {

       Thread.sleep(150);
      } catch (InterruptedException ex) {
        Logger.getLogger(MyMaze.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    void mark(int x, int y, Character v, LinkedList<LinkedList<Character>> maze){
      maze.get(x).remove(y);
      maze.get(x).add(y,v);
      //printMaze(map);
    }
    boolean canMove(int x, int y){
        if(x >= 0 && x < map.size() && y >= 0 && y < map.get(0).size()){
          if(map.get(x).get(y) == '.'){
            return true;
          }
        }
      return false;
    }


    
    /**
     * this represent a square
     */
    class Node{
      private Character square;
      private int x,y;

      Node (int x, int y, Character square){
        this.x = x;
        this.y = y;
        this.square = square;
      }
      public void setSquare(Character square){
        this.square = square;
      }
      Character getSq(){
        return this.square;
      }
      public int getX() {
        return x;
      }
      public void setX(int x) {
        this.x = x;
      }
      public int getY() {
        return y;
      }
      public void setY(int y) {
        this.y = y;
      }
      public Node north(){
        if(isInMaze(x,y-1)){
          return new Node(x,y-1,map.get(x).get(y-1));
        } else {
          return this;
        }
      }
      public Node east(){
        if(isInMaze(x+1,y)){
          return new Node(x+1,y,map.get(x+1).get(y));
        } else {
          return this;
        }
      }
      public Node south(){
        if(isInMaze(x,y+1)){
          return new Node(x,y+1,map.get(x).get(y+1));
        } else {
          return this;
        }
      }
      public Node west(){
        if(isInMaze(x-1,y)){
          return new Node(x-1,y,map.get(x-1).get(y));
        } else {
          return this;
        }
      }
      @Override
      public String toString(){
        
        return "("+x+","+y+","+square+")";
      }
      public Node getElement() throws IllegalStateException {
        return this; 
      }

      @Override
      public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.x;
        hash = 41 * hash + this.y;
        return hash;
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (obj == null) {
          return false;
        }
        if (getClass() != obj.getClass()) {
          return false;
        }
        final Node other = (Node) obj;
        if (this.x != other.x) {
          return false;
        }
        if (this.y != other.y) {
          return false;
        }
        return true;
      }

      
    }
    /**
     * return the distance to the end node
     * @param n node to compare
     * @return distance to end
     */
    int distanceToEnd(Node n){
      return Math.abs(n.getX() -end.getX()) + Math.abs(n.getY()-end.getY());
    }
    /**
     * return the distance to the end node
     * @param n node to compare
     * @return distance to end
     */
    int manhattanDistance(Node a,Node b){
      return Math.abs(a.getX() -b.getX()) + Math.abs(a.getY()-b.getY());
    }
    
    /**
     * check if the square is reachable
     * @param squ char to probe
     * @return true if is reachable false is not
     */
    boolean charValid(Character squ){
      if( squ == '.' || squ == 'F' ){
        return true;
      } else {
        return false;
      }
    }
    boolean haveNeigs(Node n){
      if(isClear(n.north())){
        return true;
      }
      if(isClear(n.south())){
        return true;
      }
      if(isClear(n.east())){
        return true;
      }
      if(isClear(n.west())){
        return true;
      }
      return false;
    }
    /**
     * Check is a concrete position is in maze and if is open space
     * @param i - x position
     * @param j - y position
     * @return true if is open space and is in maze
     */
    public boolean isClear(int i, int j) {
      assert(isInMaze(i,j)); 
      return (map.get(i).get(j) != 'X' && map.get(i).get(j) != 'V' && map.get(i).get(j) != 'O');
    }
    /**
     * overload to check node
     * @param pos - node to check
     * @return true if is open space and is in maze
     */
    public boolean isClear(Node pos) {
      return isClear(pos.getX(), pos.getY());
    }

    /**
     * check if position is in maze
     * @param i - x position
     * @param j - y position
     * @return true if cell is within maze
     */ 
    public boolean isInMaze(int i, int j) {
      if (i >= 0 && i<map.size() && j>= 0 && j<map.get(0).size()) return true; 
      else return false;
    }
      
    /**
     * return if a node is in maze
     * @param pos - node to check
     * @return true if node is within maze
     */
    public boolean isInMaze(Node pos) {
      if(pos == null){
        return false;
      }
      return isInMaze(pos.getX(), pos.getY());
    }
    
    /**
     * Solve maze by stack backtracing method
     * @param x position of node start
     * @param y position of node start
     * @return true if reach end false if not
     */
    public boolean solveStack(int x, int y){
      LinkedList<Node> S = new LinkedList<>(); // stack
      Node v = new Node(x,y,map.get(x).get(y)); // add initial pos
      Node next;
      S.push(v);
      
      while(!S.isEmpty()){
        v = S.peekLast();  
        if(v.getSq() == 'F') {
          mark(v.getX(),v.getY(),'V');
          v.setSquare('V');
          break;
        }
        //mark as visited
        mark(v.getX(),v.getY(),'V');
        v.setSquare('V');
        c:if(haveNeigs(v)){ // have neighborgs
          //mar as visited
          
          // use break to take only one
          next  = v.south();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
            break c;
          }
          next  = v.east();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
            break c;
          }
          next  = v.north();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
            break c;
          }
          next  = v.west();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
            break c;
          }   
        } else {
          mark(v.getX(),v.getY(),'O');
          v.setSquare('O');
          S.removeLast();
        }
      }
      //restore maz & show path
      removeMazeBacktracing(map);
      System.out.println("Path:");
      printMaze(map);
      return true;
    }
    public boolean solveQueue(int x, int y){
      Queue<Node> S = new LinkedList<>(); // queue
      Node v = new Node(x,y,map.get(x).get(y)); // add initial pos
      Node next;
      S.add(v); //start node
      
      while(!S.isEmpty()){
        v = S.remove();  
        if(v.getSq() == 'F') {
          break;
        }
        //mark as visited
        mark(v.getX(),v.getY(),'V');
        v.setSquare('V');
        if(haveNeigs(v)){ // have neighborgs
          next  = v.south();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
          }
          next  = v.east();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
          }
          next  = v.north();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
          }
          next  = v.west();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
          }   
        } 
      }
      //printMaze(map);
      return true;
    }
    public boolean solveHeap(int x, int y){
      //LinkedList<Node> S = new LinkedList<>(); // stack
      Node v = new Node(x,y,map.get(x).get(y)); // add initial pos
      Node next;
 
      PriorityQueue<Node> S = new PriorityQueue<>(10, new Comparator<Node>() {
        @Override
        public int compare(Node n1, Node n2) {
          Integer d1 = distanceToEnd(n1);
          Integer d2 = distanceToEnd(n2); 
          return Integer.compare(d1, d2);        }
      });
      S.add(v);
      while(!S.isEmpty()){
        
        v = S.poll();  
        if(v.getSq() == 'F') {
          mark(v.getX(),v.getY(),'V');
          v.setSquare('V');
          break;
        }
        //mark as visited
        mark(v.getX(),v.getY(),'V');
        v.setSquare('V');
        if(haveNeigs(v)){ // have neighborgs
          //mar as visited
          
          // use break to take only one
          
          next  = v.east();
          if(isInMaze(next) && isClear(next)){
            S.add(next);

          }
          
          next  = v.south();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
            
          }
          next  = v.north();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
            
          }
          next  = v.west();
          if(isInMaze(next) && isClear(next)){
            S.add(next);
          
          }  
          
          System.out.println(S.toString());
        } 
      }
      return true;
    }
    
    void removeMazeBacktracing(LinkedList<LinkedList<Character>>map){
      for (int i = 0; i < map.size(); i++) {
        for (int j = 0; j < map.get(0).size(); j++) {
          if(map.get(i).get(j) == 'O'){
            mark(i,j,'.',map);
          }
        }
      }
    }
    
    /**
     * solve the maze
     * @param x - init.
     * @param y - end.
     */
    public void solveMaze(int x, int y) {
      //solveStack(x,y);
      //solveQueue(x,y);
      //solveHeap(x,y);
      Vertex<Node> u = null;
	  Graph<Node,String> G = graphFromMap(map,false);
      for(Vertex<Node> v : G.vertices()){
        if(v.getElement().equals(ini)){
          u = v;
          break;
        }
        else
          u = v;
      }
      graphSolveDFS(G,u);
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
      this.ini = new Node(y,x,'S'); 
    }
    public void endPos(int x,int y){
      map.get(y).remove(x);
      map.get(y).add(x, 'F');
      this.end = new Node(y,x,'S'); 
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
      //printPreMaze();
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
      solveMaze(x,y);
      //printPreMaze();
    }
    String tagger(String res,int c){
      switch(c%11){
        case 0:
          return res +"A";
        case 1:
          return res + "B";
        case 2:
          return res + "C";
        case 3:
          return res + "D";
        case 5:
          return res + "E";
        case 6:
          return res + "F";
        case 7:
          return res + "G";
        case 8:
          return res + "H";
        case 9:
          return res + "I";
        case 10:
          return res + "J";
      }
      return res;
    }
    /**
     * solve the maze by DFS
     * @return 
     */
    boolean graphSolveDFS(Graph<Node,String> G, Vertex<Node> v){
      LinkedList<Vertex<Node>> S = new LinkedList<>();    
      S.add(v);
      while(!S.isEmpty()){
        v = S.pop();
        if(v.getElement().getSq() == 'F'){          
          return true;
        }
        if(v.getElement().getSq() != 'V'){
          mark(v.getElement().getX(),v.getElement().getY(),'V');
          v.getElement().setSquare('V');
          for(Edge<String> w : G.outgoingEdges(v)){
            S.push(G.opposite(v,w));
            System.out.println(v.getElement()+ "->"+w.getElement() + "->" + G.opposite(v,w).getElement());
          }
        }
      }
      return true;
    }
    /*
    boolean graphSolveBFS(Graph<Node,String> G, Vertex<Node> v){
      //LinkedList<Vertex<Node>> S = new LinkedList<>();
      Queue<Vertex<Node>> S = new LinkedList<>();
      S.add(v);   
      while(!S.isEmpty()){
        v = S.pop();
        if(v.getElement().getSq() == 'F'){          
          return true;
        }
        if(v.getElement().getSq() != 'V'){
          mark(v.getElement().getX(),v.getElement().getY(),'V');
          v.getElement().setSquare('V');
          for(Edge<String> w : G.outgoingEdges(v)){
            S.push(G.opposite(v,w));
            System.out.println(v.getElement()+ "->"+w.getElement() + "->" + G.opposite(v,w).getElement());
          }
        }
      }
      
      return true;
    }
       */
    /**
     * Returns a Graph from a map
     * @param map - LinkedList<LinkedList<Character>> with the data
     * @param directed if is true returns a directed graph
     * @return Graph
     */
    public Graph<Node,String> graphFromMap(
            LinkedList<LinkedList<Character>> map,
            boolean directed
    ) {
     
      Graph<Node, String> G = new AdjacencyMapGraph<>(directed);
      Map<Node, Vertex<Node> > verts = new ProbeHashMap<>();
      LinkedList<Node> list = new LinkedList<>();

      for (int i = 0; i < map.size(); i++) {
        for (int j = 0; j < map.get(0).size(); j++) {
          Node tmp = new Node(i,j,map.get(i).get(j));
          if(tmp.getSq() != 'X'){
            
            verts.put(tmp, G.insertVertex(tmp));
            list.add(tmp);
          }
        }
      }
      
      Node b;
      Integer cost;
      //int cont = 0;
      for(Node a: list){
        b = a.west();
        if(verts.get(b) != null && G.getEdge(verts.get(a),verts.get(b)) == null && b!= a ){
          cost = manhattanDistance(a,b);
          G.insertEdge(verts.get(a), verts.get(b), ""+cost);
          //cont++;
        }
        b = a.east();
        if(verts.get(b) != null && G.getEdge(verts.get(a),verts.get(b)) == null && b!= a ){
          cost = manhattanDistance(a,b);
          G.insertEdge(verts.get(a), verts.get(b), ""+cost);
          //cont++;
        }
        
        b = a.south();
        if(verts.get(b) != null && G.getEdge(verts.get(a),verts.get(b)) == null && b!= a ){
          cost = manhattanDistance(a,b);
          G.insertEdge(verts.get(a), verts.get(b), ""+cost);
          //cont++;
        }
        
        b = a.north();
        if(verts.get(b) != null && G.getEdge(verts.get(a),verts.get(b)) == null && b!= a ){
          cost = manhattanDistance(a,b);
          G.insertEdge(verts.get(a), verts.get(b),""+cost);
          //cont++;
        }
        
        
        
      }
       return G;
     }
}