
package a_star;

/**
 *
 * @author Thuy Do
 */
import java.util.ArrayList;
import java.util.List;
public class Node {
    int key; //The key of a node as 0, 1, 2, ...
    float g, h;
    List<Integer> path;//The path includes nodes from start node to this node
    
    public Node()
    {
        g=0;h=0;
        path = new ArrayList<Integer>();
    }
    public Node(int _key, float _g, float _h, List _path){
        key = _key; g =_g; h=_h; path = _path;
    }
}
