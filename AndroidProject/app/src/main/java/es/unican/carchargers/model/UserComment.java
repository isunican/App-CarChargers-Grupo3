package es.unican.carchargers.model;

import com.google.gson.annotations.SerializedName;

public class UserComment {
    @SerializedName("ID")               public int id;
    //@SerializedName("ChargePointID")    public String chargePointID;
    @SerializedName("UserName")         public String userName;
    @SerializedName("DateCreated")      public String dateCreated;
    @SerializedName("Comment")          public String comment;
}
