import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruan on 16-10-21.
 */
public class TestGis {

    private static Graph graph = null;
    private static Connection conn;

    public static void init() throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Map_Scale";
        conn = DriverManager.getConnection(url, "postgres", "123");
        Statement stmt = conn.createStatement();

//        ResultSet s_node = stmt.executeQuery("SELECT array_to_json(array_agg(t)) FROM Node As t;");

        ResultSet s_edge = stmt.executeQuery("select row_to_json(t)  from edge as t ;");
        List<EdgeBean> beanns = new ArrayList<EdgeBean>();
        Gson gson = new Gson();
        while (s_edge.next()) {
            EdgeBean bean = gson.fromJson(s_edge.getString(1), EdgeBean.class);
            beanns.add(bean);
        }

        ResultSet s_node = stmt.executeQuery("select row_to_json(t)  FROM (select id, ST_ASTEXT(the_geom) from Node) As t");
        List<MapNode> MapNodeBeanns = new ArrayList<MapNode>();
        while (s_node.next()) {
            MapNode bean = gson.fromJson(s_node.getString(1), MapNode.class);
            MapNodeBeanns.add(bean);
        }

        stmt.close();

        graph = new Graph(beanns, MapNodeBeanns);

    }

    private static void showPathOnOpenlayer(ArrayList<Integer> edges) throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Map_Scale";
        conn = DriverManager.getConnection(url, "postgres", "123");
        Statement stmt = conn.createStatement();
        Gson gson = new Gson();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        List<MapNode2> MapNodeBeanns = new ArrayList<MapNode2>();
        for (Integer id : edges) {
            ResultSet s_edge = stmt.executeQuery("select *  from edge where id =" + id + " ;");
            while (s_edge.next()) {
                System.out.println(s_edge.getString(1) + "-------" + s_edge.getString(2));
                MapNode2 bean = new MapNode2();
                bean.setId(new Integer(s_edge.getString(1)));
                bean.setThe_geom(s_edge.getString(2));
                MapNodeBeanns.add(bean);
            }
        }
        stmt.close();

        conn = DriverManager.getConnection(url, "postgres", "123");
        stmt = conn.createStatement();
        stmt.execute(" TRUNCATE TABLE Path ");
        for (MapNode2 node : MapNodeBeanns) {
            stmt.execute("insert into path(id,the_geom) values(" + node.getId() + "," + "'" + node.getThe_geom() + "'" + ")");

        }
        stmt.close();
        //   TRUNCATE TABLE Path  删除所有数据
    }


    public void destroy() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
    public static void main(String args[]) throws Exception {
        long total = Runtime.getRuntime().maxMemory();
        long Strat = System.currentTimeMillis();
        init();

        A_star4 a_star = new A_star4(graph);
        a_star.runA_star(graph, graph.getNodes().get(1), graph.getNodes().get(112));
        Path path = a_star.ReturnBestPath();
        ArrayList<Integer> edges = path.edges_id;

        showPathOnOpenlayer(edges);

        long End = System.currentTimeMillis();
        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("the total time is " + (End - Strat) + " nanoseconds");
        System.out.println("the required memory is " + (total - freeMemory) / (1024) + "kb");
    }

}
