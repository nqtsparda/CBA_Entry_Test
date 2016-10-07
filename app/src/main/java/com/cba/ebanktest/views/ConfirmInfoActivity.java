package com.cba.ebanktest.views;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cba.ebanktest.Const;
import com.cba.ebanktest.R;
import com.cba.ebanktest.dao.BankDatabaseHandler;
import com.cba.ebanktest.models.TransferResp;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Map;

public class ConfirmInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvPaymentAmount;
    private TextView tvAccountName;
    private TextView tvAccountNumber;
    private TextView tvDestBank;
    private Button btnConfirm;
    private Button btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initItemControl();

        Intent intent = getIntent();
        String rawConfirmData = intent.getStringExtra(Const.KEY_RAW_DATA);
        if(null != rawConfirmData){
            setDisplayInfo(rawConfirmData);
        } else {
            showNoDataDialog();
        }

    }

    public void initItemControl() {
        tvPaymentAmount = (TextView) findViewById(R.id.tv_payment_amount);
        tvAccountName = (TextView) findViewById(R.id.tv_account_name);
        tvAccountNumber = (TextView) findViewById(R.id.tv_account_number);
        tvDestBank = (TextView) findViewById(R.id.tv_at_bank);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    public void showNoDataDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Action fail !");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void setDisplayInfo(String rawConfirmData) {
        TransferResp transferResp = new Gson().fromJson(rawConfirmData, TransferResp.class);
        if(null != transferResp){
            Map<String, Object> transferInfoMap = transferResp.getTransferInfo();
            Double paymentAmount = (Double) transferInfoMap.get(Const.KEY_AMOUNT);
            String accountName = transferInfoMap.get(Const.KEY_DEST_ACCOUNT_NAME).toString();
            String accountNumber = transferInfoMap.get(Const.KEY_DEST_ACCOUNT_NUMBER).toString();
            String destBankCode = transferInfoMap.get(Const.KEY_DEST_BANK_CODE).toString();
            BankDatabaseHandler bankDatabaseHandler = new BankDatabaseHandler(this);

            String destBankName = bankDatabaseHandler.getBankByCode(destBankCode).getName();
            String formatedMoney = convertMoneyFormat(String.valueOf(paymentAmount));
            tvPaymentAmount.setText(formatedMoney);
            tvAccountName.setText(accountName);
            tvAccountNumber.setText(accountNumber);
            tvDestBank.setText(destBankName);
        }
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            finishAffinity();
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_confirm:
                finishAffinity();
                break;
            default:
                break;
        }
    }
}
