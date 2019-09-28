import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class Solution {
    ArrayList<Node> tour;
    int tourLength = 0;

    Solution() {
        tour = new ArrayList<>(1600);
    }

    void twoOptimize() {
        int bestBoost = -1;
        int finalBoost = 0;

        while (bestBoost != 0) {
            bestBoost = 0;


            Node bestC = tour.get(0);
            Node bestB = null;
            Node last = tour.get(tour.size()-1);


            ListIterator<Node> itr;
            itr = tour.listIterator(tour.indexOf(bestC));
            itr.add(tour.get(tour.size()-1));

            while (itr.hasNext()) {
                    Node a = itr.previous();
                    itr.next();
                    Node b = itr.next();

                    if (last == b || a == b) {
                        break;
                    }

                    ListIterator<Node> itr2;
                    itr2 = tour.listIterator(tour.indexOf(b) + 2);

                    if (itr2.hasNext()) {

                        while (itr2.hasNext()) {
                            Node c = itr2.previous();
                            itr2.next();
                            Node d = itr2.next();
                            int gain = (d.dist(b) + c.dist(a)) - (a.dist(b) + c.dist(d));
                            if (gain < bestBoost) {
                                bestBoost = gain;
                                bestB = b;
                                bestC = c;
                            }
                        }
                    }
                    if (bestBoost < 0) break;
            }

            tour.remove(tour.get(0));
            finalBoost += bestBoost;
            if (bestB != null) {
                int posB = tour.indexOf(bestB);
                int posC = tour.indexOf(bestC);
                List<Node> l = tour.subList(Math.min(posC, posB), Math.max(posC, posB)+1);
                Collections.reverse(l);
            }
        }
        tourLength = tourLength+finalBoost ;
    }

    boolean validate(){
        for (int i = 0; i < tour.size(); i++) {
            if (tour.lastIndexOf(tour.get(i)) != tour.indexOf(tour.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (final Node n : tour) {
            buffer.append(n.toString()).append(" ");
        }
        return buffer.toString() + tour.get(0);

    }

    void setTour(ArrayList<Node> tour, int tourLength) {
        this.tour = tour;
        this.tourLength = tourLength;
    }

    int getTourLength() {
        return tourLength;
    }

    ArrayList<Node> getPath() {
        return tour;
    }

    Node getLast() {
        return tour.get(tour.size() - 1);
    }
}
