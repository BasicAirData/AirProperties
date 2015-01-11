package eu.basicairdata.jlj.cipm91airdensity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

//(C) BasicAirData www.basicairdata.eu JLJ
public class MainActivity extends ActionBarActivity {
    public void runcalc(View view) {
        EditText mEdit,mEdit1,mEdit2;
        TextView tView,tView1,tView2,tView3,tView4;
        double T;
        double p;
        double h;
        double rho; //Air Density
        double mu; //Speed of sound
        double sos; //Speed of sound
        double R1,xco2,A,B,C,D,alfa,bet,gama,a0,a1,a2,b0,b1,c0,c1,d,e,Ma,Mv,psv,f,xv,Z,t,Rair;
        try {
            mEdit = (EditText) findViewById(R.id.editText);  //Temperature
            mEdit1 = (EditText) findViewById(R.id.editText1);//Pressure
            mEdit2 = (EditText) findViewById(R.id.editText2);//Releative Umidity
            tView = (TextView) findViewById(R.id.textView5);//Air Density
            tView1 = (TextView) findViewById(R.id.textView7);//Vapor Pressure
            tView2 = (TextView) findViewById(R.id.textView9);//Compressibility factor
            tView3 = (TextView) findViewById(R.id.textView11);//Dynamic Viscosity
            tView4 = (TextView) findViewById(R.id.textView13);//Speed of Sound
            T = Double.parseDouble(mEdit.getText().toString());
            p = Double.parseDouble(mEdit1.getText().toString());
            h = Double.parseDouble(mEdit2.getText().toString());
            R1 = 8.314510;//J/(mol°K)
            xco2 = 0.0004;//Co2 fraction
            A = 1.2378847e-5;
            B = -1.9121316e-2;
            C = 33.93711047;
            D = -6.3431645e3;
            alfa = 1.00062;
            bet = 3.14e-8;
            gama = 5.6e-7;
            a0 = 1.58123e-6;
            a1 = -2.9331e-8;
            a2 = 1.1043e-10;
            b0 = 5.707e-6;
            b1 = -2.051e-8;
            c0 = 1.9898e-4;
            c1 = -2.376e-6;
            d = 1.83e-11;
            e = -0.765e-8;
            Ma = 28.9635 + 12.011 * (xco2 - 0.0004);
            Mv = 18.01528;
            Rair=R1/Ma*1000;
            psv = 1 * exp(A * pow(T, 2) + B * T + C + D / T); //Sat. pressure
            t = T - 273.15;
            f = alfa + bet * p + gama*pow(t, 2);
            xv = h * f * psv / p;
            Z = 1 - p / T * (a0 + a1 * t + a2 * pow(t, 2) + (b0 + b1 * t) * xv + (c0 + c1 * t) * pow(xv, 2)) + pow(p, 2) / pow(T, 2) * (d + e * pow(xv, 2)); //Z Air
            rho = p * Ma / (Z * R1 * T) * (1 - xv * (1 - Mv / Ma)) * 0.001; //Air Density
            //Calculate viscosity. Sutherland's formula
            mu= 18.27*(291.15+120)/(T+120)*pow((T/291.15),(3/2)); //10^-6 Pas
            //Calculate Speed of Sound
            sos=sqrt(1.4*Rair*T); //Cp fixed to 1.4;
            //  tView.setText(Double.toString(rho));  //Density
            tView.setText(String.format("%.4f", rho ) );//Density
            tView1.setText(String.format("%.1f", psv)); //Vapor Pressure
            tView2.setText(String.format("%.6f",Z)); //Compressibility factor
            tView3.setText(String.format("%.4f",mu/1000)); //Dynamic Viscosity mPas
            tView4.setText(String.format("%.1f",sos)); //Speed of Sound
        }
        catch (Exception e1){
            AlertDialog.Builder messagebox=new AlertDialog.Builder(this);
            messagebox.setTitle("Error");
            messagebox.setMessage(e1.getMessage());
            messagebox.setNeutralButton("OK",null);
            messagebox.show();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
      //  if (id == R.id.action_settings) {
      //      return true;
      //  }
        //About message
        if (id == R.id.about) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("About BasicAirData");
            ad.setMessage("www.basicairdata.eu");
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            ad.show();
        }

        return super.onOptionsItemSelected(item);
    }
    //Limits on entry values

}
