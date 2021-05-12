package exchange.register;

import exchange.Authentication;
import exchange.OnPageCompleteListener;
import exchange.PageCompleter;
import exchange.api.ExchangeService;
import exchange.api.model.Token;
import exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register implements PageCompleter {

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }

    public OnPageCompleteListener getOnPageCompleteListener() {
        return onPageCompleteListener;
    }

    public TextField usernameTextField;
    public TextField passwordTextField;
    private OnPageCompleteListener onPageCompleteListener;

    public void register(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(), passwordTextField.getText());

        ExchangeService.exchangeApi().addUser(user).enqueue(new
                Callback<User>() {


                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        ExchangeService.exchangeApi().authenticate(user).enqueue(new
                                Callback<Token>(){


                                    @Override
                                    public void onResponse(Call<Token> call, Response<Token> response) {
                                        Authentication.getInstance().saveToken(response.body().getToken());
                                        Platform.runLater(() -> {
                                            onPageCompleteListener.onPageCompleted();
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<Token> call, Throwable t) {

                                    }

                                });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }

    @Override
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
}