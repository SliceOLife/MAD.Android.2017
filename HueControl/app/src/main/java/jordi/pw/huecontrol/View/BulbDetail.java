package jordi.pw.huecontrol.View;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import jordi.pw.huecontrol.Models.Bulb;
import jordi.pw.huecontrol.R;

public class BulbDetail extends AppCompatActivity  {
    private Bulb bulb;

    private TextView bulbName;
    private Switch bulbStateSwitch;
    private SeekBar bulbHueSeekbar, bulbSaturationSeekbar, bulbBrightnessSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulb_detail);

        // Retrieve bulb from intent
        this.bulb = getIntent().getParcelableExtra("bulb");

        bulbName = (TextView) findViewById(R.id.textviewBulbNameDetail);
        bulbStateSwitch = (Switch) findViewById(R.id.switchBulbStateDetail);
        bulbHueSeekbar = (SeekBar) findViewById(R.id.seekbarHueLevel);
        bulbSaturationSeekbar = (SeekBar) findViewById(R.id.seekbarSaturationLevel);
        bulbBrightnessSeekbar = (SeekBar) findViewById(R.id.seekbarBrightnessLevel);

        bulbName.setText(bulb.getName());
        bulbStateSwitch.setChecked(bulb.isState());
        bulbHueSeekbar.setProgress(bulb.getHue());
        //bulbSaturationSeekbar.setProgress(bulb.getSaturation());
        bulbBrightnessSeekbar.setProgress(bulb.getBrightness());

        // Switch listener
        bulbStateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bulb.toggle();
            }
        });

        // Seekbar Listeners
        bulbHueSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bulb.updateHue(seekBar.getProgress());
            }
        });

        bulbBrightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bulb.updateBrightness(seekBar.getProgress());
            }
        });

        bulbSaturationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bulb.updateSaturation(seekBar.getProgress());
            }
        });
    }
}
