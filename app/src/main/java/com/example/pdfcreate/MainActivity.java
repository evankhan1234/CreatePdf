package com.example.pdfcreate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.widget.ImageView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    ImageView cancel_action;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancel_action=findViewById(R.id.cancel_action);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("","Permission is granted");

            } else {

                Log.v("","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("", "Permission is granted");

        }
        CreatePdfFile(Common.getPath(MainActivity.this)+"etest_pdf.pdf");
            Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {

            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        });

            final PDFTest pdfTest= new PDFTest(MainActivity.this);
            Bengali bengali=new Bengali(MainActivity.this);

            pdfTest.getIntentPDF();
        final byte[] bFile = new byte[0];
            final Bitmap b=pdfTest.getBitmap();
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    String s=pdfTest.getFilePath();
                    try {
                        byte[] bFile = Files.readAllBytes(Paths.get(s));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap myBitmap = BitmapFactory.decodeFile(pdfTest.getFilePath());
                    Bitmap b = BitmapFactory.decodeByteArray(bFile , 0, bFile .length);
//                    cancel_action.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
                   // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

                 //   cancel_action.setImageBitmap(myBitmap);

                   // cancel_action.setIM(pdfTest.getBitmap());
                }
            });

    }

    private void CreatePdfFile(String path){

        if (new File(path).exists())
            new File(path).delete();
        Document document= new Document();
        try {
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Evan");
            document.addAuthor("Evan");
            float fontSize=20.0f;
            float valueFontSize=24.0f;
            addNewItem(document,"Order details", Element.ALIGN_CENTER);
            addNewItem(document,"Order No", Element.ALIGN_LEFT);
            addNewItem(document,"#717171", Element.ALIGN_LEFT);
            //addNewItem(document,"Order details", Element.ALIGN_CENTER);
           // BaseColor colorAccent= new BaseColor(0,0,0,0,0,0)
            addLineSeperator(document);
            document.close();
            getIntentPDF();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPdf() {
        try {
            PrintManager printManager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printDocumentAdapter = new PDFDocumentAdapter(MainActivity.this,Common.getPath(MainActivity.this));

            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Log.e("Evan",e.getMessage());
            e.printStackTrace();
        }
    }
    public Intent getIntentPDF() {

        File targetFile = new File(Common.getPath(MainActivity.this)+"etest_pdf.pdf");
        byte[] bytesArray = new byte[(int) targetFile.length()];
        Log.e("bytes","bytes"+bytesArray);

        Uri targetUri = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() + ".fileProvider", targetFile);

        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(targetUri, "application/pdf");

        return intent;
    }

    private void addLineSeperator(Document document) {
        LineSeparator lineSeparator=new LineSeparator();
        addLineSpace(document);
        try {
            document.add(new Chunk(lineSeparator));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        addLineSpace(document);
    }

    private void addLineSpace(Document document) {
        try {
            document.add(new Paragraph(""));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void addNewItem(Document document, String order_details, int alignCenter) {
        Chunk chunk= new Chunk(order_details);
        Paragraph paragraph= new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
