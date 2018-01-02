package com.teamtreehouse.musicmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamtreehouse.musicmachine.models.Song;

public class DetailActivity extends AppCompatActivity {

    private Song song;
    private RelativeLayout rootLayout;

    public static final String SHARE_SONG = "com.teamtreehouse.intent.action.SHARE_SONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView titleLabel = (TextView)findViewById(R.id.songTitleLabel);
        final CheckBox favoriteCheckbox = (CheckBox)findViewById(R.id.checkBox);
        rootLayout = findViewById(R.id.rootLayout);

        Intent intent = getIntent();

        // check if it's an implicit intent coming from "outside"
        // or if it's an explicit intent coming from our app
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            // implicit intent coming from "outside"
            handleSendIntent(intent);
        } else {
            // explicit intent coming from our app

//        if (intent.getStringExtra(MainActivity.EXTRA_TITLE) != null) {
//            String songTitle = intent.getStringExtra(MainActivity.EXTRA_TITLE);
//            titleLabel.setText(songTitle);
//        }
            if (intent.getParcelableExtra(MainActivity.EXTRA_SONG) != null) {
                song = intent.getParcelableExtra(MainActivity.EXTRA_SONG);
                titleLabel.setText(song.getTitle());
                favoriteCheckbox.setChecked(song.isFavorite());
            }
            int listPosition = intent.getIntExtra(MainActivity.EXTRA_LIST_POSITION, 0);

            favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_FAVORITE, isChecked);
                    resultIntent.putExtra(MainActivity.EXTRA_LIST_POSITION, listPosition);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }
    }

    private void handleSendIntent(Intent intent) {
        if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            Snackbar.make(rootLayout, intent.getStringExtra(Intent.EXTRA_TEXT),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_share) {
            if (song != null) {
                Intent customIntent = new Intent(SHARE_SONG);
                customIntent.putExtra(MainActivity.EXTRA_SONG, song);
                // switching to a broadcast intent and receiver by getting rid of
                // the below two lines and sending the intent via the
                // sendBroadcast method vs the startActivity method
//                Intent chooser = Intent.createChooser(customIntent, "Share song");
//                startActivity(chooser);
                // Custom broadcast intents and receivers work the same way
                // as local broadcast. The only thing that's different from a system
                // broadcast is that the custom components both utilize a shared
                // common action
                sendBroadcast(customIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
