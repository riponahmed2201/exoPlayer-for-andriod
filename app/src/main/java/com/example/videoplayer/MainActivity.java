package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;
    SimpleExoPlayer simpleExoPlayer;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btFullScreen = findViewById(R.id.bt_full_screen);

        // make activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //video url
        Uri videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        //Initialize load control
        LoadControl loadControl = new DefaultLoadControl();

        //Initialize band width meter
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        //Initialize track selector
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        //Initialize simple exo player
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector, loadControl);

        //Initialize data source factory
        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

        //Initialize extractors factory
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        //Initialize media source
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl, factory,extractorsFactory,null,null);

        //Set player
        playerView.setPlayer(simpleExoPlayer);

        //keep screen on
        playerView.setKeepScreenOn(true);

        //prepare media
        simpleExoPlayer.prepare(mediaSource);

        //Play video when ready
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                // check condition
                if (playbackState == Player.STATE_BUFFERING){
                    //when buffering
                    //show progress bar
                    progressBar.setVisibility(View.VISIBLE);
                }else if (playbackState == Player.STATE_READY){
                    //when ready
                    //Hide progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

    btFullScreen.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //check condition
            if (flag){
                //when flag is true
                //set enter full screen image
                btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));

                //set portrait orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                //set flag value is false
                flag =false;
            }else {
                //when flag is false
                //set exit full screen image
                btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));

                //set landscape orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //set flag value is true
                flag = true;
            }
        }
    });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //set video when ready
        simpleExoPlayer.setPlayWhenReady(false);

        //Get playback state
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Play video when ready
        simpleExoPlayer.setPlayWhenReady(true);

        //Get playback state
        simpleExoPlayer.getPlaybackState();
    }
}