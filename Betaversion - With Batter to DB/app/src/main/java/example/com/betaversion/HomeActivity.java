package example.com.betaversion;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HomeActivity extends AppCompatActivity {
    private ImageView iv_battery;
    private TextView tv_battery;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_battery = (ImageView) findViewById(R.id.iv_battery);
        tv_battery = (TextView) findViewById(R.id.tv_battery);

        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryLevel();

                tv_battery.setText("BATTERY: " + level + "%");

                if(level > 75) {
                    iv_battery.setImageResource(R.drawable.battery_100);
                }

                if(level > 50 && level <= 75) {
                    iv_battery.setImageResource(R.drawable.battery_75);
                }

                if(level > 25 && level <= 50) {
                    iv_battery.setImageResource(R.drawable.battery_50);
                }

                if(level > 5 && level <= 25) {
                    iv_battery.setImageResource(R.drawable.battery_25);
                }

                if(level <= 5) {
                    iv_battery.setImageResource(R.drawable.battery_5);
                }

                handler.postDelayed(runnable, 5000);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 0);

        public void insertDb (View insertBtn) {
            // Create the references
            TextView textView = (TextView) findViewById(R.id.tv_battery);

            // Instantiate the book object
            Book bookObj = new Book(textView.getText().toString());

            // Call the bookhelper's add method
            BookHelper helper = new BookHelper(this);
            helper.addBook(bookObj);

            Toast.makeText(this, "Record insert successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public float batteryLevel(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }
}