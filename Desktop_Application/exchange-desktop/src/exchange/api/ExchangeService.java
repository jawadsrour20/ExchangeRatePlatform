package exchange.api;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ExchangeService {

    public static String getApiUrl() {
        return API_URL;
    }

    static String API_URL = "http://localhost:5000";
    public static Exchange exchangeApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new
                        GsonBuilder().setLenient().create()))
                .build();
        return retrofit.create(Exchange.class);
    } }