import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;

class Grid extends JFrame implements KeyListener{
    
    int width, height;
    int rows, columns;

    Color gridColor = new Color(217, 183, 115);
    Color backgroundColor = new Color(235, 174, 52);

    public Grid(int rows, int columns, int width, int height){
        this.rows = rows;
        this.columns = columns;
        this.width = width;
        this.height = height;
        setSize(width, height);
        setLocationRelativeTo(null);
        setFocusable(true);
        setVisible(true);
        addKeyListener(this);

        repaint();
    }


    public void paint(Graphics g){
        // Background.
        g.setColor(backgroundColor);
        g.fillRect(0,0, width, height);

        drawGrid(g);
        //FILL IN TAKEN SQUARES
        // g.setColor(new Color(255,0,0));
        // for (int x = 0; x < GRIDX; x++){
        //     for (int y = 0; y < GRIDY; y++){
        //         if (nodes[x][y].filled){
        //             g.setColor(new Color(255,0,0));
        //             g.fillRect(x*nodeSpanX, y*nodeSpanX, nodeSpanX, nodeSpanX);
        //         }

        //         if (nodes[x][y].goalNode){
        //             g.setColor(new Color(0,255,0));
        //             g.fillRect(x*nodeSpanX, y*nodeSpanY, nodeSpanX, nodeSpanY);
        //         }
        //     }
        // }

        // //FILL IN OPEN LIST - DEBUG ONLY
        // g.setColor(new Color(0,0,255));
        // g.fillRect(startNode.x*nodeSpanX, startNode.y*nodeSpanX, nodeSpanX, nodeSpanY);
        // g.setColor(new Color(0,255,0));
        // g.fillRect(endNode.x*nodeSpanX, endNode.y*nodeSpanX, nodeSpanX, nodeSpanY);

        // //Draw Grid 31 + 31 lines

        

        // // //Draw Heuristics - CONDITIONAL
        // // if (drawNodeInfo){
        // //     for (int x = 0; x < GRIDX; x++){
        // //         for (int y = 0; y < GRIDY; y++){
        // //             g.drawString(""+ nodes[x][y].h, nodes[x][y].x*nodeSpanX + 40, nodes[x][y].y*nodeSpanY + 70);
        // //             g.drawString(""+ nodes[x][y].g, nodes[x][y].x*nodeSpanX + 5, nodes[x][y].y*nodeSpanY + 70);
        // //             g.drawString(""+ nodes[x][y].f, nodes[x][y].x*nodeSpanX + 5, nodes[x][y].y*nodeSpanY + 20);
        // //         }
        // //     }
        // // }

        // //DRAW PATH
        // Node n = endNode;
        // g.setColor(new Color(255,255,255));
        // while (n.g > 0){
        //     g.drawLine(n.x*nodeSpanX + (nodeSpanX/2), n.y*nodeSpanY + (nodeSpanY/2), n.parentNode.x*nodeSpanX + (nodeSpanX/2), n.parentNode.y*nodeSpanY + (nodeSpanY/2));
        //     n = n.parentNode;
        // }

        // //Draw Selection Node
        // g.setColor(new Color(255,255,255));
        // g.drawRect(selectionNode.x*nodeSpanX, selectionNode.y*nodeSpanY, nodeSpanX, nodeSpanY);
    }

    private void drawGrid(Graphics g){
        int nodeSpanX = width / columns;
        int nodeSpanY = height / rows;

        g.setColor(gridColor);
        for (int x = 0; x < columns - 1; x++){
            g.drawLine(x*nodeSpanX + nodeSpanX, 0, x*nodeSpanX + nodeSpanX, width);
        }
        for (int y = 0; y < rows - 1; y++){
            g.drawLine(0, y*nodeSpanY + nodeSpanY, height, y*nodeSpanY + nodeSpanY);
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        // switch (e.getKeyCode()){
        //     case KeyEvent.VK_Left: 
        // }
        // if (e.getKeyCode() == KeyEvent.VK_LEFT){
        //     if (selectionNode.x > 0){
        //         selectionNode = nodes[selectionNode.x - 1][selectionNode.y];
        //     }
        // }
        // if (e.getKeyCode() == KeyEvent.VK_RIGHT){
        //     if (selectionNode.x < GRIDX - 1){
        //         selectionNode = nodes[selectionNode.x + 1][selectionNode.y];
        //     }
        // }
        // if (e.getKeyCode() == KeyEvent.VK_UP){
        //     if (selectionNode.y > 0){
        //         selectionNode = nodes[selectionNode.x][selectionNode.y - 1];
        //     }
        // }
        // if (e.getKeyCode() == KeyEvent.VK_DOWN){
        //     if (selectionNode.y < GRIDY - 1){
        //         selectionNode = nodes[selectionNode.x][selectionNode.y + 1];
        //     }
        // }
        // if (e.getKeyCode() == KeyEvent.VK_SPACE){
        //     selectionNode.filled = !selectionNode.filled;
        // }
        // if (e.getKeyCode() == KeyEvent.VK_C){
        //     performSearch(startNode, endNode, false);
        // }
        // if (e.getKeyCode() == KeyEvent.VK_V){
        //     performSearch(startNode, endNode, true);
        // }
        // if (e.getKeyCode() == KeyEvent.VK_T){
        //     drawNodeInfo = !drawNodeInfo;
        // }
        // if (e.getKeyCode() == KeyEvent.VK_S){
        //     startNode = selectionNode;
        // }
        // if (e.getKeyCode() == KeyEvent.VK_E){
        //     selectionNode.goalNode = !selectionNode.goalNode;
        // }
        // if (e.getKeyCode() == KeyEvent.VK_Q){System.exit(0);}
        // repaint();
    }

    /** Handle the key-released event from the text field. */
    

    

    class ScopedTimer
    {
        long startTime;
        String name;
        ScopedTimer(String name)
        {
            this.name = name;
            startTime = System.nanoTime();
        }

        void finish()
        {
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double millis = elapsedTime / 1000000.0;
            System.out.println("Timer - " + name + ": " + millis);
        }
    }
}
