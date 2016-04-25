package sinhaguild.protoone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final String TAG = "MyActivity";
    int inputValueFromApp = 0;

    int REQUEST_CAMERA = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        TextView changeFontHeader = (TextView) findViewById(R.id.welcome);
        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/BebasNeue Regular.ttf");
        changeFontHeader.setTypeface(robotoFont);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int total = inputValueFromApp * 10;
        EditText edtText = (EditText) findViewById(R.id.editText);
        CheckBox box = (CheckBox) findViewById(R.id.checkBox);
        Boolean checked = box.isChecked();
        if (checked) {
            total *= 100;
        }
        String msg = "Total Value is $" + total + " \n" + checked + " \n" + edtText.getText();
        /**
         * Publish boolean state to Toast
         */
        Toast.makeText(this, checked.toString(), Toast.LENGTH_SHORT).show();
        displayStringMessage(msg);
    }

    /**
     * Image Capture and Display functions
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView imageView = (ImageView) findViewById(R.id.selectPhotoIV);
                imageView.setImageBitmap(thumbnail);

            }
        }
        Log.v(TAG,"Failed");
    }

    public void selectImage(){
        Intent selectImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(selectImageIntent, REQUEST_CAMERA);
        if (selectImageIntent.resolveActivity(getPackageManager())!= null){
            onActivityResult(REQUEST_CAMERA,RESULT_OK, selectImageIntent);
            displayStringMessage("Done");
        }
    }


    /**
         * increment/decrement functions
         * @param view
         */

    public void incrementOne(View view){
        if (inputValueFromApp<=100){
            inputValueFromApp+=1;
            Log.v(TAG,"Added");
            display(inputValueFromApp);
        }
    }

    public void decrementOne(View view){
        if (inputValueFromApp>0){
            inputValueFromApp -= 1;
            Log.v(TAG,"Removed");
            display(inputValueFromApp);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.inputValue);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.outputValue);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    private void displayStringMessage(String msg){
        TextView appendedText = (TextView) findViewById(R.id.outputValue);
        appendedText.setText(msg);
    }


    /**
     * ONSTART AND ONSTOP
     */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://sinhaguild.protoone/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://sinhaguild.protoone/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}