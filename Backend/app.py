from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask import request
from flask import jsonify
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
from flask import abort
## from flask_apscheduler import APScheduler
import datetime
import jwt



app = Flask(__name__)    # created a flask app
ma = Marshmallow(app)   # used for data serialization
bcrypt = Bcrypt(app)  # encryption algo used to create hashed passwords for users
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:<Password>@localhost:3306/exchange'
CORS(app)
db = SQLAlchemy(app)


######################## NEW ###################################################################################
"""
scheduler = APScheduler()

if __name__ == '__main__':
    scheduler.add_job(id = 'hourlyExchangeRate' , func= exchangeRate, trigger= 'interval', seconds = 3600)
    scheduler.start()

"""
######################## NEW ###################################################################################

class Transaction(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)
    added_date = db.Column(db.DateTime)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=True)

    def __init__(self, usd_amount, lbp_amount, usd_to_lbp, user_id):
        super(Transaction, self).__init__(usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp,
                                          user_id=user_id,
                                          added_date=datetime.datetime.now())


class TransactionSchema(ma.Schema):
    class Meta:
        fields = ("id", "usd_amount", "lbp_amount", "usd_to_lbp", "user_id", "added_date")
        model = Transaction

transaction_schema = TransactionSchema()
transactions_schema = TransactionSchema(many=True)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_name = db.Column(db.String(30), unique=True)
    hashed_password = db.Column(db.String(128))
    ## constructor of Class User
    def __init__(self, user_name, password):
        super(User, self).__init__(user_name=user_name)
        self.hashed_password = bcrypt.generate_password_hash(password)


class UserSchema(ma.Schema):
    class Meta:
        fields = ("id", "user_name")
        model = User

user_schema = UserSchema()

########## NEW #######################################################################################################
## New table for saving exchange rates based on value and date
class Rates(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    usd_to_lbp_amount = db.Column(db.Float)
    lbp_to_usd_amount = db.Column(db.Float)
    added_date = db.Column(db.DateTime)
    ## Constructor of class Rates
    def __init__(self, usd_to_lbp_amount, lbp_to_usd_amount):
        super(Rates, self).__init__(usd_to_lbp_amount= usd_to_lbp_amount, 
                                    lbp_to_usd_amount= lbp_to_usd_amount, 
                                    added_date= datetime.datetime.now())


class RatesSchema(ma.Schema):
    class Meta:
        fields = ("id", "usd_to_lbp_amount", "lbp_to_usd_amount", "added_date")
        model = Rates

rates_schema = RatesSchema(many=True)


# Class that records exchanges between users
class UserExchanges(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    user_initiated_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    user_received_username = db.Column(db.String(30))
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    date_of_transaction = db.Column(db.DateTime)
    usd_to_lbp = db.Column(db.Boolean)
    ## constructor of the class:
    def __init__(self, user_initiated_id, user_received_username, usd_amount, lbp_amount, usd_to_lbp):
        super(UserExchanges, self).__init__(user_initiated_id= user_initiated_id,
                                            user_received_username= user_received_username,
                                            usd_amount= usd_amount,
                                            lbp_amount= lbp_amount,
                                            date_of_transaction= datetime.datetime.now(),
                                            usd_to_lbp= usd_to_lbp)


class UserExchangesSchema(ma.Schema):
    class Meta:
        fields = ("id", "user_initiated_id", "user_received_username", "usd_amount", "lbp_amount", "date_of_transaction", "usd_to_lbp")
        model = UserExchanges

user_exchange_schema = UserExchangesSchema()
user_exchanges_schema = UserExchangesSchema(many= True)

########### NEW #######################################################################################################

## API for transaction creation
@app.route('/transaction', methods=['POST'])
def transaction():
    usd_amount = request.json["usd_amount"]
    lbp_amount = request.json["lbp_amount"]
    usd_to_lbp = request.json["usd_to_lbp"]
    tken = extract_auth_token(request)
    if tken is None:
        newTransaction = Transaction(usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp, user_id= None)
    else:
        try:
            userId = decode_token(tken)
            newTransaction = Transaction(usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp, user_id=userId)
        except:
            abort(403)
    db.session.add(newTransaction)
    db.session.commit()
    return jsonify(transaction_schema.dump(newTransaction))


## API for transaction fetching
@app.route('/transaction', methods=['GET'])
def transactionGet():
    tken = extract_auth_token(request)
    if tken is None:
        abort(403)
    else:
        try:
            userId = decode_token(tken)
            transactionsOfUser = Transaction.query.filter_by(user_id = userId).all()
        except:
            abort(403)
    return jsonify(transactions_schema.dump(transactionsOfUser))

## API for user
@app.route('/user', methods=['POST'])
def user():
    user_name = request.json["user_name"]
    password = request.json["password"]
    newUser = User(user_name=user_name, password=password)
    db.session.add(newUser)
    db.session.commit()
    return jsonify(user_schema.dump(newUser))



SECRET_KEY = "b'|\xe7\xbfU3`\xc4\xec\xa7\xa9zf:}\xb5\xc7\xb9\x139^3@Dv'"
## API for user authentication
@app.route('/authentication', methods=['POST'])
def userAuthentication():
    user_name = request.json["user_name"]
    password = request.json["password"]
    ## Check if both username and passwords are passed
    if user_name=='' or password=='':
        abort(400)
    ## Check if username exists in database or not
    username = User.query.filter_by(user_name = user_name).first()
    if username is None:
        abort(403)
    ## check if password and hashed password are the same
    if bcrypt.check_password_hash(username.hashed_password, password)==False:
        abort(403)
    token = create_token(username.id)
    return jsonify(
        token = token
    )



## API to get the exchange rate, done by calculating the average from the SQL database
@app.route('/exchangeRate', methods=['GET'])
def exchangeRate():
    usd_to_lbp_Transactions = Transaction.query.filter(Transaction.usd_to_lbp== 1)\
        .filter(Transaction.added_date <= datetime.datetime.now())\
        .filter(Transaction.added_date >= datetime.datetime.now() - datetime.timedelta(3) ).all()

    lbp_to_usd_Transactions = Transaction.query.filter(Transaction.usd_to_lbp== 0)\
                                                .filter(Transaction.added_date.between(
                                                              datetime.datetime.now() - datetime.timedelta(days = 3),
                                                              datetime.datetime.now()
                                                        )).all()

    # get average of transactions from USD to LBP
    average_USD_to_LBP = 0
    for i in range(len(usd_to_lbp_Transactions)):
        ratio = usd_to_lbp_Transactions[i].lbp_amount / usd_to_lbp_Transactions[i].usd_amount
        average_USD_to_LBP = average_USD_to_LBP + ratio

    if len(usd_to_lbp_Transactions)!=0:
        average_USD_to_LBP = average_USD_to_LBP/len(usd_to_lbp_Transactions)

    # get average of transactions from LBP to USD
    average_LBP_to_USD = 0
    for i in range(len(lbp_to_usd_Transactions)):
        ratio = lbp_to_usd_Transactions[i].lbp_amount / lbp_to_usd_Transactions[i].usd_amount
        average_LBP_to_USD = average_LBP_to_USD + ratio

    if len(lbp_to_usd_Transactions)!=0:
        average_LBP_to_USD = average_LBP_to_USD / len(lbp_to_usd_Transactions)

    newRate = Rates(usd_to_lbp_amount= average_USD_to_LBP, lbp_to_usd_amount= average_LBP_to_USD)
    db.session.add(newRate)
    db.session.commit()

    return jsonify(
        usd_to_lbp = average_USD_to_LBP,
        lbp_to_usd = average_LBP_to_USD
    )


def create_token(user_id):
    payload = {
        'exp': datetime.datetime.utcnow() + datetime.timedelta(days=4),
        'iat': datetime.datetime.utcnow(),
        'sub': user_id
    }
    return jwt.encode(
     payload,
     SECRET_KEY,
     algorithm='HS256'
    )

def extract_auth_token(authenticated_request):
     auth_header = authenticated_request.headers.get('Authorization')
     if auth_header:
        return auth_header.split(" ")[1]
     else:
        return None


def decode_token(token):
     payload = jwt.decode(token, SECRET_KEY, 'HS256')
     return payload['sub']


############ NEW ###################################################################################################
## Gets exchange based on the last 't' days  
## takes a json from the request that tells it what 't' is in days
@app.route('/exchangeRateCustom', methods=['POST'])
def exchangeRateCustom():
    timeNeeded = request.json["time_requested"]
    timeNeeded = int(timeNeeded)
    usd_to_lbp_Transactions = Transaction.query.filter(Transaction.usd_to_lbp== 1)\
        .filter(Transaction.added_date <= datetime.datetime.now())\
        .filter(Transaction.added_date >= datetime.datetime.now() - datetime.timedelta(days = timeNeeded) ).all()

    lbp_to_usd_Transactions = Transaction.query.filter(Transaction.usd_to_lbp== 0)\
        .filter(Transaction.added_date <= datetime.datetime.now())\
        .filter(Transaction.added_date >= datetime.datetime.now() - datetime.timedelta(days= timeNeeded) ).all()

    # get average of transactions from USD to LBP
    average_USD_to_LBP = 0
    for i in range(len(usd_to_lbp_Transactions)):
        ratio = usd_to_lbp_Transactions[i].lbp_amount / usd_to_lbp_Transactions[i].usd_amount
        average_USD_to_LBP = average_USD_to_LBP + ratio

    if len(usd_to_lbp_Transactions)!=0:
        average_USD_to_LBP = average_USD_to_LBP/len(usd_to_lbp_Transactions)

    # get average of transactions from LBP to USD
    average_LBP_to_USD = 0
    for i in range(len(lbp_to_usd_Transactions)):
        ratio = lbp_to_usd_Transactions[i].lbp_amount / lbp_to_usd_Transactions[i].usd_amount
        average_LBP_to_USD = average_LBP_to_USD + ratio

    if len(lbp_to_usd_Transactions)!=0:
        average_LBP_to_USD = average_LBP_to_USD / len(lbp_to_usd_Transactions)

    return jsonify(
        usd_to_lbp = average_USD_to_LBP,
        lbp_to_usd = average_LBP_to_USD
    )


## Gets exchange based on the transactions that happened between 'start date (in days to go back)' 
# and 'end date (in days to go back)' 
# example: start = 14 and end = 7 uses the transaction of the second week back starting from today   
@app.route('/exchangeRateStartEnd', methods=['POST'])
def exchangeRateFromStartToEnd():
    timeToStart = request.json["time_to_start"]
    timeToStart = int(timeToStart)
    timeToEnd = request.json["time_to_end"]
    timeToEnd = int(timeToEnd)
    if timeToStart < timeToEnd:
        abort(400, {'message': 'Time to start has to be bigger than time to end!'})
    usd_to_lbp_Transactions = Transaction.query.filter(Transaction.usd_to_lbp== 1)\
        .filter(Transaction.added_date <= datetime.datetime.now() - datetime.timedelta(days=timeToEnd))\
        .filter(Transaction.added_date >= datetime.datetime.now() - datetime.timedelta(days = timeToStart) ).all()

    lbp_to_usd_Transactions = Transaction.query.filter(Transaction.usd_to_lbp== 0)\
        .filter(Transaction.added_date <= datetime.datetime.now() - datetime.timedelta(days=timeToEnd))\
        .filter(Transaction.added_date >= datetime.datetime.now() - datetime.timedelta(days= timeToStart) ).all()

    # get average of transactions from USD to LBP
    average_USD_to_LBP = 0
    for i in range(len(usd_to_lbp_Transactions)):
        ratio = usd_to_lbp_Transactions[i].lbp_amount / usd_to_lbp_Transactions[i].usd_amount
        average_USD_to_LBP = average_USD_to_LBP + ratio

    if len(usd_to_lbp_Transactions)!=0:
        average_USD_to_LBP = average_USD_to_LBP/len(usd_to_lbp_Transactions)

    # get average of transactions from LBP to USD
    average_LBP_to_USD = 0
    for i in range(len(lbp_to_usd_Transactions)):
        ratio = lbp_to_usd_Transactions[i].lbp_amount / lbp_to_usd_Transactions[i].usd_amount
        average_LBP_to_USD = average_LBP_to_USD + ratio

    if len(lbp_to_usd_Transactions)!=0:
        average_LBP_to_USD = average_LBP_to_USD / len(lbp_to_usd_Transactions)

    return jsonify(
        usd_to_lbp = average_USD_to_LBP,
        lbp_to_usd = average_LBP_to_USD
    )


# Fetches all the rates going back to x days
@app.route('/getRates', methods=['POST'])
def getRates():
    time_needed = request.json["time_needed"]
    time_needed = int(time_needed)
    rates = Rates.query.filter(Rates.added_date <= datetime.datetime.now())\
        .filter(Rates.added_date >= datetime.datetime.now() - datetime.timedelta(days=time_needed))\
            .filter(Rates.lbp_to_usd_amount > 0)\
                .filter(Rates.usd_to_lbp_amount > 0)\
                    .order_by(Rates.added_date.asc())\
                        .all()

    return jsonify(rates_schema.dump(rates))


## Returns the highest exchange rate for both LBP and USD as JSON (For the past week)
# NOTE: the rates are taken based on previous rate calculations based on multiple transactions,
# NOT on indivisual transactions!!!
# ex: added new transaction: sell 1USD for 1500LBP, the new rate is calculated based on the exchangeRate API
# The new lowest rate does NOT become 1500
@app.route('/highestAndLowestRates', methods=['GET'])
def getHighestExchangeRates():
    # Only takes in consideration the rates from a week ago
    # Only takes in consideration the non zero rates
    exchanges = Rates.query.filter(Rates.added_date <= datetime.datetime.now())\
        .filter(Rates.added_date >= datetime.datetime.now() - datetime.timedelta(days=7))\
            .filter(Rates.lbp_to_usd_amount > 0)\
                .filter(Rates.usd_to_lbp_amount > 0).all()

    if len(exchanges)>0:
        minBuyUSD = exchanges[0].lbp_to_usd_amount
        minSellUSD = exchanges[0].usd_to_lbp_amount
        maxBuyUSD = exchanges[0].lbp_to_usd_amount
        maxSellUSD = exchanges[0].usd_to_lbp_amount

        for i in range(len(exchanges)):
            if exchanges[i].lbp_to_usd_amount > maxBuyUSD:
                maxBuyUSD = exchanges[i].lbp_to_usd_amount
            if exchanges[i].lbp_to_usd_amount < minBuyUSD:
                minBuyUSD = exchanges[i].lbp_to_usd_amount
            if exchanges[i].usd_to_lbp_amount > maxSellUSD:
                maxSellUSD = exchanges[i].usd_to_lbp_amount
            if exchanges[i].usd_to_lbp_amount < minSellUSD:
                minSellUSD = exchanges[i].usd_to_lbp_amount

        return jsonify(
            maxBuyUSD = maxBuyUSD,
            maxSellUSD = maxSellUSD,
            minBuyUSD = minBuyUSD,
            minSellUSD = minSellUSD
        )

    else:
        return jsonify(
            maxBuyUSD = 0,
            maxSellUSD = 0,
            minBuyUSD = 0,
            minSellUSD = 0
        )


## Returns rate of fluctuation for both USD and LBP over 'x' amount of days
# 'x' is given through the request json['time_requested']
# rate is returned as a value from 0 + (signifies increase) or 0 - (signifies decrease)
# NOTE: based on previoulsy calculated Rates, NOT individual transactions!!!
# Returns 'buyFluctuation' (Buying USD -> Convert LBP to USD) and returns 'sellFluctuation' (Selling USD -> Convert USD to LBP)
# ex: sellFluctuation = 63 -> 63% increase in the USD to LBP exchange
# ex: sellFluctuation = -44 -> 44% decrease in the USD to LBP exchange
@app.route('/fluctuation', methods=['POST'])
def getFluctuationRate():
    timeNeeded = request.json["time_requested"]
    timeNeeded = int(timeNeeded)
    # gives the rates going back 'x' days and orders them by ascending added_date
    exchanges = Rates.query.filter(Rates.added_date <= datetime.datetime.now())\
        .filter(Rates.added_date >= datetime.datetime.now() - datetime.timedelta(days=timeNeeded))\
            .filter(Rates.lbp_to_usd_amount > 0)\
                .filter(Rates.usd_to_lbp_amount > 0)\
                    .order_by(Rates.added_date.asc())\
                        .all()

    earliestBuyRate = 1
    earliestSellRate = 1
    latestBuyRate = 1
    latestSellRate = 1

    n = len(exchanges)

    # No rates to use, return 0
    if n==0:
        return jsonify(
            sellFluctuation = 0,
            buyFluctuation = 0
        ) 

    if n>0:
        earliestBuyRate = exchanges[0].lbp_to_usd_amount
        earliestSellRate = exchanges[0].usd_to_lbp_amount
        if n>1:
            latestBuyRate = exchanges[n-1].lbp_to_usd_amount
            latestSellRate = exchanges[n-1].usd_to_lbp_amount
        else:
            # only one rate to use, therefore no fluctuation, return 0
            return jsonify(
                sellFluctuation = 0,
                buyFluctuation = 0
            ) 
    
    sellRateFluctuation = ( (latestSellRate - earliestSellRate) / earliestSellRate ) * 100
    buyRateFluctuation = ( (latestBuyRate - earliestBuyRate) / earliestBuyRate ) * 100

    return jsonify(
        sellFluctuation = sellRateFluctuation,
        buyFluctuation = buyRateFluctuation
    ) 


## Returns rate of fluctuation for both USD and LBP over the last week
@app.route('/fluctuationWeek', methods=['GET'])
def getFluctuationRateWeek():
    # gives the rates going back '7' days and orders them by ascending added_date
    exchanges = Rates.query.filter(Rates.added_date <= datetime.datetime.now())\
        .filter(Rates.added_date >= datetime.datetime.now() - datetime.timedelta(days=7))\
            .filter(Rates.lbp_to_usd_amount > 0)\
                .filter(Rates.usd_to_lbp_amount > 0)\
                    .order_by(Rates.added_date.asc())\
                        .all()

    earliestBuyRate = 1
    earliestSellRate = 1
    latestBuyRate = 1
    latestSellRate = 1

    n = len(exchanges)

    # No rates to use, return 0
    if n==0:
        return jsonify(
            sellFluctuation = 0,
            buyFluctuation = 0
        ) 

    if n>0:
        earliestBuyRate = exchanges[0].lbp_to_usd_amount
        earliestSellRate = exchanges[0].usd_to_lbp_amount
        if n>1:
            latestBuyRate = exchanges[n-1].lbp_to_usd_amount
            latestSellRate = exchanges[n-1].usd_to_lbp_amount
        else:
            # only one rate to use, therefore no fluctuation, return 0
            return jsonify(
                sellFluctuation = 0,
                buyFluctuation = 0
            ) 
    
    sellRateFluctuation = ( (latestSellRate - earliestSellRate) / earliestSellRate ) * 100
    buyRateFluctuation = ( (latestBuyRate - earliestBuyRate) / earliestBuyRate ) * 100

    return jsonify(
        sellFluctuation = sellRateFluctuation,
        buyFluctuation = buyRateFluctuation
    ) 



    
# Add exchange between 2 users
@app.route('/addUserExchange', methods=['POST'])
def addUserToUserExchange():
    user_receiving = request.json["user_received_username"]
    amount_usd = request.json["usd_amount"]
    amount_lbp = request.json["lbp_amount"]
    usd_to_lbp = request.json["usd_to_lbp"]

    userR = User.query.filter(User.user_name == user_receiving).first()
    if not userR:
        abort(400, {'message': 'User receiving doesnt exist!'})
    
    tken = extract_auth_token(request)
    if tken is None:
        abort(403)
    else:
        try:
            userId = decode_token(tken)
            user_sending = User.query.filter_by(id= userId).first()
            if user_sending.user_name == user_receiving:
                abort(400, {'message' :'User cannot do an exchange with him/her self!'})
            ex = UserExchanges(user_initiated_id= userId, 
                                user_received_username= user_receiving, 
                                usd_amount= amount_usd, 
                                lbp_amount= amount_lbp, 
                                usd_to_lbp= usd_to_lbp)
        except:
            abort(403)
        
    db.session.add(ex)
    db.session.commit()

    return jsonify(user_exchange_schema.dump(ex))


# Get a user's exchanges with other users
@app.route('/getUserExchanges', methods=['GET'])
def getUserToUserExchanges():
    tken = extract_auth_token(request)
    if tken is None:
        abort(403)
    else:
        try:
            userId = decode_token(tken)
            exchangesOfUser = UserExchanges.query.filter_by(user_initiated_id = userId).all()
        except:
            abort(403)
    return jsonify(user_exchanges_schema.dump(exchangesOfUser))


############ NEW ###################################################################################################
