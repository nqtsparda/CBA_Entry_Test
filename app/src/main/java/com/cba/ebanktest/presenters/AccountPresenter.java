package com.cba.ebanktest.presenters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.cba.ebanktest.Const;
import com.cba.ebanktest.models.Balance;
import com.cba.ebanktest.models.Transfer;
import com.cba.ebanktest.models.TransferResp;
import com.cba.ebanktest.services.AccountService;
import com.cba.ebanktest.views.ConfirmInfoActivity;
import com.cba.ebanktest.views.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tung on 8/14/2016.
 */
public class AccountPresenter {

    TextView txtMoney;
    AccountService mForum;

    public AccountPresenter(TextView txtMoney, AccountService forum) {

        this.txtMoney = txtMoney;
        this.mForum = forum;
    }

    public AccountPresenter(AccountService forum) {
        this.mForum = forum;
    }

    public void loadAccInfo(final ProgressDialog progressDialog) throws IOException {
        Observable<List<Balance>> account = mForum.getApi()
                .getAccountBalance();
        account.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Balance>>() {
                    @Override
                    public void onCompleted() {
                        if(null != progressDialog && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Log.d("system_message", "Load Balance Info complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("system_message", "Load Balance Info Error");
                    }

                    @Override
                    public void onNext(List<Balance> fetchedLst) {
                        Balance fetchedAcc = fetchedLst.get(0);
                        Map<String, String> accInfo = fetchedAcc.getAcctInfo();
                        String formattedAmount = convertMoneyFormat(accInfo.get("BalanceAmount"));
                        String currencyCd = accInfo.get("CurrencyCode");
                        StringBuilder amountWithUnit = new StringBuilder();
                        if (currencyCd != null && !currencyCd.isEmpty()) {
                            switch (currencyCd) {
                                case Const.CURRENCY_CODE_360:
                                    amountWithUnit.append("RP ").append(formattedAmount);
                                    break;
                                default:
                                    amountWithUnit.append(formattedAmount);
                                    break;
                            }

                        }
                        txtMoney.setText(amountWithUnit.toString());
                    }
                });

    }

    private String convertMoneyFormat(String balanceAmount) {
        if (balanceAmount == null) {
            return Const.DEFAUL_MONEY_VALUE;
        }
        double amount = Double.valueOf(balanceAmount);
        DecimalFormat decimalFormat = new DecimalFormat(Const.MONEY_FORMAT);
        String convertedMoney = decimalFormat.format(amount);
        if (convertedMoney != null) {
            return convertedMoney;
        }
        return balanceAmount;
    }

    public void transferMoney(final MainActivity context, Transfer tr) {
        Observable<ResponseBody> tranferResObservable = mForum.getApi()
                .transferMoney(tr);
        tranferResObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    Intent confirmIntent = new Intent(context, ConfirmInfoActivity.class);
                    @Override
                    public void onCompleted() {
                        context.startActivity(confirmIntent);
                        Log.d("system_message", "Transfer money complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("system_message", "Load Balance Info Error");
                    }

                    @Override
                    public void onNext(ResponseBody respBody) {
                        try {
                            String rawConfirmData = respBody.string();
                            TransferResp transferResp = new Gson().fromJson(rawConfirmData, TransferResp.class);
                            if(null != transferResp){
                                Map<String, String> serverStt = transferResp.getServerStatus();
                                if(Const.SERVER_SUCCESS_CODE.equals(serverStt.get(Const.KEY_CODE))){
                                    confirmIntent.putExtra(Const.KEY_RAW_DATA, rawConfirmData);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}

