import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.ArrayList;


class SharkColony {

    private ArrayList<Node> nodeSet;
    private int optimalSolution;

    private double[][] pheromone;
    private ArrayList<Shark> sharks;

    private int numOfSharks = 5;
    private double initialQ = 0.9d; // the probability for an ant to move from city i to city j
    private int beta = 2;           // heuristic importance
    private double alpha = 0.1d;    // evaporation rate
    private double ro =  0.1d;      // local update
    private double initialTau;      // basic pheromone level

    private Shark myBestKnownSolution;

    private final Random random;
    private final long startTime;

    SharkColony(ArrayList<Node> nodeSet, final Solution solution, final Random random, final long startTime, int optimalSolution) {
        this.initialTau =( 1.0d/solution.getTourLength())* nodeSet.size();
        this.nodeSet = nodeSet;
        this.optimalSolution = optimalSolution;
        this.myBestKnownSolution = null;
        this.pheromone = new double[nodeSet.size()][nodeSet.size()];
        this.sharks = new ArrayList<>(numOfSharks);

        this.random = random;
        this.startTime = startTime;
    }

    Solution optimize() {
        IntStream.range(0, nodeSet.size()).forEach(i -> Arrays.fill(pheromone[i], initialTau));

        while(  (System.currentTimeMillis() - startTime)/1000.0 < 178 &&
                (myBestKnownSolution == null || myBestKnownSolution.getTourLength() > optimalSolution)) {

            //setup sharks
            IntStream.range(0, numOfSharks).forEach(i -> sharks.add(new Shark(this)));

            //move all the sharks
            IntStream.range(0, nodeSet.size() - 1).mapToObj(i -> sharks.stream()).flatMap(Function.identity()).forEach(shark -> {
                final Node next = shark.nextNode();
                shark.goToNode(next, false);
            });

            //find local best Shark
            double localMinTourLen = sharks.get(0).getTourLength();
            Shark localBestShark = sharks.get(0);
            for (int i = 0, sharksSize = sharks.size(); i < sharksSize; i++) {
                Shark shark = sharks.get(i);
                double toCmp = shark.getTourLength();
                if (toCmp <= localMinTourLen) {
                    localBestShark = shark;
                    localMinTourLen = toCmp;
                }
            }

            // two-optimize the local best shark
            localBestShark.twoOptimize();

            // update the best shark
            if (myBestKnownSolution == null || localBestShark.getTourLength() < myBestKnownSolution.getTourLength()) {
                myBestKnownSolution = localBestShark;
            }

            // update pheromone of best shark
            if (myBestKnownSolution == null) globalTrailUpdate(localBestShark);
            else globalTrailUpdate(myBestKnownSolution);



            sharks.clear();
        }
        return myBestKnownSolution;
    }

    private void globalTrailUpdate(final Shark shark) {
        //double k = 1.0d/ shark.getTourLength();
        Node lastNode = shark.getLast();
        for (Node thisNode : shark.getPath()) {
            double currentTau = pheromone[lastNode.getId()][thisNode.getId()];
            double newTau = (1 - alpha) * currentTau + alpha * (initialQ);
            pheromone[lastNode.getId()][thisNode.getId()] = newTau;
            pheromone[thisNode.getId()][lastNode.getId()] = newTau;
            lastNode = thisNode;
        }
    }

    double[][] getPheromoneTable() {
        return pheromone;
    }
    ArrayList<Node> getNodes() {
        return nodeSet;
    }

    double getInitialQ() {
        return initialQ;
    }
    double getInitialTau() {
        return initialTau;
    }
    double getRo() {
        return ro;
    }

    int getBeta() {
        return beta;
    }
    Random getRandom() {
        return random;
    }
}
