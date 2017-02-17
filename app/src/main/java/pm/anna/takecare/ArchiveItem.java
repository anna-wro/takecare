package pm.anna.takecare;

/**
 * Created by Anna on 17.02.2017.
 */

public class ArchiveItem {
    private long id;
    private String date;
    private int points;

    public ArchiveItem(long id, String date, int points) {
        this.id = id;
        this.date = date;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}