package com.example.pdfcreate;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                CreatePdfFile(Common.getPath(MainActivity.this)+"test_pdf.pdf");
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

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
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


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
