import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.event.*;
public class AStarImplementation extends JFrame implements KeyListener{
    public static final int WORLDX = 640;
    public static final int WORLDY = 640;
    public static final int GRIDX = 8;
    public static final int GRIDY = 8;
    public static int nodeSpanX, nodeSpanY;
    boolean [][] nodeField = new boolean[32][32];
    Node [][] nodes;
    
    ArrayList<Node> openList = new ArrayList<Node>();
    ArrayList<Node> closedList = new ArrayList<Node>();
    
    static boolean drawNodeInfo;
    Node startNode, endNode;
    Node selectionNode;
    public static void main(){
        nodeSpanX = WORLDX / GRIDX;
        nodeSpanY = WORLDY / GRIDY;
        drawNodeInfo = false;
        AStarImplementation a = new AStarImplementation();
        a.setSize(WORLDX, WORLDY);
        
        a.setLocationRelativeTo(null);
        a.setFocusable(true);
        //a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        a.setUndecorated(true);
        a.setVisible(true);
        
        
    }
    
    public AStarImplementation(){
        addKeyListener(this);
        //init grid
        nodes = new Node[GRIDX][GRIDY];
        for (int x = 0; x < GRIDX; x++){
            for (int y = 0; y < GRIDY; y++){
                nodes[x][y] = new Node(x, y);
            }
        }
        selectionNode = nodes[1][1];
        //set start and end nodes
        startNode = nodes[0][0];
        endNode = nodes[7][7];
        
        //fill in some nodes
        nodes[4][4].filled = true;
        nodes[5][4].filled = true;
        nodes[3][4].filled = true;
        nodes[3][2].filled = true;
        nodes[4][3].filled = true;
        nodes[4][2].filled = true;
        //nodes[6][6].goalNode = true;
        performSearch(startNode, endNode);
        repaint();
    }
    
    public void paint(Graphics g){
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0, 640, 640);
        
        //FILL IN TAKEN SQUARES
        g.setColor(new Color(255,0,0));
        for (int x = 0; x < GRIDX; x++){
            for (int y = 0; y < GRIDY; y++){
                if (nodes[x][y].filled){
                    g.setColor(new Color(255,0,0));
                    g.fillRect(x*nodeSpanX, y*nodeSpanX, nodeSpanX, nodeSpanX);
                }
                
                if (nodes[x][y].goalNode){
                    g.setColor(new Color(0,255,0));
                    g.fillRect(x*nodeSpanX, y*nodeSpanY, nodeSpanX, nodeSpanY);
                }
            }
        }
        
        //FILL IN OPEN LIST - DEBUG ONLY
        g.setColor(new Color(0,0,255));
        g.fillRect(startNode.x*nodeSpanX, startNode.y*nodeSpanX, nodeSpanX, nodeSpanY);
        g.setColor(new Color(0,255,0));
        g.fillRect(endNode.x*nodeSpanX, endNode.y*nodeSpanX, nodeSpanX, nodeSpanY);
        
        //Draw Grid 31 + 31 lines
        
        g.setColor(new Color(120,120,120));
        for (int x = 0; x < GRIDX - 1; x++){
            g.drawLine(x*nodeSpanX + nodeSpanX, 0, x*nodeSpanX + nodeSpanX, 640);
        }
        for (int y = 0; y < GRIDY - 1; y++){
            g.drawLine(0, y*nodeSpanY + nodeSpanY, 640, y*nodeSpanY + nodeSpanY);
        }
        
        //Draw Heuristics - CONDITIONAL
        if (drawNodeInfo){
            for (int x = 0; x < GRIDX; x++){
                for (int y = 0; y < GRIDY; y++){
                    g.drawString(""+ nodes[x][y].h, nodes[x][y].x*nodeSpanX + 40, nodes[x][y].y*nodeSpanY + 70);
                    g.drawString(""+ nodes[x][y].g, nodes[x][y].x*nodeSpanX + 5, nodes[x][y].y*nodeSpanY + 70);
                    g.drawString(""+ nodes[x][y].f, nodes[x][y].x*nodeSpanX + 5, nodes[x][y].y*nodeSpanY + 20);
                }
            }
        }
        
        //DRAW PATH
        Node n = endNode;
        g.setColor(new Color(255,255,255));
        while (n.g > 0){
            g.drawLine(n.x*nodeSpanX + (nodeSpanX/2), n.y*nodeSpanY + (nodeSpanY/2), n.parentNode.x*nodeSpanX + (nodeSpanX/2), n.parentNode.y*nodeSpanY + (nodeSpanY/2));
            n = n.parentNode;
        }
        
        //Draw Selection Node
        g.setColor(new Color(255,255,255));
        g.drawRect(selectionNode.x*nodeSpanX, selectionNode.y*nodeSpanY, nodeSpanX, nodeSpanY);
    }
    
    public void performSearch(Node startNode, Node endNode){
        for (int x = 0; x < GRIDX; x++){
            for (int y = 0; y < GRIDY; y++){
                nodes[x][y].setHeuristic(endNode.x, endNode.y);
                //nodes[x][y].zeroHeuristic();
                nodes[x][y].g = 0;
                nodes[x][y].calculateF();
            }
        }
        
        openList.add(startNode);
        int openListCount = 1;
        int closedListCount = 0;
        //add 8 neighbor nodes to openList
        //check and add bottom left
        while (openListCount > 0){
            //find lowest f cost in openList
            int lowestIndex = 0;
            for (int i = 0; i < openListCount; i++){
                if ( openList.get(i).f < openList.get(lowestIndex).f) lowestIndex = i;
            }
            //remove this node from the open list
            Node currentNode = openList.get(lowestIndex);
            openList.remove(currentNode);
            openListCount--;
            //add the node to the closed list
            closedList.add(currentNode);
            
            closedListCount++;
            if (currentNode == endNode) break;
            //now check conditions for each neighbor node
            
            //south west
            
            if (currentNode.x - 1 >= 0 && currentNode.y - 1 >= 0){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x - 1][currentNode.y - 1];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //south
            
            if (currentNode.x >= 0 && currentNode.y - 1 >= 0){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x][currentNode.y - 1];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //west
            
            if (currentNode.x - 1 >= 0 && currentNode.y >= 0){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x - 1][currentNode.y];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //north west
            
            if (currentNode.x - 1 >= 0 && currentNode.y + 1 <= GRIDY - 1){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x - 1][currentNode.y + 1];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //north
            
            if (currentNode.x >= 0 && currentNode.y + 1 <= GRIDY - 1){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x][currentNode.y + 1];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //north east
            
            if (currentNode.x + 1 <= GRIDX - 1 && currentNode.y + 1 <= GRIDY - 1){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x + 1][currentNode.y + 1];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //east
            
            if (currentNode.x + 1 <= GRIDX - 1 && currentNode.y >= 0){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x + 1][currentNode.y];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
            
            //south east
            
            if (currentNode.x + 1 <= GRIDX - 1 && currentNode.y - 1 >= 0){//check to see if south west falls out of grid
                Node testNode = nodes[currentNode.x + 1][currentNode.y - 1];
                if (testNode.filled == false && closedList.contains(testNode) == false){//check to see if south west is a wall
                    if (openList.contains(testNode) == false){//check to see if south west node is in openList
                        testNode.parentNode = currentNode;
                        testNode.setG(currentNode);
                        testNode.calculateF();
                        openList.add(testNode);//bottom left
                        openListCount++;
                    } else {
                        if (getGCost(currentNode.x, currentNode.y, testNode.x, testNode.y) + currentNode.g < testNode.g){
                            testNode.parentNode = currentNode;
                            testNode.setG(currentNode);
                            testNode.calculateF();
                        }
                    }
                    
                }
            }
        //openList.add(nodes[startNode.x + 1][startNode.y - 1]);//bottom right
        //endNode = currentNode;
    }
    openList.clear();
    closedList.clear();
    
    }
    
    public void testNode(Node currentNode, Node testNode){
    }
    public int getGCost(int x1, int y1, int x2, int y2){
        int cost;
        if (x1 == x2 || y1 == y2){
            cost = 10;
        } else {
            cost = 14;
        }
        //System.out.println("NGIHSFDJK");
        return cost;
        
    }
    
    
    
    
    public void keyTyped(KeyEvent e) {
        
    }

    
    public void keyPressed(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            if (selectionNode.x > 0){
                selectionNode = nodes[selectionNode.x - 1][selectionNode.y];
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            if (selectionNode.x < GRIDX - 1){
                selectionNode = nodes[selectionNode.x + 1][selectionNode.y];
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){
            if (selectionNode.y > 0){
                selectionNode = nodes[selectionNode.x][selectionNode.y - 1];
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            if (selectionNode.y < GRIDY - 1){
                selectionNode = nodes[selectionNode.x][selectionNode.y + 1];
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            selectionNode.filled = !selectionNode.filled;
        }
        if (e.getKeyCode() == KeyEvent.VK_C){
            performSearch(startNode, endNode);
        }
        if (e.getKeyCode() == KeyEvent.VK_T){
            drawNodeInfo = !drawNodeInfo;
        }
        if (e.getKeyCode() == KeyEvent.VK_S){
            startNode = selectionNode;
        }
        if (e.getKeyCode() == KeyEvent.VK_E){
            selectionNode.goalNode = !selectionNode.goalNode;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q){System.exit(0);}
        repaint();
    }

    /** Handle the key-released event from the text field. */
    public void keyReleased(KeyEvent e) {
        
    }
    
    class Node{
    public Node(int x, int y){
        filled = false;
        this.x = x;
        this.y = y;
        state = 0;
        goalNode = false;
    }
    //state tells us explicity if it is opened, closed or unsearched.
    int state;
    //filled true means its an obstacle
    boolean filled;
    boolean goalNode;
    //position
    int x, y;
    //parent node
    Node parentNode;
    //PATH SCORE
    int h;
    int g;
    int f;
    //Sets heuristic according to x and y (end point)
    public void setHeuristic(int x, int y){
        h = 10*(Math.abs(this.x - x) + Math.abs(this.y - y));
    }
    
    public void zeroHeuristic(){
        h = 0;
    }
    
    public void setG(Node parent){
        if (parent.x == x || parent.y == x){
            g = parent.g + 10;
        } else {
            g = parent.g + 14;
        }
    }
    
    public void calculateF(){
        f = g + h;
    }
}
}