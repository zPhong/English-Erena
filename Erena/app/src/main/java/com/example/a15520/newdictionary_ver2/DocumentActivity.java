package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.IOException;
import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity {

    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

       pdfView = (PDFView)findViewById(R.id.pdfViewer);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Topic");
       pdfView.fromAsset(bundle.getString("Name") + ".pdf").defaultPage(0)
               .enableSwipe(true)
               .scrollHandle(new DefaultScrollHandle(this))
               .spacing(10).pageFitPolicy(FitPolicy.BOTH).load();;
    }

}
