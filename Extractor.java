import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Extractor {
    public String name;
    public int dimension;
    public int bestKnown;
    public ArrayList<Node> nodeSet = new ArrayList<>(1600);


    void extract(String fileName) {

        try {
            File f = new File(fileName);
            String tempHolder;
            Scanner sc = new Scanner(f);

            tempHolder = sc.nextLine();
            name = tempHolder.substring(tempHolder.indexOf(": ") + 2);

            sc.nextLine();
            sc.nextLine();

            tempHolder = sc.nextLine();
            dimension = Integer.parseInt(tempHolder.substring(tempHolder.indexOf(": ") + 2));

            sc.nextLine();

            tempHolder = sc.nextLine();
            bestKnown = Integer.parseInt(tempHolder.substring(tempHolder.indexOf(": ") + 2));

            sc.nextLine();

            for (int i = 0; i < dimension; i++) {
                tempHolder = sc.nextLine();
                tempHolder = tempHolder.substring(tempHolder.indexOf(" ") + 1);

                Integer id = i;
                float x = Float.parseFloat(tempHolder.substring(0, tempHolder.indexOf(" ")));
                float y = Float.parseFloat(tempHolder.substring(tempHolder.indexOf(" ") + 1));
                Node node = new Node(id, x, y, dimension);
                nodeSet.add(node);
            }

        } catch (FileNotFoundException ex) {
            System.exit(-1);
        }
    }

}
