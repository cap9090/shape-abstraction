/* if you want to change the number of signigant points 
 * just adjust the fields MAX and MIN in this shape
 * abstraction class
 */
package mylinkedlist;

import simplegui.SimpleGUI;
import java.util.Scanner;
import simplegui.GUIListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
/**
 *
 * @author Christian Prajzner
 */
public class ShapeAbstraction implements GUIListener {
    
    static final double INFINITY = 9e90;
    static final int MAX = 400; // most possible is 400
    static final int MIN = 38; // least possible is 1
    SimpleGUI sg;
    static LinkedList<Point> [] my;

    @Override
    public void reactToButton1() {
        
    }

    @Override
    public void reactToButton2() {
        
    }

    @Override
    public void reactToSwitch(boolean bln) {
        
    }

    @Override
    public void reactToSlider(int i) {
        
        sg.eraseAllDrawables();
        i = ((((MAX -1) - MIN) * (i))/100) + MIN;
        
        LinkedList <Point> currentList = my[i - MIN]; 
        ListIterator<Point> itr = currentList.listIterator();
        
        
        while (itr.hasNext()){
            Point p =  itr.next();
            sg.drawDot(p.getX(), p.getY(), 2, java.awt.Color.red, 1, null);    
        }
        
        for (int q = 1; q < currentList.size(); q ++){
            if (q == currentList.size()){
                Point p1 = currentList.get(q -1);
                Point p2 = currentList.get(0);
                sg.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), java.awt.Color.red, 1, 5, null);
            }
            else {
                Point p1 = currentList.get(q);
                Point p2 = currentList.get(q -1);
                sg.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), java.awt.Color.red, 1, 5, null);
            }      
        }
    }
  
    public ShapeAbstraction (){
        sg = new SimpleGUI();
        sg.print("Use the slider!");
        sg.maximizeGUIonScreen();
        sg.registerToGUI(this);
    }
    
    public static void main(String [] args){
        
        new ShapeAbstraction();
        LinkedList<Point> myList = new LinkedList<Point> ();
        String filename = "shapelist.txt";
        Scanner inputStream = null;
        try{
            inputStream = new Scanner(new File(filename));
        }
        catch(FileNotFoundException e){
            e.getMessage();  
        }
        
        int count = 0;
        while(inputStream.hasNextLine()){
            String line = inputStream.nextLine();
            Scanner in = new Scanner (line);
            while(in.hasNextInt()){
                int x = in.nextInt() + 500; //adjustment so fits better on screen eg. more in the center
                int y = 654 - in.nextInt(); //654 is the heigh of the  simplegui window. found using sg.getHeigh();
                Point p = new Point ((double) x, (double) y);
                myList.addLast(p);
                count++;
            }   
        }
        
        inputStream.close();
        
        //System.out.println("count = " + count);
        //System.out.println(myList.size());
        
        my = new LinkedList [myList.size() - (MIN -1)];
        my[myList.size() -MIN] = myList;
        int heya = myList.size() - MIN;
        for (int i = 0 ; i < heya ; i ++){
            double [] sigValues = new double[myList.size()];
            for (int j = -1; j<= sigValues.length -2 ; j ++){
                if (j == -1 ){
                    sigValues[0] = INFINITY; //FIRST POINT
                }
                else if (j == myList.size() -2 ){
                    sigValues [j + 1] = INFINITY;
                }
                else 
                {
                ListIterator<Point> itr = myList.listIterator(j);
                Point left =  itr.next();
                Point middle =  itr.next();
                Point right =  itr.next(); 
                double l1 = left.distance(middle);
                double l2 = middle.distance(right);
                double l3 = left.distance(right);
                sigValues[j + 1] = l1 + l2 - l3;
                }

            }//outside of innner for loop
            
            int min = findMinIndexOfArray(sigValues);
            myList.remove(min);
            //System.out.println(myList.size());
            LinkedList<Point> ref = new LinkedList<Point> ();
            ListIterator iter = myList.listIterator();
            ListIterator iter2 = ref.listIterator();
            for(int q = 0; q < myList.size(); q ++){
                Point p = (Point) iter.next();
                iter2.add(p);
            }
            my[myList.size() - MIN] = ref;
            ref = null;
            //my[myList.size -37 - 1] = myList;
        }
    }
    
    private static int findMinIndexOfArray(double [] A){
        double min = INFINITY;
        int j = 0;
        for (int i = 0; i <A.length; i ++){
            if (A[i] < min){
                min = A[i];
                j=i;
            }
        }
        return j;
    }
   
}
