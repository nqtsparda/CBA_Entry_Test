package com.cba.ebanktest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tung on 8/14/2016.
 */

public class Transfer {
    @SerializedName("TransferAddRq")
    @Expose
    private TransferAddRq transferAddRq;

    public Transfer(String destAcctNo, String destBankCode, String amt, String message, boolean saveAsBenAccount) {
        TranferInfo tranferInfo = new TranferInfo(destAcctNo, destBankCode, amt, message, saveAsBenAccount);
        TransferAddRq transferAddRq = new TransferAddRq(tranferInfo);
        this.transferAddRq = transferAddRq;
    }

    public Transfer() {
    }

    public TransferAddRq getTransferAddRq() {
        return transferAddRq;
    }

    public void setTransferAddRq(TransferAddRq transferAddRq) {
        this.transferAddRq = transferAddRq;
    }
}

class TranferInfo {
    @SerializedName("DestAcctNo")
    private String destAcctNo;
    @SerializedName("DestBankCode")
    private String destBankCode;
    @SerializedName("Amt")
    private String amt;
    @SerializedName("Message")
    private String message;
    @SerializedName("SaveAsBenAccount")
    private Boolean saveAsBenAccount;

    public TranferInfo(String destAcctNo, String destBankCode, String amt, String message, Boolean saveAsBenAccount) {
        this.destAcctNo = destAcctNo;
        this.destBankCode = destBankCode;
        this.amt = amt;
        this.message = message;
        this.saveAsBenAccount = saveAsBenAccount;
    }

    public String getDestAcctNo() {
        return destAcctNo;
    }

    public void setDestAcctNo(String destAcctNo) {
        this.destAcctNo = destAcctNo;
    }

    public String getDestBankCode() {
        return destBankCode;
    }

    public void setDestBankCode(String destBankCode) {
        this.destBankCode = destBankCode;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSaveAsBenAccount() {
        return saveAsBenAccount;
    }

    public void setSaveAsBenAccount(Boolean saveAsBenAccount) {
        this.saveAsBenAccount = saveAsBenAccount;
    }
}

class TransferAddRq {

    @SerializedName("TranferInfo")
    private TranferInfo tranferInfo;

    public TransferAddRq(TranferInfo tranferInfo) {
        this.tranferInfo = tranferInfo;
    }

    public TranferInfo getTranferInfo() {
        return tranferInfo;
    }

    public void setTranferInfo(TranferInfo tranferInfo) {
        this.tranferInfo = tranferInfo;
    }
}
