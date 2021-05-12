# EECE430L Project
# Desktop JavaFX based Application


###### Setting up and Running the Application ################################################################################# 


## Download IntelliJ IDEA 

1) Download from https://www.jetbrains.com/idea/download/

2) Download the community version which is free, or the premium version if you are currently a student as you get a free license for 1 year.


## Download the JavaFX SDK﻿ and JDK15

3) Download JavaFX SDK from https://gluonhq.com/products/javafx/  and JDK15 from https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html

## Make sure the JavaFX plugin is enabled﻿


4) In the Settings/Preferences dialog use (CMD + ,) on MAC or (CTRL + ,) on windows and Linux. 

5) Select Plugins.

6) Switch to the Installed tab and make sure that the JavaFX plugin is enabled.
	If the plugin is disabled, select the checkbox next to it.
	Apply the changes and close the dialog. Restart the IDE if prompted.


## Run the Application

7) From the main menu, select Run | Run 'Main' 


# You can always refer to https://www.jetbrains.com/help/idea/javafx.html#run if you needede any help with JavaFX.


###### Functionality the Software Delivers ######################################################################################### 


1) User can sign-up, login, logout, or use the platform as a guest user.

2) Check The Exchange Rate for Buying USD and Selling USD based on the transactions of last 3 days

3) Option to customize Exchange Rate Time Span. User Can get the exchange rate to be based on the transacitons of the last X days or based on a start date and end date specified by the user (in days).

4) Adding transactions USD -> LBP or LBP -> USD. May be done by a platform user or a guest user.

5) Converting a given amount of money from USD to LBP or LBP to USD. Calculation is based on the exchange rate.

6) Viewing user transactions in a table. 

7) Performing money exchange between two Users. (User specifies receiver name, type of exchange, and amount of money to exchange).

8) Viewing previous user exchanges in a table.

9) Viewing the highest and lowest exchanges rates for both buying and selling USD, as well as a graphical representation of this data.
 
10) Viewing the  increase/decrease in fluctuation percentages for buying and selling USD.

11) Viewing a graph that shows the fluctuation of the exchange rate over time (over the last 8 weeks).




###### General Architecture (Functions) of the Software #############################################################################


## Package exchange.exchangesgraph

1) fetchFluctuation() -> performs a @GET request and gets the sell and buy fluctuation percentages. A positive percentage gets presented in green to the user indicating an increase, a negative percentage gets presented in red to the user indicating a decrease, and a 0 percentage gets presented in yellow indicating being constant.

2) fetch()  -> performs @GET requests retrieving values for buying and selling USD over a span of 8 weeks.

3) renderGraph() -> asssociated with the buton of text-value "render" which populates a line chart with the buy and sell USD values over the past 8 weeks.

## Pacakge exchange.highestandlowestrates

1) fetchRates() -> performs a @GET request fetching maximum buying, maximum selling, minimum buying, and minimum selling USD rates and plots them on a line chart.


## Package exchange.register

1) register(ActionEvent actionEvent) -> allows for user registration

2) setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) -> allows for re-directing the user to the Exchange Rates section upon successful registration.



## Package exchange.login

1) login(ActionEvent actionevent) -> allows for user log-in to the application

2) setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) -> allows for re-directing the user to the Exchange Rates Section upon successful log-in.

## Package exchange.postuserexchanges

1) isNumeric() -> private (abstracted from the user), serves the purpose of validating that an input value for the conversion functionality (USD->LBP or LBP->USD) is taking numeric values to do its job correct accordingly.

2) clearForm() -> clears the user exchange form upon  submission of a an exchange by the user.

3) addUserExchange(ActionEvent actionEvent) -> allows for adding user exchanges based on user inputs for an input USD, input LBP, selected transactiont type, and a receiver username.

4) initialize() -> overriden method from Initializable interface. Gets called upon accessing the section, thus initializing it and supplying the user with the relevant UI data for the section. It also disables input boxes as only when the user chooses a transaction type an input box based on that gets enabled to take user input. In addition, conversion get calculated upon losing focus from an input box which is thanks to using an eventlistener internally. 

## Package exchange.rates

1) isNumeric() -> private (abstracted from the user), serves the purpose of validating that an input value for the conversion functionality (USD->LBP or LBP->USD) is taking numeric values to do its job correct accordingly.

2) clearForm() -> clears the transactio form upon  submission of a a transction by the user.

3) fetchRates() -> fetches Exchange Rates and displays them to the user.

4) fetchRatesCustom() -> fetches Exchange Rates  based on a  time span specified by the user where user inputs a value which is the number of days ago (exchange rate is calculated based on transactions from X days ago) and then displays it.

5) fetchRatesCustom2() -> fetches Exchange Rates based on a time span specified by the user where user gives a start date and an end date. Exchange rate is then calculated based on that time duration range. Then it is displayed to the user.

6) addTransaction(ActionEvent actionEvent) -> Allows a user to add a transaction to the database. User should submit an LBP input, USD input, and a transaction type.

7) initialize() -> overriden method from the Initializable interface. Gets called upon accessing the section, thus initializing it and supplying the user with the relevant UI data for the section.

## Package exchange.transactions

1) initialize() -> upon accessing the "Transactions" section, it presents the user with a table populated with his/her previous transactions.

## Package exchange.userexchanges

1) initialize() -> upon accessing the "Exchange Records" section, it presents the user with a table populated with his/her previous user exchanges.

## Package exchange -> Parent.java

1) initialize() -> calls updateNavigation()

2) updateNavigation() -> sets which sections are visible and which are not based on whether the user is authenticated or not.

3) onPageCompleted() -> allows for going to Exchange Rates section upon page completion. serves login and registration re-direction to Exchange Rates section.

4) ratesSelected() -> allows going to the Exchange Rates section upon click of a button (button has an onAction="#ratesSelected" allowing for the functionality). Similarly for the others "...Selected()" functions.

## Package exchange -> Authentication.java

1) getToken() -> gets user token

2) saveToken() -> saves user token

3) deleteToken() -> deletes a token

###### END ############################################################################################################################








