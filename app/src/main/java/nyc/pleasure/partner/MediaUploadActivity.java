package nyc.pleasure.partner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import nyc.pleasure.partner.s3.UtilityS3;

public class MediaUploadActivity extends AppCompatActivity {

    public static final String LOG_TAG = MediaUploadActivity.class.getSimpleName();

    private AmazonS3Client s3Client;
    private String s3BucketName;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> transferRecordMaps;

    private Button btnUploadImg = null;
    private Button btnUploadVideo = null;

    private String URLS3 = "https://s3.amazonaws.com/";
    private String URLBucket = "helloworld-contentdelivery-mobilehub-1745560474/";
    private String URLFolder = "Folder456/";
    private String URLFileVideo = "VID_20160111_154049.mp4";
    private String URLFileImage = "IMG_20160111_153927.jpg";
//    private String URL = "https://s3.amazonaws.com/helloworld-contentdelivery-mobilehub-1745560474/Folder456/VID_20160111_154037.mp4";
    private Uri UriVideo = Uri.parse(URLS3 + URLBucket + URLFolder + URLFileVideo);
    private Uri UriImage = Uri.parse(URLS3 + URLBucket + URLFolder + URLFileImage);

    public ImageView imgViewProfile = null;

    public VideoView videoViewProfile = null;
    public MediaController mediaControls;
    public int currentPosition = 0;

    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;


/////////////////////////////////////////////////////////////////////////////////////
////    LIFECYCLE FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_upload);
        initData();
        initUI();
    }


    private void initData() {
        s3Client = UtilityS3.getS3Client(MediaUploadActivity.this);
        s3BucketName = getResources().getString(R.string.s3_bucket_name);
        transferRecordMaps = new ArrayList<HashMap<String, Object>>();

        simpleAdapter = new SimpleAdapter(this, transferRecordMaps, R.layout.list_item_image,
                new String[] {"key"}, new int[] {R.id.key});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                switch (view.getId()) {
                    case R.id.key:
                        Uri imgUri = Uri.parse(URLS3 + URLBucket + URLFolder + (String) data);
                        ImageView currentImg = (ImageView) view;
                        currentImg.setImageURI(imgUri);
                        return true;
                }

                return false;
            }
        });
//        setListAdapter(simpleAdapter);

        // Initializes TransferUtility, always do this before using it.
        transferUtility = UtilityS3.getTransferUtility(this);

    }

    private void initUI() {
        btnUploadImg = (Button) findViewById(R.id.btnUploadImg);

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUploadImg();
            }
        });

        btnUploadVideo = (Button) findViewById(R.id.btnUploadVideo);
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUploadVideo();
            }
        });

        //////////////////////////////////////////////////////////////////

        imgViewProfile = (ImageView) findViewById(R.id.image_view);
//        imgViewProfile.setImageBitmap(getBitmapFromUrl( URLS3 + URLBucket + URLFolder + URLFileImage ));
        new DownloadImageTask(imgViewProfile).execute(URLS3 + URLBucket + URLFolder + URLFileImage);

        //////////////////////////////////////////////////////////////////

        videoViewProfile = (VideoView) findViewById(R.id.video_view);
        mediaControls = new MediaController(MediaUploadActivity.this);
        videoViewProfile.setMediaController(mediaControls);
        videoViewProfile.setVideoURI(UriVideo);
        videoViewProfile.requestFocus();

        videoViewProfile.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoViewProfile.seekTo(currentPosition);
                videoViewProfile.pause();
            }
        });


    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// CLICK ACTION FUNCTIONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void doUploadImg() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // For Android versions of KitKat or later, we use a
            // different intent to ensure
            // we can get the file path from the returned intent URI
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void doUploadVideo() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // For Android versions of KitKat or later, we use a
            // different intent to ensure
            // we can get the file path from the returned intent URI
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("video/*");
        startActivityForResult(intent, 0);
    }

/////////////////////////////////////////////////////////////////////////////////////
////    CALLBACK FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            try {
                String path = getPath(uri);
                String userId = Utility.getLoggedInUserId(this);
                beginUpload(userId, path);
            } catch (URISyntaxException e) {
                Toast.makeText(this,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "Unable to upload file from the given uri", e);
            }
        }
    }


/////////////////////////////////////////////////////////////////////////////////////
////    HELPER FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {

                cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
                        //// URI of the table. Columns to return,  Selection critera column, Selection criteria value, Sort order for result.
                        ////
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Unable to build Cursor from ContentResolver", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }



    private void beginUpload(String userId, String filePath) {
        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload(getResources().getString(R.string.s3_bucket_name)
                , URLFolder + file.getName()
                , file);


/*
        observers.add(observer);
        HashMap<String, Object> map = new HashMap<String, Object>();
        Util.fillMap(map, observer, false);
        transferRecordMaps.add(map);
        observer.setTransferListener(new UploadListener());
        simpleAdapter.notifyDataSetChanged();
 */

    }



    private class GetFileListTask extends AsyncTask<Void, Void, Void> {
        // The list of objects we find in the S3 bucket
        private List<S3ObjectSummary> s3ObjList;
        // A dialog to let the user know we are retrieving the files
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MediaUploadActivity.this,
                    getString(R.string.refreshing),
                    getString(R.string.please_wait));
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            // Queries files in the bucket from S3.
            s3ObjList = s3Client.listObjects(s3BucketName).getObjectSummaries();
            transferRecordMaps.clear();
            for (S3ObjectSummary summary : s3ObjList) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("key", summary.getKey());
                transferRecordMaps.add(map);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            simpleAdapter.notifyDataSetChanged();
        }
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
