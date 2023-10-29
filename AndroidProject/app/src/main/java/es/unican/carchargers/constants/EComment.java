package es.unican.carchargers.constants;

public class EComment {
    public final int id;
    public final String userName;
    public final String dateCreated;
    public final String comment;

    private EComment(int id, String userName, String dateCreated, String comment) {
        this.id = id;
        this.userName = userName;
        this.dateCreated = dateCreated;
        this.comment = comment;
    }


}
