package com.vit.tipcalculator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
private TextView percenttv;
private TextView tiptv;
private TextView totaltv;
private TextView amounttv;
private EditText amountet;
private SeekBar seekBar;
private static double percent=0.15;
private static double amount=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        percenttv=(TextView)findViewById(R.id.percent);
        tiptv=(TextView)findViewById(R.id.tv_tip);
        totaltv=(TextView)findViewById(R.id.tv_total);
        amounttv=(TextView)findViewById(R.id.amounttv);
        amountet=(EditText)findViewById(R.id.editText);
        seekBar=(SeekBar) findViewById(R.id.sBar);
        amountet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {if (amountet.getText().length()==0){ amount=0.0;
                calculate();
                return;}
                    amount=Double.valueOf(s.toString())/100;
                amounttv.setText(String.valueOf(amount));
                } catch (NumberFormatException e){amount=0.0;
                    Toast toast=Toast.makeText(getApplicationContext(),R.string.error,Toast.LENGTH_LONG);
                toast.show(); }
            calculate(); }});
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            percenttv.setText(progress+"%");
            percent=(double)progress/100;
            calculate(); }});}
private void calculate(){
    if (amount==0) {
        amounttv.setText(R.string.start_value);
        tiptv.setText(R.string.start_value);
        totaltv.setText(R.string.start_value);
        return; }
    BigDecimal bigtip=new BigDecimal(amount*percent);
    tiptv.setText("$"+bigtip.setScale(2,RoundingMode.HALF_UP));
    BigDecimal bigtotal=bigtip.add(new BigDecimal(amount));
    totaltv.setText("$"+bigtotal.setScale(2,RoundingMode.HALF_UP));}}