import java.util.*;

/**
 * Created by ruan on 9/12/16.
 */
public class A_star3 {
    private double L = 0;
    private static long Strat;
    private static long End;
    private boolean isFirstPath;
    private List<Path> allPathsInfomation;
    private Path bestPath;
    private Path shortestPath;
    private Graph graph;
    private boolean isRun;

    //init data
    public A_star3(Graph graph) {
        this.graph = graph;
        this.isFirstPath = true;
        this.isRun = true;
        allPathsInfomation = new ArrayList<>();

    }

    private void ShowBestPath(Path bestPath) {

        ArrayList stack = bestPath.stack;
        if (stack == null) {
            System.out.print("There must be no best path ~~~");
        } else {
            Iterator iterator = stack.iterator();
            while (iterator.hasNext()) {
                Node temp = (Node) iterator.next();
                System.out.print(temp.N + " <-");
            }

            System.out.println();
            System.out.println("Distaance cost:" + bestPath.G);
            System.out.println("Sum of utility:" + bestPath.U);

        }
    }

    private void SavePath(Node node) {
        ArrayList<Node> path = new ArrayList();


        float tempCost = (float) node.G;
        float tempUtility = 0;
        while (node != null) {
            Node FromNode = node;
            path.add(node);
            node = node.parent;
            Node ToNode = node;
            if (ToNode != null) {
                tempUtility = (float) (tempUtility + FromNode.getAdjEdge().get(ToNode.N).utility);
            }

        }
        //tempPath is used to compare Cost and Utility
        Path tempPath = new Path(tempCost, tempUtility, path);

        // we compare the utility while running algorithm
        if (isFirstPath) {
            shortestPath = tempPath;
            bestPath = shortestPath;
            //threshold
            L = shortestPath.G * 0.2;
            //we get the first path named shortest path,the second ,or third etc path will be longer~
            isFirstPath = false;
        } else {
            if (bestPath != null) {
                if (shortestPath.G + L < tempPath.G) {
                    isRun = false;
                } else if (shortestPath.G + L > tempPath.G && bestPath.U < tempPath.U) {
                    bestPath = tempPath;
                }
            } else {
                System.out.println("Occur some problem！");
            }
        }
        //store path (actually , there is that one more path will be store !!)
        allPathsInfomation.add(tempPath);

    }

    public void runA_star(Graph graph, Node s, Node d) {
        PointComparator pointComparator = new PointComparator();
        PriorityQueue priorityQueue = new PriorityQueue(graph.getV(), pointComparator);

        s.G = 0;
        s.H = (float) Util.getDis(s, d);
        s.F = s.G + s.H;

        s.parent = null;
        priorityQueue.offer(s);


        while (!priorityQueue.isEmpty() && isRun) {
            Node n = (Node) priorityQueue.poll();
            for (Node nn : n.getNeighbors()) {
                // Make sure that next node will not arise again ,it means that we will not go back same place .
                if (!IsNParentContainNN(n, nn)) {

                    double temp_G = n.G + Util.getDis(n, nn);
                    double temp_H = Util.getDis(nn, d);
                    double temp_F = temp_G + temp_H;

                    if (nn.N == d.N) {

                        nn.G = temp_G;
                        nn.H = temp_H;
                        nn.F = temp_F;

                        d.parent = n;
                        SavePath(d);

                        continue;

                    }
                    // We must create a copy node of original node,so that we will change nothing on original data
////                    if (priorityQueue.contains(nn)){
//                    if (MyPqContains(priorityQueue, nn)) {
//                        Node old_point = getNodeFromPq(priorityQueue, nn);
//                        if (temp_F < old_point.F) {
////                            updatePq(priorityQueue, n, nn, temp_G, temp_H, temp_F, old_point);
//                            Node new_nn = new Node();
//                            insertPq(priorityQueue, n, nn.clone(new_nn), temp_G, temp_H, temp_F);
//                        } else {
////                            continue;
//                            Node new_nn = new Node();
//                            insertPq(priorityQueue, n, nn.clone(new_nn), temp_G, temp_H, temp_F);
//                        }
//                    } else {
//                        Node new_nn = new Node();
//                    }
                        Node new_nn = new Node();
                        insertPq(priorityQueue, n, nn.clone(new_nn), temp_G, temp_H, temp_F);
                }

            }

        }

        ShowBestPath(bestPath);

        ShoAllwPathInfomation(allPathsInfomation);
    }

    private void ShoAllwPathInfomation(List<Path> allPathsInfomation) {
        System.out.println("paths:" + (allPathsInfomation.size() - 1));
        for (Path path : allPathsInfomation) {
            ArrayList stack = (ArrayList) path.stack;
            if (stack == null) {
                System.out.print("impossible to reach");
            } else {
                Iterator iterator = stack.iterator();
                System.out.print("The distance is :" + path.G + "------");

                while (iterator.hasNext()) {
                    Node temp = (Node) iterator.next();
                    System.out.print(temp.N + " <-");

                }
                System.out.println();

            }
        }
    }

    private boolean IsNParentContainNN(Node n, Node nn) {
        while (n != null) {
            if (nn.N == n.N) {
                return true;
            }
            n = n.parent;
        }
        return false;
    }

    private void insertPq(PriorityQueue priorityQueue, Node n, Node next_n, double temp_G, double temp_H, double temp_F) {

        next_n.G = temp_G;
        next_n.H = temp_H;
        next_n.F = temp_F;
        next_n.parent = n;

        priorityQueue.offer(next_n);
    }


    class PointComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.F < o2.F) {
                return -1;
            } else if (o1.F == o2.F) {
                return 0;
            } else {
                return 1;
            }
        }
    }
//    private boolean MyPqContains(PriorityQueue priorityQueue, Node nn) {
//        Iterator iterator = priorityQueue.iterator();
//        while (iterator.hasNext()) {
//            Node old_point = (Node) iterator.next();
//            if (old_point.N == nn.N) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    private void updatePq(PriorityQueue closeQueue, Node n, Node next_n, double temp_G, double temp_H, double temp_F, Node old_point) {
//        closeQueue.remove(old_point);
//        insertPq(closeQueue, n, next_n, temp_G, temp_H, temp_F);
//    }
//
//    private Node getNodeFromPq(PriorityQueue closeQueue, Node next_n) {
//        Iterator iterator = closeQueue.iterator();
//        Node old_point = null;
//        while (iterator.hasNext()) {
//            old_point = (Node) iterator.next();
//            if (old_point.N == next_n.N) {
//                break;
//            }
//        }
//        return old_point;
//    }



    public static void main(String args[]) {
        long total = Runtime.getRuntime().maxMemory();
        Strat = System.currentTimeMillis();
        In in = new In(args[0]);
        Graph graph = new Graph(in);
        A_star1 a_star = new A_star1(graph);
        Runtime.getRuntime().totalMemory();
        a_star.runA_star(graph, graph.getNodes().get(0), graph.getNodes().get(4));
        End = System.currentTimeMillis();
        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("the total time is " + (End - Strat) + " nanoseconds");
        System.out.println("the required memory is " + (total - freeMemory) / (1024) + "kb");
    }


}
