import { useState, useEffect, useCallback } from "react";
import './App.css';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import UserCredentialsDialog from './UserCredentialsDialog/UserCredentialsDialog.js'
import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { getUserToken, saveUserToken, removeUserToken } from "./localStorage";
import { DataGrid } from '@material-ui/data-grid';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import LineChart from "./Components/linechart.js"

var SERVER_URL = "http://127.0.0.1:5000"  // url of the server that will host our requests

//Styling needed for line chart
const useStyles = makeStyles((theme) => ({
    root: {
        '& > *': {
            margin: theme.spacing(1),
            width: '25ch',
        },
    },
}));


const States = {
    PENDING: "PENDING",
    USER_CREATION: "USER_CREATION",
    USER_LOG_IN: "USER_LOG_IN",
    USER_AUTHENTICATED: "USER_AUTHENTICATED",
};

const columns = [
    { field: 'id', headerName: 'Transaction ID', width: 150 },
    { field: 'usd_amount', headerName: 'USD Amount', width: 150 },
    { field: 'lbp_amount', headerName: 'LBP Amount', width: 150 },
    { field: 'usd_to_lbp', headerName: 'USD to LBP', width: 150 },
    { field: 'added_date', headerName: 'Added Date', width: 160 },
];

const columns2 = [
    { field: 'id', headerName: 'Transaction ID', width: 150 },
    { field: 'lbp_amount', headerName: 'LBP Amount', width: 150 },
    { field: 'usd_amount', headerName: 'USD Amount', width: 150 },
    { field: 'usd_to_lbp', headerName: 'USD to LBP', width: 150 },
    { field: 'user_received_username', headerName: 'User Received', width: 150 },
]

function App() {

    let [buyUsdRate, setBuyUsdRate] = useState(null);
    let [sellUsdRate, setSellUsdRate] = useState(null);
    let [lbpInput, setLbpInput] = useState("");
    let [usdInput, setUsdInput] = useState("");
    let [transactionType, setTransactionType] = useState("usd-to-lbp");
    let [authState, setAuthState] = useState(States.PENDING);
    let [userToken, setUserToken] = useState(getUserToken())
    let [changeInput, setChangeInput] = useState("");
    let [changeOutput, setChangeOutput] = useState("");
    let [changeType, setChangeType] = useState("");
    let [userTransactions, setUserTransactions] = useState([]);
    let [userSendTransactions, setUserSendTransactions] = useState([]);
    let [highestUsd, setHighestUsd] = useState(0);
    let [lowestUsd, setLowestUsd] = useState(0);
    let [highestLbp, setHighestLbp] = useState(0);
    let [lowestLbp, setLowestLbp] = useState(0);
    let [fluctuation, setFluctuation] = useState(0);
    let [convertAndSendChangeAmount, setConvertAndSendChangeAmount] = useState("");
    let [convertAndSendType, setConvertAndSendType] = useState("usd-to-lbp");
    let [convertAndSendReceivingUsername, setConvertAndSendReceivingUsername] = useState("");
    let [errorMessage, setErrorMessage] = useState("");

    const classes = useStyles();

    /////////////////////////////////////////////////////////////////////////////
    //////////////////////// FUNCTIONS START HERE ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    // function that fetches the exchange rates 
    function fetchRates() {
        fetch(`${SERVER_URL}/exchangeRate`)
            .then(response => response.json())
            .then(data => {
                setSellUsdRate(data.usd_to_lbp.toFixed(2));
                setBuyUsdRate(data.lbp_to_usd.toFixed(2));
            });
    }

    useEffect(() => { fetchRates() }, []);

    //
    // Converts usd to lbp and vice versa
    function change() {
        let changeOut = 0;
        if (changeType == 'usd-to-lbp') {
            changeOut = changeInput * sellUsdRate;
        }
        else {
            changeOut = changeInput * buyUsdRate;
        }
        if (changeOut !== 0) {
            setChangeOutput(changeOut);
        }

    }
    // NEW
    //fetches highest and lowest the usd and lbp rate has been over the past week
    function fetchHighestLowestLbpUsd() {
        fetch(`${SERVER_URL}/highestAndLowestRates`)
            .then(function (response) {
                return response.json();
            }).then(function (obj) {
                setHighestUsd(obj.maxBuyUSD);
                setHighestLbp(obj.maxSellUSD);
                setLowestUsd(obj.minBuyUSD);
                setLowestLbp(obj.minSellUSD);
            })
    }
    useEffect(() => { fetchHighestLowestLbpUsd() }, []);


    //NEW
    //fetchFluctuations fetches the increase/decrease in the rate since the week before

    function fetchFluctuation() {
        fetch(`${SERVER_URL}/fluctuationWeek`)
            .then(function (response) {
                return response.json();
            }).then(function (obj) {

                setFluctuation(obj.sellFluctuation.toFixed(2))

            })
    }
    useEffect(() => { fetchFluctuation() }, []);

    //
    // new Add Item to server
    function addItem() {
        if (usdInput == "" || lbpInput == "") {
            alert("Both inputs should be full to proceed!");
        }
        else if (usdInput != "" && lbpInput != "") {
            let usdTOLBP = false;
            if (transactionType == "usd-to-lbp") usdTOLBP = true;
            if (userToken !== null) {
                var tt = "Token: " + userToken
                fetch(`${SERVER_URL}/transaction`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': tt
                    },
                    body: JSON.stringify({
                        "usd_amount": usdInput,
                        "lbp_amount": lbpInput,
                        "usd_to_lbp": usdTOLBP
                    })
                }).then(res => console.log(res)).then(() => { fetchRates() });

                setUsdInput('');
                setLbpInput('');

            }


            else {
                fetch(`${SERVER_URL}/transaction`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        "usd_amount": usdInput,
                        "lbp_amount": lbpInput,
                        "usd_to_lbp": usdTOLBP
                    })
                }).then(res => console.log(res)).then(() => { fetchRates() });

                setUsdInput('');
                setLbpInput('');

            }
        }
    }


    //NEW
    //Adds a new user to user transaction

    function convertAndSend() {
        if (convertAndSendChangeAmount == "" || convertAndSendReceivingUsername == "") {
            alert("Please provide input to proceed!");
        }
        else if (convertAndSendChangeAmount != "" && convertAndSendReceivingUsername != "") {
            let usdTOLBP = false;
            if (convertAndSendType == "usd-to-lbp") {
                let usdTOLbp = true;
                var usd = convertAndSendChangeAmount;
                var lbp = convertAndSendChangeAmount * sellUsdRate;
            }
            else {
                var lbp = convertAndSendChangeAmount;
                var usd = convertAndSendChangeAmount * buyUsdRate;
            }
            
            if (userToken !== null) {
                var tt = "Token: " + userToken
                fetch(`${SERVER_URL}/addUserExchange`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': tt
                    },
                    body: JSON.stringify({
                        "usd_amount": usd,
                        "lbp_amount": lbp,
                        "usd_to_lbp": usdTOLBP,
                        "user_received_username": convertAndSendReceivingUsername
                    })
                }).then(response => response.json())
                .then(() => { fetchRates() })
                .catch((error)=> {setErrorMessage("Please make sure you enter a valid user")});

                setUsdInput('');
                setLbpInput('');

            }
            

        }
    }



    //
    //logs in user
    function login(username, password) {
        return fetch(`${SERVER_URL}/authentication`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                user_name: username,
                password: password
            }),
        })
            .then((response) => response.json())
            .then((body) => {
                setAuthState(States.USER_AUTHENTICATED);
                setUserToken(body.token);
                saveUserToken(body.token);
            });
    }
    //
    //creates a new user
    function createUser(username, password) {
        return fetch(`${SERVER_URL}/user`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                user_name: username,
                password: password
            }),
        }).then((response) => login(username, password));
    }
    //
    //logs out the user
    function logout() {
        setUserToken(null);
        removeUserToken();
    }

    //fetches all user transactions
    const fetchUserTransactions = useCallback(() => {
        fetch(`${SERVER_URL}/transaction`, {
            headers: {
                Authorization: `bearer ${userToken}`,
            },
        })
            .then((response) => response.json())
            .then((transactions) => setUserTransactions(transactions));
    }, [userToken]);


    useEffect(() => {
        if (userToken) {
            fetchUserTransactions();
        }
    }, [fetchUserTransactions, userToken]);

    //NEW
    //fetches all user-to-user interactions

    const fetchUserSendTransactions = useCallback(() => {
        fetch(`${SERVER_URL}/getUserExchanges`, {
            headers: {
                Authorization: `bearer ${userToken}`,
            },
        })
            .then((response) => response.json())
            .then((transactions) => setUserSendTransactions(transactions));
    }, [userToken]);
    useEffect(() => {
        if (userToken) {
            fetchUserSendTransactions();
        }
    }, [fetchUserSendTransactions, userToken]);



    return (
        <div>
            <UserCredentialsDialog
                open={authState == States.USER_CREATION ? true : false}
                onSubmit={(username, password) => createUser(username, password)}
                onClose={() => setAuthState(States.PENDING)}
                title="Registration"
                submitText="Register"
            />

            <UserCredentialsDialog
                open={authState == States.USER_LOG_IN ? true : false}
                onSubmit={(username, password) => login(username, password)}
                onClose={() => setAuthState(States.PENDING)}
                title="Login"
                submitText="Login"
            />

            <Snackbar
                elevation={6}
                variant="filled"
                open={authState === States.USER_AUTHENTICATED}
                autoHideDuration={2000}
                onClose={() => setAuthState(States.PENDING)}
            >
                <Alert severity="success">Success</Alert>
            </Snackbar>

            <div>
                <AppBar position="static">
                    <Toolbar classes={{ root: "nav" }}>
                        <Typography variant="h5">LBP - USD Converter </Typography>
                        <div>
                            {userToken !== null ? (
                                <Button color="inherit" onClick={logout}> Logout </Button>
                            ) : (
                                <div>
                                    <Button color="inherit" onClick={() => setAuthState(States.USER_CREATION)}> Register </Button>
                                    <Button color="inherit" onClick={() => setAuthState(States.USER_LOG_IN)}> Login </Button>
                                </div>
                            )
                            }
                        </div>
                    </Toolbar>
                </AppBar>
            </div>
            <div className="wrapper">
                <Typography variant="h5">Today's Exchange Rate</Typography>
                <p>LBP to USD Exchange Rate</p>
                <h3>Buy USD: <span id="buy-usd-rate">{buyUsdRate == null ? "Not Available yet" : buyUsdRate}</span></h3>
                <h3>Sell USD: <span id="sell-usd-rate">{sellUsdRate == null ? "Not Available yet" : sellUsdRate}</span></h3>
            </div>
            <div className="wrapper">
                <Typography variant="h5"> Convert </Typography>
                <p> This section allows you to convert from currency to another </p>
                <form className={classes.root} noValidate autoComplete="off">
                    <div className="amount-input">

                        <TextField id="change-amount" label="Please Enter Input" value={changeInput} onChange={e => setChangeInput(e.target.value)} />

                        <div>
                            <br></br>
                            <select id="change-transaction-type" value={changeType} onChange={e => setChangeType(e.target.value)}>
                                <option value="usd-to-lbp">USD to LBP</option>
                                <option value="lbp-to-usd">LBP to USD</option>
                            </select>
                        </div>
                        <button id="add-Button" className="button" type="button" onClick={change}> Convert </button>
                    </div>
                </form>
                <h2> Your input corresponds to <span id="changeOutput">{changeOutput}</span></h2>

            </div>
            <div className="wrapper">
                <Typography variant="h5">Record a recent transaction</Typography>
                <p> This section allows you to record a recent transaction that you made </p>
                <form className={classes.root} noValidate autoComplete="off">
                    <div className="amount-input">
                        <TextField id="change-amount" label="Please Enter USD value" value={usdInput} onChange={e => setUsdInput(e.target.value)} />
                        <br /><br />
                        <TextField id="change-amount" label="Please Enter LBP value" value={lbpInput} onChange={e => setLbpInput(e.target.value)} />

                        <br /><br />
                        <select id="transaction-type" value={transactionType} onChange={e => setTransactionType(e.target.value)}>
                            <option value="usd-to-lbp">USD to LBP</option>
                            <option value="lbp-to-usd">LBP to USD</option>
                        </select>

                        <button id="add-button" className="button" type="button" onClick={addItem}>Add</button>
                    </div>
                </form>
            </div>
            <div className="wrapper">
                <h2> Latest Trends </h2>
                <h3>Highest USD Buy Rate Over Last Week: <span id="highest-usd">{highestUsd}</span></h3>
                <h3>Lowest USD Buy Rate Over Last Week: <span id="lowest-usd">{lowestUsd}</span></h3>
                <h3>Highest USD Sell Rate Over Last Week: <span id="highest-lbp">{highestLbp}</span></h3>
                <h3>Lowest USD Sell Rate Over Last Week: <span id="lowest-lbp">{lowestLbp}</span></h3>
                <h3>Increase/Decrease Since The Week Before: <span id="fluctuation">{fluctuation}</span>%</h3>
            </div>
            <div className="wrapper">
                <div>
                    <LineChart />
                </div>
            </div>
            {userToken && (
                <div className="wrapper">
                    <Typography variant="h5">Your Transactions</Typography>
                    <br/>
                    <DataGrid
                        columns={columns}
                        rows={userTransactions}
                        autoHeight
                    />
                </div>
            )}
            
    {userToken && (
      <div className="wrapper">

        <Typography variant="h5">Convert and Send Money:</Typography>
        <form className={classes.root} noValidate autoComplete="off">
          <div className="amount-input">

            <TextField id="convert-and-send-change-amount" label="Please enter the amount..." value={convertAndSendChangeAmount} onChange={e => setConvertAndSendChangeAmount(e.target.value)} />
            <TextField id="convert-user-receiving-username" label="Please enter the username of the receiver" value={convertAndSendReceivingUsername} onChange={e => setConvertAndSendReceivingUsername(e.target.value)} />
            <div>
              <br></br>
              
              <select id="convert-and-send-type">
                
                <option value="usd-to-lbp">USD to LBP</option>
                <option value="lbp-to-usd">LBP to USD</option>
              </select>
            </div>
            
            <button id="add-Button" className="button" type="button" onClick={convertAndSend}> Convert and Send </button>
            <br/> <span>{errorMessage}</span>
          </div>
        </form>
      </div>
    )}
    
    {userToken && (
      <div className="wrapper">

        <Typography variant="h5">Your User Transactions</Typography>
        <br/>
        <DataGrid
          columns={columns2}
          rows={userSendTransactions}
          autoHeight
        />
      </div>
    )}
  </div>

        
    );
}

export default App;
