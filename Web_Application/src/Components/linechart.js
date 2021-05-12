import React from 'react';
import { Line , defaults } from "react-chartjs-2";
import { useState } from "react";
import { useEffect } from "react";

export default function LineChart() {
    var SERVER_URL = "http://127.0.0.1:5000"
    //defining useStates for the data -> dataZeroToOne means data from week 0 to weak 1
    let [dataZeroToOne,setDataZeroToOne] = useState(0);
    let [dataOneToTwo,setDataOneToTwo] = useState(0);
    let [dataTwoToThree,setDataTwoToThree] = useState(0);
    let [dataThreeToFour,setDataThreeToFour] = useState(0);
    let [dataFourToFive,setDataFourToFive] = useState(0);
    let [dataFiveToSix,setDataFiveToSix] = useState(0);
    let [dataSixToSeven,setDataSixToSeven] = useState(0);
    let [dataSevenToEight,setDataSevenToEight] = useState(0);

    // week 0 to week 1 
    function fetchDataZeroToOne() {
        return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            time_to_start: 7,
            time_to_end: 0,
          }),
        })
          .then((response) => response.json())
          .then((body) => {
            setDataZeroToOne(body.usd_to_lbp)
          });
      }
      useEffect(fetchDataZeroToOne, []);

    // week 1 to week 2 
    function fetchDataOneToTwo() {
        return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            time_to_start: 14,
            time_to_end: 7,
          }),
        })
          .then((response) => response.json())
          .then((body) => {
            setDataOneToTwo(body.usd_to_lbp)
          });
      }
      useEffect(fetchDataOneToTwo, []);

      
    // week 2 to week 3 
    function fetchDataTwoToThree() {
        return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            time_to_start: 21,
            time_to_end: 14,
          }),
        })
          .then((response) => response.json())
          .then((body) => {
            setDataTwoToThree(body.usd_to_lbp)
          });
      }
      useEffect(fetchDataTwoToThree, []);

// week 3 to week 4 
function fetchDataThreeToFour() {
    return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        time_to_start: 28,
        time_to_end: 21,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setDataThreeToFour(body.usd_to_lbp)
      });
  }
  useEffect(fetchDataThreeToFour, []);

  // week 4 to week 5 
function fetchDataFourToFive() {
    return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        time_to_start: 35,
        time_to_end: 28,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setDataFourToFive(body.usd_to_lbp)
      });
  }
  useEffect(fetchDataFourToFive, []);
  
    // week 5 to week 6 
function fetchDataFiveToSix() {
    return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        time_to_start: 42,
        time_to_end: 35,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setDataFiveToSix(body.usd_to_lbp)
      });
  }
  useEffect(fetchDataFiveToSix, []);

    // week 6 to week 7 
function fetchDataSixToSeven() {
    return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        time_to_start: 49,
        time_to_end: 42,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setDataSixToSeven(body.usd_to_lbp)
      });
  }
  useEffect(fetchDataSixToSeven, []);


    // week 7 to week 8 
function fetchDataSevenToEight() {
    return fetch(`${SERVER_URL}/exchangeRateStartEnd`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        time_to_start: 56,
        time_to_end: 49,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setDataSevenToEight(body.usd_to_lbp)
      });
  }
  useEffect(fetchDataSevenToEight, []);


    const data = {
        labels: [
            
            'Seven Weeks Ago',
            'Six Weeks Ago',
            'Five Week Ago',
            'Four Weeks Ago',
            'Three Weeks Ago',
            'Two Weeks Ago',
            'One Week Ago',
            'Today'
        ],
        datasets: [
            {
                label: 'USD to LBP Exchange Rate Over The Past Eight Weeks',
                data: [dataSevenToEight,dataSixToSeven,dataFiveToSix,dataFourToFive, dataThreeToFour, dataTwoToThree, dataOneToTwo, dataZeroToOne],
                borderColor: ['rgba(255,206,86,0.8)'],
                backgroundColor: ['rgba(255,206,86,0.8)'],
                pointBackgroundColor: ['rgba(255,206,86,0.8]'],
                pointBorderColor:['rgba(255,206,86,0.8]']
            },
        ]
    }
    return  <Line data={data}/>
}

