package com.example.root.formarsupport;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class Sign_Up_As extends AppCompatActivity {
    Bundle bundle;
    static String chosen;
    Animation anim_alpha = null;
    broker_signUP broker_signUP =new broker_signUP();
    Farmer_signUp farmer_signUp=new Farmer_signUp();
    Fertilizer_signUp fertilizer=new Fertilizer_signUp();
    trader_sign_up trader_sign_up=new trader_sign_up();
    FragmentTransaction transaction;
    FragmentManager manager=getFragmentManager();
    static String Former_choice=null;
    String CamOrGal;
    private FarmerChoiceDone farmerChoiceDone;
    public static String cameraIs=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_as);
        anim_alpha= AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        bundle = getIntent().getExtras();
        chosen = bundle.getString("signUp_as");
        Log.d("chosenValue", chosen);
        // farmerChoiceDone= (FarmerChoiceDone) this;
        go_to_page(chosen);
    }

    /**
     * https://stackoverflow.com/questions/45602139/casting-interface-in-activity
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FarmerChoiceDone) {
            farmerChoiceDone = (FarmerChoiceDone) fragment;
        }
    }

    private void go_to_page(String chosen) {

        if(chosen.equals("farmer"))
        {
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame,farmer_signUp);
            transaction.commit();
        }
        else if(chosen.equals("fertilizer"))
        {
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame,fertilizer);
            transaction.commit();

        }
        else if(chosen.equals("trader"))
        {
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame,trader_sign_up);
            transaction.commit();
        }
        else{
            transaction=manager.beginTransaction();
            transaction.replace(R.id.frame, broker_signUP);
            transaction.commit();

        }
    }
    public void move_forward(View view)
    {
        view.startAnimation(anim_alpha);

            if (Former_choice == null) {

                Toast.makeText(this, R.string.atleastOne, Toast.LENGTH_LONG).show();
            } else {
                farmerChoiceDone.sendData(Former_choice);
            }

    }
    public void crops(View view)
    {
        Former_choice="crop";
    }
    public void fruits_vegetables(View view)
    {
        Former_choice="fruit";
    }

    public void use_camera(View view) {
        CamOrGal="camera";
        farmerChoiceDone.sendData(CamOrGal);
    }

    public void use_gallery(View view) {
        CamOrGal="gallery";
        farmerChoiceDone.sendData(CamOrGal);
    }

   /* public interface FarmerChoiceDone{


    }*/


}

