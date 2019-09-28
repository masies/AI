public class Node {
    private int id;
    private float x;
    private float y;
    private int dist[];

    Node(int id, float x, float y, int numberOfNode) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dist = new int[numberOfNode];
    }

    void setDist(final Node n){
        int d = (int) Math.round(Math.sqrt(Math.pow(this.x - n.x,2) + Math.pow(this.y - n.y,2)));
        dist[n.getId()] = d;
    }

    int dist(final Node n) {
        return dist[n.getId()];
    }

    int getId() {
        return id;
    }

    public String toString() {
        return Integer.toString(id);
    }

}
