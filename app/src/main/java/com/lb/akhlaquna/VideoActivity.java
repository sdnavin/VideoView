package com.lb.akhlaquna;

import android.net.Uri;
import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import android.media.MediaPlayer;

import android.content.Intent;
import android.content.IntentFilter;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoActivity extends Activity {

    /**
     * Video Init
     */


    private FrameLayout[] pageLayout;

    /*Tablet1*/

    private FrameLayout[] t1pageLayout;
    private ImageButton t1proceedButton;
    private  EditText t1InputFb;
    private ImageButton t1submitButton;
    private ImageButton t1cancelButton;

     /*Tablet2*/

    private FrameLayout[] t2pageLayout;
    private  EditText t2InputFb;
    private ImageButton t2submitButton;
    private ImageButton t2cancelButton;

      /*Tablet3*/

    private FrameLayout[] t3pageLayout;
    private  EditText t3InputFb;
    private  EditText t3Name;
    private  EditText t3Email;
    private ImageButton t3submitButton;
    private ImageButton t3cancelButton;
    private ImageButton t3proceedButton;
    private ImageButton t3regButton;
    private ImageView t3Warning;

      /*Tablet4*/

    private FrameLayout[] t4pageLayout;
    private  EditText t4InputFb;
    private ImageButton t4submitButton;
    private ImageView t4Warning;

    private  boolean requested = false;



    public int TabletNo;
    public int PageNo=0;




    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private static final String[] powerActionStrings = { "Power Down",
            "Cold Reset", "Warm Reset" };

    private static final String[] stateStrings = { "Unknown", "Absent",
            "Present", "Swallowed", "Powered", "Negotiable", "Specific" };

    List<String> Uid1;//="0377C2ED900000";
    List<String> Uid2;//="F317C2ED900000";
    List<String> Uid3;//="A33CC2ED900000";
    List<String> Uid4;//="7375C2ED900000";
//    String Uid5="";
//    String Uid6="";
//    String Uid7="";
//    String Uid8="";

    String SrcPath1 = "/Movies/video01.mp4";
    String SrcPath2 = "/Movies/video02.mp4";
    String SrcPath3 = "/Movies/video03.mp4";
    String SrcPath4 = "/Movies/video04.mp4";
    String SrcPath5 = "/Movies/video05.mp4";
    String SrcPath6 = "/Movies/video06.mp4";
    String SrcPath7 = "/Movies/video07.mp4";
    String SrcPath8 = "/Movies/video08.mp4";

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



//    private ArrayAdapter<String> mReaderAdapter;
//    private ArrayAdapter<String> mSlotAdapter;

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
                            RequestDevice();

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
//                    mReaderAdapter.clear();
//                    for (UsbDevice device : mManager.getDeviceList().values()) {
//                        if (mReader.isSupported(device)) {
//                            mReaderAdapter.add(device.getDeviceName());
//                        }
//                    }
                    // Clear slot items
//                    mSlotAdapter.clear();

                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null && device.equals(mReader.getDevice())) {

                        // Disable buttons

                        // Clear slot items
//                        mSlotAdapter.clear();
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
                RequestDevice();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {

            if (result != null) {

//                    textView.setText(result.toString());

            } else {
                int numSlots = mReader.getNumSlots();
                // Add slot items
//                mSlotAdapter.clear();
//                for (int i = 0; i < numSlots; i++) {
//                    mSlotAdapter.add(Integer.toString(i));
//                }

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
                    logBuffer(result.atr, result.atr.length);

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


    private void logBuffer(byte[] buffer, int bufferLength) {

        String bufferString = "";

        for (int i = 0; i < bufferLength; i++) {

            String hexChar = Integer.toHexString(buffer[i] & 0x90);
            if (hexChar.length() == 1) {
                hexChar = "0" + hexChar;
            }

            if (i % 16 == 0) {

                if (bufferString != "") {

                    //logMsg(bufferString);
                    bufferString = "";
                }
            }

            bufferString += hexChar.toUpperCase() + " ";
        }

        if (bufferString != "") {
            // logMsg(bufferString);
        }
    }

    private String UIDBuffer(byte[] buffer, int bufferLength) {
//        //5352C1ED9000000000000
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                mainbg.setBackground(getResources().getDrawable(R.color.green));
//            }
//        });

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

    String DataToAdd="";
    String FileName="";
;    private OnClickListener buttonSubmitListener = new OnClickListener() {
        @Override
        public void onClick(View v){
            DataToAdd="";
            FileName="";
            java.util.Locale locale = new 	java.util.Locale("EN");
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("hh-mm-ss-a", locale);
            FileName=df.format(new Date());
            DataToAdd=(TabletNo==1?t1InputFb:TabletNo==2?t2InputFb:TabletNo==3?t3InputFb:null).getText().toString();
            if(TabletNo==3){
                DataToAdd+="-"+t3Name.getText().toString();
            }
            if(TabletNo!=3) {
                writeStringAsFile(DataToAdd, FileName);
                (TabletNo==1?t1InputFb:TabletNo==2?t2InputFb:TabletNo==3?t3InputFb:null).setText("");
            }
            GotoNextPage(TabletNo);
        }
    };

    private OnClickListener buttonEmailListener= new OnClickListener() {
        @Override
        public void onClick(View v){

                if(basic.isValidEmail( ((TabletNo==3)?t3Email: t4InputFb).getText().toString())){
                    ((TabletNo==3)?t3Warning:t4Warning).setVisibility(View.INVISIBLE);
                    GotoNextPage(TabletNo);
                    if(TabletNo==3){
                        DataToAdd+="-"+t3Email.getText().toString();
                            writeStringAsFile(DataToAdd,FileName);
                    }else{
                        DataToAdd=t4InputFb.getText().toString();

                        java.util.Locale locale = new 	java.util.Locale("EN");
                        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("hh-mm-ss-a", locale);
                        FileName=df.format(new Date());
                        writeStringAsFile(DataToAdd,FileName);
                    }
                    if(TabletNo==3) {
                        t3InputFb.setText("");
                        t3Name.setText("");
                    }
                    ((TabletNo==3)?t3Email: t4InputFb).setText("");
                }else{
                    ((TabletNo==3)?t3Warning:t4Warning).setVisibility(View.VISIBLE);
                }
        }
    };



    private OnClickListener buttonProceedListener = new OnClickListener() {
        @Override
        public void onClick(View v){
            GotoNextPage(TabletNo);
        }
    };

    private OnClickListener buttonProceedDelayListener = new OnClickListener() {
        @Override
        public void onClick(View v){
            GotoNextPage(TabletNo);
            /*basic.setTimeout(new Runnable() {
                @Override
                public void run() {
                    Log.d("video", "Executed after 5000 ms!");
                    GotoNextPage(TabletNo);
                }
            },3000);*/
        }
    };

    public void GotoNextPage(int ScanNo){
        if(ScanNo!=TabletNo)
            return;
        if(TabletNo==1){
            for(int t=0;t<t1pageLayout.length;t++){
            if(t1pageLayout[t].getVisibility()== View.VISIBLE) {

                PageNo=t+1;
                t1pageLayout[t].setVisibility(View.INVISIBLE);
                t1pageLayout[PageNo].setVisibility(View.VISIBLE);
                if(PageNo==(t1pageLayout.length-1)){
                    basic.setTimeout(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("video", "Executed after 5000 ms!");
                            t1pageLayout[PageNo].setVisibility(View.INVISIBLE);
                            PageNo=0;
                            t1pageLayout[PageNo].setVisibility(View.VISIBLE);
                        }
                    },5000);
                    break;
                }
                break;
            }
            }
        }else if(TabletNo==2){
            for(int t=0;t<t2pageLayout.length;t++){
                if(t2pageLayout[t].getVisibility()== View.VISIBLE) {

                    PageNo=t+1;
                    t2pageLayout[t].setVisibility(View.INVISIBLE);
                    t2pageLayout[PageNo].setVisibility(View.VISIBLE);
                    if(PageNo==(t2pageLayout.length-1)){
                        basic.setTimeout(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("video", "Executed after 5000 ms!");
                                t2pageLayout[PageNo].setVisibility(View.INVISIBLE);
                                PageNo=0;
                                t2pageLayout[PageNo].setVisibility(View.VISIBLE);
                            }
                        },5000);
                        break;
                    }
                    break;
                }
            }

        }else if(TabletNo==3){
            for(int t=0;t<t3pageLayout.length;t++){
                if(t3pageLayout[t].getVisibility()== View.VISIBLE) {

                    PageNo=t+1;
                    t3pageLayout[t].setVisibility(View.INVISIBLE);
                    t3pageLayout[PageNo].setVisibility(View.VISIBLE);
                    if(PageNo==(t3pageLayout.length-1)){
                        basic.setTimeout(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("video", "Executed after 5000 ms!");
                                t3pageLayout[PageNo].setVisibility(View.INVISIBLE);
                                PageNo=0;
                                t3pageLayout[PageNo].setVisibility(View.VISIBLE);
                            }
                        },5000);
                        break;
                    }
                    break;
                }
            }

        }else if(TabletNo==4){
            for(int t=0;t<t4pageLayout.length;t++){
                if(t4pageLayout[t].getVisibility()== View.VISIBLE) {

                    PageNo=t+1;
                    t4pageLayout[t].setVisibility(View.INVISIBLE);
                    t4pageLayout[PageNo].setVisibility(View.VISIBLE);
                    if(PageNo==(t4pageLayout.length-1)){
                        basic.setTimeout(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("video", "Executed after 5000 ms!");
                                t4pageLayout[PageNo].setVisibility(View.INVISIBLE);
                                PageNo=0;
                                t4pageLayout[PageNo].setVisibility(View.VISIBLE);
                            }
                        },5000);
                        break;
                    }
                    break;
                }
            }

        }
    }



    public void writeStringAsFile(String fileContents, String fileName) {
        if(fileContents.trim().length()<=0){
            return;
        }
//        Log.d("video",fileName);
        Context context = this.getBaseContext();
        java.util.Locale locale = new 	java.util.Locale("EN");
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd", locale);
        String dataIn=df.format(new Date());
        try {
            String dirPath=Environment.getExternalStorageDirectory()+"/Feedback/"+dataIn+"/";
            File folder = new File(dirPath);
            if(!folder.exists()){
                folder.mkdirs();
            }
            Log.d("video",dirPath+fileName);
            FileWriter out = new FileWriter(new File(dirPath, fileName+".txt"));
            out.write(fileContents);
            out.close();

            File filecheck = new File(dirPath+fileName);
            Log.d("video","Check :"+filecheck.exists());
        } catch (java.io.IOException e) {
            writeStringAsFile(fileContents,fileName);
        }
    }

    void ReadRFIDs(){
        FileInputStream is;
        BufferedReader reader;
//        final File file = new File(Environment.getExternalStorageDirectory() +"/Movies/rfid.txt");
//        final File file = new File("android.resource://com.lb.videoview/" + R.raw.rfid);
//        Log.d("video", "E : "+file.exists());
        try {
//            if (file.exists())
            {
//                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(this.getBaseContext().getResources().openRawResource(R.raw.rfid)));
                String line = reader.readLine();
                int count=1;

                Uid1=new ArrayList<String>();
                Uid2=new ArrayList<String>();
                Uid3=new ArrayList<String>();
                Uid4=new ArrayList<String>();
                Uid1.add(line);
                while (line != null) {
                    line = reader.readLine();
                    if(!TextUtils.isEmpty(line)){
                        if(count==1){
                            Uid1.add(line);
//                            Log.d("video", line+"--"+Uid1.size()+"");
                        }else if(count==2){
                            Uid2.add(line);
                        }else if(count==3){
                            Uid3.add(line);
                        }else if(count==4){
                            Uid4.add(line);
                        }
                    }else{
                        count+=1;
                    }
                }

            }

        }catch (java.io.IOException e){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        canPlay=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        TabletNo=4;
        pageLayout=new FrameLayout[4];

        pageLayout[0]=findViewById(R.id.t1);
        pageLayout[1]=findViewById(R.id.t2);
        pageLayout[2]=findViewById(R.id.t3);
        pageLayout[3]=findViewById(R.id.t4);

        t1pageLayout= new FrameLayout[4];

        t1pageLayout[0]=findViewById(R.id.t1p1);
        t1pageLayout[1]=findViewById(R.id.t1p2);
        t1pageLayout[2]=findViewById(R.id.t1p3);
        t1pageLayout[3]=findViewById(R.id.t1p4);
        t1InputFb=findViewById(R.id.t1feedback);
        t1InputFb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    HideView();
//                }
                HideView();
                final InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && !hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        // set listeners
        t1InputFb.addTextChangedListener(new android.text.TextWatcher() {
            int lastSpecialRequestsCursorPosition=0;
            String specialRequests="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = t1InputFb.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                t1InputFb.removeTextChangedListener(this);

                if (t1InputFb.getLineCount() > 8) {
                    t1InputFb.setText(specialRequests);
                    t1InputFb.setSelection(lastSpecialRequestsCursorPosition);
                }
                else
                    specialRequests = t1InputFb.getText().toString();

                t1InputFb.addTextChangedListener(this);
            }
        });
        t1proceedButton=findViewById(R.id.t1proceedBut);
        t1proceedButton.setOnClickListener(buttonProceedListener);

        t1submitButton=findViewById(R.id.t1sendBut);
        t1submitButton.setOnClickListener(buttonSubmitListener);
        t1cancelButton=findViewById(R.id.t1cancelBut);
        t1cancelButton.setOnClickListener(buttonProceedListener);


        t2pageLayout= new FrameLayout[3];

        t2pageLayout[0]=findViewById(R.id.t2p1);
        t2pageLayout[1]=findViewById(R.id.t2p2);
        t2pageLayout[2]=findViewById(R.id.t2p3);
        t2InputFb=findViewById(R.id.t2feedback);

        // set listeners
        t2InputFb.addTextChangedListener(new android.text.TextWatcher() {
            int lastSpecialRequestsCursorPosition=0;
            String specialRequests="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = t2InputFb.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                t2InputFb.removeTextChangedListener(this);

                if (t2InputFb.getLineCount() > 8) {
                    t2InputFb.setText(specialRequests);
                    t2InputFb.setSelection(lastSpecialRequestsCursorPosition);
                }
                else
                    specialRequests = t2InputFb.getText().toString();

                t2InputFb.addTextChangedListener(this);
            }
        });
        t2InputFb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    HideView();
//                }
                HideView();
                final InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && !hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        t2submitButton=findViewById(R.id.t2sendBut);
        t2submitButton.setOnClickListener(buttonSubmitListener);
        t2cancelButton=findViewById(R.id.t2cancelBut);
        t2cancelButton.setOnClickListener(buttonProceedListener);


        t3pageLayout= new FrameLayout[5];

        t3pageLayout[0]=findViewById(R.id.t3p1);
        t3pageLayout[1]=findViewById(R.id.t3p2);
        t3pageLayout[2]=findViewById(R.id.t3p3);
        t3pageLayout[3]=findViewById(R.id.t3p5);
        t3pageLayout[4]=findViewById(R.id.t3p6);

        t3InputFb=findViewById(R.id.t3feedback);
        t3Name=findViewById(R.id.t3Name);
        t3Email=findViewById(R.id.t3Email);
        t3Warning=findViewById(R.id.t3Warn);

        t3InputFb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    HideView();
                }
                final InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && !hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });

        // set listeners
        t3InputFb.addTextChangedListener(new android.text.TextWatcher() {
            int lastSpecialRequestsCursorPosition=0;
            String specialRequests="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = t4InputFb.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                t3InputFb.removeTextChangedListener(this);

                if (t3InputFb.getLineCount() > 6) {
                    t3InputFb.setText(specialRequests);
                    t3InputFb.setSelection(lastSpecialRequestsCursorPosition);
                }
                else
                    specialRequests = t3InputFb.getText().toString();

                t3InputFb.addTextChangedListener(this);
            }
        });
        t3Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    HideView();
                }
                final InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && !hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        t3Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    HideView();
                }
                final InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && !hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        t3proceedButton=findViewById((R.id.t3proceedBut));
        t3proceedButton.setOnClickListener(buttonEmailListener);

        t3submitButton=findViewById(R.id.t3sendBut);
        t3submitButton.setOnClickListener(buttonSubmitListener);
        t3cancelButton=findViewById(R.id.t3cancelBut);
        t3cancelButton.setOnClickListener(buttonProceedListener);
        t3regButton=findViewById(R.id.t3regBut);
        t3regButton.setOnClickListener(buttonProceedDelayListener);


        t4pageLayout= new FrameLayout[3];

        t4pageLayout[0]=findViewById(R.id.t4p1);
        t4pageLayout[1]=findViewById(R.id.t4p2);
        t4pageLayout[2]=findViewById(R.id.t4p3);
        t4InputFb=findViewById(R.id.t4feedback);

        // set listeners
        t4InputFb.addTextChangedListener(new android.text.TextWatcher() {
            int lastSpecialRequestsCursorPosition=0;
            String specialRequests="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastSpecialRequestsCursorPosition = t4InputFb.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                t4InputFb.removeTextChangedListener(this);

                if (t4InputFb.getLineCount() > 6) {
                    t4InputFb.setText(specialRequests);
                    t4InputFb.setSelection(lastSpecialRequestsCursorPosition);
                }
                else
                    specialRequests = t4InputFb.getText().toString();

                t4InputFb.addTextChangedListener(this);
            }
        });
        t4Warning=findViewById(R.id.t4Warn);
        t4InputFb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    HideView();
//                }
                HideView();
                final InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && !hasFocus) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        t4submitButton=findViewById(R.id.t4proceedBut);
        t4submitButton.setOnClickListener(buttonEmailListener);

        ReadRFIDs();


//        submitButton.setOnClickListener(buttonSubmitListener);
        // Get USB manager
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // Initialize reader
        mReader = new Reader(mManager);

        for(int t=0;t<pageLayout.length;t++){
            pageLayout[t].setVisibility(View.INVISIBLE);
        }
        pageLayout[TabletNo-1].setVisibility(View.VISIBLE);

//        InputFb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    HideView();
//                }
//            }
//        });
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
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Create output string
                //final String outputString = "Slot " + slotNum + ": " + stateStrings[prevState] + " -> " + stateStrings[currState];
                String mystring="";
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
                    mystring = UIDBuffer(baResp,baReadUIDx.length);
                    Log.e("UID", mystring);
                    int y = mReader.control(0,3500,baSwitchOnLCD,baSwitchOnLCD.length,baResp,baResp.length);
                    int z = mReader.control(0,3500,displaytext,displaytext.length,baResp,baResp.length);

                } catch (ReaderException e) {
                    mystring = e.getMessage();
//                    Log.e("video","Error : "+ mystring);
//                    e.printStackTrace();
                }


                final String outputString = mystring;


//                if(stateStrings[currState]==stateStrings[2]){
//                    canPlay=true;
//                }
                // Show output
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        checkPage(outputString);
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
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

try {
    // Register receiver for USB permission
    mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
            ACTION_USB_PERMISSION), 0);
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION_USB_PERMISSION);
    filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
    registerReceiver(mReceiver, filter);
//        playVideo();
}catch(Exception e){
    e.printStackTrace();
}
        // Initialize reader spinner
//        mReaderAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item);
//        mSlotAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item);
//        for (UsbDevice device : mManager.getDeviceList().values()) {
//            if (mReader.isSupported(device)) {
//                mReaderAdapter.add(device.getDeviceName());
//            }
//        }
        RequestDevice();
HideView();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            HideView();
        }
    }

    void HideView(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void RequestDevice(){
//        if(!requested) {

            for (UsbDevice device : mManager.getDeviceList().values()) {

                // If device name is found
            if (mReader.isSupported(device)) {

                // Request permission
                mManager.requestPermission(device,
                        mPermissionIntent);

                requested = true;
                break;
            }
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

    boolean CheckPageList(String msg,List<String> videos){
        for(int t=0;t<videos.size();t++){
//            Log.e("video",videos.get(t));
            if(videos.get(t).contains(msg)){
                return true;
            }
        }
        return false;
    }
    private void checkPage(String msg){
        Log.d("video",msg);
        if(msg.length()>14)
            return;
        if(PageNo>0)
            return;
        if(CheckPageList(msg,Uid1)){
            GotoNextPage(1);
            //okay concern video
        }else if(CheckPageList(msg,Uid2)){
            GotoNextPage(2);
            //okay concern video
        }else if(CheckPageList(msg,Uid3)){
            //okay concern video
            GotoNextPage(3);
        }else if(CheckPageList(msg,Uid4)){
            //okay concern video
            GotoNextPage(4);
        }
    }
}
