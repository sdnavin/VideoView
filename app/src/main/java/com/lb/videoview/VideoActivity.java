package com.lb.videoview;

import android.net.Uri;
import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;


import android.content.Intent;
import android.content.IntentFilter;


import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.acs.smartcard.Features;
import com.acs.smartcard.PinModify;
import com.acs.smartcard.PinProperties;
import com.acs.smartcard.PinVerify;
import com.acs.smartcard.ReadKeyOption;
import com.acs.smartcard.Reader;
import com.acs.smartcard.Reader.OnStateChangeListener;
import com.acs.smartcard.TlvProperties;

import org.w3c.dom.Text;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoActivity extends Activity {

    /**
     * Video Init
     */
    private VideoView videoView;


    private FrameLayout mainLayout;
    private FrameLayout fbLayout;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private static final String[] powerActionStrings = { "Power Down",
            "Cold Reset", "Warm Reset" };

    private static final String[] stateStrings = { "Unknown", "Absent",
            "Present", "Swallowed", "Powered", "Negotiable", "Specific" };

    private static final String[] featureStrings = { "FEATURE_UNKNOWN",
            "FEATURE_VERIFY_PIN_START", "FEATURE_VERIFY_PIN_FINISH",
            "FEATURE_MODIFY_PIN_START", "FEATURE_MODIFY_PIN_FINISH",
            "FEATURE_GET_KEY_PRESSED", "FEATURE_VERIFY_PIN_DIRECT",
            "FEATURE_MODIFY_PIN_DIRECT", "FEATURE_MCT_READER_DIRECT",
            "FEATURE_MCT_UNIVERSAL", "FEATURE_IFD_PIN_PROPERTIES",
            "FEATURE_ABORT", "FEATURE_SET_SPE_MESSAGE",
            "FEATURE_VERIFY_PIN_DIRECT_APP_ID",
            "FEATURE_MODIFY_PIN_DIRECT_APP_ID", "FEATURE_WRITE_DISPLAY",
            "FEATURE_GET_KEY", "FEATURE_IFD_DISPLAY_PROPERTIES",
            "FEATURE_GET_TLV_PROPERTIES", "FEATURE_CCID_ESC_COMMAND" };

    private static final String[] propertyStrings = { "Unknown", "wLcdLayout",
            "bEntryValidationCondition", "bTimeOut2", "wLcdMaxCharacters",
            "wLcdMaxLines", "bMinPINSize", "bMaxPINSize", "sFirmwareID",
            "bPPDUSupport", "dwMaxAPDUDataSize", "wIdVendor", "wIdProduct" };

    private static final int DIALOG_VERIFY_PIN_ID = 0;
    private static final int DIALOG_MODIFY_PIN_ID = 1;
    private static final int DIALOG_READ_KEY_ID = 2;
    private static final int DIALOG_DISPLAY_LCD_MESSAGE_ID = 3;



    private ArrayAdapter<String> mReaderAdapter;

    private UsbManager mManager;
    private Reader mReader;
    private PendingIntent mPermissionIntent;


    private boolean canPlay;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {

                synchronized (this) {

                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                        if (device != null) {

                            // Open reader
//                            textView.setText("Opening reader: " + device.getDeviceName()
//                                    + "...");
                            new OpenTask().execute(device);
                        }

                    } else {

//                        textView.setText("Permission denied for device "
//                                + device.getDeviceName());

                        // Enable open button
//                        mOpenButton.setEnabled(true);
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                synchronized (this) {

                    // Update reader list
                    mReaderAdapter.clear();
                    for (UsbDevice device : mManager.getDeviceList().values()) {
                        if (mReader.isSupported(device)) {
                            mReaderAdapter.add(device.getDeviceName());
                        }
                    }

                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null && device.equals(mReader.getDevice())) {

                        // Disable buttons

                        // Clear slot items

                        // Close reader
//                        logMsg("Closing reader...");
                        new CloseTask().execute();
                    }
                }
            }
        }
    };


    private class OpenTask extends AsyncTask<UsbDevice, Void, Exception> {

        @Override
        protected Exception doInBackground(UsbDevice... params) {

            Exception result = null;

            try {

                mReader.open(params[0]);

            } catch (Exception e) {

                result = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {

            if (result != null) {

//                    textView.setText(result.toString());

            } else {

//                textView.setText("Reader name: " + mReader.getReaderName());

                int numSlots = mReader.getNumSlots();
//                textView.setText("Number of slots: " + numSlots);
//                textView.setText("s : "+numSlots);
                if (numSlots > 0) {
                    for (UsbDevice device : mManager.getDeviceList().values()) {

                        {
                            // Request permission
                            mManager.requestPermission(device,
                                    mPermissionIntent);
                            break;
                        }
                    }
                }

                // Remove all control codes
//                mFeatures.clear();
//
//                // Enable buttons
//                mCloseButton.setEnabled(true);
//                mSlotSpinner.setEnabled(true);
//                mGetStateButton.setEnabled(true);
//                mPowerSpinner.setEnabled(true);
//                mPowerButton.setEnabled(true);
//                mGetAtrButton.setEnabled(true);
//                mT0CheckBox.setEnabled(true);
//                mT1CheckBox.setEnabled(true);
//                mSetProtocolButton.setEnabled(true);
//                mGetProtocolButton.setEnabled(true);
//                mTransmitButton.setEnabled(true);
//                mControlButton.setEnabled(true);
//                mGetFeaturesButton.setEnabled(true);
//                mReadKeyButton.setEnabled(true);
//                mDisplayLcdMessageButton.setEnabled(true);
            }
        }
    }

    private class CloseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mReader.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            mOpenButton.setEnabled(true);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        canPlay=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
//        textView=findViewById(R.id.textView);
        videoView=findViewById(R.id.videoView);
        // Get USB manager
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // Initialize reader
        mReader = new Reader(mManager);
        mReader.setOnStateChangeListener(new OnStateChangeListener() {

            @Override
            public void onStateChange(int slotNum, int prevState, int currState) {

                if (prevState < Reader.CARD_UNKNOWN
                        || prevState > Reader.CARD_SPECIFIC) {
                    prevState = Reader.CARD_UNKNOWN;
                }

                if (currState < Reader.CARD_UNKNOWN
                        || currState > Reader.CARD_SPECIFIC) {
                    currState = Reader.CARD_UNKNOWN;
                }

                // Create output string
                final String outputString = "Slot " + slotNum + ": "
                        + stateStrings[prevState] + " -> "
                        + stateStrings[currState];


                if(stateStrings[currState]==stateStrings[2]){
                    canPlay=true;
                }


                // Show output
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        textView.setText(outputString);
                      if(canPlay) {
                          playVideo();
                          canPlay=false;
                      }
                    }
                });



            }
        });


//Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        // Register receiver for USB permission
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);
//        playVideo();

        // Initialize reader spinner
        mReaderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (UsbDevice device : mManager.getDeviceList().values()) {
            if (mReader.isSupported(device)) {
                mReaderAdapter.add(device.getDeviceName());
            }
        }

        for (UsbDevice device : mManager.getDeviceList().values()) {

            // If device name is found
//            if (deviceName.equals(device.getDeviceName())) {

                // Request permission
                mManager.requestPermission(device,
                        mPermissionIntent);

//                requested = true;
//                break;
            }
    }

    @Override
    protected void onDestroy() {

        // Close reader
        mReader.close();

        // Unregister receiver
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }



    public void playVideo() {
        MediaController m = new MediaController(this);
        videoView.setMediaController(m);
        String path = "android.resource://com.lb.videoview/"+ R.raw.marvel;

        Uri u = Uri.parse(path);

        videoView.setVideoURI(u);
        videoView.seekTo(0);
        videoView.start();
    }
    public void stopVideo() {
        videoView.stopPlayback();
    }

}
