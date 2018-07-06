package ab3.impl.GagglGundackerKopali;

import ab3.BNode;
import ab3.BTreeMap;

import java.util.ArrayList;
import java.util.List;

public class BTreeMapImpl extends BNode implements BTreeMap {
    private int minimalVal;
    private int maxVal;
    private int called = 0;
    private int size;
    private BNode root;
    private BNode children[];
    private boolean leaf;
    private int key[];





    public BTreeMapImpl() {
        root = new BNode();
        key = new int[maxVal];
        leaf = true;
        children = new BNode[maxVal];
        size=size();
    }

    @Override
    public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException {
        if (t < 2) throw new IllegalArgumentException("Must be greater than 2");
        if (called != 0)
            throw new IllegalStateException("Already called");//checking if it is called, this called value will be changed also at clear method
        setMaxDegree(t);
        minimalVal = t - 1;//Setting the minimal value
        called++;
    }

    private void setMaxDegree(int t) throws IllegalStateException, IllegalArgumentException {
        if (t < 2) throw new IllegalArgumentException("Must be greater than 2");
        if (called != 0)
            throw new IllegalStateException("Already called");//checking if it is called, this called value will be changed also at clear method

        maxVal = 2*t-1;//Setting the maximal value
        called++;
    }

    @Override
    public boolean put(int key, String value) throws IllegalStateException, IllegalArgumentException {
        if (called == 0) throw new IllegalStateException();
        List<KeyValuePair> x=new ArrayList<>();//Thats how it will work?
        x.add(new KeyValuePair(key,value));
        root.setKeyValuePairs(x);

        return true;
    }

    @Override
    public boolean delete(int key) throws IllegalStateException, IllegalArgumentException {
        if (called == 0) throw new IllegalStateException();
        return false;
    }

    @Override
    public String contains(int key) throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        int i=0;
        while (i<size){
        if(root.getKeys().get(i)==key){
            return root.getKeys().get(i).toString();

        }
            i++;
        }
        return null;
    }


    @Override
    public BNode getRoot() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return root;
    }

    @Override
    public List<Integer> getKeys() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return root.getKeys();
    }

    @Override
    public List<String> getValues() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return root.getValues();
    }

    @Override
    public void clear() {
        called = 0;
        size = 0;

    }

    @Override
    public int size() {
        return size;
    }

}
