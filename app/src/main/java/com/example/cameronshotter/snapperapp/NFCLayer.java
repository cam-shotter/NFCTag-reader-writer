package com.example.cameronshotter.snapperapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;


public class NFCLayer extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    ToggleButton tglReadWrite;
    TextView textTagContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfclayer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter!=null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC Enabled!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC Not Enabled!", Toast.LENGTH_LONG).show();
        }

        tglReadWrite = (ToggleButton)findViewById(R.id.tglReadWrite);
        textTagContent = (TextView)findViewById(R.id.textTagContent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);



        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            Toast.makeText(this, "NFC Intent Received!", Toast.LENGTH_SHORT).show();

            if (tglReadWrite.isChecked()) {

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (parcelables != null && parcelables.length > 0) {

                    readTextFromMessage((NdefMessage)parcelables[0]);

                } else {

                    Toast.makeText(this, "No NDEF Messages Found!", Toast.LENGTH_SHORT).show();

                }

            } else {

//                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                NdefMessage ndefMessage = createNdefMessage(tag);
//
//                writeNdefMessage(tag, ndefMessage);

            }



        } else {
            Toast.makeText(this, "NFC Intent Not Received!", Toast.LENGTH_SHORT).show();
        }



    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            textTagContent.setText(tagContent);

        } else {

            Toast.makeText(this, "No NDEF Records Found!", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, NFCLayer.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();

        nfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tglReadWriteOnClick(View view) {

        textTagContent.setText("");

    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;

        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;

            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }

        return tagContent;
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            Ndef ndef = Ndef.get(tag);

            ndef.connect();

            if (!ndef.isWritable()) {
                Toast.makeText(this, "Tag Is Not Writable!", Toast.LENGTH_SHORT).show();

                ndef.close();
                return;
            }

            ndef.writeNdefMessage(ndefMessage);
            ndef.close();

            Toast.makeText(this, "Wrote to Tag!", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            Log.e("writeNdefMessage", e.getMessage());

        }

    }

}
