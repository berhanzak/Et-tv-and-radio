package stream.site90.xoolu.com.xoolutvandradio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import stream.site90.xoolu.com.xoolutvandradio.Fragment.Radio;
import stream.site90.xoolu.com.xoolutvandradio.Fragment.Record;
import stream.site90.xoolu.com.xoolutvandradio.Fragment.Tv;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //main fragment for bottom nav
    private Tv tv;
    private Radio radio;
    private Record record;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //adding a listener for the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        //initiating fragment transaction
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();


        //check if the base fragment contain tv_image fragment
        if(getSupportFragmentManager().findFragmentById(R.id.base) instanceof Tv){

            //get the tv_image fragment from fragment manager by its tag
            tv =(Tv) getSupportFragmentManager().findFragmentByTag("TV");

            //if tv_image fragment is not found then create one and replace the base container by the new tv_image fragment
            if(tv ==null){
                tv = new Tv();
                ft.add(R.id.base, tv,"TV");

             //if tv_image fragment is found replace the base container by the tv_image
            }else {
                ft.replace(R.id.base, tv);
            }

            //check if the base fragment contain radio fragment
        }else if(getSupportFragmentManager().findFragmentById(R.id.base) instanceof Radio){

                //get the radio fragment from fragment manager by its tag
                radio =(Radio) getSupportFragmentManager().findFragmentByTag("RADIO");

                //if radio fragment is not found then create one and replace the base container by the new radio fragment
                if(radio ==null){
                    radio=new Radio();
                    ft.add(R.id.base, radio,"RADIO");

                    //if tv_image fragment is found replace the base container by the radio
                }else {
                    ft.replace(R.id.base, radio);
                }

        }else if(getSupportFragmentManager().findFragmentById(R.id.base) instanceof Record){

            //get the radio fragment from fragment manager by its tag
            record =(Record) getSupportFragmentManager().findFragmentByTag("RECORD");

            //if recod fragment is not found then create one and replace the base container by the new record fragment
            if(record ==null){
                record=new Record();
                ft.add(R.id.base, record,"RECORD");

                //if record fragment is found replace the base container by the record
            }else {
                ft.replace(R.id.base, record);
            }
         //if there is no any fragment inside the fragment manager then create a new fragment and replasce the base container by the new tv_image fragment
        }else {

            tv = new Tv();
            ft.add(R.id.base, tv,"TV");
        }

        ft.commit();

    }



    //called when bottom nav item are selected
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //initiating fragment transaction
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();

        switch(menuItem.getItemId()) {

            //called when the tv_image bottom item pressed
            case R.id.bottom_tv:
                //get the tv_image fragment from fragment manager by its tag
                tv =(Tv) getSupportFragmentManager().findFragmentByTag("TV");

                //if tv_image fragment is not found then create one and replace the base container by the new tv_image fragment
                if(tv ==null){
                    tv = new Tv();
                    ft.replace(R.id.base, tv,"TV");

                    //if tv_image fragment is found replace the base container by the tv_image
                }else {
                    ft.replace(R.id.base, tv);
                }

                break;

            //called when the radio bottom item pressed
            case R.id.bottom_radio:

                //get the radio fragment from fragment manager by its tag
                radio =(Radio) getSupportFragmentManager().findFragmentByTag("RADIO");

                //if radio fragment is not found then create one and replace the base container by the new radio fragment
                if(radio ==null){
                    radio=new Radio();
                    ft.replace(R.id.base, radio,"RADIO");

                    //if tv_image fragment is found replace the base container by the radio
                }else {
                    ft.replace(R.id.base, radio);
                }
                break;

            //called when the record bottom item pressed
            case R.id.bottom_mine:

                record =(Record) getSupportFragmentManager().findFragmentByTag("RECORD");
                //if record fragment is not found then create one and replace the base container by the new record fragment
                if(record ==null){
                    record=new Record();
                    ft.replace(R.id.base, record,"RECORD");
                    //if record fragment is found replace the base container by the record
                }else {
                    ft.replace(R.id.base, record);
                }
        }
        //setting the animation for fragment transaction
//        ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        ft.commit();
        return true;
    }
}
