package com.example.pdfcreate;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;


import org.w3c.dom.Node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Bengali  {
    PdfFont f;
    PdfFont f1;
    public static final String ARABIC
            = "\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a";
    public static final String DEST
            = "./target/test/resources/sandbox/fonts/arabic_example.pdf";
    public static final String FONT
            = "app/src/main/assets/SutonnyMJ_1.ttf";
     FontSet set;
    Context context;
    public Bengali(Context contexts)  {
        context=contexts;
        try {
            AssetManager am = contexts.getAssets();
            try (InputStream inStream = am.open("SutonnyMJ_1.ttf")) {
             //   byte[] fontData = IOUtils.toByteArray(inStream);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                byte[] byteArray = buffer.toByteArray();
              //  f = PdfFontFactory.createFont(StandardFonts., PdfEncodings.IDENTITY_H);
                f = PdfFontFactory.createFont(byteArray, PdfEncodings.IDENTITY_H);
                f1 = PdfFontFactory.createFont(byteArray, PdfEncodings.IDENTITY_H, true);
            }
              //  f = PdfFontFactory.createFont(AssetManager, PdfEncodings.IDENTITY_H);
            //f   = PdfFontFactory.createFont("assets/SutonnyMJ_1.ttf", PdfEncodings.IDENTITY_H);
            manipulatePdf();
//            set = new FontSet();
//            set.addFont("assets/SutonnyMJ_1.ttf");
//            set.addFont("fonts/NotoSansTamil-Regular.ttf");
//            set.addFont("fonts/FreeSans.ttf");
           // document.setFontProvider(new FontProvider(set));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  new ArabicExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf() throws Exception {
        //Load the license file to use advanced typography features
    //    LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(getFilePath()));
        Document doc = new Document(pdfDoc);



        Paragraph p = new Paragraph("This is auto detection: ");
//        p.add(new Text("মোট"));
//        p.add(new Text(": 50.00 USD"));
//        doc.add(p);


        String s="\u09ec.\u09b8\u09c1\u09aa\u09be\u09b0\u09ae\u09cd\u09af\u09be\u09a8-(\u09e8\u09df \u0996\u09a8\u09cd\u09a1";

        String a= URLEncoder.encode("নাম", "UTF-8");
        String aa=convertStringToUTF8(a);
        byte[] bytes = a.getBytes("UTF-8");
        String message = new String(bytes);
        p = new Paragraph("হার").setBaseDirection(BaseDirection.LEFT_TO_RIGHT);
       // p.add(new Text("\\u09ec.\\u09b8\\u09c1\\u09aa\\u09be\\u09b0\\u09ae\\u09cd\\u09af\\u09be\\u09a8-(\\u09e8\\u09df \\u0996\\u09a8\\u09cd\\u09a1").setFont(f));
                p.add(new Text("\u09ec").setFont(f));
    //    p.add(new Text("\u09ec.\u09b8\u09c1\u09aa\u09be\u09b0\u09ae\u09cd\u09af\u09be\u09a8-(\u09e8\u09df \u0996\u09a8\u09cd\u09a1"));
        doc.add(p);


        doc.close();
    }
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    public String getFilePath() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Evam";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + "SS.pdf";
    }
    public  void test() throws IOException {
        final String[] sources = {"english.xml", "arabic.xml", "hindi.xml", "tamil.xml"};
        final PdfWriter writer = new PdfWriter(getFilePath());
        final PdfDocument pdfDocument = new PdfDocument(writer);
        final Document document = new Document(pdfDocument);

        set.addFont("fonts/NotoNaskhArabic-Regular.ttf");
        set.addFont("fonts/NotoSansTamil-Regular.ttf");
        set.addFont("fonts/FreeSans.ttf");
        document.setFontProvider(new FontProvider(set));
        document.setProperty(Property.FONT, new String[]{"MyFontFamilyName"});
//        for (final String source : sources) {
//            final File xmlFile = new File(source);
//            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            final DocumentBuilder builder = factory.newDocumentBuilder();
//            final org.w3c.dom.Document doc = builder.parse(xmlFile);
//            final Node element = doc.getElementsByTagName("text").item(0);
//            final Paragraph paragraph = new Paragraph();
//            final Node textDirectionElement = element.getAttributes().getNamedItem("direction");
//            boolean rtl = textDirectionElement != null && textDirectionElement.getTextContent()
//                    .equalsIgnoreCase("rtl");
//            if (rtl) {
//                paragraph.setTextAlignment(TextAlignment.RIGHT);
//            }
//            paragraph.add("");
//            document.add(paragraph);
//        }
        final Paragraph paragraph = new Paragraph();
       // final Node textDirectionElement = element.getAttributes().getNamedItem("direction");
       // boolean rtl = textDirectionElement != null && textDirectionElement.getTextContent()
              //  .equalsIgnoreCase("rtl");
      //  if (rtl) {
            paragraph.setTextAlignment(TextAlignment.RIGHT);
       // }
        paragraph.add("হার");
        document.add(paragraph);
        document.close();
        pdfDocument.close();
        writer.close();
    }
}