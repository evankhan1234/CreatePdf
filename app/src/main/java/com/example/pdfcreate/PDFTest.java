package com.example.pdfcreate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.util.Property;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.itextpdf.text.html.HtmlTags.FONT;
import static com.itextpdf.text.pdf.PdfName.DEST;
import static java.awt.font.NumericShaper.BENGALI;
import static java.security.AccessController.getContext;
import static java.util.Locale.CHINESE;

public class PDFTest {
    private Document mDocument;
    boolean boolean_permission;
    private Context mContext;
    PdfContentByte cb;
    Font f;
    public static int REQUEST_PERMISSIONS = 1;
    PdfWriter writer;
    public PDFTest(Context context)
    {
        mContext = context;

        fn_permission();
        Rectangle one = new Rectangle(270,270);
        mDocument = new Document(one, 36, 36, 36, 36);
        try {
             writer = PdfWriter.getInstance(mDocument,
                    new FileOutputStream(getFilePath()));
            writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
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
          //  PdfFont fq   = PdfFontFactory.createFont("assets/SutonnyMJ_1.ttf", PdfEncodings.IDENTITY_H);

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
            df();
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
    public  void df(){
        try {
            FontFactory.registerDirectories();
            Rectangle pagesize = new Rectangle(8.5f * 72, 11 * 72);
            Document document = new Document(pagesize, 72, 72, 72, 72);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(getFilePathe()));
            document.open();
            Font font = FontFactory.getFont("Simplified Arabic", BaseFont.IDENTITY_H, true, 22, Font.BOLD);
            ColumnText column = new ColumnText(writer.getDirectContent());
            column.setSimpleColumn(36, 770, 569, 36);
            column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column.addElement(new Paragraph("لوحة المفاتيح العربي", font));
            column.go();
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generatePDF() throws DocumentException, IOException {
        final String[] sources = {"english.xml", "arabic.xml", "hindi.xml", "tamil.xml"};
        Font font = null;
//        try {
//            font = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        }
        String str = "০০৪";
        byte[] charset = str.getBytes("UTF-16");
        String result = new String(charset, "UTF-16");
        PdfPTable headerTable = getEmptyTable(new float[]{1});
        headerTable.getDefaultCell().setPadding(2);
        headerTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        String BILL = "";

        StringBuilder b = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (c >= 128)
                b.append("\\u").append(String.format("%04X", (int) c));
            else
                b.append(c);
        }



       BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "পরিমাণ", "হার", "মোট",BENGALI);
        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "dfd", "g", "g", "মোট",BENGALI);

        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", result);
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

        BILL = BILL
                + "\n-----------------------------------------------";
         headerTable.addCell(getPara(BILL, 14));
         char c='5';
        String s = Character.toString((char)c);
        String valz="নকশী কাঁথা";
        final char[] chars = Character.toChars(0x1F701);
        final String s1 = new String(chars);
        final byte[] asBytes = s1.getBytes(StandardCharsets.UTF_8);
       // String message = new String(asBytes , "");
        Charset utf8 = Charset.forName("UTF-8");
        Charset def = Charset.defaultCharset();
       // cb.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        String charToPrint = "\\u09ae\\u0997 \\u09ac\\u0995\\u09cd\\u09b8-\\u09e8\\u09e6\\u09e7\\u09ef";

        byte[] bytes = charToPrint.getBytes("UTF-8");
        String message = new String(bytes , def.name());

        //    String  str = new String(bytes, Charset.forName("UTF-8"));

       // System.out.println(str);
        Phrase p = new Phrase("This is incorrect: ");
        p.add(new Chunk("নকশী", f));
        p.add(new Chunk("001",f));
        PdfPCell pdfCell = new PdfPCell(new Phrase(message));
        pdfCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        mDocument.add(pdfCell);
        Files.write(Paths.get(getFilePathevan()), BILL.getBytes());
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
        ss();

        mDocument.add(p);
        ColumnText canvas = new ColumnText(writer.getDirectContent());
        canvas.setSimpleColumn(36, 750, 559, 780);
        canvas.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        canvas.addElement(p);
        canvas.go();
        Bitmap bitmap=StringToBitMap("নকশী");
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
    public  void ss(){
        String BILL = "";

        BILL =    "                  "+ "তাজিন লাইব্রেরী " +" \n \n"+

                "                   ক্রেতা         \n \n"+
                "Invoice Number : ১১১১১২২১১১১১\n"+
                "Date : dfgdfg\n"+

                "Customer Name: ইভান \n"+
                "Phone Number: ১১১১১২২১১১১১\n"+
                "\n";
        BILL = BILL
                + "-----------------------------------------------\n";

      //  BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "বইয়ের নাম", "পরিমাণ", "হার", "মোট",BENGALI);

        BILL = BILL + String.format("%-22s%-10s%-10s%-10s", "বইয়ের নাম", "পরিমাণ", "হার", "মোট");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------";

        for (int i=0;i<5;i++){


            BILL = BILL + "\n" + String.format("%-22s%-9s%-9s%-10s", "মগ বক্স-২০১৯"+i, "১"+i, "৪"+i, "৩"+i);
        }


        BILL = BILL
                + "-----------------------------------------------";
        BILL = BILL + "\n";

        BILL = BILL + "                   Sub Total    :" + "  " + "50" + " Tk"+"\n";
        BILL = BILL + "                   Discount     :" + "  " + "50" +" %"+ "\n";
        BILL = BILL + "                   Return       :" + "  " + "50"+ " Tk"+"\n";
        BILL = BILL + "                   Total Amount :" + " " + "50" + " Tk"+"\n";
        BILL = BILL + "                   Payment Via  :" + " " + "50" + ""+"\n";

        BILL = BILL
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n";
        drawText(BILL,12);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap StringToBitMap(String encodedString){
//        Base64.Encoder encoder = Base64.getEncoder();
//        String str = encoder.encodeToString(encodedString.getBytes());
//        Log.e("Encoded string: ",str);
//
//        Base64.Decoder decoder = Base64.getDecoder();
//        // Decoding string
//        String dStr = new String(decoder.decode(str));
        byte[] data = new byte[0];
        try {
            data = encodedString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        byte[] encodeBytesd = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeByteArray(encodeBytesd, 0, encodeBytesd.length, options);

       // image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        Bitmap v=stringToBitmap(base64);
        String imageDataBytes = "";
//        if (base64 != null)
//        {
//            if (base64 != null) {
//                if (base64.length() > 0) {
//                    imageDataBytes = base64.substring(base64.indexOf(",") + 1);
//                    if (imageDataBytes.length() > 0) {
//                        if (IsBase64Encoded(imageDataBytes)) {
//                          //  holder.a.setImageBitmap(stringToBitmap(imageDataBytes));
//                        }}}}}

        Log.e("Ecode string: ",base64);

// Receiving side
        byte[] data1 = Base64.decode(base64, Base64.DEFAULT);
        try {
            String text = new String(data, "UTF-8");
            Log.e("Decoded string: ",text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

      //  e.getMessage();

            return null;
        }
    public void drawText(String text, int textSize) {


        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint,
                370, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(370, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        //printBitmap(b);
    }
    public static Bitmap stringToBitmap(String fileString){
        byte[] valueDecoded = Base64.decode(fileString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(valueDecoded, 0,
                valueDecoded.length);
    }
    private boolean IsBase64Encoded(String value) {
        try {
            byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return true;
        } catch (Exception e) {
            return false;
        }
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
    public String getFilePathe() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Evam";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + "e.pdf";
    }
    public String getFilePathevan() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Evam";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + "evans.txt";
    }
}
