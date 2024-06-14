//Tree Code from  https://rosettacode.org/wiki/K-d_tree#Java
import java.util.*;

public class KdTree {

    private int dimension_;  // Number of variable used
    private Node root_ = null; //pointer to the root
    private Node[] Best_ = new Node[7] ;//pointer to the answer
    private Node Best_mean = null;
    private double[] BestDistance_ = {-1,-1,-1,-1,-1,-1,-1};
    private double[] temp = new double[10];
    private double[] temp_ = new double[10];
    private int count = 0;
    private String result_;
    private int visited_ = 0;
   private double[] trash = { -1,-1,-1,-1,-1,-1,-1};
   private Node Tra;

    // Default Constructor
    public KdTree(int dimensions, List <Node> nodes){
        dimension_ = dimensions;
        root_ = makeTree (nodes, 0, nodes.size(), 0);
        Tra = new Node(trash,0,"C");
       for (int i = 0; i < 7; i++){
            Best_[i] = Tra;
            temp[i] = -1;
        }
       temp[7] = -1;
       temp[8] = -1;
       temp[9] = -1;


    }
    public Node FindNearest(Node target, int k) throws IllegalAccessException {
        if (root_ == null)
            throw new IllegalAccessException("Tree is Empty");
        // Reset the values before finding the nearest Node
        for (int i = 0; i < 7; i++){
            Best_[i] = Tra;
       }

        visited_ = 0;

        for (int i =0; i < 7; i++){
            BestDistance_[i] = -1;
        }
        nearest(root_, target, 0); //updates the best node with the current nearest node
        count = 0; //reset the counter
        // Creating the mean of the dimension
        if (BestDistance_[6]== -1){
            Best_[6] = new Node (Best_[5]) ;
        }
        for (int i =0; i < k; i++) {
            if ( Best_[i].diagnosis.equals("B")){
                count ++;
            }
            for (int j = 0; j < dimension_; j++) {
                temp_ = Best_[i].coords_;
                temp[j] = temp_[j] + temp[j];
            }
        }
        for (int j = 0; j < dimension_; j++) {
            temp[j] =  temp[j]/k;
            }
        if (k == 1) result_ = Best_[0].diagnosis;
        else if ( count > k/2) result_ = "B";
        else result_ = "M";
        Best_mean =  new Node(temp, 0,result_);
        return Best_mean;
    }
   
    //nearest neighbour search 
    //searches the closest value to the target value at a specified dimension by traversing the tree recursively (coordinates plane) 
    //begins by computing the distance between the root node value and target value
    //if the node value at that dimension differs from the target value, then the function is recursively call on the left or right subtree 
    private void nearest (Node root, Node target, int  index) {
        if (root == null) return; //tree is empty
        ++visited_;
        double d = root.distance(target);
        //in the first iteration, the distance between the root and the target is the shortest ('best') distance
        //otherwise, if the current distance is smaller than the best distance, then set best distance to the current distance
        if (Best_[6].equals(Tra) ){
            for (int i = 0; i < 7; i++) {
                if (Best_[i].equals(Tra)) { // Find empty spot
                    BestDistance_[i] = d;
                    Best_[i] = new Node (root);

                    //sort the used spots
                    for (int k = 1; k < i+1; k++) {
                        double key = BestDistance_[k];
                        Node key2 = new Node(Best_[k]);
                        int j = k - 1;
                        while (j >= 0 && BestDistance_[j] > key) {
                            BestDistance_[j + 1] = BestDistance_[j];
                            Best_[j + 1] = new Node( Best_[j]);
                            j = j - 1;
                        }
                        BestDistance_[j + 1] = key;
                        Best_[j + 1] = new Node(key2);
                    }//Fill the empty sort

                    break;
                }

            }


        }
        else if (d < BestDistance_[6]){
            for (int i = 0; i<7;i++){
                if ( BestDistance_[i] > d && i < 6){
                    double temp2 = BestDistance_[i];
                    Node temp = new Node ( Best_[i]);
                    for (int j = 5; j >= i ;j--){
                        BestDistance_[j+1] = BestDistance_[j];
                        Best_[j+1] = new Node (Best_[j]);
                    }
                    BestDistance_[i] = d;
                    Best_[i] = new Node (root) ;
                    break;
                }else {
                    BestDistance_[6] = d;
                    Best_[6] = new Node (root);
                }
            }
        }
        if (BestDistance_[6] == 0) return; //halt the search if the distance between a node and the target is zero since the target is found
        double dx = root.getdata(index) - target.getdata(index); //subtract the root and target values at the current index (i.e., ith dimension)
        index = (index + 1) % dimension_; //current index is updated to the next dimension
       
        //if the value contained in the root is greater than the value in the target, then recursively call function 'nearest' on the left subtree
        //if the value contained in the root is smaller than the value in the target. then recursively call function  'nearest' on the right subtree
        nearest(dx > 0 ? root.left_ : root.right, target, index); 

        //if the square of the difference 'dx' is greater than or equal to the current shortest distance to the target, then NO need to search in this subtree
        if (dx * dx >= BestDistance_[6]) return;
        //checks the opposite subtree (either left or right) if the squared of the difference 'dx' is smaller than the current best distance
        nearest(dx > 0 ? root.right : root.left_, target, index);
    }

    //builds the k-d tree from the list of node instances where each node has a unique id, a diagnosis, and ten attributes of its corresponding body cell
    private Node makeTree(List<Node> nodes, int begin, int end, int index) {
        if ( end <= begin) return null; //once the beginning pointer and the end pointer cross, all dimensions ('attributes') have been exhausted
        int n = begin + (end - begin)/2; //computes the index corresponding to the middle dimension
       
        Node node = QuickSelect.select(nodes, begin, end-1, n, new NodeComparator(index)); //computes the median value at the current dimension
       
        index = (index + 1) % dimension_; // The change of dimension happens here. 
        node.left_ = makeTree(nodes, begin, n, index); //recursive call to the function 'makeTree' on the left side of the current 'parent' node
        node.right = makeTree(nodes, n + 1, end, index); //recursive call to the function 'makeTree' on the right side of the current 'parent' node
       
        return node; //returns the root of the tree
    }

    //class to compare the value contained in two nodes at the current index ('dimension')
     private static class NodeComparator implements Comparator<Node> {
        private int index_;

        private NodeComparator(int index) {
            index_ = index;
        }
        public int compare(Node n1, Node n2) { // I dont think we are allowed to use this Need to check
            return Double.compare(n1.getdata(index_), n2.getdata(index_));
        }
    }


    //===========================================================================================================================
    // The link list class
    public static class Node {
        private double[] coords_ = new double[10];  //10 element and all result
        private int Id;
        private String diagnosis;
        private Node left_ = null;
        private double distance = 0;
        private Node right = null;

        //Default constructor
        public Node(double[] coords, int id, String D){
            for ( int i = 0; i < coords.length; i++){
                coords_[i]= coords[i];
            }
           //coords_ = coords;// I am pretty sure this set the Array size, the array size should be the same as the dimension
            Id = id;
            diagnosis = D;
        }public Node (Node node) {
            for (int i = 0; i < node.coords_.length; i++) {
                coords_[i] = node.coords_[i];
            }
            Id = node.Id;
            diagnosis = node.diagnosis;


        }
         public void set(int index, double Value) {  //Changes node value at the specified dimension
            coords_[index] = Value;
        }
        public double getdata(int index) { // Get the node's value back from the dimension array at the specified dimension
            return coords_[index];
        }
        public int getId() { //get the node's id number
            return  Id;
        }
        public String getDiagnosis(){ //get the node's diagnosis
            return  diagnosis;
        }
        double distance (Node node){ // computes the squared distance from a node to the target node by considering the difference in every dimension (It is square for the negative distance )

            double dist = 0;
            for (int i = 0; i <coords_.length; i++){
                double d = coords_[i] - node.coords_[i]; //computes the difference between the values contained in two different nodes
                dist += d * d; //distance is squared to avoid negative distance values 
                               //the squared distance is added to the current distance to account for all dimensions
            }
            return  dist;
        }
        public String toString() {  // Print Function
            StringBuilder s = new StringBuilder("(");
            for (int i = 0; i < coords_.length; ++i){
                if (i > 0) s.append(", ");
                s.append(coords_[i]);
            }
            s.append(")");
            return  s.toString();

        }



    }
    //===========================================================================================================================
    //The Input class


}
