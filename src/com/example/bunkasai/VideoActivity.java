package com.example.bunkasai;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

public class VideoActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		String videoPath = getIntent().getStringExtra("videoPath");
		
		VideoView videoView = (VideoView) findViewById(R.id.videoView);
		videoView.setVideoURI(Uri.parse(videoPath));
		videoView.start();
		videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				finish();
			}
		});
	}

}
