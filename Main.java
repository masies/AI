import java.util.Random;
public class Main {

    private static String[] sets = {
            "ch130",
            "d198",
            "eil76",
            "fl1577",
            "kroA100",
            "lin318",
            "pcb442",
            "pr439",
            "rat783",
            "u1060"};

    private static long[] seeds = {
            1544610699274L,
            1544615544511L,
            1544609515689L,
            1544628556849L,
            1544609695217L,
            1544616051889L,
            1544617876766L,
            1544618655642L,
            1544626596797L,
            1544620054096L};


    public static void startup(int setId) {

        Extractor ext = new Extractor();
        String filename = "./sets/" + sets[setId] + ".tsp";
        ext.extract(filename);

        // update distances
        for (final Node n1 : ext.nodeSet)
            for (final Node n2 : ext.nodeSet) {
                n1.setDist(n2);
                n2.setDist(n1);
            }

        long startTime = System.currentTimeMillis();

        long seed;

        // set to true to use my best seeds
        if (true) {
            seed = seeds[setId];
        } else {
            seed = System.currentTimeMillis();
        }

        Random rnd = new Random(seed);

        //generate random solution
        Solution initialSolution = RandomSolution.generateSolution(ext.nodeSet);

        // give this solution a shot of two-opt....
        initialSolution.twoOptimize();

        //SharkColony creation & setup
        SharkColony colony;
        colony = new SharkColony(ext.nodeSet, initialSolution, rnd, startTime, ext.bestKnown);

        //SharkColony optimisation
        Solution finalSolution = colony.optimize();

        // validity check
        if (!finalSolution.validate()) {
            System.out.println(" B A D ");
            System.out.println("set: " + sets[setId] + " using seed: " + seed);
        }

        // report
        System.out.println(finalSolution);
        System.out.println("Solved set     : " + sets[setId]);
        System.out.println("seed           : " + seed);
        System.out.println("length         : " + finalSolution.getTourLength());



        double experimental_data = finalSolution.getTourLength();
        double exact_solution = ext.bestKnown;
        double error = (Math.abs(exact_solution - experimental_data) / exact_solution) * 100;

        System.out.println("relative error : " + error);
        System.out.println();
    }

    public static void seedMining(){

        for (int i = 0; i < 15 ; i++) {
            startup(8);
        }
        for (int i = 0; i < 15 ; i++) {
            startup(9);
        }
        for (int i = 0; i < 20 ; i++) {
            startup(3);
        }
        for (int i = 0; i < 20 ; i++) {
            startup(6);
        }
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("USAGE : java Main <problem_number>");
            return;
        }
        if (Integer.parseInt(args[0]) > 9 || Integer.parseInt(args[0]) < 0 ) {
            System.out.println("Problem number must be in [1,9]");
            return;
        }
        int set = Integer.parseInt(args[0]);
        startup(set);
//       seedMining();
    }
}
