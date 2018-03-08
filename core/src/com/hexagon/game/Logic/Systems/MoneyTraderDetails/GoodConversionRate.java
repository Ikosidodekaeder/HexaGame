package com.hexagon.game.Logic.Systems.MoneyTraderDetails;

import com.hexagon.game.Logic.HexaComponents;

/**
 * Created by Johannes on 08.03.2018.
 */

public class GoodConversionRate {

    HexaComponents          Good;
    int                     Amount;
    long                    Value;

    public GoodConversionRate(HexaComponents good,int amount,long value){
        this.Good = good;
        this.Amount = amount;
        this.Value = value;
    }

    public HexaComponents getGood() {
        return Good;
    }

    public void setGood(HexaComponents good) {
        Good = good;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public long getValue() {
        return Value;
    }

    public void setValue(long value) {
        Value = value;
    }

    public double rate(){
        return Amount / Value;
    }
}
