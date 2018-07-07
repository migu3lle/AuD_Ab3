package ab3.test;

import ab3.impl.GagglGundackerKopali.BTreeMapImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BTreeTest {
    BTreeMapImpl tree;
    int minDegree = 2;

    @Before
    public void createTree(){
        tree = new BTreeMapImpl();
    }

    @After
    public void clearTree(){
        tree.clear();
    }

    @Test(expected = IllegalStateException.class)
    public void setMinDegreeTest(){
        tree.setMinDegree(minDegree);
        tree.setMinDegree(minDegree);
    }

    @Test(expected = IllegalStateException.class)
    public void putIllegalTest(){
        tree.put(1, "1");
    }

    @Test
    public void put1Test(){
        tree.setMinDegree(2);
        Assert.assertTrue(tree.put(1,"1"));
    }

    @Test
    public void put2Test(){
        tree.setMinDegree(2);

        tree.put(1, "Eins");
        tree.printRecursive(tree.getRoot());
        tree.put(2, "Zwei");
        tree.printRecursive(tree.getRoot());
        tree.put(3, "Drei");
        tree.printRecursive(tree.getRoot());
        tree.put(4, "Vier");
        tree.printRecursive(tree.getRoot());
        tree.put(5, "Fünf");
        tree.printRecursive(tree.getRoot());
        tree.put(6, "Sechs");
        tree.printRecursive(tree.getRoot());
        tree.put(7, "Sieben");
        tree.printRecursive(tree.getRoot());
        tree.put(8, "Acht");
        tree.printRecursive(tree.getRoot());
        tree.put(9, "Neun");
        tree.printRecursive(tree.getRoot());
        tree.put(10, "Zehn");
        tree.printRecursive(tree.getRoot());
    }
}
