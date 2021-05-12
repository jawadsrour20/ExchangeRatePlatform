package exchange.api.model;
import com.google.gson.annotations.SerializedName;

public class HighestAndLowestRates {

    @SerializedName("maxBuyUSD")
    public Float maxBuyUSD;
    @SerializedName("maxSellUSD")
    public Float maxSellUSD;

    @SerializedName("minBuyUSD")
    public Float minBuyUSD;
    @SerializedName("minSellUSD")
    public Float minSellUSD;

}

