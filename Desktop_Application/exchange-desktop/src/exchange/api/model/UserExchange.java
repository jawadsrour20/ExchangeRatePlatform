package exchange.api.model;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.SerializedName;

public class UserExchange {

    @SerializedName("usd_amount")
    Float usdAmount;
    @SerializedName("lbp_amount")
    Float lbpAmount;
    @SerializedName("usd_to_lbp")
    Boolean usdToLbp;
    @SerializedName("id")
    Integer id;
    @SerializedName("user_received_username")
    String receivedUsername;
    @SerializedName("date_of_transaction")
    String transactionDate;


    public UserExchange(Float usdAmount, Float lbpAmount, Boolean usdToLbp, String receivedUsername)
    {
        this.usdAmount = usdAmount;
        this.lbpAmount = lbpAmount;
        this.usdToLbp = usdToLbp;
        this.receivedUsername = receivedUsername;
    }

    public Float getUsdAmount() {
        return usdAmount;
    }

    public Float getLbpAmount() {
        return lbpAmount;
    }

    public Boolean getUsdToLbp() {
        return usdToLbp;
    }

    public Integer getId() {
        return id;
    }

    public String getReceivedUsername() {
        return receivedUsername;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}