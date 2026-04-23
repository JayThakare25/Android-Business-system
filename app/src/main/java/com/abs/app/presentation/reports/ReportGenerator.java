package com.abs.app.presentation.reports;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportGenerator {
    
    public static File generatePdfReport(Context context, String reportTitle, String bodyText) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // Standard A4
        PdfDocument.Page page = document.startPage(pageInfo);
        
        Canvas canvas = page.getCanvas();
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#006497"));
        titlePaint.setTextSize(24f);
        titlePaint.setFakeBoldText(true);
        
        Paint bodyPaint = new Paint();
        bodyPaint.setColor(Color.BLACK);
        bodyPaint.setTextSize(14f);

        canvas.drawText(reportTitle, 50, 50, titlePaint);
        
        int yAxis = 100;
        for (String line : bodyText.split("\n")) {
            canvas.drawText(line, 50, yAxis, bodyPaint);
            yAxis += 20;
        }

        document.finishPage(page);

        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Reports");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "SummaryReport_" + System.currentTimeMillis() + ".pdf");
        
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        return file;
    }
}
