package com.account.manager.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PdfGenrateActivity extends AppCompatActivity {
    ArrayList<String> CreditAmount = new ArrayList<>();
    ArrayList<String> DebitAmount = new ArrayList<>();
    ArrayList<String> PartyBalance = new ArrayList<>();
    ArrayList<String> PartyNote = new ArrayList<>();
    ArrayList<String> Paydate = new ArrayList<>();
    TextView PdfMsg;
    String S_Address;
    String S_Balance;
    String S_Name = "";
    String S_Number;
    String S_OpenDate;
    String UserID = "";
    ArrayList<String> UserIDList = new ArrayList<>();
    DatabaseHelper f459db;
    ListView list;
    BaseColor myColor = WebColors.getRGBColor("#f2f2f2");
    BaseColor myColor1 = WebColors.getRGBColor("#b6b6b6");
    private Image bgImage;
    private PdfPCell cell;
    private File dir;
    private File file;
    private String path;
    private String textAnswer;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_pdfgenrate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.f459db = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        this.UserID = intent.getStringExtra("User_ID");
        this.S_Name = intent.getStringExtra("P_NAME");
        this.S_Number = intent.getStringExtra("P_NUMBER");
        this.S_Address = intent.getStringExtra("P_ADDRESS");
        this.S_OpenDate = intent.getStringExtra("R_Date");
        this.S_Balance = String.valueOf(intent.getStringExtra("P_Bal"));
        this.list = (ListView) findViewById(R.id.list);
        this.PdfMsg = (TextView) findViewById(R.id.txt_p);
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("/AccountManager/Reports");
        this.path = sb.toString();
        this.dir = new File(this.path);
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        if (this.UserID != null && this.S_Name != null) {
            GetPartyTransactionRecords();
            try {
                createPDF();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
                Log.e("VML", e.getMessage());
            }
        }
    }

    @SuppressLint({"WrongConstant", "ResourceType"})
    public void onResume() {
        super.onResume();
        try {
            this.PdfMsg.setVisibility(8);
            ArrayList GetFiles = GetFiles("/sdcard/AccountManager/Reports");
            if (GetFiles == null) {
                Toast.makeText(getApplicationContext(), "No Pdf Found.", Toast.LENGTH_SHORT).show();
                this.PdfMsg.setVisibility(0);
            }
            if (GetFiles.size() != 0) {
                this.list.setAdapter(new ArrayAdapter(this, 17367043, GetFiles));
            }
            this.list.setLongClickable(true);
            this.list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    Uri uri;
                    String obj = PdfGenrateActivity.this.list.getItemAtPosition(i).toString();
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                    sb.append("/AccountManager/Reports/");
                    sb.append(obj);
                    File file = new File(sb.toString());
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(PdfGenrateActivity.this.getApplicationContext(), "com.account.manager.app.provider", file);
                        intent.addFlags(1);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/pdf");
                    PdfGenrateActivity.this.startActivity(intent);
                    PrintStream printStream = System.out;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("RESULT_");
                    sb2.append(obj);
                    printStream.println(sb2.toString());
                }
            });
            this.list.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i1, long j) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PdfGenrateActivity.this);
                    builder.setTitle((CharSequence) "Delete!!");
                    builder.setMessage((CharSequence) "do you want to delete this file?");
                    builder.setPositiveButton((CharSequence) "YES", (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String obj = list.getItemAtPosition(i1).toString();
                            StringBuilder sb = new StringBuilder();
                            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                            sb.append("/AccountManager/Reports/");
                            sb.append(obj);
                            File file = new File(sb.toString());
                            if (file.exists()) {
                                if (file.delete()) {
                                    Toast.makeText(PdfGenrateActivity.this.getApplicationContext(), "Delete Sucessfully...", Toast.LENGTH_SHORT).show();
                                    PdfGenrateActivity.this.finish();
                                } else {
                                    Toast.makeText(PdfGenrateActivity.this.getApplicationContext(), "File not deleted error...", Toast.LENGTH_SHORT).show();
                                }
                            }
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton((CharSequence) "NO", (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> GetFiles(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file2 = new File(str);
        file2.mkdirs();
        File[] listFiles = file2.listFiles();
        if (listFiles.length == 0) {
            return null;
        }
        for (File name : listFiles) {
            arrayList.add(name.getName());
        }
        return arrayList;
    }

    @SuppressLint({"WrongConstant"})
    public void createPDF() throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4, 0.0f, 0.0f, 0.0f, 0.0f);
        String str = "PDFCreator";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("PDF Path: ");
            sb.append(this.path);
            Log.e(str, sb.toString());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy_hh:mm");
            File file2 = this.dir;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.S_Name);
            sb2.append("_PDF_");
            sb2.append(simpleDateFormat.format(Calendar.getInstance().getTime()));
            sb2.append(".pdf");
            this.file = new File(file2, sb2.toString());
            PdfWriter.getInstance(document, new FileOutputStream(this.file));
            document.open();

            PdfPTable pdfPTable = new PdfPTable(5);
            pdfPTable.setSpacingBefore(100);
            pdfPTable.setWidthPercentage(100.0f);
            pdfPTable.setWidths(new float[]{5.0f,55.0f,10.0f,20.0f,10.0f});
            this.cell = new PdfPCell();
            this.cell.setBorder(0);
            pdfPTable.addCell(cell);
            this.cell = new PdfPCell();
            this.cell.setColspan(1);
            this.cell.setBorder(0);
            this.cell.addElement(new Paragraph("Summary Details of all Transactions"));
            cell.addElement(new Paragraph("Person Name :- "+S_Name.toUpperCase()));
            cell.addElement(new Paragraph("Opening Date :- "+S_OpenDate));
            cell.addElement(new Paragraph("Number :- "+S_Number));
            cell.addElement(new Paragraph("Address :- "+S_Address));
            pdfPTable.addCell(cell);
            this.cell = new PdfPCell();
            this.cell.setBorder(0);
            pdfPTable.addCell(cell);
            this.cell = new PdfPCell();
            int i = 0;
            this.cell.setBorder(0);
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.transaction)).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
            this.bgImage = Image.getInstance(byteArrayOutputStream.toByteArray());
            this.bgImage.setAbsolutePosition(330.0f, 642.0f);
            this.cell.addElement(this.bgImage);
            this.cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(this.cell);
            this.cell = new PdfPCell();
            this.cell.setBorder(0);
            pdfPTable.addCell(cell);


     /*       PdfPTable pdfPTable1 = new PdfPTable(1);
            pdfPTable1.setWidthPercentage(100.0f);
            this.cell = new PdfPCell();
            this.cell.setColspan(1);
            this.cell.setBorder(0);
            this.cell.addElement(new Paragraph("Summary Details of all Transactions"));
            cell.addElement(new Paragraph("Person Name :- "+S_Name.toUpperCase()));
            cell.addElement(new Paragraph("Opening Date :- "+S_OpenDate));
            cell.addElement(new Paragraph("Number :- "+S_Number));
            cell.addElement(new Paragraph("Address :- "+S_Address));
            this.cell.addElement(pdfPTable);
            pdfPTable1.addCell(this.cell);*/



            PdfPTable pdfPTable2 = new PdfPTable(1);
            pdfPTable2.setWidthPercentage(100.0f);
            this.cell = new PdfPCell();
            this.cell.setColspan(1);
            this.cell.addElement(pdfPTable);
            pdfPTable2.addCell(this.cell);


            PdfPTable pdfPTable3 = new PdfPTable(5);
            pdfPTable3.setWidths(new float[]{10.0f, 30.0f, 30.0f, 30.0f, 40.0f});
            this.cell = new PdfPCell();
            this.cell.setBackgroundColor(this.myColor);
            this.cell.setColspan(5);
            this.cell.addElement(pdfPTable2);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell(new Phrase(" "));
            this.cell.setColspan(5);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell();
            this.cell.setColspan(5);
            this.cell.setBackgroundColor(this.myColor1);
            this.cell = new PdfPCell(new Phrase("S.No"));
            this.cell.setBackgroundColor(this.myColor1);
            this.cell.setFixedHeight(30.0f);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell(new Phrase("Date"));
            this.cell.setBackgroundColor(this.myColor1);
            this.cell.setFixedHeight(30.0f);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell(new Phrase("Credit Amt( + )"));
            this.cell.setBackgroundColor(this.myColor1);
            this.cell.setFixedHeight(30.0f);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell(new Phrase("Debit Amt( - )"));
            this.cell.setBackgroundColor(this.myColor1);
            this.cell.setFixedHeight(30.0f);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell(new Phrase("Note"));
            this.cell.setBackgroundColor(this.myColor1);
            this.cell.setFixedHeight(30.0f);
            pdfPTable3.addCell(this.cell);

            this.cell = new PdfPCell();
            this.cell.setColspan(6);
            Font font = new Font(BaseFont.createFont("assets/font/robotoregular.ttf", BaseFont.IDENTITY_H, true), 12.0f);
            while (i < this.UserIDList.size()) {
                PdfPCell pdfPCell5 = new PdfPCell(new Phrase((String) this.PartyNote.get(i), font));
                int i2 = i + 1;
                pdfPTable3.addCell(String.valueOf(i2));
                pdfPTable3.addCell((String) this.Paydate.get(i));
                pdfPTable3.addCell((String) this.CreditAmount.get(i));
                pdfPTable3.addCell((String) this.DebitAmount.get(i));
                pdfPTable3.addCell(pdfPCell5);
                i = i2;
            }
            PdfPTable pdfPTable4 = new PdfPTable(6);
            pdfPTable4.setWidthPercentage(100.0f);
            pdfPTable4.setWidths(new float[]{30.0f, 10.0f, 30.0f, 10.0f, 30.0f, 10.0f});

            this.cell = new PdfPCell((Phrase) new Paragraph("Balance Amount = "+S_Balance+"/-"));
            this.cell.setColspan(6);
            this.cell.setPadding(5.0f);
            this.cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            this.cell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable4.addCell(this.cell);
            this.cell = new PdfPCell();
            this.cell.setColspan(6);
            this.cell.addElement(pdfPTable4);
            pdfPTable3.addCell(this.cell);
            document.add(pdfPTable3);

            Toast.makeText(getApplicationContext(), "PDF created successfully..", Toast.LENGTH_LONG).show();
            document.close();
        } catch (DocumentException e) {
            document.close();
        } catch (IOException e2) {
            document.close();
        } catch (Exception e3) {
            e3.printStackTrace();
        } catch (Throwable th) {
            document.close();
            throw th;
        }
    }

    @SuppressLint({"WrongConstant"})
    private void GetPartyTransactionRecords() {
        Cursor GetPartyTransaction = this.f459db.GetPartyTransaction(Long.parseLong(this.UserID));
        if (GetPartyTransaction.moveToFirst()) {
            do {
                DisplayContact(GetPartyTransaction);
            } while (GetPartyTransaction.moveToNext());
        } else {
            Toast.makeText(getBaseContext(), "No Transaction Found!", 1).show();
        }
        this.f459db.close();
    }

    private void DisplayContact(Cursor cursor) {
        this.UserIDList.add(cursor.getString(0));
        this.DebitAmount.add(cursor.getString(1));
        this.CreditAmount.add(cursor.getString(2));
        this.PartyBalance.add(cursor.getString(3));
        this.Paydate.add(cursor.getString(4));
        this.PartyNote.add(cursor.getString(5));
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
