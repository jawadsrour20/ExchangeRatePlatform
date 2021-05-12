package exchange.api;
import exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;

public interface Exchange {
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();

    @POST("/user")
    Call<User> addUser(@Body User user);

    @POST("/authentication")
    Call<Token> authenticate(@Body User user);

    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction, @Header("Authorization") String authorization);

    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization") String authorization);

    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction);

    @POST("/addUserExchange")
    Call<Object> addUserExchange(@Body UserExchange userExchange, @Header("Authorization") String authorization);

    @GET("/getUserExchanges")
    Call<List<UserExchange>> getUserExchanges(@Header("Authorization") String authorization);

    @POST("/exchangeRateCustom")
    Call<ExchangeRates> getExchangeRatesCustom(@Body ExchangeRatesCustomX numberOfDays);

    @POST("/exchangeRateStartEnd")
    Call<ExchangeRates> getExchangeRatesCustom2(@Body ExchangeRatesCustomAB AB);

    @GET("/highestAndLowestRates")
    Call<HighestAndLowestRates> getHighestAndLowestRates();

    @GET("/fluctuationWeek")
    Call<Fluctuation> getFluctuation();

}
