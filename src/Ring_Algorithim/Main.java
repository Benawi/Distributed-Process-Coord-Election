package Ring_Algorithim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int MAX_ID = 200;
    private static final int RESET = -1;
    private static Random gen = new Random(System.currentTimeMillis());
    private static Iterator<Integer> shuffledListIterator;
    private static ArrayList<Integer> shuffledList;
    private static ArrayList<Node> nodes;

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        System.out.print("💻 Input number of nodes: ");
        int n = reader.nextInt();
        createNodes(n);
        createRingStructure();
        System.out.print("✍ Input index to begin election or -1 to RESET: ");
        while (true) {
            n = reader.nextInt();
            System.out.println("🖐 Election Onprogress/Result:");
            switch (n) {
                case RESET:
                    System.out.print("💻 Input number of nodes: ");
                    n = reader.nextInt();
                    createNodes(n);
                    createRingStructure();
                    break;
                default:
                    beginElection(n);
                    break;

            }
        }
    }

    private static void createRingStructure() {
        // prepare random unique id  gen
        shuffledListIterator = shuffledList.iterator();
        Collections.shuffle(shuffledList);
        // create ring structure
        Iterator it1 = nodes.iterator();
        Iterator it2 = nodes.iterator();
        it2.next();
        for (; it2.hasNext();) {
            Node node1 = (Node) it1.next();
            Node node2 = (Node) it2.next();
            node1.init(randomId(), node2);
        }
        ((Node) it1.next()).init(randomId(), nodes.get(0));

        // print info of all nodes in ring
        for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
            Node node = (Node) iterator.next();
            System.out.println(node);
        }

    }

    private static void createNodes(int nNodes) {
        nodes = new ArrayList<Node>(nNodes);

        shuffledList = new ArrayList<Integer>(nNodes);

        for (int i = 0; i < nNodes; i++) {
            nodes.add(new Node());
            shuffledList.add(new Integer(i));
        }
    }

    private static void beginElection() {
        beginElection(0);
    }

    private static void beginElection(int index) {

        // begin election: 1st node
        nodes.get(index).beginElection();

        //begin election: all nodes
//       		for (Node node : nodes)
//       			node.beginElection();
    }

    public static int randomId() {
        return shuffledListIterator.next();
    }

    public static int randomId_gen() {
        return gen.nextInt(MAX_ID);
    }

}
