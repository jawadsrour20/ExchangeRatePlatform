package exchange.login;

import exchange.Authentication;
import exchange.OnPageCompleteListener;
import exchange.PageCompleter;
import exchange.api.ExchangeService;
import exchange.api.model.Token;
import exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import retrofit2.Callback;
import retrofit2.Response;


public class Login implements PageCompleter {

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


    public void login(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordTextField.getText());
        ExchangeService.exchangeApi().authenticate(user).enqueue(new Callback<Token>()
            {
                @Override
                public void onResponse(retrofit2.Call<Token> call, Response<Token> response)
                {
                        Authentication.getInstance().saveToken(response.body().getToken());
                    Platform.runLater(() -> {
                        onPageCompleteListener.onPageCompleted();
                    });
                 }
            @Override
            public void onFailure(retrofit2.Call<Token> call, Throwable throwable) {
            }
        }); }

    @Override
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
}