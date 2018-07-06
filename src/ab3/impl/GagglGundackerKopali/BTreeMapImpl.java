package ab3.impl.GagglGundackerKopali;

import ab3.BNode;
import ab3.BTreeMap;

import java.util.List;

public class BTreeMapImpl extends BNode implements BTreeMap {
    private int minimalVal;
    private int called =0;
    private int size;
    private BNode root;

    public BTreeMapImpl(){

    }

    @Override
    public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException {
        if(t<2) throw new IllegalArgumentException("Must be greater than 2");
        if(called!=0) throw new IllegalStateException("Already called");//checking if it is called, this called value will be changed also at clear method
        minimalVal=t-1;//Setting the minimal value
        called++;
    }

    @Override
    public boolean put(int key, String value) throws IllegalStateException, IllegalArgumentException {
        if(called==0)throw new IllegalStateException();
        return false;
    }

    @Override
    public boolean delete(int key) throws IllegalStateException, IllegalArgumentException {
        if(called==0)throw new IllegalStateException();
        return false;
    }

    @Override
    public String contains(int key) throws IllegalStateException {
        if(called==0)throw new IllegalStateException();
        int i=0;

        return null;
    }

    @Override
    public BNode getRoot() throws IllegalStateException {
        if(called==0)throw new IllegalStateException();
        return null;
    }

    @Override
    public List<Integer> getKeys() throws IllegalStateException {
        if(called==0)throw new IllegalStateException();
        return null;
    }

    @Override
    public List<String> getValues() throws IllegalStateException {
        if(called==0)throw new IllegalStateException();
        return null;
    }

    @Override
    public void clear() {
        called=0;
        size=0;

    }

    @Override
    public int size() {
        return size;
    }

}
