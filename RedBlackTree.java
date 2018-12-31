/**
 * Class that implements a Red Black Tree that contains Strings
 * @author Christian Castro
 *@version 1.0	12/7/2018
 * @param <Key> Data type that is stored in the tree
 */
public class RedBlackTree<Key extends Comparable<Key>> {	
	private static RedBlackTree.Node<String> root;
	
	/**
	 * Represents a node in the tree
	 * @param <Key>
	 */
	public static class Node<Key extends Comparable<Key>> { //changed to static 
		
		  Key key;  		  
		  Node<String> parent;
		  Node<String> leftChild;
		  Node<String> rightChild;
		  boolean isRed;
		  int color;	//Red=0 Black=1
		  
		  public Node(Key data){
			  this.key = data;
			  leftChild = null;
			  rightChild = null;
		  }		
		  
		  /**
		   * Compares key values of 2 different nodes
		   * @param n Node to be compared
		   * @return int value
		   */
		  public int compareTo(Node<Key> n){ 	//this<that  <0
		 		return key.compareTo(n.key);  	//this>that  >0
		  }
		  
		  /**
		   * If this node is a leaf return true
		   * @return Boolean depending on whether node is leaf
		   */
		  public boolean isLeaf(){
			  if (this.equals(root) && this.leftChild == null && this.rightChild == null) return true;
			  if (this.equals(root)) return false;
			  if (this.leftChild == null && this.rightChild == null){
				  return true;
			  }
			  return false;
		  }
	}
	
	public boolean isLeaf(RedBlackTree.Node<String> n){
		if (n.equals(root) && n.leftChild == null && n.rightChild == null) return true;
		if (n.equals(root)) return false;
		if (n.leftChild == null && n.rightChild == null){
			return true;
		}
		return false;
	}
	
	public interface Visitor<Key extends Comparable<Key>> {
		/**
		This method is called at each node.
		@param n the visited node
		*/
		void visit(Node<Key> n);  
	}
	
	/**
	 * Prints key value of node passed in parameter
	 * @param n Node in tree
	 */
	public void visit(Node<Key> n){
		System.out.println(n.key);
	}
	
	/**
	 * Prints key values of nodes in tree in preorder
	 */
	public void printTree(){
		RedBlackTree.Node<String> currentNode = root;	
		printTree(currentNode);
	}
	
	/**
	 * Prints key values of nodes in tree in preorder
	 * @param node
	 */
	public void printTree(RedBlackTree.Node<String> node){
		System.out.print(node.key);
		if (node.isLeaf()){
			return;
		}
		printTree(node.leftChild);	//Go to the left node
		printTree(node.rightChild);	//Go to right node
	}
	
	/**
	 * Calls addNode method to add new node to tree
	 * @param data Key value of new node
	 */
	public void insert(String data){
		addNode(data);	
	}
	
	/**
	 * Finds the parent node of the new node that is about to be inserted into the tree
	 * @param start	Node used to determine where the new data should be placed
	 * @param data Key value of new node
	 * @return Node that will be parent of new node
	 */
	public RedBlackTree.Node<String> newParent(RedBlackTree.Node<String> start, String data){
		if (start.leftChild!=null&&data.compareTo(start.key) < 0) {//node is less than initial node
			start=newParent(start.leftChild, data);	//Recursively visit left child
		}
		else if(start.rightChild!=null&&data.compareTo(start.key) > 0)
			start=newParent(start.rightChild, data);	//recursively visit right chiild
		return start;
	}
	
	/**
	 * Adds new node into the tree and fixes tree so that it maintains the RBT properties
	 * @param data Key value of new node
	 */
	public void addNode(String data){  	//this < that  <0.  this > that  >0
		//If tree is empty
		if(root==null) {
			root=new RedBlackTree.Node<String>(data);
			root.color=1;	//Change color to black
	 		return;
		}
		
		RedBlackTree.Node<String> newParent=newParent(root,data);
		
		if(data.compareTo(newParent.key) < 0) {
			newParent.leftChild=new RedBlackTree.Node<String>(data);
			newParent.leftChild.parent=newParent;
			newParent.leftChild.color=0;
			fixTree(newParent.leftChild);
		}
		else{
			newParent.rightChild=new RedBlackTree.Node<String>(data);
			newParent.rightChild.parent=newParent;
			newParent.rightChild.color=0;
			fixTree(newParent.rightChild);
		}
	}
	
	/**
	 * Rotates subtree to the left with regards to node n
	 * @param n Node that the tree is being rotated around
	 */
	public void rotateLeft(RedBlackTree.Node<String> n){
		RedBlackTree.Node<String> newRoot=n.rightChild;	//New root of subtree being fixed
		
		//Update Children of all nodes being rotated
		n.rightChild=newRoot.leftChild;
		if(newRoot.leftChild!=null)
			newRoot.leftChild.parent=n;
		
		newRoot.parent=n.parent;
		
		if(n.parent==null) 
			root=newRoot;
		else if(isLeftChild(n.parent,n))
			n.parent.leftChild=newRoot;
		else
			n.parent.rightChild=newRoot;
		
		newRoot.leftChild=n;
		n.parent=newRoot;
	}
	
	/**
	 * Rotates subtree to the right with regards to node n
	 * @param n Node that the tree is being rotated around
	 */
	public void rotateRight(RedBlackTree.Node<String> n){
		RedBlackTree.Node<String> newRoot=n.leftChild;
		
		//Update Children of all nodes being rotated
		n.leftChild=newRoot.rightChild;
		if(newRoot.rightChild!=null)
			newRoot.rightChild.parent=n;
		
		newRoot.parent=n.parent;
		
		if(n.parent==null)
			root=newRoot;
		else if(n.equals(n.parent.rightChild))
			n.parent.rightChild=newRoot;
		else
			n.parent.leftChild=newRoot;
		
		newRoot.rightChild=n;
		n.parent=newRoot;
	}
	
	/**
	 * Identifies which violation occurs and handles accordingly
	 * @param current Node
	 */
	public void fixTree(RedBlackTree.Node<String> current) {
		if(current.equals(root))	//If at root make root black
			root.color=1;
		
		else if(current.parent.color==1)	//If parent is black no fix required
			return;
		
		else if(current.color==0&&current.parent.color==0) {	//Violation if Parent and Child are red
			if(getAunt(current)!=null&&getAunt(current).color==0)	//Aunt is red
				case1(current);
			
			else if(getAunt(current)==null||getAunt(current).color==1) {	//Aunt is black
				if((!isLeftChild(current.parent.parent,current.parent)&&isLeftChild(current.parent,current))
						&&(isLeftChild(current.parent.parent,current.parent)&&!isLeftChild(current.parent,current))) {	//Parent and Child are opposite
					case2(current);
				}
				else
					case3(current);
			}
		}
	}
	
	/**
	 * Adjusts tree to maintain RBT properties
	 * @param current Node
	 */
	public void case1(RedBlackTree.Node<String> current) {
		current.parent.color=1;	//Makes parent black
		getAunt(current).color=1;	//Get aunt
		getGrandparent(current).color=0;	//Make grandparent red
		fixTree(getGrandparent(current));	//Move to grandparent node and fix tree
	}
	
	/**
	 * Adjusts tree to maintain RBT properties
	 * @param current	Node
	 */
	public void case2(RedBlackTree.Node<String> current) {
		RedBlackTree.Node<String> x=current.parent;
		if(!isLeftChild(current.parent,current)) {	//Child node is right child
			rotateLeft(x);
			case3(x);
		}
		else {
			rotateRight(x);
			case3(x);
		}
	}
	
	/**
	 * Adjusts tree to maintain RBT properties
	 * @param current	Node
	 */
	public void case3(RedBlackTree.Node<String> current) {
		current.parent.color=1;	//Make parent black
		getGrandparent(current).color=0;	//Make grandparent red	
		if(isLeftChild(current.parent.parent,current.parent))
			rotateRight(getGrandparent(current));	//Rotate to the right with respect to grandparent of current node
		else
			rotateLeft(getGrandparent(current));	//Rotate to the left
	}
	
	/**
	 * Finds node in tree that corresponds to key value given in the parameter
	 * @param k Key Value
	 * @return Node with key=k
	 */
	public RedBlackTree.Node<String> lookup(String k){ 
		RedBlackTree.Node<String> current=root;	//Start from root
		//Go through tree to find node with key=k
		while(!current.isLeaf()&&!current.key.equals(k)) {
			if(k.compareTo(current.key)<0) {
				current=current.leftChild;
			}
			else
				current=current.rightChild;
		}
		
		if(current.key.equals(k))
			return current;
		else
			return null;
	}
	
	/**
	 * Returns sibling node of node given in the parameter
	 * @param n Node
	 * @return Sibling node
	 */
	public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> n){  
		RedBlackTree.Node<String> parent=n.parent;	//Go to parent
		if(parent.leftChild.equals(n))	//If n is left child return right child
			return parent.rightChild;
		else
			return parent.leftChild;	//Return left child
	}
	
	/**
	 * Gets aunt node of node given in the parameter
	 * @param n Node
	 * @return Aunt node
	 */
	public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> n){
		RedBlackTree.Node<String> grandparent=n.parent.parent;
		RedBlackTree.Node<String> aunt = null;
		//Check which child the parent of n is and then aunt is the other node
		if(grandparent.leftChild!=null&&grandparent.leftChild.equals(n.parent))
			aunt= grandparent.rightChild;
		else if(grandparent.rightChild!=null&&grandparent.rightChild.equals(n.parent))
			aunt=grandparent.leftChild;
		return aunt;
	}
	
	/**
	 * Gets granparent node of node in the parameter
	 * @param n Node
	 * @return Grandparent node
	 */
	public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> n){
		return n.parent.parent;
	}
	
	/**
	 * Checks if node has a key value
	 * @param n Node
	 * @return	True or false
	 */
	public boolean isEmpty(RedBlackTree.Node<String> n){
		if (n.key == null){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if node in parameter is the let child of the parent
	 * @param parent parent node of left child
	 * @param child of parent node
	 * @return True or false
	 */
	public boolean isLeftChild(RedBlackTree.Node<String> parent, RedBlackTree.Node<String> child)
	{
		if (child.compareTo(parent) < 0 ) {//child is less than parent
			return true;
		}
		return false;
	}
	
	/**
	 * Calls preOrderVisit and passes visitor object to preOrderVisit method
	 * @param v Visitor object
	 */
	public void preOrderVisit(Visitor<String> v) {
	   	preOrderVisit(root, v);
	}
	 
	/**
	 * Goes to each node in the tree in preorder
	 * @param n Node in tree
	 * @param v Visitor
	 */
	private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
	  	if (n == null) {
	  		return;
	  	}
	  	v.visit(n);	//Prints key of n
	  	preOrderVisit(n.leftChild, v);	//Go to left child
	  	preOrderVisit(n.rightChild, v);	///Go to right child
	}	
}