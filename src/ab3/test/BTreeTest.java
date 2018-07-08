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

        for (int i = 1; i <= 10; i++) {
            System.out.println("------ Now putting element " + i + " --------");
            tree.put(i, i+"");
            tree.printElements();

            tree.getKeys();
        }
    }
}
