package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserComment implements Serializable {
    @SerializedName("ID")               public String id;
    @SerializedName("UserName")         public String userName;
    @SerializedName("DateCreated")      public String dateCreated;
    @SerializedName("Comment")          public String comment;

    public UserComment(String id, String userName, String comment, String dateCreated) {
        this.id = id;
        this.userName = userName;
        this.comment = comment;
        this.dateCreated = dateCreated;
    }

}
