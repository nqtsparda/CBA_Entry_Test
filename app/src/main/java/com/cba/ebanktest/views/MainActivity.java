package com.cba.ebanktest.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cba.ebanktest.Const;
import com.cba.ebanktest.R;
import com.cba.ebanktest.dao.BankDatabaseHandler;
import com.cba.ebanktest.models.Bank;
import com.cba.ebanktest.models.Transfer;
import com.cba.ebanktest.presenters.AccountPresenter;
import com.cba.ebanktest.services.AccountService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSend;
    private Button btnContactPicker;
    private TextView txtMoney;
    private TextInputEditText amount;
    private TextInputEditText contact;
    private TextInputEditText bank;
    private TextInputEditText message;
    private TextInputEditText accNumber;
    private Map<String, String> destBank;
    private ProgressDialog progressDialog;
    private Spinner spnBank;
    private HashMap<String,String> spinnerMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create DB sample for bank list testing
        initSampleDB();

        initControlItem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isNetworkConnected()) {
            showAlertDialog();
            return;
        }

        // Call Balance Api
        AccountService service = new AccountService();
        AccountPresenter accountPresenter = new AccountPresenter(txtMoney, service);
        try {
            showWaitingDialog();
            accountPresenter.loadAccInfo(progressDialog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initControlItem() {
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnContactPicker = (Button) findViewById(R.id.btn_contact_picker);
        amount = (TextInputEditText) findViewById(R.id.edt_amount);
        contact = (TextInputEditText) findViewById(R.id.edt_contact);
        message = (TextInputEditText) findViewById(R.id.edt_message);
        accNumber = (TextInputEditText) findViewById(R.id.edt_acc_number);
        spnBank = (Spinner) findViewById(R.id.spn_bank);
        txtMoney = (TextView) findViewById(R.id.tv_money);
        btnContactPicker.setOnClickListener(this);
        destBank = new HashMap<>();


        BankDatabaseHandler bankDatabaseHandler = new BankDatabaseHandler(this);
        List<Bank> banks = bankDatabaseHandler.getAllBanks();
        String[] spinnerArray = new String[banks.size()];
        spinnerMap = new HashMap<String, String>();
        for (int i = 0; i < banks.size(); i++)
        {
            spinnerMap.put(banks.get(i).getCode(),banks.get(i).getName());
            spinnerArray[i] = banks.get(i).getName();
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBank.setAdapter(adapter);
    }

    private void showWaitingDialog() {
        progressDialog = ProgressDialog
                .show(MainActivity.this, "Fetching Data", "Please wait...", true);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm.getActiveNetworkInfo())
            return true;
        return false;
    }

    private void initSampleDB() {
        BankDatabaseHandler bankDatabaseHandler = new BankDatabaseHandler(this);
        bankDatabaseHandler.deleteAllBank();
        bankDatabaseHandler.addBank(new Bank("PT Bank Ganesha", "950"));
        bankDatabaseHandler.addBank(new Bank("Saigon Bank", "960"));
        bankDatabaseHandler.addBank(new Bank("Vietcombank", "970"));
        bankDatabaseHandler.addBank(new Bank("Tien Phong Bank", "980"));
    }

    public void showAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("This action need data from internet /n Please activate Wifi or 3G network");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_send:
                sendTransferReq();
                break;
            case R.id.btn_contact_picker:
                startContactPicker();
                break;
            case R.id.btn_quit:
                finishAffinity();
                break;
            default:
                break;
        }
    }

    private void startContactPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Const.CODE_CONTACT);
    }

    private void sendTransferReq() {
        if(!validated()){
            return;
        }
        String destAccNumber = accNumber.getText().toString();
        String destBankName = spnBank.getSelectedItem().toString();
        String destBankCode = spinnerMap.get(destBankName);
        String paymentAmount = amount.getText().toString();
        String paymentMsg = message.getText().toString();
        Transfer tr = new Transfer(destAccNumber, destBankCode, paymentAmount, paymentMsg, true);
        AccountService service = new AccountService();
        AccountPresenter accountPresenter = new AccountPresenter(service);
        accountPresenter.transferMoney(this, tr);
    }

    public boolean validated() {
        boolean valid = true;

        String amountMn = amount.getText().toString();
        String msg = message.getText().toString();
        String accNum = accNumber.getText().toString();

        if (amountMn.isEmpty() ) {
            amount.setError("Amount cannot be empty");
            valid = false;
        } else {
            amount.setError(null);
        }

        if (msg.isEmpty() ) {
            message.setError("Message cannot be empty");
            valid = false;
        } else {
            message.setError(null);
        }

        if (accNum.isEmpty()) {
            accNumber.setError("Balance number cannot be empty");
            valid = false;
        } else {
            accNumber.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.CODE_CONTACT:
                if (resultCode == RESULT_OK) {
                    Uri contactUri = data.getData();
                    Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String contactName = cursor.getString(column);
                    contact.setText(contactName);
                }
                break;
            default:
                break;
        }
    }
}
