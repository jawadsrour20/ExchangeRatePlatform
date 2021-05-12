package exchange;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Parent implements Initializable, OnPageCompleteListener{
    public BorderPane borderPane;
    public Button transactionButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;
    public Button addUserExchangeButton;
    public Button userExchangesButton;
    public Button highestAndLowestRatesButton;
    public Button exchangesGraphButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        updateNavigation();
    }

    public void ratesSelected() {
        swapContent(Section.RATES);
    }
    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }
    public void loginSelected() {
        swapContent(Section.LOGIN);
    }
    public void registerSelected() {
        swapContent(Section.REGISTER);
    }

    public void addUserExchangeSelected() {
        swapContent(Section.ADDUSEREXCHANGE);
    }

    public void userExchangeSelected() {swapContent(Section.USEREXCHANGES);}
    public void highestAndLowestRatesSelected() {swapContent(Section.HIGHESTANDLOWEST);}
    public void exchangesGraphSelected() {swapContent(Section.EXCHANGESGRAPH);}
    public void logoutSelected() {
        Authentication.getInstance().deleteToken();
        swapContent(Section.RATES);
    }

    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
            if (section.doesComplete()) {
                ((PageCompleter)
                        loader.getController()).setOnPageCompleteListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateNavigation();
    }

//    What is visible for authenticate and not authenticated users
    private void updateNavigation() {
        boolean authenticated = Authentication.getInstance().getToken() !=
                null;
        transactionButton.setManaged(authenticated);
        transactionButton.setVisible(authenticated);
        loginButton.setManaged(!authenticated);
        loginButton.setVisible(!authenticated);
        registerButton.setManaged(!authenticated);
        registerButton.setVisible(!authenticated);
        logoutButton.setManaged(authenticated);
        logoutButton.setVisible(authenticated);
        addUserExchangeButton.setManaged(authenticated);
        addUserExchangeButton.setVisible(authenticated);
        userExchangesButton.setVisible(authenticated);
        userExchangesButton.setManaged(authenticated);


    }

    @Override
    public void onPageCompleted() {
        swapContent(Section.RATES);
    }

    private enum Section {
        RATES,
        TRANSACTIONS,
        ADDUSEREXCHANGE,
        USEREXCHANGES,
        HIGHESTANDLOWEST,
        EXCHANGESGRAPH,
        LOGIN,
        REGISTER;

        public String getResource() {
            return switch (this) {
                case RATES -> "/exchange/rates/rates.fxml";
                case TRANSACTIONS -> "/exchange/transactions/transactions.fxml";
                case LOGIN -> "/exchange/login/login.fxml";
                case REGISTER -> "/exchange/register/register.fxml";
                case ADDUSEREXCHANGE -> "/exchange/postuserexchanges/postuserexchanges.fxml";
                case USEREXCHANGES -> "/exchange/userexchanges/userexchanges.fxml";
                case HIGHESTANDLOWEST -> "/exchange/highestandlowestrates/highestandlowestrates.fxml";
                case EXCHANGESGRAPH -> "/exchange/exchangesgraph/exchangesgraph.fxml";
                default -> null;
            }; }
        public boolean doesComplete() {
            return switch (this) {
                case LOGIN, REGISTER -> true;
                default -> false;
            };
        }
    }
}