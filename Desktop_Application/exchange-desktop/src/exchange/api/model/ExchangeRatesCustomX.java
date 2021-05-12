package exchange.api.model;
import com.google.gson.annotations.SerializedName;
public class ExchangeRatesCustomX {

    @SerializedName("time_requested")
    public Integer x;

    public ExchangeRatesCustomX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }
}
