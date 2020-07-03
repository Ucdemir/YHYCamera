package yazilim.hilal.yesil.yhycamera.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.databinding.ActivityCameraBinding;
import yazilim.hilal.yesil.yhycamera.fragments.camera2.CameraFragment;
import yazilim.hilal.yesil.yhycamera.fragments.galery.YHYGalery;

public class MainActivity extends AppCompatActivity {


    private ActivityCameraBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      ActivityCameraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);



      binding.btnCamara2.setOnClickListener(v->{

          getSupportFragmentManager().beginTransaction()
                      .replace(R.id.container, CameraFragment.newInstance(),"Camera").addToBackStack(null)
                      .commit();

      });

      binding.btnCamara1.setOnClickListener(v->{

      });



      binding.btnGalery.setOnClickListener(v->{
          getSupportFragmentManager().beginTransaction()
                  .replace(R.id.container, new YHYGalery(),"Galery").addToBackStack(null)
                  .commit();

      });



    }


    private void invisibleLL(){
    }
}
