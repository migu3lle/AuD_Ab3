package ab3.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ab3.AB3;
import ab3.BNode;
import ab3.BTreeMap;
import ab3.impl.GagglGundackerKopali.AB3Impl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests {

    private AB3 ab3 = new AB3Impl();

    private BTreeMap tree = ab3.newBTreeInstance();

    private static int pts;

    private static Random rand = new Random(System.currentTimeMillis());

    private final static int TEST_SIZE = 1000000;

    @Test(expected = IllegalStateException.class)
    public void testMissingMinDegree() {
	tree.clear();

	tree.put(2, "2");
    }

    @Test(expected = IllegalStateException.class)
    public void testMinDegree() {
	tree.clear();
	tree.setMinDegree(2);
	tree.setMinDegree(2);
    }

    @Test
    public void testInsertBasic_2() {
	tree.clear();
	tree.setMinDegree(2);

	List<Integer> data = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

	data.forEach(i -> tree.put(i, i + ""));

	checkBTreeProps(tree, 2);

	// Daten müssen alle enthalten sein (data ist bereits sortiert)
	Assert.assertEquals(data, tree.getKeys());

	pts += 2;
    }

    @Test
    public void testInsertAdvanced_2() {
	tree.clear();
	tree.setMinDegree(3);

	Set<Integer> s = getRandomSet(TEST_SIZE, 10 * TEST_SIZE);

	// Füge Werte ein
	s.forEach(i -> tree.put(i, i + ""));

	// B-Baum Eigenschaften müssen erfüllt sein
	checkBTreeProps(tree, 3);

	// Alle Elemente müssen enthalten sein
	assertEquals(s.size(), tree.size());

	pts += 2;
    }

    @Test
    public void testContains_2() {
	tree.clear();
	tree.setMinDegree(2);

	Set<Integer> s1 = getRandomSet(TEST_SIZE, 10 * TEST_SIZE);
	Set<Integer> s2 = getRandomSet(TEST_SIZE, 10 * TEST_SIZE);

	s1.forEach(i -> tree.put(i, i + ""));

	checkBTreeProps(tree, 2);

	// Testet, ob die Werte aus s2 auch in s1 sind und ob tree.contains das selbe
	// aussage
	s2.forEach(v -> assertEquals(s1.contains(v), tree.contains(v) != null));

	// Test auch, ob die richtigen Werte zurück kommen

	Map<Integer, String> refTreeMap = new TreeMap<>();
	s1.forEach(i -> refTreeMap.put(i, i + ""));

	// Größe muss passen
	assertEquals(refTreeMap.size(), tree.size());

	// zu jedem enthaltenen Schlüssel der refTreeMap muss tree den selben String
	// liefern
	refTreeMap.keySet().forEach(k -> assertEquals(refTreeMap.get(k), tree.contains(k)));

	pts += 2;
    }

    @Test
    public void testDeleteBasic_3() {
	tree.clear();
	tree.setMinDegree(2);

	List<Integer> data = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	List<Integer> data2 = Arrays.asList(-1, 10);

	// Füge die Elemente ein
	data.forEach(i -> tree.put(i, i + ""));

	// Test die B-Baum Eigenschaften
	checkBTreeProps(tree, 2);

	// Lösche nicht eingefügt Elemente
	data2.forEach(i -> assertEquals(false, tree.delete(i)));

	// Baum muss weiterhin aus data.size() Elementen bestehen
	assertEquals(data.size(), tree.size());

	// Lösche alle Elemente der Reihe nach
	data.forEach(i -> tree.delete(i));

	// Teste abermals die B-Baum Eigenschaften
	checkBTreeProps(tree, 2);

	// Baum muss leer ein
	assertEquals(0, tree.size());

	pts += 3;
    }

    @Test
    public void testDeleteAdvanced_3() {
	tree.clear();
	tree.setMinDegree(2);

	// Erzeuge zwei zufällig ausgewählte Mengen
	Set<Integer> s1 = getRandomSet(TEST_SIZE, 10 * TEST_SIZE);
	Set<Integer> s2 = getRandomSet(TEST_SIZE, 10 * TEST_SIZE);

	// Bestimme s1-s2
	Set<Integer> diff = new HashSet<>(s1);
	diff.removeAll(s2);

	// Füge alle Elemente aus s1 ein, lösche alle aus s2
	s1.forEach(i -> tree.put(i, i + ""));
	s2.forEach(tree::delete);

	checkBTreeProps(tree, 2);

	// Tree muss die Größe der Differenz haben
	assertEquals(diff.size(), tree.size());

	List<Integer> diffAsList = new ArrayList<>(diff);
	Collections.sort(diffAsList);

	// Der Baum muss aus genau die Keys aus diffAsList enthalten
	assertEquals(diffAsList, tree.getKeys());

	pts += 3;
    }

    @AfterClass
    public static void printPts() {
	System.out.println("Punkte: " + pts);
    }

    private static Set<Integer> getRandomSet(int maxSize, int maxValue) {
	Set<Integer> s = new HashSet<>();

	for (int i = 0; i < maxSize; i++) {
	    s.add(rand.nextInt(maxValue));
	}

	return s;
    }

    private static void checkBTreeProps(BTreeMap tree, int minDegree) {
	List<Integer> heights = new ArrayList<>();

	getLeafHeights(tree.getRoot(), heights, 0);

	// bestimme die unterschiedlichen Höhen der Blätter (keine Duplikate)
	Set<Integer> s = new HashSet<>(heights);

	// Des darf nur eine Höhe geben
	assertEquals(1, s.size());

	List<Integer> orderTree = tree.getKeys();
	List<Integer> orderCheck = new ArrayList<>();
	getOrder(tree.getRoot(), orderCheck);

	// Ordnung der Elemente muss korrekt aus den Knoten ausgelesen werden
	assertEquals(orderCheck, orderTree);

	// Zur Sicherheit auch noch auf Sortierung testen
	Collections.sort(orderCheck);
	assertEquals(orderCheck, orderTree);

	// Teste die Anzahl der Elemente
	assertEquals(orderCheck.size(), tree.size());

	// Es muss gleich viele Schlüssel wie Werte geben
	assertEquals(tree.getKeys().size(), tree.getValues().size());

	// Teste auch den Knotengrad
	assertEquals(true, checkTreeDegree(tree, minDegree));

	// Test auch die Anzahl der Kinder
	assertEquals(true, checkChildrenCount(tree.getRoot()));
    }

    private static void getLeafHeights(BNode node, List<Integer> heights, int actHeight) {
	if (node == null) {
	    return;
	}

	if (node.getChildren() == null || node.getChildren().size() == 0) {
	    heights.add(actHeight);
	} else {
	    for (BNode nnode : node.getChildren()) {
		getLeafHeights(nnode, heights, actHeight + 1);
	    }
	}
    }

    private static void getOrder(BNode node, List<Integer> order) {
	if (node == null) {
	    return;
	}

	if (node.getChildren() != null && node.getChildren().size() > 0) {
	    getOrder(node.getChildren().get(0), order);
	}
	for (int i = 0; i < node.getKeys().size(); i++) {
	    order.add(node.getKeys().get(i));
	    if (node.getChildren() != null && node.getChildren().size() > 0) {
		getOrder(node.getChildren().get(i + 1), order);
	    }
	}
    }

    private static boolean checkChildrenCount(BNode node) {
	if (node.getChildren() == null) {
	    return true;
	}

	if (node.getChildren().size() != node.getKeys().size() + 1 && node.getChildren().size() > 0) {
	    return false;
	}

	for (BNode nnode : node.getChildren()) {
	    if (!checkChildrenCount(nnode)) {
		return false;
	    }
	}

	return true;
    }

    private static boolean checkTreeDegree(BTreeMap tree, int minDegree) {
	BNode root = tree.getRoot();

	if (root.getKeys().size() > 2 * minDegree - 1) {
	    return false;
	}

	for (BNode node : root.getChildren()) {
	    if (!checkTreeDegree(node, minDegree)) {
		return false;
	    }
	}

	return true;
    }

    private static boolean checkTreeDegree(BNode node, int minDegree) {
	if (node.getKeys().size() > 2 * minDegree - 1 || node.getKeys().size() < minDegree - 1) {
	    return false;
	}

	if (node.getChildren() == null) {
	    return true;
	}

	for (BNode nnode : node.getChildren()) {
	    if (!checkTreeDegree(nnode, minDegree)) {
		return false;
	    }
	}

	return true;
    }
}