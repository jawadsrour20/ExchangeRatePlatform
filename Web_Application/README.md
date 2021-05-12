### Requirements
### `Node JS`

Make sure that you have Node JS installed in your machine from https://nodejs.org/en/download/

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.


### Exchange Application Functionality
This platform allows you to:
1. Check today's exchange rate
2. Create a user and login. This will help track your transactions and allow you to convert and send money to another user.
3. Calculate the USD-lBP conversion. You can input a USD amount and see what it corresponds to in LBP and vice versa.
4. Post a transaction. This will be stored in the database and be used to calculate the exchange rate
5. Check statistics regarding the exchange rate from last week. These include: highest and lowest usd buy and sell rates, and the percentage increase/decrease in the rate.
6. Check your list of transactions.
7. Convert from lbp to usd and vice versa and send to a valid user.
8. Check the list of user interactions that you've sent.

### Functions:
1. change() (input: changeInput, input: changeType, output: changeOutput): Converts from LBP to USD and vice versa.
2. fetchRates() (input: None, output: SellUsdRate, output: BuyUsdRate): Fetches from the server URL today's buy and sell exchange rates.
3. fetchHighestAndLowestLbpUsd() (input: None, output: HighestUsd, output: HighestLbp, output: LowestUsd, output: LowestUsd): fetches from the server URL the highest and lowest the rate has been over the last week.
4. fetchFluctuation() (input: None, output: Fluctuation): Fetches from the server URL the percentage increase/decrease of the rate since last week.
5. addItem() (input: usdInput, input:lbpInput, input: transactionType, optional-input: userToken, output: None): adds a new transaction to the database by sending a POST request to the server URL.
6. convertAndSend() (input: convertAndSendChangeAmount, input: convertAndSendReceivingUsername, input: convertAndSendType, input: sellUsdRate, input: buyUsdRate): Adds a new user to user transaction by sending a POST request to the server URL.
7. login() (input: username, input: password, output: userToken): Logs in a user if they are authorized to login.
8. createUser() (input: username, input: password): Adds a new user to the database
9. fetchUserTransactions() (input: userToken, output-list: UserTransactions): fetches from the database all the transactions (not user to user) that the current user has posted.
10. fetchUserSendTransactions() (input: userToken, output-list: UserSendTransactions): fetches all user to user interaction sent by the logged in user 



