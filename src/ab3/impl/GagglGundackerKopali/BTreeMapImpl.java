package ab3.impl.GagglGundackerKopali;

import ab3.BNode;
import ab3.BTreeMap;

import java.util.List;

public class BTreeMapImpl implements BTreeMap {

    @Override
    public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException {

    }

    @Override
    public boolean put(int key, String value) throws IllegalStateException, IllegalArgumentException {
        return false;
    }

    @Override
    public boolean delete(int key) throws IllegalStateException, IllegalArgumentException {
        return false;
    }

    @Override
    public String contains(int key) throws IllegalStateException {
        return null;
    }

    @Override
    public BNode getRoot() throws IllegalStateException {
        return null;
    }

    @Override
    public List<Integer> getKeys() throws IllegalStateException {
        return null;
    }

    @Override
    public List<String> getValues() throws IllegalStateException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }
}
