package com.example.pdfcreate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;


import static com.itextpdf.text.html.HtmlTags.FONT;
import static java.awt.font.NumericShaper.BENGALI;
import static java.security.AccessController.getContext;
import static java.util.Locale.CHINESE;

public class PDFTest {
    private Document mDocument;
    boolean boolean_permission;
    private Context mContext;
    Font f;
    public static int REQUEST_PERMISSIONS = 1;

    public PDFTest(Context context) {
        mContext = context;
        fn_permission();
        Rectangle one = new Rectangle(270,270);
        mDocument = new Document(one, 36, 36, 36, 36);
        try {
            PdfWriter writer = PdfWriter.getInstance(mDocument,
                    new FileOutputStream(getFilePath()));

            Rectangle rect = new Rectangle(30, 30, 450, 800);
            writer.setBoxSize("art", rect);
         //   HeaderFooterPageEvent event = new HeaderFooterPageEvent();
         //   writer.setPageEvent(event);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mDocument.open();
        BaseFont bf = null;
        try {
            //Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 18.0f, Font.BOLD, BaseColor.BLACK);

            bf = BaseFont.createFont("assets/SutonnyMJ_1.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//             bf = BaseFont.createFont(
//                     line, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
             f = new Font(bf, 12);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mDocument.add(new Chunk(""));
//            final FontSet set = new FontSet();
//            set.addFont("fonts/NotoSansTamil-Regular.ttf");
//            mDocument.set(new FontProvider(set));

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {


            generatePDF();
        } catch (NullPointerException | DocumentException e) {
            Log.e("EX", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDocument.close();
    }

    public byte[]  getData(){
        fn_permission();
        File targetFile = new File(getFilePath());

        byte[] bytesArray = new byte[(int) targetFile.length()];
        return bytesArray;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Intent getIntentPDF() {
        fn_permission();
        File targetFile = new File(getFilePath());
        byte[] bytesArray = new byte[(int) targetFile.length()];
        Log.e("bytes","bytes"+bytesArray);

        Bitmap bitmap=getBitmap();
        Uri targetUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileProvider", targetFile);

        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(targetUri, "application/pdf");

        return intent;
    }

    public String getSharedPrefDateFrom() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userName = sharedPref.getString("DateFrom", null);
        return userName;
    }

    public String getSharedPrefDateTo() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userName = sharedPref.getString("DateTo", null);
        return userName;
    }

    private String Name() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userName = sharedPref.getString("Name", null);
        return userName;
    }

    private void generatePDF() throws DocumentException, IOException {
        final String[] sources = {"english.xml", "arabic.xml", "hindi.xml", "tamil.xml"};
        Font font = null;
//        try {
//            font = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        }
        PdfPTable headerTable = getEmptyTable(new float[]{1});
        headerTable.getDefaultCell().setPadding(2);
        headerTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        String BILL = "";

       BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "পরিমাণ", "হার", "মোট",BENGALI);
        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "dfd", "g", "g", "মোট",BENGALI);

        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

        BILL = BILL
                + "\n-----------------------------------------------";
     //   headerTable.addCell(getPara(BILL, 14));
        Paragraph p = new Paragraph("poriman", f);
      //  mDocument.add(new Paragraph(String.format("CP1250: %s", BENGALI), 14));
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getParaS("Rupayan Centre (8th Floor), 72, Mohakhali C/A, Bir Uttam AK Khandakar Road ",12));
//        headerTable.addCell(getParaS("Dhaka 1212, Bangladesh ",12));
//
//        headerTable.addCell(getParaS("Phone: 9856358, Fax: 88-02-9855949", 12));
//        headerTable.addCell(getParaS("Email: saifop@bdmail.net", 12));
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getPara(" Division Name:- "+getSharedPrefDataDivisionName(), 12));
//
//
//
//        headerTable.addCell(getPara(" "));
//     //   headerTable.addCell(getPara(" Designation Name:- "+pdfData.employeeTimeKeeping.get(0).Designation, 12));
//
//        headerTable.addCell(getPara(" "));
//        headerTable.addCell(getParaColor("From :-  "+getSharedPrefDateFrom()+" To :- "+getSharedPrefDateTo(), 12));
//
//        Paragraph reportHeading = getFormatReportHeading("Employee-wise Time Keeping Report");
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//    //    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.);
//     //   bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
//  //      Image myImg = Image.getInstance(stream.toByteArray());
//
//
//
//        PdfPTable logoTable = getEmptyTable(new float[]{1, 1, 1, 1, 1});
//        logoTable.getDefaultCell().setPadding(0);
//
//        logoTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//        logoTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
//      //  logoTable.addCell(myImg);
//        logoTable.addCell(getPara(" "));
//        logoTable.addCell(getPara(" "));
//        logoTable.addCell(getPara(" "));
//
//        logoTable.addCell(getPara(" "));
//        mDocument.add(logoTable);
//
//        mDocument.add(getPara(" "));

        mDocument.add(p);
        mDocument.add(headerTable);
      //  mDocument.add(Chunk.NEWLINE);
      //  mDocument.add(reportHeading);
      //  mDocument.add(Chunk.NEWLINE);

     //   mDocument.add(table);

      //  mDocument.add(getPara(" "));

       // mDocument.add(getReport(" Report Summary", 14));
      //  mDocument.add(getPara(" "));
      //  mDocument.add(getPara(" "));
     //   mDocument.add(table1);


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getBitmap(){
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(mContext);
        try {
            byte[] bFile = Files.readAllBytes(Paths.get(getFilePath()));
            PdfDocument pdfDocument = pdfiumCore.newDocument(bFile);
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private String getSharedPrefDataDivisionName() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String DivisionName = sharedPref.getString("DivisionName", null);
        return DivisionName;
    }
    private Paragraph getFormatReportHeading(String content) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }


    private Paragraph getPara(String content) {
        Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
        Paragraph p = new Paragraph(content, font);

        return p;
    }


    private Paragraph getReport(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }


    private Paragraph getPara(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaColor(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaColorName(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.BLUE);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaS(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        return p;
    }

    private Paragraph getParaReportFormat(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD);
        Paragraph p = new Paragraph(content, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }


    private PdfPTable getEmptyTable(float[] col) {
        PdfPTable table = new PdfPTable(col);
        table.getDefaultCell().setBorderColor(BaseColor.BLACK);
        table.getDefaultCell().setPadding(3);
        table.setWidthPercentage(100);

        return table;
    }
    private Paragraph getRedColorText(String content, int fontSize) {
        Font font = new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD, BaseColor.RED);
        Paragraph p = new Paragraph(content, font);
        return p;
    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(mContext.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(mContext.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }

    public String getFilePath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Evam";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + "scd.pdf";
    }
}
