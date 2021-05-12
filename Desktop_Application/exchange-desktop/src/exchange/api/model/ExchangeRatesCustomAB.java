package exchange.api.model;
import com.google.gson.annotations.SerializedName;
public class ExchangeRatesCustomAB {

    @SerializedName("time_to_start")
    public Integer A;

    @SerializedName("time_to_end")
    public Integer B;

    public ExchangeRatesCustomAB(Integer a, Integer b) {
        A = a;
        B = b;
    }

    public Integer getA() {
        return A;
    }

    public Integer getB() {
        return B;
    }
}
