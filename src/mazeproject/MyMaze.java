package mazeproject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
    interface Position<E> {
    /**
     * returns the stament in position
     * @throws IllegalStateException 
     */
      E getElement() throws IllegalStateException;
    }
    
    class Graph<V,E> {
      //Nested classes
      private class Edge<E>{
        private E element;
        private Position<Edge<E>> pos;
        private Vertex<V>[ ] endpoints;
        /** 
         * Constructs Edge instance from u to v, storing the given element.
         */
        public Edge(Vertex<V> u, Vertex<V> v, E elem) {
          element = elem;
          endpoints = (Vertex<V>[ ]) new Vertex[ ]{u,v}; // array of length 2
        }
        /** Returns the element associated with the edge. */
        public E getElement() { return element; }
        /** Returns reference to the endpoint array. */
        public Vertex<V>[ ] getEndpoints() { return endpoints; }
        /** Stores the position of this edge within the graph's vertex list. */
        public void setPosition(Position<Edge<E>> p) { pos = p; }
        /** Returns the position of this edge within the graph's vertex list. */
        public Position<Edge<E>> getPosition() { return pos; }
      }
      private class Vertex<V>{
        private V element;
        private Position<Vertex<V>> pos;
        private Map<Vertex<V>, Edge<E>> outgoing, incoming;

        public Vertex(V elem, boolean graphIsDirected) {
          element = elem;
          outgoing = new HashMap<>();
          if (graphIsDirected)
            incoming = new HashMap<>();
          else
            incoming = outgoing; // if undirected, alias outgoing map
          }
          /** Returns the element associated with the vertex. */
          public V getElement() { return element; }
          /** Stores the position of this vertex within the graph's vertex list. */
          public void setPosition(Position<Vertex<V>> p) { pos = p; }
          /** Returns the position of this vertex within the graph's vertex list. */
          public Position<Vertex<V>> getPosition() { return pos; }
          /** Returns reference to the underlying map of outgoing edges. */
          public Map<Vertex<V>, Edge<E>> getOutgoing() { return outgoing; }
          /** Returns reference to the underlying map of incoming edges. */
          public Map<Vertex<V>, Edge<E>> getIncoming() { return incoming; }
      }
      
      
      //instance variables
      private boolean isDirected;
      private LinkedList<Vertex<V>> vertices = new LinkedList<>();
      private LinkedList<Edge<E>> edges = new LinkedList<>();
      //methods
      
      
      public Graph(boolean directed) { isDirected = directed; }
      /** Returns the number of vertices of the graph */
      public int numVertices() { return vertices.size(); }
      /**Returns the vertices of the graph as an iterable collection */
      public Iterable<Vertex<V>> vertices() { return vertices; }
      /**Returns the number of edges of the graph */
      public int numEdges() { return edges.size(); }
      /**Returns the edges of the graph as an iterable collection */
      public Iterable<Edge<E>> edges() { return edges; }
      /**Returns the number of edges for which vertex v is the origin. */
      public int outDegree(Vertex<V> v) {
        Vertex<V> vert = validate(v);
        return vert.getOutgoing().size();
      }
      /**Returns an iterable collection of edges for which vertex v is the origin. */
      public Iterable<Edge<E>> outgoingEdges(Vertex<V> v) {
        Vertex<V> vert = validate(v);
        return vert.getOutgoing().values(); // edges are the values in the adjacency map
      }
      /**Returns the number of edges for which vertex v is the destination. */
      public int inDegree(Vertex<V> v) {
        Vertex<V> vert = validate(v);
        return vert.getIncoming().size();
      }
      /** Returns an iterable collection of edges for which vertex v is the destination. */
      public Iterable<Edge<E>> incomingEdges(Vertex<V> v) {
        Vertex<V> vert = validate(v);
        return vert.getIncoming().values(); // edges are the values in the adjacency map
      }
      public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) {
      /** Returns the edge from u to v, or null if they are not adjacent. */
        Vertex<V> origin = validate(u);
        return origin.getOutgoing().get(v); // will be null if no edge from u to v
      }
      /** Returns the vertices of edge e as an array of length two. */
      public Vertex<V>[ ] endVertices(Edge<E> e) {
        Edge<E> edge = validate(e);
        return edge.getEndpoints();
      }
      /* Returns the vertex that is opposite vertex v on edge e. */
      public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException {
        Edge<E> edge = validate(e);
        Vertex<V>[ ] endpoints = edge.getEndpoints();
        
		if (endpoints[0] == v)
          return endpoints[1];
        else if (endpoints[1] == v)
          return endpoints[0];
        else
          throw new IllegalArgumentException("v is not incident to this edge");
      }
      /** Inserts and returns a new vertex with the given element. */
      public Vertex<V> insertVertex(V element) {
        Vertex<V> v = new Vertex<>(element, isDirected);
        vertices.addLast(v);
        v.setPosition(vertices.getLast().pos);
        return v;
      }
      /** Inserts and returns a new edge between u and v, storing given element. */
      public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException {
        if (getEdge(u,v) == null) {
          Edge<E> e = new Edge<>(u, v, element);
          edges.addLast(e);
          e.setPosition(edges.getLast().pos);
          Vertex<V> origin = validate(u);
          Vertex<V> dest = validate(v);
          origin.getOutgoing().put(v, e);
          dest.getIncoming().put(u, e);
          return e;
        } else {
		    throw new IllegalArgumentException("Edge from u to v exists");
		}
      }
      /**Removes a vertex and all its incident edges from the graph. */
      public void removeVertex(Vertex<V> v) {
        Vertex<V> vert = validate(v);
        // remove all incident edges from the graph
        for (Edge<E> e : vert.getOutgoing().values())
          removeEdge(e);
        for (Edge<E> e : vert.getIncoming().values())
          removeEdge(e);
          // remove this vertex from the list of vertices
        vertices.remove(vert.getPosition());
        
      }
      /** Not implemented yet */
      public Vertex<V> validate(Vertex<V> v){
        if(v != null)
          return v;
        else
          throw new IllegalArgumentException("Is invalid");
      }
      /** Not implemented yet */
      public Edge<E> validate(Edge<E> e){
        
        return e;
      }
      public void removeEdge(Edge<E> e){
        edges.remove(e);
      }
      
    }

    
    boolean graphSolveDFS(){
      Graph<Node,Integer> G = new Graph<>(false);
      G.insertVertex(ini);
      Node n = ini,s = ini ,e = ini ,w = ini;

      for(int i = 0; i < map.size();i++){
		for(int j = 0; j < map.get(0).size();j++){
          n = n.north();
          if(n != null && n.square != 'X'){
            G.insertVertex(n);
          }
          s = s.south();
          if(s != null && s.square != 'X'){
            G.insertVertex(s);
          }
          e = e.east();
          if(e != null && e.square != 'X'){
            G.insertVertex(e);
          }
          w = w.west();
          if(w != null && w.square != 'X'){
            G.insertVertex(w);
          }
        }
      }
      
      
      System.out.println("Vertex: "+G.numVertices() + " Edges: "+ G.numEdges());
      
      
      
      return true;
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
      graphSolveDFS();
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

}