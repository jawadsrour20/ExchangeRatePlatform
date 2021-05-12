package exchange.highestandlowestrates;



import exchange.api.ExchangeService;
import exchange.api.model.HighestAndLowestRates;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;
import java.net.URL;
import java.util.ResourceBundle;


public class GetHighestAndLowestRates implements Initializable {

    public Label maxBuyUSDLabel;
    public Label maxSellUSDLabel;
    public Label minBuyUSDLabel;
    public Label minSellUSDLabel;

    @FXML LineChart<String, Number> lineChartMaxMin;



    private void fetchRates() {

        ExchangeService.exchangeApi().getHighestAndLowestRates().enqueue(new
        Callback<HighestAndLowestRates>() {
        @Override
        public void onResponse(Call<HighestAndLowestRates> call,
                            Response<HighestAndLowestRates> response) {
            HighestAndLowestRates rates = response.body();
         Platform.runLater(() -> {
             maxBuyUSDLabel.setText(rates.maxBuyUSD.toString());
             maxSellUSDLabel.setText(rates.maxSellUSD.toString());
             minSellUSDLabel.setText(rates.minSellUSD.toString());
             minBuyUSDLabel.setText(rates.minBuyUSD.toString());
             XYChart.Series<String, Number> series = new  XYChart.Series<String, Number>();
             series.getData().add(new XYChart.Data<String, Number>("", Float.parseFloat(maxBuyUSDLabel.getText())));
             series.getData().add(new XYChart.Data<String, Number>("            ", Float.parseFloat(minBuyUSDLabel.getText())));
             series.setName("Buy USD");

             XYChart.Series<String, Number> series2 = new  XYChart.Series<String, Number>();
             series2.getData().add(new XYChart.Data<String, Number>("   ", Float.parseFloat(maxSellUSDLabel.getText())));
             series2.getData().add(new XYChart.Data<String, Number>("                                   ", Float.parseFloat(minSellUSDLabel.getText())));
             series2.setName("Sell USD");

             lineChartMaxMin.getData().addAll(series, series2);
         });
        }
        @Override
        public void onFailure(Call<HighestAndLowestRates> call, Throwable
             throwable) {
        } });
        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fetchRates();



    }
}
