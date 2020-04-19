package com.account.manager.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreditDebitOfPersonActivity extends AppCompatActivity {
    public SimpleDateFormat dateFormatter;
    public SimpleDateFormat dateFormatter1;
    public DatePickerDialog fromDatePickerDialog;
    public Intent intent;
    public DatePickerDialog toDatePickerDialog;
    RadioGroup AmountGroup;
    TextView BalanceAmount;
    Button BtnPdf;
    TextView CrDbt;
    ArrayList<String> CreditAmount = new ArrayList<>();
    Double CreditAmt;
    RadioButton CreditRadio;
    ArrayList<String> DebitAmount = new ArrayList<>();
    Double DebitAmt;
    RadioButton DebitRedio;
    RelativeLayout Deleteparty;
    RelativeLayout EditPartyInfo;
    Double FinalBalance = Double.valueOf(0.0d);
    Double FinalCredit = Double.valueOf(0.0d);
    Double FinalDebit = Double.valueOf(0.0d);
    String GetBalance;
    String GetCr_Amt;
    String GetDate;
    String GetDbt_Amt;
    String GetFinalBal;
    String GetFinalCr;
    String GetFinalDbt;
    String GetNote = "";
    ImageView I_Call;
    EditText Newdate;
    TextView OpenDate;
    TextView PartyAddress;
    ArrayList<String> PartyBalance = new ArrayList<>();
    TextView PartyName;
    ArrayList<String> PartyNote = new ArrayList<>();
    TextView PartyNumber;
    String PayType = "Credit";
    ArrayList<String> Paydate = new ArrayList<>();
    String S_Address;
    String S_ID;
    String S_Name;
    String S_Number;
    String S_OpenDate;
    Button ShareInfo;
    ArrayList<String> UserID = new ArrayList<>();
    MainApplication mainApplication;
    DatabaseHelper databaseHelper;
    DecimalFormat decimalFormat;
    Dialog dialog;
    Button edit;
    EditText edtdate;
    EditText edtnote;
    EditText edtxtamount;
    String strTemp;
    String sharestring = "";
    TableLayout tableLayout;
    ImageView ic_close;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_creditdebit);
        getSupportActionBar().hide();
        mainApplication = (MainApplication) getApplicationContext();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.US);
        dateFormatter1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.US);
        FindIds();
        edtdate.setInputType(0);
        setDateTimeField();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DATE, instance.get(Calendar.DATE));
        edtdate.setText(dateFormatter.format(instance.getTime()));
        Intent intent2 = getIntent();
        S_ID = intent2.getStringExtra("P_ID");
        S_Name = intent2.getStringExtra("P_NAME");
        S_Number = intent2.getStringExtra("P_NUMBER");
        S_Address = intent2.getStringExtra("P_ADDRESS");
        S_OpenDate = intent2.getStringExtra("R_Date");
        GetPartyTransactionRecords();
        PartyName.setText(S_Name);
        PartyNumber.setText(getString(R.string.Number)+S_Number);
        PartyAddress.setText(getString(R.string.Address_)+S_Address);
        OpenDate.setText(getString(R.string.OpeningDate)+S_OpenDate);

        ic_close = findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (S_Number.equalsIgnoreCase("") || S_Number.equalsIgnoreCase("null")) {
            I_Call.setVisibility(View.GONE);
        }
        I_Call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:"+S_Number));
                startActivity(intent);
            }
        });

        AmountGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.credit) {
                    PayType = "Credit";
                    return;
                }
                PayType = "Debit";
            }
        });
        decimalFormat = new DecimalFormat("##.##");
        edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtxtamount.getText().toString().isEmpty()) {
                    edtxtamount.setError("Enter Amount");
                    return;
                }
                FinalCredit = Double.valueOf(0.0d);
                FinalDebit = Double.valueOf(0.0d);
                FinalBalance = Double.valueOf(0.0d);
                GetFinalBal = "0";
                GetFinalCr = "0";
                GetFinalDbt = "0";
                sharestring = "";
                GetNote = edtnote.getText().toString();
                GetDate = edtdate.getText().toString();
                if (PayType.equals("Credit")) {
                    CreditAmt = Double.valueOf(Double.parseDouble(edtxtamount.getText().toString()));
                    GetCr_Amt = decimalFormat.format(CreditAmt);
                    GetDbt_Amt = "0";
                    GetBalance = "";
                }
                if (PayType.equals("Debit")) {
                    DebitAmt = Double.valueOf(Double.parseDouble(edtxtamount.getText().toString()));
                    GetDbt_Amt = decimalFormat.format(DebitAmt);
                    GetCr_Amt = "0";
                    GetBalance = "";
                }
                databaseHelper.inserttransaction(S_ID, GetCr_Amt, GetDbt_Amt, GetBalance, GetDate, GetNote);
                Toast.makeText(CreditDebitOfPersonActivity.this, "Added...", Toast.LENGTH_SHORT).show();
                edtxtamount.setText("");
                edtnote.setText("");
                UserID.clear();
                DebitAmount.clear();
                CreditAmount.clear();
                PartyBalance.clear();
                Paydate.clear();
                PartyNote.clear();
                GetPartyTransactionRecords();
            }
        });
        EditPartyInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CreditDebitOfPersonActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.editinfo);
                dialog.setCancelable(true);
                Newdate = (EditText) dialog.findViewById(R.id.fromdate);
                final EditText editText = (EditText) dialog.findViewById(R.id.edtname);
                final EditText editText2 = (EditText) dialog.findViewById(R.id.edtphone);
                final EditText editText3 = (EditText) dialog.findViewById(R.id.address);
                Button button = (Button) dialog.findViewById(R.id.updaterecord);
                Button button1 = (Button) dialog.findViewById(R.id.cancel);
                button1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                editText.setText(S_Name);
                editText2.setText(S_Number);
                Newdate.setText(S_OpenDate);
                editText3.setText(S_Address);
                Newdate.setInputType(0);
                setDateTimeFieldnew();
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        databaseHelper.UpdatePartyInfo(S_ID, editText.getText().toString(), editText2.getText().toString(), Newdate.getText().toString(), editText3.getText().toString());
                        startActivity(new Intent(getApplicationContext(), AddViewDeleteTransactionActivity.class));
                        Toast.makeText(getApplicationContext(), "Update Successfully...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                dialog.show();
            }
        });
        Deleteparty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDeleteDialoge();
            }
        });
        ShareInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                sb.append("Transaction of ");
                sb.append(S_Name);
                sb.append("\n\nDATE || CreditAmount || DebitAmount || Note\n");
                sb.append(sharestring);
                sb.append(" \n\n Balance Amount = ");
                sb.append(FinalBalance);
                sb.append(" /-");
                intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", sb.toString());
                intent.setType("text*//*");
                startActivity(intent);
            }
        });
        BtnPdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PdfGenrateActivity.class);
                intent.putExtra("User_ID", S_ID);
                intent.putExtra("P_NAME", S_Name);
                intent.putExtra("P_NUMBER", S_Number);
                intent.putExtra("P_ADDRESS", S_Address);
                intent.putExtra("R_Date", S_OpenDate);
                intent.putExtra("P_Bal", GetFinalBal);
                startActivity(intent);
            }
        });
    }

    public void ShowDeleteDialoge() {
        Builder builder = new Builder(this);
        Builder title = builder.setTitle("Delete");
        StringBuilder sb = new StringBuilder();
        sb.append("Do You Want To Delete ");
        sb.append(S_Name);
        sb.append(" Permanently ?");
        title.setMessage(sb.toString()).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @SuppressLint({"WrongConstant"})
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (databaseHelper != null) {
                    databaseHelper.delparty(Long.parseLong(S_ID));
                    databaseHelper.delrecords(Long.parseLong(S_ID));
                    Toast.makeText(CreditDebitOfPersonActivity.this, "Delete Successfully..", 0).show();
                }
                dialogInterface.dismiss();
                startActivity(new Intent(getApplicationContext(), AddViewDeleteTransactionActivity.class));
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog = builder.show();
    }

    private void FindIds() {
        PartyName = (TextView) findViewById(R.id.partyname);
        PartyNumber = (TextView) findViewById(R.id.partynumber);
        PartyAddress = (TextView) findViewById(R.id.partyaddress);
        OpenDate = (TextView) findViewById(R.id.opendate);
        BalanceAmount = (TextView) findViewById(R.id.balanceamt);
        CrDbt = (TextView) findViewById(R.id.crdbt_amt);
        edtxtamount = (EditText) findViewById(R.id.edtamt);
        edtnote = (EditText) findViewById(R.id.edtnote);
        edtdate = (EditText) findViewById(R.id.edtdate);
        edit = (Button) findViewById(R.id.btnedit);
        ShareInfo = (Button) findViewById(R.id.btnshareinfo);
        BtnPdf = (Button) findViewById(R.id.btnpdf);
        AmountGroup = (RadioGroup) findViewById(R.id.amtgroup);
        CreditRadio = (RadioButton) findViewById(R.id.credit);
        DebitRedio = (RadioButton) findViewById(R.id.debit);
        tableLayout = (TableLayout) findViewById(R.id.tbl);
        Deleteparty = (RelativeLayout) findViewById(R.id.deleteimg);
        EditPartyInfo = (RelativeLayout) findViewById(R.id.editimg);
        I_Call = (ImageView) findViewById(R.id.call);

    }

    @SuppressLint({"WrongConstant"})
    private void DisplayContact(Cursor cursor) {
        Cursor cursor2 = cursor;
        BtnPdf.setVisibility(0);
        UserID.add(cursor2.getString(0));
        DebitAmount.add(cursor2.getString(1));
        CreditAmount.add(cursor2.getString(2));
        PartyBalance.add(cursor2.getString(3));
        Paydate.add(cursor2.getString(4));
        PartyNote.add(cursor2.getString(5));
        if (tableLayout != null) {
            tableLayout.removeAllViews();
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.backlist));
            TextView textView = new TextView(getApplicationContext());
            textView.setText("Date");
            textView.setGravity(17);
            textView.setTypeface(null, 1);
            textView.setTextSize(16.0f);
            textView.setTextColor(getResources().getColor(R.color.Black));
            textView.setPadding(10, 10, 10, 10);
            tableRow.addView(textView, new LayoutParams(0, -1, 1.0f));
            TextView textView2 = new TextView(getApplicationContext());
            textView2.setText("Cr.Amt");
            textView2.setGravity(17);
            textView2.setTextSize(16.0f);
            textView2.setTextColor(getResources().getColor(R.color.Black));
            textView2.setPadding(10, 10, 10, 10);
            textView2.setTypeface(null, 1);
            tableRow.addView(textView2, new LayoutParams(0, -1, 1.0f));
            TextView textView3 = new TextView(getApplicationContext());
            textView3.setText("Dbt.Amt");
            textView3.setGravity(17);
            textView3.setTextSize(16.0f);
            textView3.setTextColor(getResources().getColor(R.color.Black));
            textView3.setPadding(10, 10, 10, 10);
            textView3.setTypeface(null, 1);
            tableRow.addView(textView3, new LayoutParams(0, -1, 1.0f));
            TextView textView4 = new TextView(getApplicationContext());
            textView4.setText("Note");
            textView4.setGravity(17);
            textView4.setTextSize(16.0f);
            textView4.setTextColor(getResources().getColor(R.color.Black));
            textView4.setPadding(10, 10, 10, 10);
            textView4.setTypeface(null, 1);
            tableRow.addView(textView4, new LayoutParams(0, -1, 1.0f));
            TextView textView5 = new TextView(getApplicationContext());
            textView5.setText("");
            textView5.setGravity(17);
            textView5.setTextSize(16.0f);
            textView5.setTextColor(getResources().getColor(R.color.Black));
            textView5.setPadding(10, 10, 10, 10);
            textView5.setTypeface(null, 1);
            tableRow.addView(textView5, new LayoutParams(0, -1, 0.5f));
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(-2, -2));
            for (int i = 0; i < UserID.size(); i++) {
                TableRow tableRow2 = new TableRow(getApplicationContext());
                tableRow2.setBackgroundResource(R.drawable.nlinearlayout_bg1);
                TextView textView6 = new TextView(getApplicationContext());
                textView6.setText((CharSequence) Paydate.get(i));
                textView6.setGravity(17);
                textView6.setTextColor(getResources().getColor(R.color.Black));
                textView6.setTextSize(13.0f);
                textView6.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView6, new LayoutParams(0, -1, 1.0f));
                TextView textView7 = new TextView(getApplicationContext());
                textView7.setText((CharSequence) CreditAmount.get(i));
                textView7.setGravity(17);
                textView7.setTextSize(13.0f);
                textView7.setTextColor(getResources().getColor(R.color.Green));
                textView7.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView7, new LayoutParams(0, -1, 1.0f));
                TextView textView8 = new TextView(getApplicationContext());
                textView8.setText((CharSequence) DebitAmount.get(i));
                textView8.setGravity(17);
                textView8.setTextColor(getResources().getColor(R.color.Red));
                textView8.setTextSize(13.0f);
                textView8.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView8, new LayoutParams(0, -1, 1.0f));
                TextView textView9 = new TextView(getApplicationContext());
                textView9.setText((CharSequence) PartyNote.get(i));
                textView9.setGravity(17);
                textView9.setTextColor(getResources().getColor(R.color.Black));
                textView9.setTextSize(12.0f);
                textView9.setPadding(0, 10, 0, 10);
                tableRow2.addView(textView9, new LayoutParams(0, -1, 1.0f));
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setPadding(5, 25, 5, 25);
                imageView.setImageResource(R.drawable.edit);
                tableRow2.addView(imageView, new LayoutParams(0, -1, 0.5f));
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        int indexOfChild = tableLayout.indexOfChild((TableRow) view.getParent()) - 1;
                        strTemp = (String) UserID.get(indexOfChild);
                        String str = (String) CreditAmount.get(indexOfChild);
                        String str2 = (String) DebitAmount.get(indexOfChild);
                        String str3 = (String) Paydate.get(indexOfChild);
                        String str4 = (String) PartyNote.get(indexOfChild);
                        final Dialog dialog = new Dialog(CreditDebitOfPersonActivity.this);
                        dialog.requestWindowFeature(1);
                        dialog.setContentView(R.layout.activity_edit_transaction);
                        dialog.setCancelable(true);
                        edtdate = (EditText) dialog.findViewById(R.id.edtdate);
                        final EditText editText = (EditText) dialog.findViewById(R.id.edtcr);
                        final EditText editText2 = (EditText) dialog.findViewById(R.id.edtdr);
                        final EditText editText3 = (EditText) dialog.findViewById(R.id.edtnote);
                        Button button = (Button) dialog.findViewById(R.id.update);
                        Button button2 = (Button) dialog.findViewById(R.id.deletebtn);
                        editText.setText(str);
                        editText2.setText(str2);
                        edtdate.setText(str3);
                        editText3.setText(str4);
                        editText.setEnabled(false);
                        editText2.setEnabled(true);
                        edtdate.setInputType(0);
                        setDateTimeField();
                        if (!str.equals("0")) {
                            editText.setEnabled(true);
                            editText2.setEnabled(false);
                        }
                        final Dialog dialog2 = dialog;
                        button.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CreditAmt = Double.valueOf(Double.parseDouble(editText.getText().toString()));
                                GetCr_Amt = decimalFormat.format(CreditAmt);
                                DebitAmt = Double.valueOf(Double.parseDouble(editText2.getText().toString()));
                                GetDbt_Amt = decimalFormat.format(DebitAmt);
                                databaseHelper.UpdateTransactionRecord(strTemp, GetCr_Amt, GetDbt_Amt, edtdate.getText().toString(), editText3.getText().toString());
                                Toast.makeText(CreditDebitOfPersonActivity.this, "Update Sucessfully..", 0).show();
                                dialog2.dismiss();
                                UserID.clear();
                                DebitAmount.clear();
                                CreditAmount.clear();
                                PartyBalance.clear();
                                Paydate.clear();
                                PartyNote.clear();
                                FinalCredit = Double.valueOf(0.0d);
                                FinalDebit = Double.valueOf(0.0d);
                                FinalBalance = Double.valueOf(0.0d);
                                sharestring = "";
                                GetFinalBal = "0";
                                GetFinalCr = "0";
                                GetFinalDbt = "0";
                                GetPartyTransactionRecords();
                            }
                        });
                        button2.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                if (databaseHelper != null) {
                                    databaseHelper.DeletTransaction(Long.parseLong(strTemp));
                                    Toast.makeText(CreditDebitOfPersonActivity.this, "Delete Successfully..", 0).show();
                                }
                                dialog.dismiss();
                                UserID.clear();
                                DebitAmount.clear();
                                CreditAmount.clear();
                                PartyBalance.clear();
                                Paydate.clear();
                                PartyNote.clear();
                                FinalCredit = Double.valueOf(0.0d);
                                FinalDebit = Double.valueOf(0.0d);
                                FinalBalance = Double.valueOf(0.0d);
                                sharestring = "";
                                GetFinalBal = "0";
                                GetFinalCr = "0";
                                GetFinalDbt = "0";
                                GetPartyTransactionRecords();
                            }
                        });
                        dialog.show();
                    }
                });
                tableLayout.addView(tableRow2, new TableLayout.LayoutParams(-2, -2));
            }
            FinalDebit = Double.valueOf(FinalDebit.doubleValue() + Double.parseDouble(cursor2.getString(1)));
            FinalCredit = Double.valueOf(FinalCredit.doubleValue() + Double.parseDouble(cursor2.getString(2)));
            FinalBalance = Double.valueOf(FinalCredit.doubleValue() - FinalDebit.doubleValue());
            GetFinalBal = String.format("%.2f", new Object[]{FinalBalance});
            GetFinalCr = String.format("%.2f", new Object[]{FinalCredit});
            GetFinalDbt = String.format("%.2f", new Object[]{FinalDebit});

            BalanceAmount.setText(getString(R.string.BalanceAmount)+" "+GetFinalBal+"/-");
            CrDbt.setText("Totl Cr = "+FinalCredit+" ,Totl Dbt = "+FinalDebit);

            StringBuilder sb4 = new StringBuilder();
            sb4.append(sharestring);
            sb4.append("\n");
            sb4.append(cursor2.getString(4));
            sb4.append(" || ");
            sb4.append(cursor2.getString(2));
            sb4.append(" Crd. || ");
            sb4.append(cursor2.getString(1));
            sb4.append(" Dbt. || ");
            sb4.append(cursor2.getString(5));
            sb4.append("\n-------------------------------");
            sharestring = sb4.toString();
        }
    }

    @SuppressLint({"WrongConstant"})
    public void GetPartyTransactionRecords() {
        Cursor GetPartyTransaction = databaseHelper.GetPartyTransaction(Long.parseLong(S_ID));
        if (GetPartyTransaction.moveToFirst()) {
            do {
                DisplayContact(GetPartyTransaction);
            } while (GetPartyTransaction.moveToNext());
            return;
        }
        Toast.makeText(getBaseContext(), "No Transaction Found!", 1).show();
        databaseHelper.close();
        BtnPdf.setVisibility(8);
    }

    public void setDateTimeField() {
        edtdate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        Calendar instance = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Calendar instance = Calendar.getInstance();
                instance.set(i, i2, i3);
                edtdate.setText(dateFormatter.format(instance.getTime()));
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
        fromDatePickerDialog = datePickerDialog;
    }

    public void setDateTimeFieldnew() {
        Newdate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                toDatePickerDialog.show();
            }
        });
        Calendar instance = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Calendar instance = Calendar.getInstance();
                instance.set(i, i2, i3);
                Newdate.setText(dateFormatter1.format(instance.getTime()));
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
        toDatePickerDialog = datePickerDialog;
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AddViewDeleteTransactionActivity.class));
        finish();
    }
}
