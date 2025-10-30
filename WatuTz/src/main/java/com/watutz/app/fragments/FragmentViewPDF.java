package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;

import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.ViewPDFActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.ypyproductions.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FragmentViewPDF extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static ViewPDFActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    PDFView pdfView;
    private File pdfFile;
    Uri pdfUri;

    ImageButton btnDownload,btnShare;
    boolean isDirectDownload=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_view_pdf, container, false);
        parentActivity = (ViewPDFActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        pdfView = parentView.findViewById(R.id.pdfView);
        btnDownload =parentView.findViewById(R.id.btnDownload);
        btnShare= parentView.findViewById(R.id.btnShare);

        String base64 = parentActivity.pdfString;
        String pdfPath = parentActivity.pdfFilePath;



        try {

            Intent mIntent = requireActivity().getIntent();
            if (mIntent == null) {
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument) + "ERR:400", Toast.LENGTH_SHORT).show();
                parentActivity.goBack();
            }

            // 1) Preferred: a URI set by setData(uri)
            Uri uri = mIntent.getData();

            if (uri != null) {


                btnDownload.setOnClickListener(v -> downloadPdfUri());
                btnShare.setOnClickListener(v -> sharePdfUri());
                isDirectDownload = true;

                openFromUri(uri);
            } else
            {


                if (!StringUtils.isStringEmpty(base64)) {


                    displayPdf(base64);

                    btnDownload.setOnClickListener(v -> downloadPdf());
                    btnShare.setOnClickListener(v -> sharePdf());


                } else if (!StringUtils.isStringEmpty(pdfPath)) {

                    btnDownload.setOnClickListener(v -> downloadPdfUri());
                    btnShare.setOnClickListener(v -> sharePdfUri());


                    uri = Uri.parse(pdfPath);

                    if (!StringUtils.isStringEmpty(uri.toString())) {
                        isDirectDownload = true;
                        showPdfFromUri(uri);
                    } else {
                        Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument) + "ERR:400", Toast.LENGTH_SHORT).show();
                        parentActivity.goBack();
                    }
                } else {
                    Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument) + "ERR:500", Toast.LENGTH_SHORT).show();
                    parentActivity.goBack();
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument) + "ERR:600", Toast.LENGTH_SHORT).show();
            parentActivity.goBack();
        }


        return parentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                GeneralCommands.logEvent(parentActivity,"view_page","view_page");
                // TODO stop audio playback
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    public void goToPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

    private void displayPdf(String base64Content) {
        try {
            byte[] pdfBytes = Base64.decode(base64Content, Base64.DEFAULT);

            pdfFile= new File(parentActivity.getCacheDir(), "Downloaded_" + System.currentTimeMillis()+".pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(pdfBytes);
            fos.close();

            pdfView.fromFile(pdfFile)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPdfFromUri(Uri uri) {
        try {
            Log.d("ZePath", "Trying to load URI: " + uri.toString());

            // Save URI for future use
            pdfUri = uri;

            // Create a temp file to use for pdfFile
            pdfFile = new File(parentActivity.getCacheDir(), "Downloaded_" + System.currentTimeMillis() + ".pdf");

            // Copy input stream from the URI to the file
            InputStream inputStream = parentActivity.getContentResolver().openInputStream(uri);
            if (inputStream == null) throw new IOException("InputStream is null for URI: " + uri);

            OutputStream outputStream = new FileOutputStream(pdfFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();

            // Now display the PDF from the local file
            pdfView.fromFile(pdfFile)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .load();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument) + " ERR:408", Toast.LENGTH_SHORT).show();
            parentActivity.goBack();
        }
    }


    private void downloadPdfUri() {
        try {
            if (pdfUri == null) {
                Toast.makeText(parentActivity, "PDF URI not available", Toast.LENGTH_SHORT).show();
                return;
            }

            InputStream inputStream = parentActivity.getContentResolver().openInputStream(pdfUri);
            if (inputStream == null) {
                throw new IOException("Unable to open InputStream from URI");
            }

            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File destFile = new File(downloadDir, "downloaded_" + System.currentTimeMillis() + ".pdf");

            FileOutputStream outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            MediaScannerConnection.scanFile(parentActivity, new String[]{destFile.getAbsolutePath()}, null, null);
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.pdfsavedtodownloads), Toast.LENGTH_LONG).show();

            if(isDirectDownload)
            {
//               parentActivity.goBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.downloadfailed), Toast.LENGTH_SHORT).show();
        }
    }


    private void sharePdfUri() {
        try {
            if (pdfUri == null) {
                Toast.makeText(parentActivity, "PDF URI not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, mContext.getResources().getString(R.string.sharefilevia)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotsharedocument), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadPdf() {
        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File destFile = new File(downloadDir, "downloaded_" + System.currentTimeMillis() + ".pdf");

            FileInputStream fis = new FileInputStream(pdfFile);
            FileOutputStream fos = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fis.close();
            fos.close();

            MediaScannerConnection.scanFile(parentActivity, new String[]{destFile.getAbsolutePath()}, null, null);
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.pdfsavedtodownloads), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.downloadfailed), Toast.LENGTH_SHORT).show();
        }
    }

    private void sharePdf() {
        try {
            Uri pdfUri = FileProvider.getUriForFile(
                    parentActivity,
                    parentActivity.getPackageName() + ".fileprovider",
                    pdfFile
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, mContext.getResources().getString(R.string.sharefilevia)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotsharedocument), Toast.LENGTH_SHORT).show();
        }
    }


    private void displayPdfFromFile(File file) {


        pdfFile= new File(parentActivity.getCacheDir(), "Downloaded_" + System.currentTimeMillis()+".pdf");




        try {
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.couldnotdisplaydocument), Toast.LENGTH_SHORT).show();
        }
    }


    /** Use AndroidPdfViewer directly from a content:// or file:// URI. */
    private void openFromUri(@NonNull Uri uri) {
        // If uri came from ACTION_OPEN_DOCUMENT you may want to persist:
        // final int flags = requireActivity().getIntent().getFlags()
        //        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // try { requireContext().getContentResolver().takePersistableUriPermission(uri, flags); } catch (Exception ignore) {}

        // Save URI for future use
        pdfUri = uri;

        pdfView.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();
    }


}
