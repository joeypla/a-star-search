import java.util.PriorityQueue;
import java.util.ArrayList;

class AStarImplementation{
    
    Int2 nodeCount;
    
    Node [][] nodes;

    PriorityQueue<Node> openSet = new PriorityQueue<Node>();

    private ArrayList<Node> GetNeighborsFor(Node n){
        Int2 loc = n.loc;
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                if (x == 0 && y == 0) continue; // skip center cell.

                Node neighbor = GetNode(new Int2(loc.x + x, loc.y + y));
                if (neighbor != null){
                    neighbors.add(neighbor);
                }

            }
        }

        return neighbors;
    }

    private Node GetNode(Int2 cell){
        if (cell.x < 0 || cell.y < 0) return null;
        if (cell.x > nodeCount.x || cell.y > nodeCount.y) return null;

        return nodes[cell.x][cell.y];
    }


    public void search(Node start, Node goal){
        while (openSet.size() > 0){
            Node current = openSet.remove(); // Pull lowest f-value.
            if (current == goal) break; // we're done.

            ArrayList<Node> Neighbors = GetNeighborsFor(current);
            for (Node n : Neighbors){
                float g = current.g + Int2.distance(n.loc, current.loc);
                if (g < n.g){
                    n.parent = current;
                    n.g = g;
                    n.f = n.g + n.h;
                    if (!openSet.contains(n))
                        openSet.add(n);
                }

            }
        }
    }

    class Int2{ 
        public int x = 0;
        public int y = 0;
        public Int2(int x, int y) { this.x = x; this.y = y; }
        public static float distance(Int2 b, Int2 a){ return (float)Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));}
    }
    class Node implements Comparable<Node>{
        static final int VERY_LARGE = 832187;
        public Int2 loc;
        Node parent;
        public float h;
        public float g;
        public float f;
        Node [] neighbors;
        public Node(int x, int y){
            g = VERY_LARGE;
        }
    
        @Override // Needed for PriorityQueue.
        public int compareTo(Node otherNode) { return this.f > otherNode.f ? 1 : (this.f < otherNode.f ? -1 : 0); }
    }
    
}
