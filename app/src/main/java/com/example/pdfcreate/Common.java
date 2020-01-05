package com.example.pdfcreate;

import android.content.Context;
import android.content.IntentFilter;

import java.io.File;

public class Common {
    public static String getPath(Context context){
        File dir=new File(android.os.Environment.getExternalStorageDirectory()+File.separator+context.getResources().getString(R.string.app_name)+File.separator);


        if (!dir.exists())
            dir.mkdir();
        return dir.getPath()+File.separator;
    }
}
