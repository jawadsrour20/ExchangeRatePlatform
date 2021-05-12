package exchange.postuserexchanges;



import exchange.Authentication;
import exchange.api.ExchangeService;
import exchange.api.model.ExchangeRates;
import exchange.api.model.Transaction;
import exchange.api.model.UserExchange;
import exchange.rates.Rates;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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



public class PostUserExchanges implements Initializable {


    public TextField lbpTextField;
    public TextField usdTextField;
    public TextField receiverName;
    public ToggleGroup transactionType;
    public RadioButton buyUSDButton;
    public RadioButton sellUSDButton;

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

        usdTextField.setText("");
        lbpTextField.setText("");
        lbpTextField.setDisable(true);
        usdTextField.setDisable(true);

    }

    public void addUserExchange(ActionEvent actionEvent) {
        UserExchange userExchange = new UserExchange(
                Float.parseFloat(usdTextField.getText()),
                Float.parseFloat(lbpTextField.getText()),
                ((RadioButton) transactionType.getSelectedToggle()).getText().equals("Sell USD"),
                receiverName.getText()
        );
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addUserExchange(userExchange,
                authHeader).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Platform.runLater(() -> {
                    usdTextField.setText("");
                    lbpTextField.setText("");
                    receiverName.setText("");
                    lbpTextField.setDisable(true);
                    usdTextField.setDisable(true);
                });
                //            System.out.println("Im at response");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                System.out.println("Im at FAIL");
                System.out.println(throwable.getMessage());

            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lbpTextField.setDisable(true);
        usdTextField.setDisable(true);



/**
 * Reference
 * https://stackoverflow.com/questions/42943652/how-to-trigger-an-event-on-focus-out-for-a-textfield-in-javafx-using-fxml
 * OnFocusOut functionality
 * Once user loses focus from TextField, conversion calculation is done
 * From LBP to USD and vice versa
 */


usdTextField.focusedProperty().addListener((ov, oldV, newV) -> {
if (!newV) { // focus lost


if(isNumeric(usdTextField.getText())) {
float usdToLbp = Rates.getSellUsdRate() * Float.parseFloat(usdTextField.getText());
lbpTextField.setText(String.valueOf(usdToLbp));

}
}
});

lbpTextField.focusedProperty().addListener((ov, oldV, newV) -> {
if (!newV) { // focus lost

if(isNumeric(lbpTextField.getText())) {
float lbpToUsd = Float.parseFloat(lbpTextField.getText()) / Rates.getBuyUsdRate();
usdTextField.setText(String.valueOf(lbpToUsd));
}
}
});

        buyUSDButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    lbpTextField.setDisable(false);
                    usdTextField.setDisable(true);
                } else {
                    lbpTextField.setDisable(true);
                    usdTextField.setDisable(true);
                }
            }
        });

        sellUSDButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    lbpTextField.setDisable(true);
                    usdTextField.setDisable(false);
                } else {
                    lbpTextField.setDisable(true);
                    usdTextField.setDisable(true);
                }
            }
        });

}

    }

