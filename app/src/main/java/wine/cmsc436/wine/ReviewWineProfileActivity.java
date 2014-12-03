package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class ReviewWineProfileActivity extends BaseActivity {

    TextView sweetnessLabel, tanninsLabel, acidityLabel, bodyLabel;
    SeekBar sweetnessSeekBar, tanninsSeekBar, aciditySeekBar, bodySeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_wine_profile);

        Number sweetness = (Number) getIntent().getSerializableExtra("sweetness");
        Number tannins = (Number) getIntent().getSerializableExtra("tannins");
        Number acidity = (Number) getIntent().getSerializableExtra("acidity");
        Number body = (Number) getIntent().getSerializableExtra("body");

        sweetnessLabel = (TextView) findViewById(R.id.sweetnessLabel);
        tanninsLabel = (TextView) findViewById(R.id.tanninsLabel);
        acidityLabel = (TextView) findViewById(R.id.acidityLabel);
        bodyLabel = (TextView) findViewById(R.id.bodyLabel);

        sweetnessSeekBar = (SeekBar) findViewById(R.id.sweetnessSeekBar);
        tanninsSeekBar = (SeekBar) findViewById(R.id.tanninsSeekBar);
        aciditySeekBar = (SeekBar) findViewById(R.id.aciditySeekBar);
        bodySeekBar = (SeekBar) findViewById(R.id.bodySeekBar);

        sweetnessLabel.setText("Sweetness: "+sweetness);
        tanninsLabel.setText("Tannins: "+tannins);
        acidityLabel.setText("Acidity: "+acidity);
        bodyLabel.setText("Body: "+body);

        sweetnessSeekBar.setProgress(sweetness.intValue());
        tanninsSeekBar.setProgress(tannins.intValue());
        aciditySeekBar.setProgress(acidity.intValue());
        bodySeekBar.setProgress(body.intValue());

        sweetnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sweetnessLabel.setText("Sweetness: "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tanninsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tanninsLabel.setText("Tannins: "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        aciditySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                acidityLabel.setText("Acidity: "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bodySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bodyLabel.setText("Body: "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_wine_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            Intent intent = new Intent();
            intent.putExtra("sweetness", sweetnessSeekBar.getProgress());
            intent.putExtra("tannins", tanninsSeekBar.getProgress());
            intent.putExtra("acidity", aciditySeekBar.getProgress());
            intent.putExtra("body", bodySeekBar.getProgress());
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
