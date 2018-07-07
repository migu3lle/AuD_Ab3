package ab3.impl.GagglGundackerKopali;

import ab3.BNode;
import ab3.BTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

        KeyValuePair keyValuePair = new KeyValuePair(key,value);

        if(size == 0){
            //Erstelle eine neue Liste von keyValuePairs und übergib sie dem Root-Node, der Liste wird das einzufügende keyvaluepair hinzugefügt
            List<KeyValuePair> keyValuePairsList = new ArrayList<>();
            List<BNode> childs = new ArrayList<>();
            keyValuePairsList.add(keyValuePair);
            root.setKeyValuePairs(keyValuePairsList);
            root.setChildren(childs);
            size++;
        }else{

            searchCorrectLeaf();

            BNode currentNode = root;
            //Wenn die Wurzel voll ist dann muss aufgespalten werden
            if(currentNode.getKeyValuePairs().size() == maxVal){
                BNode newRoot = new BNode();
                newRoot.setChildren(Arrays.asList(root));

                List<KeyValuePair> keyValuePairList = new ArrayList<>();
                int middleIndex = root.getKeyValuePairs().size();
                keyValuePairList.add(root.getKeyValuePairs().get(middleIndex));
                newRoot.setKeyValuePairs(keyValuePairList);





            }


                if(currentNode.getChildren() == null){
                    for (KeyValuePair k : currentNode.getKeyValuePairs()){
                       if(key > k.getKey()){
                           currentNode.getKeyValuePairs().add(keyValuePair);

                           size++;
                       }else if(key < k.getKey()){

                       }
                    }





                }


            }






        return false;
    }

    private void searchCorrectLeaf() {
        BNode currentNode = root;
        if(currentNode.getKeyValuePairs().size() == maxVal){
            split();
        }

        while(currentNode != null){
            for (KeyValuePair k : currentNode.getKeyValuePairs()){
                

            }

        }





    }

    //Helper Method to split up when a node is full
    private void split() {


    }

    @Override
    public boolean delete(int key) throws IllegalStateException, IllegalArgumentException {
        if (called == 0) throw new IllegalStateException();
        return false;
    }

    @Override
    public String contains(int key) throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        int i = 0;

        return null;
    }

    @Override
    public BNode getRoot() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return null;
    }

    @Override
    public List<Integer> getKeys() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return null;
    }

    @Override
    public List<String> getValues() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return null;
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
