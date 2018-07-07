package ab3.impl.GagglGundackerKopali;

import ab3.BNode;
import ab3.BTreeMap;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BTreeMapImpl implements BTreeMap {
    private int minDegree;
    private int minNodeKeys;
    private int maxNodeKeys;

    private int called = 0;
    private int size;
    private BNode root;
    private BNode currNode;
    private BNode parentNode;


    //Internal lists which are filled immediately when adding a node or KeyValuePair
    private List<BNode> nodeList = new ArrayList<>();
    private List<BNode.KeyValuePair> keyValuePairList = new ArrayList<>();

    @Override
    public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException {
        //Check for minimum degree criterion
        if (t < 2) {
            throw new IllegalArgumentException("Must be greater than 2");
        }
        //Check if setMinDegree already called
        if (called != 0) {
            throw new IllegalStateException("Already called");//checking if it is called, this called value will be changed also at clear method
        }
        //Setting the minimal degree of the B-tree
        minDegree = t;
        minNodeKeys = t - 1;
        maxNodeKeys = 2 * t - 1;
        called++;
    }

    @Override
    public boolean put(int key, String value) throws IllegalStateException, IllegalArgumentException {
        //KeyValuePair to be inserted
        BNode.KeyValuePair keyValuePair = new BNode.KeyValuePair(key, value);
        //Temporary list to retrieve all KeyValuePairs of a node and add the KeyValuePair to be inserted
        List<BNode.KeyValuePair> insertList = new ArrayList<>();

        if (called == 0) {
            throw new IllegalStateException();
        }

        if (root == null) {
            //If tree is empty create root node with one KeyValuePair
            root = new BNode();
            nodeList.add(root);
            insert(root, 0, keyValuePair);
            return true;
        }
        //Root has no children yet and space for another KeyValuePair (depending on minDegree)
        else if ((root.getChildren() == null || root.getChildren().isEmpty()) && root.getKeys().size() < maxNodeKeys) {
            //Check if root holds same element already
            if (nodeHasElement(root, key)) {
                return false;
            }
            for (BNode.KeyValuePair pair : root.getKeyValuePairs()) {
                //Find KeyValuePair in list which is greater and put new element to this index
                if (pair.getKey() > keyValuePair.getKey()) {
                    insert(root, root.getKeyValuePairs().indexOf(pair), keyValuePair);
                    return true;
                }
            }
            //Insert as last element in root
            insert(root, root.getKeyValuePairs().size(), keyValuePair);
            return true;
        }
        //Now we have root's children --> put in helper method
        else {
            //Start with root
            currNode = root;
            if (put(keyValuePair)) {
                return true;
            } else {
                return false;
            }
        }
    }

    //Helper method to insert to nodes
    public boolean put(BNode.KeyValuePair element) {

        //No children and free space --> put to currNode
        if((currNode.getChildren() == null || currNode.getChildren().isEmpty()) && currNode.getKeys().size() < maxNodeKeys){
            insert(currNode, element);
            return true;
        }
        //No children but full --> split and put
        else if((currNode.getChildren() == null || currNode.getChildren().isEmpty())){
            split(currNode);
            System.out.println("Split done. Now put element " + element.getValue());
            return put(element);
        }
        //If children --> search for next currNode in childs
        for (BNode.KeyValuePair keyValuePair : currNode.getKeyValuePairs()) {
            //Left child
            if(keyValuePair.getKey() > element.getKey()){
                //Next node after key[i] = child[i]
                parentNode = currNode;
                currNode = currNode.getChildren().get(currNode.getKeyValuePairs().indexOf(keyValuePair));
                return put(element);
            }
        }
        //Next node after key[i] = child[i+1]
        parentNode = currNode;
        currNode = currNode.getChildren().get(currNode.getKeyValuePairs().size());
        return put(element);
    }

    //Helper method to split nodes
    private void split(BNode nodeToSpilt) {
        BNode leftNode = new BNode();
        BNode rightNode = new BNode();
        nodeList.add(leftNode);
        nodeList.add(rightNode);

        List<BNode.KeyValuePair> keyValuePairs = nodeToSpilt.getKeyValuePairs();
        List<BNode.KeyValuePair> leftPairs = new ArrayList<>();
        List<BNode.KeyValuePair> rightPairs = new ArrayList<>();
        List<BNode.KeyValuePair> rootPairs = new ArrayList<>();
        List<BNode> newChildren = new ArrayList<>();


        //Fill left node with KeyValuePairs
        for (int i = 0; i < getMedianIndex(nodeToSpilt); i++) {
            System.out.println("adding element " + keyValuePairs.get(i).getValue() + " to left node");
            leftPairs.add(keyValuePairs.get(i));
            leftNode.setKeyValuePairs(leftPairs);
        }

        //Fill right node with KeyValuePairs
        for (int i = getMedianIndex(nodeToSpilt)+1; i < keyValuePairs.size(); i++) {
            System.out.println("adding element " + keyValuePairs.get(i).getValue() + " to right node");
            rightPairs.add(keyValuePairs.get(i));
            rightNode.setKeyValuePairs(rightPairs);
        }

        //Add median to rootPairs
        rootPairs.add(keyValuePairs.get(getMedianIndex(nodeToSpilt)));
        System.out.println("adding element " + keyValuePairs.get(getMedianIndex(nodeToSpilt)).getValue() + " to new root node");

        //New root is standalone
        if(parentNode == null){
            nodeToSpilt.setKeyValuePairs(rootPairs);
            newChildren.add(leftNode);
            newChildren.add(rightNode);
            nodeToSpilt.setChildren(newChildren);
        }
        //Median to be integrated in it's parent node
        else if(parentNode != null && parentNode.getKeys().size() < maxNodeKeys){
            //Inserting median to proper position in it's parent
            insert(parentNode, parentNode.getChildren().indexOf(currNode), keyValuePairs.get(getMedianIndex(nodeToSpilt)));
            //Link new child nodes (left & right) to proper KeyValuePair in parent node
            List<BNode> tempNewChilds;
            tempNewChilds = parentNode.getChildren();

            tempNewChilds.add(parentNode.getChildren().indexOf(currNode), leftNode);    //Moves index of currNode to the right
            tempNewChilds.add(parentNode.getChildren().indexOf(currNode), rightNode);   //Moves index of currNode to the right
            tempNewChilds.remove(parentNode.getChildren().indexOf(currNode));
            nodeList.remove(currNode);
            parentNode.setChildren(tempNewChilds);  //Re-define parent's children list
            currNode = parentNode;

        }
        //Need to split parent before integrating new root to it's parent
        else if(parentNode != null){
            split(parentNode);
            currNode = parentNode;
        }

    }

    private int getMedianIndex(BNode node){
        return node.getKeys().size() / 2;
    }

    //Helper method to insert element to certain position in node
    public void insert(BNode node, int index, BNode.KeyValuePair element) {
        List<BNode.KeyValuePair> tempElements;
        if(node.getKeyValuePairs() == null){
            tempElements = new ArrayList<>();
        }
        else{
            tempElements = node.getKeyValuePairs();
        }
        tempElements.add(index, element);
        node.setKeyValuePairs(tempElements);
        keyValuePairList.add(element);
    }

    //Helper method to insert element to node, finding the proper position
    public void insert(BNode node, BNode.KeyValuePair element){
        for (BNode.KeyValuePair pair : node.getKeyValuePairs()) {
            if(pair.getKey() > element.getKey()){
                //Insert as first element or in between others
                insert(node, node.getKeyValuePairs().indexOf(pair), element);
                return;
            }
        }
        //Insert as last element in node
        insert(node, node.getKeyValuePairs().size(), element);
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

    //Helper method to check if a node holds KeyValuePair to be inserted
    public boolean nodeHasElement(BNode node, int key) {
        for (int nodeKey : node.getKeys()) {
            if (nodeKey == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BNode getRoot() throws IllegalStateException {
        if (called == 0) throw new IllegalStateException();
        return root;
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
        minDegree = -1;
        minNodeKeys = -1;
        maxNodeKeys = -1;
        called = 0;
        size = 0;
        root = null;
        currNode = null;
        nodeList = null;
        keyValuePairList = null;
    }

    @Override
    public int size() {
        return size;
    }

    public void printElements(){
        System.out.println("----------------START PRINT --------------------");

        for (BNode node : nodeList) {
            System.out.println("Printing elements of node: " + nodeList.indexOf(node));

            for (BNode.KeyValuePair pair : node.getKeyValuePairs()) {
                System.out.println("Node " + nodeList.indexOf(node) + " element: " + pair.getValue());
            }
        }
        System.out.println("Parent node: " + nodeList.indexOf(parentNode));
        System.out.println("Current node: " + nodeList.indexOf(currNode));
        System.out.println("----------------END PRINT --------------------");
    }



    public void printRecursive(BNode node){
        System.out.println("----------------START PRINT--------------------");
        System.out.println("Node: " + Arrays.toString(node.getValues().toArray()));
        if(node.getChildren() != null) {
            for (BNode childNode : node.getChildren()) {
                System.out.print("Child: ");
                for (BNode.KeyValuePair pair : childNode.getKeyValuePairs()) {
                    System.out.print(pair.getValue() + " ");
                }
                System.out.println("");
            }
            for(BNode childNode : node.getChildren()){
                printRecursive(childNode);
            }
        }
        System.out.println("----------------END PRINT --------------------");
    }


}
