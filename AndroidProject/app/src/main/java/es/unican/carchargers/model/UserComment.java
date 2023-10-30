package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserComment implements Serializable {
    @SerializedName("ID")               public String id;
    //@SerializedName("ChargePointID")    public String chargePointID;
    @SerializedName("UserName")         public String userName;
    @SerializedName("DateCreated")      public String dateCreated;
    @SerializedName("Comment")          public String comment;

    public UserComment(String ID, String userName, String comment, String dateCreated) {
        this.id = ID;
        this.userName = userName;
        this.comment = comment;
        this.dateCreated = dateCreated;
    }

}
