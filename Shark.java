import java.util.ArrayList;
import java.util.Random;

class Shark extends Solution {
    private final SharkColony colony;
    private final double pheromoneTable[][];
    private ArrayList<Node> NodesToVisit;
    private final Random random;

    Shark(final SharkColony colony) {
        super();
        this.random = colony.getRandom();
        this.colony = colony;
        this.pheromoneTable = colony.getPheromoneTable();
        this.NodesToVisit = new ArrayList<>(colony.getNodes());

        goToNode(NodesToVisit.get(random.nextInt(NodesToVisit.size())), true);
    }

    void goToNode(final Node node, boolean start) {
        if (!start) {
            localUpdate(tour.get(tour.size()-1), node);
            tourLength += tour.get(tour.size()-1).dist(node);
        } 
        tour.add(node);
        NodesToVisit.remove(node);
        if (NodesToVisit.isEmpty()) {
            tourLength += node.dist(tour.get(0));
            localUpdate(node,tour.get(0));
        }
    }

    private void localUpdate(final Node current, final Node node) {
        double newTau = (1 - colony.getRo()) * pheromoneTable[current.getId()][node.getId()] + (colony.getRo() * colony.getInitialTau());
        pheromoneTable[current.getId()][node.getId()] = newTau;
        pheromoneTable[node.getId()][current.getId()] = newTau;
    }

    Node nextNode() {
        Node toVisit;
        Node lastNode = tour.get(tour.size() - 1);
        if (random.nextDouble() <= colony.getInitialQ()) {
            double maxProb = -1;
            toVisit = NodesToVisit.get(0);
            for (Node c : NodesToVisit) {
                double toCmp;
                toCmp = pheromoneTable[tour.get(tour.size() - 1).getId()][c.getId()] * Math.pow(1.0d/lastNode.dist(c), colony.getBeta());
                if (toCmp > maxProb) {
                    maxProb = toCmp;
                    toVisit = c;
                }
            }

        } else {
            double sum = 0;
            double probs[] = new double[NodesToVisit.size()];

            for (int i = 0; i<NodesToVisit.size(); i++) {
                probs[i] = pheromoneTable[tour.get(tour.size()-1).getId()][NodesToVisit.get(i).getId()] * Math.pow(1.0d/lastNode.dist(NodesToVisit.get(i)), colony.getBeta());
                sum += probs[i];
            }
            int i = 0;

            final double rnd = random.nextDouble();
            double p = 0;
            toVisit = NodesToVisit.get(probs.length-1);
            if (i < probs.length && rnd >= (p += probs[i] / sum)) {
                do {
                    toVisit = NodesToVisit.get(++i);
                } while (i < probs.length && rnd >= (p += probs[i] / sum));
            }
        }
        return toVisit;
    }

}
