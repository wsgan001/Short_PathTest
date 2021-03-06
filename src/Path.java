import java.util.ArrayList;

/**
 * Created by ruan on 16-9-14.
 */
public class Path {
    double G = 0;
    double U = 0;
    ArrayList<Node> stack;
    ArrayList<Integer> edges_id;

    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getU() {
        return U;
    }

    public void setU(double u) {
        U = u;
    }

    public Path(double g, double u) {
        G = g;
        U = u;
    }

    public Path(double g, double u, ArrayList<Node> stack) {
        G = g;
        U = u;
        this.stack = stack;
    }

    public Path(double g, double u, ArrayList<Node> nodes, ArrayList<Integer> edges) {
        G = g;
        U = u;
        this.stack = nodes;
        this.edges_id = edges;
    }
}
