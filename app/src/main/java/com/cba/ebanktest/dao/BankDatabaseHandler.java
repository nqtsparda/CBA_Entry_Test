package com.cba.ebanktest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cba.ebanktest.Const;
import com.cba.ebanktest.models.Bank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TungNQ5 on 2016/09/14.
 */
public class BankDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = Const.DATABASE_VERSION;

    private static final String DATABASE_NAME = "bankUtil";

    private static final String TABLE_BANK = "bank";

    // Columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CODE = "code";

    public BankDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createBankTable = new StringBuilder()
                .append("CREATE TABLE ").append(TABLE_BANK).append("(")
                .append("\n")
                .append(KEY_ID).append(" INTEGER PRIMARY KEY,")
                .append("\n")
                .append(KEY_NAME).append(" TEXT,")
                .append("\n")
                .append(KEY_CODE).append(" TEXT)").toString();
        sqLiteDatabase.execSQL(createBankTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(new StringBuilder("DROP TABLE IF EXISTS ")
                .append(TABLE_BANK).toString());
        onCreate(sqLiteDatabase);
    }

    public void addBank(Bank bank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, bank.getName());
        values.put(KEY_CODE, bank.getCode());

        db.insert(TABLE_BANK, null, values);
        db.close();
    }

    public Bank getBankByCode(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BANK, new String[]{KEY_ID,
                        KEY_NAME, KEY_CODE}, KEY_CODE + "=?",
                new String[]{code}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Bank bank = new Bank(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        return bank;
    }

    public List<Bank> getAllBanks() {
        List<Bank> BankList = new ArrayList<>();
        // Select All Query
        String selectQuery = new StringBuilder("SELECT  * FROM ").append(TABLE_BANK).toString();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Bank bank = new Bank();
                bank.setId(Integer.parseInt(cursor.getString(0)));
                bank.setName(cursor.getString(1));
                bank.setCode(cursor.getString(2));
                BankList.add(bank);
            } while (cursor.moveToNext());
        }

        return BankList;
    }

    public void deleteAllBank() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BANK, null, null);
        db.close();
    }
}
