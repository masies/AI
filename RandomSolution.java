import java.util.ArrayList;

class RandomSolution {

     static Solution generateSolution(ArrayList<Node> nodeSet) {
        Solution solution = new Solution();
        int travelLen = 0;
        ArrayList<Node> myRandomTour = new ArrayList<>(1600);
        myRandomTour.add(nodeSet.get(0));

         int i = 1;
         while (i< nodeSet.size()) {
             Node next = nodeSet.get(i);
             travelLen += myRandomTour.get(myRandomTour.size()-1).dist(next);
             myRandomTour.add(next);
             i++;
         }

         travelLen += myRandomTour.get(myRandomTour.size()-1).dist(myRandomTour.get(0));

         solution.setTour(myRandomTour,travelLen);

         return solution;
    }

}
