package exchange.rates;

import exchange.Authentication;
import exchange.api.ExchangeService;
import exchange.api.model.ExchangeRates;
import exchange.api.model.ExchangeRatesCustomAB;
import exchange.api.model.ExchangeRatesCustomX;
import exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class Rates implements Initializable {
    public Label getBuyUsdRateLabel() {
        return buyUsdRateLabel;
    }

    public Label getSellUsdRateLabel() {
        return sellUsdRateLabel;
    }

    public static float getBuyUsdRate() {
        return buyUsdRate;
    }

    public static float getSellUsdRate() {
        return sellUsdRate;
    }

    public TextField getLbpTextField() {
        return lbpTextField;
    }

    public TextField getUsdTextField() {
        return usdTextField;
    }

    public TextField getUsdConversion() {
        return usdConversion;
    }

    public TextField getLbpConversion() {
        return lbpConversion;
    }

    public ToggleGroup getTransactionType() {
        return transactionType;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public TextField lbpTextField;
    public TextField usdTextField;
    public TextField numberDaysAgo;
    public TextField startDate;
    public TextField endDate;

    @FXML
    public TextField usdConversion;
    public TextField lbpConversion;
    public ToggleGroup transactionType;

    private boolean isCustom = false;
    private boolean isCustom2 = false;

    private static float buyUsdRate;
    private static float sellUsdRate;



    /**
     * Reference
     * https://www.baeldung.com/java-check-string-number
     * Regular expression checking that input string is numeric.
     * Function returns true if string is numeric, else returns false
     */
    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
    public void clearForm() {

        usdConversion.setText("");
        lbpConversion.setText("");
        usdConversion.setDisable(false);
        lbpConversion.setDisable(false);


    }
    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new
         Callback<ExchangeRates>() {
             @Override
             public void onResponse(Call<ExchangeRates> call,
                                    Response<ExchangeRates> response) {
                 ExchangeRates exchangeRates = response.body();
                 Platform.runLater(() -> {
                     buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                     sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                     buyUsdRate = exchangeRates.lbpToUsd;
                     sellUsdRate = exchangeRates.usdToLbp;
                 });
             }
             @Override
             public void onFailure(Call<ExchangeRates> call, Throwable
                     throwable) {
             } });
    }

    public void fetchRatesCustom() {
        isCustom = true;
        isCustom2 = false;
        ExchangeRatesCustomX x = new ExchangeRatesCustomX(Integer.parseInt(numberDaysAgo.getText()));
        ExchangeService.exchangeApi().getExchangeRatesCustom(x).enqueue(new
                 Callback<ExchangeRates>() {
                     @Override
                     public void onResponse(Call<ExchangeRates> call,
                                            Response<ExchangeRates> response) {
                         ExchangeRates exchangeRates = response.body();
                         Platform.runLater(() -> {
                             buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                             sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                             buyUsdRate = exchangeRates.lbpToUsd;
                             sellUsdRate = exchangeRates.usdToLbp;
                         });
                     }
                     @Override
                     public void onFailure(Call<ExchangeRates> call, Throwable
                             throwable) {
                     } });


    }public void fetchRatesCustom2() {
        isCustom2 = true;
        ExchangeRatesCustomAB ab = new ExchangeRatesCustomAB(Integer.parseInt(startDate.getText()), Integer.parseInt(endDate.getText()));
        ExchangeService.exchangeApi().getExchangeRatesCustom2(ab).enqueue(new
                 Callback<ExchangeRates>() {
                     @Override
                     public void onResponse(Call<ExchangeRates> call,
                                            Response<ExchangeRates> response) {
                         ExchangeRates exchangeRates = response.body();
                         Platform.runLater(() -> {
                             buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                             sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                             buyUsdRate = exchangeRates.lbpToUsd;
                             sellUsdRate = exchangeRates.usdToLbp;
                         });
                     }
                     @Override
                     public void onFailure(Call<ExchangeRates> call, Throwable
                             throwable) {
                     } });


    }

    public void addTransaction(ActionEvent actionEvent) {
        Transaction transaction = new Transaction(
                Float.parseFloat(usdTextField.getText()),
                Float.parseFloat(lbpTextField.getText()),
                ((RadioButton) transactionType.getSelectedToggle()).getText().equals("Sell USD")
        );
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addTransaction(transaction,
                authHeader).enqueue(new Callback<Object>() {

        @Override
        public void onResponse(Call<Object> call, Response<Object> response)
        {
            if (isCustom)
                fetchRatesCustom();
            else if (isCustom2)
                fetchRatesCustom2();
            else
                fetchRates();

            Platform.runLater(() -> {
                usdTextField.setText("");
                lbpTextField.setText("");
            });
//            System.out.println("Im at response");
        }
        @Override
        public void onFailure(Call<Object> call, Throwable throwable)
        {
            System.out.println("Im at FAIL");
            System.out.println(throwable.getMessage());

        } });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        isCustom = false;
        isCustom2 = false;
        fetchRates();

        /**
         * Reference
         * https://stackoverflow.com/questions/42943652/how-to-trigger-an-event-on-focus-out-for-a-textfield-in-javafx-using-fxml
         * OnFocusOut functionality
         * Once user loses focus from TextField, conversion calculation is done
         * From LBP to USD and vice versa
         */
        usdConversion.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost

                if(isNumeric(usdConversion.getText())) {
                    float usdToLbp = Float.parseFloat(sellUsdRateLabel.getText()) * Float.parseFloat(usdConversion.getText());
                    lbpConversion.setDisable(true);
                    lbpConversion.setText(String.valueOf(usdToLbp));

                }
            }
        });
        lbpConversion.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost

                if(isNumeric(lbpConversion.getText())) {
                    float lbpToUsd = Float.parseFloat(lbpConversion.getText()) / Float.parseFloat(buyUsdRateLabel.getText());
                    usdConversion.setDisable(true);
                    usdConversion.setText(String.valueOf(lbpToUsd));

                }
            }
        });
    }

}
