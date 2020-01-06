package com.example.pdfcreate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PDFDocumentAdapter extends PrintDocumentAdapter {

    Context context;
    String path;

    public PDFDocumentAdapter(Context context,String path){
        this.context=context;
        this.path=path;
    }
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        if (cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
        }
        else {
            PrintDocumentInfo.Builder builder=new PrintDocumentInfo.Builder("file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.CONTENT_TYPE_UNKNOWN).build()
            ;
            callback.onLayoutFinished(builder.build(),newAttributes.equals(oldAttributes));

        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

        InputStream inputStream=null;
        OutputStream outputStream=null;
        File file = new File(path+"etest_pdf.pdf");
        try {
            inputStream=new FileInputStream(file);
            outputStream= new FileOutputStream(destination.getFileDescriptor());
            byte[] buff=new byte[16334];
            int size;

            while ((size=inputStream.read(buff))>=0 && cancellationSignal.isCanceled()){
                outputStream.write(buff,0,size);
            }
            if (cancellationSignal.isCanceled()){
                callback.onWriteCancelled();
            }
            else {
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        } catch (Exception e) {
            callback.onWriteFailed(e.getMessage());
            Log.e("Evan",e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
                outputStream.close();

            }
             catch (IOException e) {
                 Log.e("Evan",e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
