package rolevax.testsecuritylib;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button button1 = (Button) findViewById(R.id.no_net_button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doTest(false);
            }
        });
        final Button button2 = (Button) findViewById(R.id.net_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doTest(true);
            }
        });
        final Button button3 = (Button) findViewById(R.id.raw_button);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TestSecLib.raw(taint);
            }
        });
        final Button button4 = (Button) findViewById(R.id.digest_betton);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TestSecLib.digest(taint, true);
            }
        });
        final Button button5 = (Button) findViewById(R.id.mac_button);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TestSecLib.mac(taint, true);
            }
        });
        final Button button6 = (Button) findViewById(R.id.private_button);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TestSecLib.privateKey(taint, true);
            }
        });
        final Button button7 = (Button) findViewById(R.id.public_button);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TestSecLib.publicKey(taint, true);
            }
        });
        final Button button8 = (Button) findViewById(R.id.dc_button);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TestSecLib.digitalCertificate(taint, true);
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double longti = location.getLongitude();
                double lati = location.getLatitude();
                taint = "location: " + longti + ", " + lati;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doTest(boolean leak) {
        if (taint.equals("not tainted yet")) {
            Log.i("info", "haven't get location info");
        } else {
            TestSecLib.test(taint, leak);
        }
    }

    String taint = "not tainted yet";
}
