/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang.mutable.MutableInt;
import java.util.List;
/**
 *
 * @author Thuy Do
 */
public class A_Star {
    /**
     * @param args the command line arguments
     */   
    public static void main(String[] args) throws IOException, Exception {

        ArrayList<String> al_d = new ArrayList();//ArrayList for d[][]
        
        String fname = "C:\\ThuyDT\\NetBeansProjects\\A_star\\src\\a_star\\pa1.in";//input file name
        readInpFile(fname,al_d);
        
        int no_node = Integer.parseInt(al_d.get(0)); //number of nodes in the graph
        if (no_node <= 0){
            System.out.println("The number of nodes in the graph must be 1 or more");
            System.exit(1);
        }
        float d[][] = new float[no_node][no_node]; //2D array of floats to save the distance cost between nodes, d[i][j] = the weight cost from node i to node j
        int al_d_size = al_d.size();
        for (int i = 0;i<no_node;i++) //marking -1 for initializing d[i][j] 
            for (int j=0; j<no_node; j++) d[i][j] = -1;
        
        for (int k = 1; k <al_d_size; k++){ // to transform the input data to d[][]
            String tmp[] = new String[3]; tmp = al_d.get(k).split(" ");
            int i = Integer.parseInt(tmp[0]);int j = Integer.parseInt(tmp[1]);
            d[i][j] = Float.parseFloat(tmp[2]);
        }
//do BFS search
        Node goal = new Node();
        String notification_BFS = do_Best_First_Search(d, no_node, goal);
        System.out.println(notification_BFS);
        String output_to_file = "";
        int path_size = goal.path.size();
        for (int i = 0; i < path_size; i++){
            System.out.print(goal.path.get(i).toString());
            output_to_file = output_to_file + goal.path.get(i).toString();
            if (i<path_size -1) {
                System.out.print(" > ");
                output_to_file = output_to_file + ">";
            }
            
        }        
//do A* search
        
        String notification_A_Star = do_A_Star_Search(d, no_node, goal);
        System.out.println(notification_A_Star);
        path_size = goal.path.size();
        output_to_file = output_to_file + "\n";
        for (int i = 0; i < path_size; i++){
            System.out.print(goal.path.get(i).toString());
            output_to_file = output_to_file + goal.path.get(i).toString();
            if (i<path_size -1) {
                System.out.print(" > ");
                output_to_file = output_to_file + ">";
            }
        }
        String out_file = "pa1.out";//out put file name
        writeOutFile(out_file, output_to_file);
    }
    
//--Read input data file into an ArrayList of Strings
    private static void readInpFile(String fname, ArrayList<String> retval) throws FileNotFoundException, IOException
    {        
        FileReader fr = new FileReader(fname);
        BufferedReader br;
        br = new BufferedReader(fr);
        String s;
        while((s = br.readLine()) != null) {
            if (s.compareTo("") != 0)
            retval.add(s);
        }                 
    }
    //write a string str to the file specified by fname
    private static void writeOutFile(String fname, String str) throws FileNotFoundException, IOException{
        BufferedWriter out = null;
        FileWriter fstream = new FileWriter(fname,true); //true tells to append data at the end of the file
        out = new BufferedWriter(fstream);
        out.write(str);
        if (out != null) out.close();
    }
    //heuristic funtion of a node h(n) = average of weights of edges from n to others.
    //h(n) = 0 if n is the goal state
    //h(n) = -1 if n is not the goal state and n has no children
    //return a string = "Success" if it finds out the solution otherwise "No wway ..."
    //path is the path of the solution
    private static String do_Best_First_Search(float[][] d, int no_node, Node goal) throws Exception{
        System.out.println("\n\nBest First Search is in progress...");
        String retString = "";
        List opened_list = new ArrayList();//the list to save nodes not expanded
        List closed_list = new ArrayList();//the list to save nodes expanded
        
        int key_start_node = 0; int key_goal_node = no_node-1;
        //int key_start_node = 9; int key_goal_node = 14;
        //float h = min_weight_successors(key_start_node, d, no_node);
        float h = avg_weight_successors(key_start_node, d, no_node);
        float g = 0;
        List node_path = new ArrayList();//To save the path from start state to a node
        node_path.add(key_start_node);
       
        Node start_node = new Node(key_start_node, g, h, node_path );//the start node
        Node goal_node = new Node(key_goal_node, g, 0, null);//the goal node
        opened_list.add(start_node);
        
        while (opened_list.size()>0){
            Node current_node = (Node)opened_list.get(0); //the first node in the opened_list always has the least f
            opened_list.remove(0);            
            if (current_node.key == goal_node.key) {
                retString = "Success";
                goal.g = current_node.g;
                goal.h = current_node.h;
                goal.path = current_node.path;
                break;
            }
            //check if current node was expanded before
            int added_to_closed_list= add_node_to_closed_list(current_node,closed_list);
            if (added_to_closed_list == 0){//cannot add the current_node to the closed_list b/c it was in the list before => back to while
                continue;
            }
            //expanding current_node    
            for (int j = 0; j < no_node; j++){//find all neighbors of current node and add them into the opened_list if possible
                if (d[current_node.key][j] != -1){ //j is a neighbor
                    //g = current_node.g + d[current_node.key][j];
                    g = 0;
                    h = avg_weight_successors(j, d, no_node);
                    //node_path = current_node.path;
                    node_path = get_node_path(current_node);
                    node_path.add(j);
                    Node successor;
                    //check if the neighbor has children?
                    if (h == -1) { //j has no children
                        if (j == goal_node.key) {// j has no child and j is the goal state => add j to the opened_list
                            h = 0;
                            successor = new Node(j, g,h,node_path);
                            add_node_to_opened_list(successor, opened_list);
                        } 
                        else {//j is not the goal state and has no child => add to closed_list
                            successor = new Node(j, g,h,node_path);
                            add_node_to_closed_list(successor, closed_list);
                        }
                        continue;
                    }
                    //the successor has children
                    successor = new Node(j, g,h,node_path);
                    if (in_list_with_lower_f(successor, opened_list) == 1) continue;//skip successor if it is in opened_list with lower f or equal
                    if (in_list_with_lower_f(successor, closed_list) == 1) continue;//skip successor if it is in closed_list with lower f or equal
                    add_node_to_opened_list(successor, opened_list);//add successor to the opened_list
                }
            }
        }                        
        if (retString.compareTo("Success") != 0) 
            retString = "No way to travel from " + start_node.key + " to " + goal_node.key + " in your graph!!!" ;

//        int closed_size = closed_list.size();
//        for (int i=0; i<closed_size; i++){
//            Node n = new Node();
//            n = (Node)closed_list.get(i);
//            System.out.print(n.key + "; ");
//        }
        System.out.println("Done!");
        return retString;
    }
    
    //heuristic funtion of a node h(n) = min {w_i: w_i is a weight of an edge n->i}
    //h(n) = 0 if n is the goal state
    //h(n) = -1 if n is not the goal state and n has no children
    //return a string = "Success" if it finds out the solution otherwise "No wway ..."
    //path is the path of the solution
    private static String do_A_Star_Search(float[][] d, int no_node, Node goal) throws Exception{
        System.out.println("\n\nA* is in progress...");
        String retString = "";
        List opened_list = new ArrayList();//the list to save nodes not expanded
        List closed_list = new ArrayList();//the list to save nodes expanded
        
        int key_start_node = 0; int key_goal_node = no_node-1;
        //int key_start_node = 9; int key_goal_node = 14;
        float h = min_weight_successors(key_start_node, d, no_node);
        float g = 0;
        List node_path = new ArrayList();//To save the path from start state to a node
        node_path.add(key_start_node);
       
        Node start_node = new Node(key_start_node, g, h, node_path );//the start node
        Node goal_node = new Node(key_goal_node, g, 0, null);//the goal node
        opened_list.add(start_node);
        
        while (opened_list.size()>0){
            Node current_node = (Node)opened_list.get(0); //the first node in the opened_list always has the least f
            opened_list.remove(0);            
            if (current_node.key == goal_node.key) {
                retString = "Success";
                goal.g = current_node.g;
                goal.h = current_node.h;
                goal.path = current_node.path;
                break;
            }
            //check if current node was expanded before
            int added_to_closed_list= add_node_to_closed_list(current_node,closed_list);
            if (added_to_closed_list == 0){//cannot add the current_node to the closed_list b/c it was in the list before => back to while
                continue;
            }
            //expanding current_node    
            for (int j = 0; j < no_node; j++){//find all neighbors of current node and add them into the opened_list if possible
                if (d[current_node.key][j] != -1){ //j is a neighbor
                    g = current_node.g + d[current_node.key][j];
                    h = min_weight_successors(j, d, no_node);
                    //node_path = current_node.path;
                    node_path = get_node_path(current_node);
                    node_path.add(j);
                    Node successor;
                    //check if the neighbor has children?
                    if (h == -1) { //j has no children
                        if (j == goal_node.key) {// j has no child and j is the goal state => add j to the opened_list
                            h = 0;
                            successor = new Node(j, g,h,node_path);
                            add_node_to_opened_list(successor, opened_list);
                        } 
                        else {//j is not the goal state and has no child => add to closed_list
                            successor = new Node(j, g,h,node_path);
                            add_node_to_closed_list(successor, closed_list);
                        }
                        continue;
                    }
                    //the successor has children
                    successor = new Node(j, g,h,node_path);
                    if (in_list_with_lower_f(successor, opened_list) == 1) continue;//skip successor if it is in opened_list with lower f or equal
                    if (in_list_with_lower_f(successor, closed_list) == 1) continue;//skip successor if it is in closed_list with lower f or equal
                    add_node_to_opened_list(successor, opened_list);//add successor to the opened_list
                }
            }
        }                        
        if (retString.compareTo("Success") != 0) 
            retString = "No way to travel from " + start_node.key + " to " + goal_node.key + " in your graph!!!" ;

        
//        int closed_size = closed_list.size();
//        for (int i=0; i<closed_size; i++){
//            Node n = new Node();
//            n = (Node)closed_list.get(i);
//            System.out.print(n.key + "; ");
//        }
        System.out.println("Done!");
        return retString;
    }
    
    //Return the min of weights of successors of a node
    //key_node is an integer number, a key of a node
    //d is the 2d array of wweights from each node to others
    //no_node: the number of nodes in the graph
    //if key_node has no child => return -1;
    private static float min_weight_successors(int key_node, float[][] d, int no_node) throws Exception {
        float min = -1;
        int t = 0;
        while ((t < no_node) && (min == -1)){
            if (d[key_node][t] == -1) t++;
            else min = d[key_node][t];           
        }
        for (int j = t+1; j < no_node; j++)
            if ((d[key_node][j] != -1)&&(min > d[key_node][j])) min = d[key_node][j];
        return min;
    }
    //return the average of weights of edges from a node with key_node
    private static float avg_weight_successors(int key_node, float[][] d, int no_node) {
        float min = -1; int t = 0; float sum = 0; int count = 0;
        while (t < no_node){
            if (d[key_node][t] != -1) {
                sum = sum + d[key_node][t];
                count++;
            }
            t++;            
        }
        if (count !=0) min = sum/count;
        return min;
    }
//add node n to list l in the order of increament of f = g + h
    private static void add_node2list_increament_f(Node n, List l){
        int i = 0; int list_size = l.size(); int found = 0;
        while ((i < list_size) && (found == 0)) {
            Node t = (Node)l.get(i);
            if ((n.g + n.h) > (t.g + t.h)) i++;
            else found = 1;
        }
        l.add(i, n);
    }
    //return 1 if node n in the list l otherwise return 0;
    private static int node_in_list(Node n, List l, MutableInt position){
        int found = 0; int i =0; int list_size = l.size();
        position.setValue(-1);
        while ((i < list_size) && (found == 0)) {
            Node t = (Node)l.get(i);
            if (n.key != t.key) i++;
            else {found = 1; position.setValue(i); }
        }      
        return found;
    }
    //add a node n to a closed_list l
    //return 1 if n is added 0 otherwise
    //if n existed in l with different f => still add n to the list l
    private static int add_node_to_closed_list(Node n, List l)
    {
        int retval = 0;
        MutableInt pos_in_list = new MutableInt();
        int found = node_in_list(n, l, pos_in_list);
        if (found ==0){
            l.add(n); retval = 1;
        } else {
            int pos = (int)pos_in_list.getValue();
            Node tmp_node = (Node) l.get(pos);
            if ((tmp_node.g + tmp_node.h) != (n.g + n.h)){
                l.add(n);retval = 1;
            }
        }
        return retval;
    }
    //add a node n to a opened_list l
    //return 1 if n is added 0 otherwise
    //if n existed in l with different f => still add n to the list l
    private static int add_node_to_opened_list(Node n, List l)
    {
        int retval = 0;
        MutableInt pos_in_list = new MutableInt();
        int found = node_in_list(n, l, pos_in_list);
        
        if (found ==0){
            add_node2list_increament_f(n, l);
            retval = 1;
        } else {
            int pos = (int)pos_in_list.getValue();
            Node tmp_node = (Node) l.get(pos);
            if ((tmp_node.g + tmp_node.h) > (n.g + n.h)){
                l.remove(pos);
                add_node2list_increament_f(n, l);
                retval = 1;
            }
        }
        return retval;
    }
    //check if node n in the list l with the lower f
    //return 1 if n in l with lower f, 0 otherwise
    private static int in_list_with_lower_f(Node n, List l){
        int found = 0;int i =0;int list_size = l.size();        
        while ((i < list_size) && (found == 0)) {
            Node t = (Node)l.get(i);
            if (n.key != t.key) i++;
            else if ((t.g + t.h) < (n.g + n.h))found = 1;
            else i++;
        }      
        return found;
    }
    //return the path of a node
    private static List<Integer> get_node_path(Node n){
        List<Integer> l = new ArrayList();
        int path_size = n.path.size();
        for (int i=0; i<path_size; i++)
            l.add(n.path.get(i));
        return l;
    }
}
