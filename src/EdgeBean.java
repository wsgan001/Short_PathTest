import java.util.List;

/**
 * Created by ruan on 16-10-22.
 */
class EdgeBean {
    /**
     * id : 19
     * sumutility : 12
     * the_geom : 0102000020E610000002000000D9710511586A3D40950BAB5323D340C0CCD0F774D04C3F40950BAB5323D340C0
     * AdjNode : [1,2]
     */

    private int id;
    private int sumutility;
    private String the_geom;
    private List<Integer> AdjNode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSumutility() {
        return sumutility;
    }

    public void setSumutility(int sumutility) {
        this.sumutility = sumutility;
    }

    public String getThe_geom() {
        return the_geom;
    }

    public void setThe_geom(String the_geom) {
        this.the_geom = the_geom;
    }

    public List<Integer> getAdjNode() {
        return AdjNode;
    }

    public void setAdjNode(List<Integer> AdjNode) {
        this.AdjNode = AdjNode;
    }
}