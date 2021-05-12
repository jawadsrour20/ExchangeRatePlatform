package exchange.api.model;
import com.google.gson.annotations.SerializedName;
public class Fluctuation {


    @SerializedName("sellFluctuation")
    public Float sellFluctuation;
    @SerializedName("buyFluctuation")
    public Float buyFluctuation;

}
