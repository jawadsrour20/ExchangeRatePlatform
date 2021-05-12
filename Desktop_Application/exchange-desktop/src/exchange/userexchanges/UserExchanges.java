package exchange.userexchanges;

import exchange.Authentication;
import exchange.api.ExchangeService;
import exchange.api.model.UserExchange;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserExchanges implements Initializable {


    public TableColumn lbpAmount;
    public TableColumn usdAmount;
    public TableColumn receiverName;
    public TableColumn transactionDate;
    public TableView tableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        receiverName.setCellValueFactory(new
                PropertyValueFactory<UserExchange, String>("receivedUsername"));
        lbpAmount.setCellValueFactory(new
                PropertyValueFactory<UserExchange, Long>("lbpAmount"));
        usdAmount.setCellValueFactory(new
                PropertyValueFactory<UserExchange, Long>("usdAmount"));
        transactionDate.setCellValueFactory(new
                PropertyValueFactory<UserExchange, String>("transactionDate"));
        ExchangeService.exchangeApi().getUserExchanges("Bearer " +
                Authentication.getInstance().getToken())
                .enqueue(new Callback<List<UserExchange>>() {
                    @Override
                    public void onResponse(Call<List<UserExchange>> call,
                                           Response<List<UserExchange>> response) {
                        tableView.getItems().setAll(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<UserExchange>> call,
                                          Throwable throwable) {
                    }

                });

    }
}