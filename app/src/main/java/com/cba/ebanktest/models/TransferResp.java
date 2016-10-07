package com.cba.ebanktest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by tung on 8/14/2016.
 */

public class TransferResp {
    @SerializedName("ServerStatus")
    @Expose
    private Map serverStatus;
    @SerializedName("PaymentRegId")
    @Expose
    private String paymentRegId;
    @SerializedName("TransferInfo")
    @Expose
    private Map transferInfo;

    public Map getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(Map serverStatus) {
        serverStatus = serverStatus;
    }

    public String getPaymentId() {
        return paymentRegId;
    }

    public void setPaymentId(String paymentId) {
        paymentRegId = paymentId;
    }

    public Map getTransferInfo() {
        return transferInfo;
    }

    public void setTransferInfo(Map transferInfo) {
        this.transferInfo = transferInfo;
    }

}
