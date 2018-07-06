package ab3.impl.GagglGundackerKopali;

import ab3.AB3;
import ab3.BNode;
import ab3.BTreeMap;

public class AB3Impl extends BNode implements AB3 {

    @Override
    public BTreeMap newBTreeInstance() {
        return new BTreeMapImpl();
    }

}