package com.cba.ebanktest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by tung on 8/14/2016.
 */

public class Balance {
    @SerializedName("ServerStatus")
    @Expose
    private Map ServerStatus;
    @SerializedName("PaymentId")
    @Expose
    private String PaymentId;
    @SerializedName("AcctInfo")
    @Expose
    private Map AcctInfo;

    public Map getServerStatus() {
        return ServerStatus;
    }

    public void setServerStatus(Map serverStatus) {
        ServerStatus = serverStatus;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public Map getAcctInfo() {
        return AcctInfo;
    }

    public void setAcctInfo(Map acctInfo) {
        AcctInfo = acctInfo;
    }

    //    public void transferMoney(double amount, Balance destAcc){
//
//    }
}
