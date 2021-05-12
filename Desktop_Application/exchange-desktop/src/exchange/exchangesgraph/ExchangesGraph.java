package exchange.exchangesgraph;



import exchange.api.ExchangeService;
import exchange.api.model.ExchangeRates;
import exchange.api.model.ExchangeRatesCustomAB;
import exchange.api.model.Fluctuation;
import exchange.api.model.HighestAndLowestRates;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;
import java.net.URL;
import java.util.ResourceBundle;


public class ExchangesGraph implements Initializable{
    @FXML LineChart<String, Number> lineChart8Weeks;

    private float week1BuyUSD;
    private float week1SellUSD;
    private float week2BuyUSD;
    private float week2SellUSD;
    private float week3BuyUSD;
    private float week3SellUSD;
    private float week4BuyUSD;
    private float week4SellUSD;
    private float week5BuyUSD;
    private float week5SellUSD;
    private float week6BuyUSD;
    private float week6SellUSD;
    private float week7BuyUSD;
    private float week7SellUSD;
    private float week8BuyUSD;
    private float week8SellUSD;

    public Label buyFluctuation;
    public Label sellFluctuation;

    public void fetchFluctuation() {

        ExchangeService.exchangeApi().getFluctuation().enqueue(new
         Callback<Fluctuation>() {
             @Override
             public void onResponse(Call<Fluctuation> call,
                                    Response<Fluctuation> response) {
                 Fluctuation fluctuation = response.body();
                 Platform.runLater(() -> {
                     assert fluctuation != null;
                     float tempBuyFluctuation = (float) (Math.round(fluctuation.buyFluctuation*100.0)/100.0);
                     float tempSellFluctuation = (float) (Math.round(fluctuation.sellFluctuation*100.0)/100.0);
//                     # ex: sellFluctuation = 63 -> 63% increase in the USD to LBP exchange
                    //# ex: sellFluctuation = -44 -> 44% decrease in the USD to LBP exchange
                     if (tempBuyFluctuation < 0)
                     {
                         tempBuyFluctuation = Math.abs(tempBuyFluctuation);
                         buyFluctuation.setText(buyFluctuation.getText()+ tempBuyFluctuation +"% decrease in LBP to USD Exchange Rates");
                         buyFluctuation.setTextFill(Color.web("#FF4136"));
                     }
                     else if (tempBuyFluctuation > 0)
                     {
                         buyFluctuation.setText(buyFluctuation.getText()+ tempBuyFluctuation +"% increase in LBP to USD Exchange Rates");
                         buyFluctuation.setTextFill(Color.web("#2ECC40"));
                     }

                     else
                     {
                         buyFluctuation.setText(buyFluctuation.getText()+ " constant LBP to USD Exchange Rates");
                         buyFluctuation.setTextFill(Color.web("#FFDC00"));
                     }

//
                     if (tempSellFluctuation < 0)
                     {
                         tempSellFluctuation = Math.abs(tempSellFluctuation);
                         sellFluctuation.setText(sellFluctuation.getText()+ tempSellFluctuation +"% decrease in USD to LBP Exchange Rates");
                         sellFluctuation.setTextFill(Color.web("#FF4136"));
                     }
                     else if (tempSellFluctuation > 0)
                     {
                         sellFluctuation.setText(sellFluctuation.getText()+ tempSellFluctuation +"% increase in USD to LBP Exchange Rates");
                         sellFluctuation.setTextFill(Color.web("#2ECC40"));
                     }
                     // case tempSellFluctuation = 0
                     else
                     {
                         sellFluctuation.setText(sellFluctuation.getText()+ " constant USD to LBP Exchange Rates");
                         sellFluctuation.setTextFill(Color.web("#FFDC00"));
                     }

                 });
             }
             @Override
             public void onFailure(Call<Fluctuation> call, Throwable
                     throwable) {
             } });

    }

    private void fetch() {

        ExchangeRatesCustomAB week1 = new ExchangeRatesCustomAB(7,0);
        ExchangeService.exchangeApi().getExchangeRatesCustom2(week1).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates1 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates1 != null;
                          week1BuyUSD = Float.parseFloat(exchangeRates1.lbpToUsd.toString());
                          week1SellUSD = Float.parseFloat(exchangeRates1.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

            ExchangeRatesCustomAB week2 = new ExchangeRatesCustomAB(14,7);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week2).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates2 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates2 != null;
                          week2BuyUSD = Float.parseFloat(exchangeRates2.lbpToUsd.toString());
                          week2SellUSD = Float.parseFloat(exchangeRates2.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

                ExchangeRatesCustomAB week3 = new ExchangeRatesCustomAB(21,14);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week3).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates3 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates3 != null;
                          week3BuyUSD = Float.parseFloat(exchangeRates3.lbpToUsd.toString());
                          week3SellUSD = Float.parseFloat(exchangeRates3.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

                ExchangeRatesCustomAB week4 = new ExchangeRatesCustomAB(28,21);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week4).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates4 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates4 != null;
                          week4BuyUSD = Float.parseFloat(exchangeRates4.lbpToUsd.toString());
                          week4SellUSD = Float.parseFloat(exchangeRates4.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

                ExchangeRatesCustomAB week5 = new ExchangeRatesCustomAB(35,28);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week5).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates5 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates5 != null;
                          week5BuyUSD = Float.parseFloat(exchangeRates5.lbpToUsd.toString());
                          week5SellUSD = Float.parseFloat(exchangeRates5.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

                ExchangeRatesCustomAB week6 = new ExchangeRatesCustomAB(42,35);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week6).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates6 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates6 != null;
                          week6BuyUSD = Float.parseFloat(exchangeRates6.lbpToUsd.toString());
                          week6SellUSD = Float.parseFloat(exchangeRates6.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

                ExchangeRatesCustomAB week7 = new ExchangeRatesCustomAB(49,42);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week7).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates7 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates7 != null;
                          week7BuyUSD = Float.parseFloat(exchangeRates7.lbpToUsd.toString());
                          week7SellUSD = Float.parseFloat(exchangeRates7.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });

                ExchangeRatesCustomAB week8 = new ExchangeRatesCustomAB(56,49);
                ExchangeService.exchangeApi().getExchangeRatesCustom2(week8).enqueue(new
              Callback<ExchangeRates>() {
                  @Override
                  public void onResponse(Call<ExchangeRates> call,
                                         Response<ExchangeRates> response) {
                      ExchangeRates exchangeRates8 = response.body();
                      Platform.runLater(() -> {
                          assert exchangeRates8 != null;
                          week8BuyUSD = Float.parseFloat(exchangeRates8.lbpToUsd.toString());
                          week8SellUSD = Float.parseFloat(exchangeRates8.usdToLbp.toString());

                      });
                  }
                  @Override
                  public void onFailure(Call<ExchangeRates> call, Throwable
                          throwable) {
                  } });



    }


    public void renderGraph() {

        lineChart8Weeks.getData().clear();
        // Buy USD line
        XYChart.Series<String, Number> series = new  XYChart.Series<String, Number>();
        series.getData().add(new XYChart.Data<String, Number>("1 Week Ago", week1BuyUSD ));
        series.getData().add(new XYChart.Data<String, Number>("2 Weeks Ago", week2BuyUSD));
            series.getData().add(new XYChart.Data<String, Number>("3 Weeks Ago", week3BuyUSD));
            series.getData().add(new XYChart.Data<String, Number>("4 Weeks Ago", week4BuyUSD));
            series.getData().add(new XYChart.Data<String, Number>("5 Weeks Ago", week5BuyUSD));
            series.getData().add(new XYChart.Data<String, Number>("6 Weeks Ago", week6BuyUSD));
            series.getData().add(new XYChart.Data<String, Number>("7 Weeks Ago", week7BuyUSD));
            series.getData().add(new XYChart.Data<String, Number>("8 Weeks Ago", week8BuyUSD));
        series.setName("Buy USD");


        // Sell USD Line
        XYChart.Series<String, Number> series2 = new  XYChart.Series<String, Number>();
        series2.getData().add(new XYChart.Data<String, Number>("1 Week Ago", week1SellUSD ));
        series2.getData().add(new XYChart.Data<String, Number>("2 Weeks Ago", week2SellUSD));
            series2.getData().add(new XYChart.Data<String, Number>("3 Weeks Ago", week3SellUSD));
            series2.getData().add(new XYChart.Data<String, Number>("4 Weeks Ago", week4SellUSD));
            series2.getData().add(new XYChart.Data<String, Number>("5 Weeks Ago", week5SellUSD));
            series2.getData().add(new XYChart.Data<String, Number>("6 Weeks Ago", week6SellUSD));
            series2.getData().add(new XYChart.Data<String, Number>("7 Weeks Ago", week7SellUSD));
            series2.getData().add(new XYChart.Data<String, Number>("8 Weeks Ago", week8SellUSD));

        series2.setName("Sell USD");

        lineChart8Weeks.getData().addAll(series, series2);



        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fetchFluctuation();
        fetch();



    }

}
