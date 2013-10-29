package com.example.bunkasai;

import java.util.HashMap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	private NfcAdapter adapter;
	
	private IntentFilter[] filters = new IntentFilter[] {
			new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
	};
	
	private static final String[][] techLists = new String[][]{
		{
			android.nfc.tech.NfcA.class.getName(),
			android.nfc.tech.NfcB.class.getName(),
			android.nfc.tech.IsoDep.class.getName(),
			android.nfc.tech.NfcV.class.getName(),
			android.nfc.tech.NfcF.class.getName(),
		}
	};
	
	private static HashMap<String, String> nfcTagIds = new HashMap<String, String>();
	static {
		nfcTagIds.put("kys_link", "4-216-82-106-187-43-128");
		nfcTagIds.put("live",     "4-204-83-106-187-43-128");
		nfcTagIds.put("cm",       "4-146-83-106-187-43-128");
		nfcTagIds.put("ashi1",    "4-174-83-106-187-43-128");
		nfcTagIds.put("ashi2",    "4-205-83-106-187-43-128");
		nfcTagIds.put("ashi3",    "4-234-83-106-187-43-128");
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	 @Override
    protected void onResume() {
        super.onResume();
		adapter = NfcAdapter.getDefaultAdapter(this);
		
		if (!enableNFC(this, adapter)) {
			finish();
			return;
		}

        PendingIntent pendingIntent = PendingIntent.getActivity(
        		/* context 	   = */ this, 
        		/* requestCode = */ 0, 
        		/* intent	   = */ new Intent(this, getClass()),
        		/* flags 	   = */ 0);
        
        
        // nfcの検出を有効にする
		adapter.enableForegroundDispatch(this, pendingIntent, filters, techLists);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // nfcの検出を無効にする
        adapter.disableForegroundDispatch(this); 
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	
    	String action = intent.getAction();
    	if (TextUtils.isEmpty(action)) return;
    	
    	if(!action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) return;
    	
    	byte[] rawId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		String id = bytesToString(rawId);
		
		// 紀友祭のリンク
		if (nfcTagIds.get("kys_link").equals(id)) {
			Log.d("nfc", "kys");
			Intent kysIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://kys2013.web.fc2.com/"));
			startActivity(kysIntent);
			
			//ライブ
		} else if (nfcTagIds.get("live").equals(id)) { 
			Intent liveIntent = new Intent(MainActivity.this, VideoActivity.class);
			liveIntent.putExtra("videoPath", getVideoPath(R.raw.live));
			startActivity(liveIntent);
			
			// CM
		} else if (nfcTagIds.get("cm").equals(id)) {
			Intent cmIntent = new Intent(MainActivity.this, VideoActivity.class);
			cmIntent.putExtra("videoPath", getVideoPath(R.raw.cm));
			startActivity(cmIntent);
			
			// 芦田その１
		} else if (nfcTagIds.get("ashi1").equals(id)) {
			Intent ashiIntent = new Intent(MainActivity.this, VideoActivity.class);
			ashiIntent.putExtra("videoPath", getVideoPath(R.raw.ashi1));
			startActivity(ashiIntent);

			// 芦田その２
		} else if (nfcTagIds.get("ashi2").equals(id)) {
			Intent ashiIntent = new Intent(MainActivity.this, VideoActivity.class);
			ashiIntent.putExtra("videoPath", getVideoPath(R.raw.ashi2));
			startActivity(ashiIntent);

			// 芦田その３
		} else if (nfcTagIds.get("ashi3").equals(id)) {
			Intent ashiIntent = new Intent(MainActivity.this, VideoActivity.class);
			ashiIntent.putExtra("videoPath", getVideoPath(R.raw.ashi3));
			startActivity(ashiIntent);
			
		} else {
			finish();
			return ;
		}
    	
    	super.onNewIntent(intent);
    }

	private String getVideoPath(int resId) {
		return "android.resource://" + getPackageName() +"/" + resId;
	}
	
	/**
	 * NFCが利用可能かどうかを調べるメソッド
	 * @param context
	 * @param adapter
	 * @return
	 */
	private static boolean enableNFC(Context context, NfcAdapter adapter) {
		if (adapter == null) {
			Toast.makeText(context.getApplicationContext(), "no Nfc feature", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!adapter.isEnabled()) {
			Toast.makeText(context.getApplicationContext(), "off Nfc feature", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	/**
	 * byte配列を文字列に変換する
	 * @param bytes
	 * @return
	 */
	public static String bytesToString(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		
		for (int i=0; i<bytes.length; i++) {
			if (i>0) builder.append("-");
			builder.append(Integer.toString(bytes[i] & 0xff));
		}
		return builder.toString();
	}
	
}
