package com.traffic.pollution;

public class PollutionService {

    public String analyzePollution(double niveau){

        if(niveau < 50){
            return "Air quality good";
        }
        else if(niveau < 150){
            return "Moderate pollution";
        }
        else{
            return "High pollution - Reduce traffic";
        }
    }
}