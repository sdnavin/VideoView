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

import android.media.MediaPlayer;

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

import com.acs.smartcard.ReaderException;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;

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

    private  EditText InputFb;


    private Button submitButton;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private static final String[] powerActionStrings = { "Power Down",
            "Cold Reset", "Warm Reset" };

    private static final String[] stateStrings = { "Unknown", "Absent",
            "Present", "Swallowed", "Powered", "Negotiable", "Specific" };

    String Uid1="";
    String Uid2="";
    String Uid3="";
    String Uid4="";
    String Uid5="";
    String Uid6="";
    String Uid7="";
    String Uid8="";

    String SrcPath1 = "/sdcard/Video/video01.mp4";
    String SrcPath2 = "/sdcard/Video/video02.mp4";
    String SrcPath3 = "/sdcard/Video/video03.mp4";
    String SrcPath4 = "/sdcard/Video/video04.mp4";
    String SrcPath5 = "/sdcard/Video/video05.mp4";
    String SrcPath6 = "/sdcard/Video/video06.mp4";
    String SrcPath7 = "/sdcard/Video/video07.mp4";
    String SrcPath8 = "/sdcard/Video/video08.mp4";

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


    private Features mFeatures = new Features();
    private PinVerify mPinVerify = new PinVerify();
    private PinModify mPinModify = new PinModify();
    private ReadKeyOption mReadKeyOption = new ReadKeyOption();
    private String mLcdMessage;

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

                int numSlots = mReader.getNumSlots();
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

            }
        }
    }

    private class PowerParams {

        public int slotNum;
        public int action;
    }

    private class PowerResult {

        public byte[] atr;
        public Exception e;
    }

    private class PowerTask extends AsyncTask<PowerParams, Void, PowerResult> {

        @Override
        protected PowerResult doInBackground(PowerParams... params) {

            PowerResult result = new PowerResult();

            try {

                result.atr = mReader.power(params[0].slotNum, params[0].action);

            } catch (Exception e) {

                result.e = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(PowerResult result) {

            if (result.e != null) {

                //logMsg(result.e.toString());

            } else {

                // Show ATR
                if (result.atr != null) {

                    //logMsg("ATR:");
//                    logBuffer(result.atr, result.atr.length);

                } else {

                    //logMsg("ATR: None");
                }
            }
        }
    }


    private class SetProtocolParams {

        public int slotNum;
        public int preferredProtocols;
    }

    private class SetProtocolResult {

        public int activeProtocol;
        public Exception e;
    }


    private class SetProtocolTask extends
            AsyncTask<SetProtocolParams, Void, SetProtocolResult> {

        @Override
        protected SetProtocolResult doInBackground(SetProtocolParams... params) {

            SetProtocolResult result = new SetProtocolResult();

            try {

                result.activeProtocol = mReader.setProtocol(params[0].slotNum,
                        params[0].preferredProtocols);

            } catch (Exception e) {

                result.e = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(SetProtocolResult result) {

            if (result.e != null) {

                //logMsg(result.e.toString());

            } else {

                String activeProtocolString = "Active Protocol: ";

                switch (result.activeProtocol) {

                    case Reader.PROTOCOL_T0:
                        activeProtocolString += "T=0";
                        break;

                    case Reader.PROTOCOL_T1:
                        activeProtocolString += "T=1";
                        break;

                    default:
                        activeProtocolString += "Unknown";
                        break;
                }

                // Show active protocol
                //logMsg(activeProtocolString);
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


    private OnClickListener buttonSubmitListener = new OnClickListener() {
        @Override
        public void onClick(View v){
            writeStringAsFile(InputFb.getText().toString(),DateFormat.getDateTimeInstance().format(new Date()));
            fbLayout.setVisibility(View.INVISIBLE);
        }
    };

    public void writeStringAsFile(String fileContents, String fileName) {
        Context context = this.getBaseContext();
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            out.close();
        } catch (java.io.IOException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        canPlay=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
//        textView=findViewById(R.id.textView);
        videoView=findViewById(R.id.videoView);
        InputFb =findViewById((R.id.feedback));
        fbLayout=findViewById(R.id.fbLayout);
        mainLayout=findViewById(R.id.mainLayout);

        submitButton=findViewById(R.id.submit_button);
        submitButton.setOnClickListener(buttonSubmitListener);
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


                PowerParams params = new PowerParams();
                params.slotNum = 0;
                params.action = Reader.CARD_WARM_RESET;
                new PowerTask().execute(params);

                SetProtocolParams sparams = new SetProtocolParams();
                sparams.slotNum = 0;
                sparams.preferredProtocols = Reader.PROTOCOL_T1;

                new SetProtocolTask().execute(sparams);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Create output string
                //final String outputString = "Slot " + slotNum + ": " + stateStrings[prevState] + " -> " + stateStrings[currState];
                String mystring;
                byte message = (byte)0xFF;
                byte[] baReadUID;
                baReadUID = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
                byte[] baSwitchOnLCD;
                baSwitchOnLCD = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0x64, (byte) 0xFF, (byte) 0x00 };
                byte[] displaytext = new byte[] { (byte) 0xFF, (byte) 0x00, (byte) 0x68, (byte) 0x00, (byte)0x01, message };
                byte[] baResp = new byte[258];
                byte[] baReadUIDx = new byte[7];


                try {
                    int x = mReader.transmit(0,baReadUID,baReadUID.length,baResp,baResp.length);
                    //mystring = UIDBuffer(baResp,baReadUIDx.length);
                    mystring = UIDBuffer(baResp,baReadUIDx.length);
                    Log.e("UID", mystring);
                    Log.e("UIDxxxx", x+"");
                    int y = mReader.control(0,3500,baSwitchOnLCD,baSwitchOnLCD.length,baResp,baResp.length);
                    int z = mReader.control(0,3500,displaytext,displaytext.length,baResp,baResp.length);



                } catch (ReaderException e) {
                    mystring = e.getMessage();

//                    e.printStackTrace();
                }


                final String outputString = mystring;


                if(stateStrings[currState]==stateStrings[2]){
                    canPlay=true;
                }
                // Show output
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        checkVideo(outputString);
                    }
                });

                // Show output
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        textView.setText(outputString);
//                      if(canPlay) {
//                          playVideo();
//                          canPlay=false;
//                      }
//                    }
//                });
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



    public void playVideo(String Dpath) {
        Log.d("D","Playing Video");
        MediaController m = new MediaController(this);
        videoView.setMediaController(m);
        String path = "android.resource://com.lb.videoview/"+ R.raw.marvel;
        path=Dpath;
        Uri u = Uri.parse(path);

        videoView.setVideoURI(u);
        videoView.seekTo(0);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fbLayout.setVisibility((View.VISIBLE));
            }
        });
    }
    public void stopVideo() {
        videoView.stopPlayback();
    }

    private void checkVideo(String msg){
        if(msg.equals(Uid1)){
            //okay concern video
            playVideo(SrcPath1);
        }else if(msg.equals(Uid2)){
            //okay concern video
            playVideo(SrcPath2);
        }else if(msg.equals(Uid3)){
            //okay concern video
            playVideo(SrcPath3);
        }else if(msg.equals(Uid4)){
            //okay concern video
            playVideo(SrcPath4);
        }else if(msg.equals(Uid5)){
            //okay concern video
            playVideo(SrcPath5);
        }else if(msg.equals(Uid6)){
            //okay concern video
            playVideo(SrcPath6);
        }else if(msg.equals(Uid7)){
            //okay concern video
            playVideo(SrcPath7);
        }else if(msg.equals(Uid8)){
            //okay concern video
            playVideo(SrcPath8);
        }
    }

    private String UIDBuffer(byte[] buffer, int bufferLength) {
        //5352C1ED9000000000000
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mainbg.setBackground(getResources().getDrawable(R.color.green));
            }
        });

        String bufferString = "";
        Log.e("bufferLength",bufferLength+"");
        for (int i = 0; i < bufferLength; i++) {

            String hexChar = Integer.toHexString(buffer[i] & 0xFF);
            if (hexChar.length() == 1) {
                hexChar = "0" + hexChar;
            }

            if (i % 16 == 0) {

                if (bufferString != "") {

                    return bufferString;
                }
            }

            bufferString += hexChar.toUpperCase();
        }
        if (bufferString != "") {
            return bufferString;
        }
        return "Error";
    }

}
