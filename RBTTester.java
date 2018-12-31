import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RBTTester {

	@Test
	void test() {
		RedBlackTree<String> rbt=new RedBlackTree<String>();
		
		//Tests functions makeString(),insert(),addNode(),newParent(),fixTree(), case1-3(),rotateLeft()
		//rotateRight(), both preOrderVisit(), and Node: compareTo()
		rbt.insert("D");
		rbt.insert("B");
		rbt.insert("A");
		rbt.insert("C");
		rbt.insert("F");
		rbt.insert("E");
		rbt.insert("H");
		rbt.insert("G");
		rbt.insert("I");
		rbt.insert("J");
		assertEquals("DBACFEHGIJ", makeString(rbt));
		
		//makeStringDetails() test
		String str="Color: 1, Key:D Parent: \n"+
                "Color: 1, Key:B Parent: D\n"+
                "Color: 1, Key:A Parent: B\n"+
                "Color: 1, Key:C Parent: B\n"+
                "Color: 1, Key:F Parent: D\n"+
                "Color: 1, Key:E Parent: F\n"+
                "Color: 0, Key:H Parent: F\n"+
                "Color: 1, Key:G Parent: H\n"+
                "Color: 1, Key:I Parent: H\n"+
                "Color: 0, Key:J Parent: I\n";
		assertEquals(str, makeStringDetails(rbt));
		
		//lookup() test
		assertEquals("G",rbt.lookup("G").key);	//Should return key of G
		
		//getSibling() test
		assertEquals(rbt.lookup("B"),rbt.getSibling(rbt.lookup("F")));	//Look Up returns B, B is sibling of F
		assertNotEquals(rbt.lookup("A"),rbt.getSibling(rbt.lookup("F")));	//A is not sibling of F
		
		//getGrandparent() test
		assertEquals(rbt.lookup("D"),rbt.getGrandparent(rbt.lookup("A")));	//D is grandparent of A
		assertNotEquals(rbt.lookup("D"),rbt.getGrandparent(rbt.lookup("J")));	//D is not grandparent of J
		
		//isLeaf() test
		assertTrue(rbt.isLeaf(rbt.lookup("J")));	//J is leaf of Tree
		assertFalse(rbt.isLeaf(rbt.lookup("F")));	//A is not a leaf
		
		//isLeftChild() test
		assertTrue(rbt.isLeftChild(rbt.lookup("D"),rbt.lookup("B")));	//B is left child of D
		assertFalse(rbt.isLeftChild(rbt.lookup("D"),rbt.lookup("J")));	//J is not left child of D
		
		//getAunt() test
		assertEquals(rbt.lookup("F"),rbt.getAunt(rbt.lookup("A")));	//Aunt of A is F
		assertNotEquals(rbt.lookup("D"),rbt.getAunt(rbt.lookup("A")));
		
		//isEmpty() test
		assertFalse(rbt.isEmpty(rbt.lookup("J")));	//J node has a key
		assertNull(rbt.lookup("J").leftChild);
	}
	
	public static String makeString(RedBlackTree t)
    {
       class MyVisitor implements RedBlackTree.Visitor {
          String result = "";
          public void visit(RedBlackTree.Node n)
          {
             result = result + n.key;
          }
       };
       MyVisitor v = new MyVisitor();
       t.preOrderVisit(v);
       return v.result;
    }

    public static String makeStringDetails(RedBlackTree t) {
    	{
    	       class MyVisitor implements RedBlackTree.Visitor {
    	          String result = "";
    	          public void visit(RedBlackTree.Node n)
    	          {
    	        	  String parent="";
    	        	  if(n.parent!=null)
    	        		  parent=n.parent.key.toString();
    	        	  if(n!=null)
    	        		  result = result +"Color: "+n.color+", Key:"+n.key+" Parent: "+parent+"\n";
    	          }
    	       };
    	       MyVisitor v = new MyVisitor();
    	       t.preOrderVisit(v);
    	       return v.result;
    	 }
    }
}
