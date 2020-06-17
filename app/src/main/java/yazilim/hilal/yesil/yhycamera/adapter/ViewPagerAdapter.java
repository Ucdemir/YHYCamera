package yazilim.hilal.yesil.yhycamera.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterPlayVideoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterShowPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenVideoBinding;
import yazilim.hilal.yesil.yhycamera.fragments.TakenFragment;
import yazilim.hilal.yesil.yhycamera.listener.HideMediaController;
import yazilim.hilal.yesil.yhycamera.pojo.DataClass;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

public class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;

    private int delay = 500;
    private Handler handler = new Handler();



    public ViewPagerAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            case 1:
                AdapterShowPhotoBinding binding = DataBindingUtil.inflate(mInflater, R.layout.adapter_show_photo, parent, false);
                return new ViewHolderPhoto(binding.getRoot(),binding);
            case 2:
                AdapterPlayVideoBinding bindingg = DataBindingUtil.inflate(mInflater, R.layout.adapter_play_video, parent, false);
                return new ViewHolderVideo(bindingg.getRoot(),bindingg);

        }
        return null;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DataClass data = DataAdapter.list.get(position);

        switch (holder.getItemViewType()) {
            case 1:
                ViewHolderPhoto viewHolder = (ViewHolderPhoto) holder;
                File imgFile = new  File(data.getPath());

                if(imgFile.exists()){




                    Glide.with(context)
                            .load(new File(data.getPath()))
                            .centerCrop()
                            .into(viewHolder.binding.shownPhoto);
                }





                break;

            case 2:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo)holder;

                File videoFile = new  File(data.getPath());

               /* if(videoFile.exists()) {
                    Glide.with(context)
                            .asBitmap()
                            .centerCrop()
                            .load(data.getPath())
                            .into(viewHolderVideo.binding.takenVideo);
                }*/


               if(videoFile.exists()) {






                   viewHolderVideo.binding.videoView.setVideoPath(data.getPath());
                   viewHolderVideo.binding.videoView.seekTo(randomSeekNumber());

                   viewHolderVideo.binding.btnPlay.setClickable(false);


                   MediaController mediaController = new MediaController(context){

                       @Override
                       public boolean dispatchKeyEvent(KeyEvent event) {
                           if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                               super.hide();

                               viewHolderVideo.binding.videoView.setMediaController(null);
                               viewHolderVideo.binding.videoView.setMediaController(this);
                               handler.removeCallbacksAndMessages(null);

                               ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStack();



                               return true;
                           }
                           return super.dispatchKeyEvent(event);
                       }
                   };







                   viewHolderVideo.binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){


                       @Override
                       public void onCompletion(MediaPlayer mp) {



                           viewHolderVideo.binding.btnPlay.setVisibility(View.VISIBLE);

                           viewHolderVideo.binding.videoView.setMediaController(null);
                           viewHolderVideo.binding.videoView.setMediaController(mediaController);
                           handler.removeCallbacksAndMessages(null);


                       }


                   });
                   viewHolderVideo.binding.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                       @Override
                       public boolean onInfo(MediaPlayer mp, int what, int extra) {



                           if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                               handler.postDelayed(
                                       new Runnable() {
                                           public void run() {
                                               //mediaController.show(0);
                                               handler.postDelayed(this, delay);;
                                           }},
                                       delay);

                               return true;
                           }
                           return false;
                       }


                   });





                   TakenFragment.mediaControllerOnHide(new HideMediaController() {
                       @Override
                       public void hideContoller() {


                           viewHolderVideo.binding.btnPlay.setVisibility(View.VISIBLE);
                           viewHolderVideo.binding.videoView.setMediaController(null);
                           viewHolderVideo.binding.videoView.setMediaController(mediaController);
                           handler.removeCallbacksAndMessages(null);

                       }
                   });

                   //------



                   viewHolderVideo.itemView.setOnClickListener(v->{

                       mediaController.show(0);
                       viewHolderVideo.binding.btnPlay.setVisibility(View.GONE);

                       mediaController.setAnchorView(viewHolderVideo.binding.videoView);



                       Uri uri = Uri.parse(data.getPath());


                       viewHolderVideo.binding.videoView.setMediaController(mediaController);
                       viewHolderVideo.binding.videoView.setVideoURI(uri);
                       viewHolderVideo.binding.videoView.requestFocus();
                       viewHolderVideo.binding.videoView.start();


                   });
               }


                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        DataClass data = DataAdapter.list.get(position);

        if(data.getType() == DataClass.DataType.Photo ){
            return 1;
        }else{
            return 2;
        }
    }
    @Override
    public int getItemCount() {
        return DataAdapter.list.size();
    }
    public class ViewHolderPhoto extends RecyclerView.ViewHolder {

        private AdapterShowPhotoBinding binding;

        ViewHolderPhoto(View itemView,AdapterShowPhotoBinding binding) {
            super(itemView);
            this.binding = binding;

        }
    }


    public class ViewHolderVideo extends RecyclerView.ViewHolder  {

        private AdapterPlayVideoBinding binding;

        ViewHolderVideo(View itemView,AdapterPlayVideoBinding binding) {
            super(itemView);
            this.binding = binding;


        }


    }

    private  int randomSeekNumber(){


        Random random = new Random();

        return (random.nextInt(1000));
    }


   /* private void onPlayVideo(ViewHolderVideo viewHolderVideo,String path){
        viewHolderVideo.binding.btnPlay.setVisibility(View.GONE);
        MediaController mediaController = new MediaController(context){

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    super.hide();

                    viewHolderVideo.binding.videoView.setMediaController(null);
                    viewHolderVideo.binding.videoView.setMediaController(this);
                    handler.removeCallbacksAndMessages(null);

                    ((AppCompatActivity) getContext()).getSupportFragmentManager().popBackStack();



                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        };



        mediaController.show(0);
        viewHolderVideo.binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){



            @Override
            public void onCompletion(MediaPlayer mp) {
                //TakenFragment.shouldViewPagerSwipe(true);


                viewHolderVideo.binding.btnPlay.setVisibility(View.VISIBLE);

                viewHolderVideo.binding.videoView.setMediaController(null);
                viewHolderVideo.binding.videoView.setMediaController(mediaController);
                handler.removeCallbacksAndMessages(null);


            }


        });
        viewHolderVideo.binding.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {





                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    handler.postDelayed(
                            new Runnable() {
                                public void run() {
                                    mediaController.show(0);
                                    handler.postDelayed(this, delay);;
                                }},
                            delay);

                    return true;
                }
                return false;
            }


        });



        mediaController.setAnchorView(viewHolderVideo.binding.videoView);



        Uri uri = Uri.parse(path);


        viewHolderVideo.binding.videoView.setMediaController(mediaController);
        viewHolderVideo.binding.videoView.setVideoURI(uri);
        viewHolderVideo.binding.videoView.requestFocus();
        viewHolderVideo.binding.videoView.start();


        TakenFragment.mediaControllerOnHide(new HideMediaController() {
            @Override
            public void hideContoller() {
                viewHolderVideo.binding.btnPlay.setVisibility(View.VISIBLE);
                viewHolderVideo.binding.videoView.setMediaController(null);
                viewHolderVideo.binding.videoView.setMediaController(mediaController);
                handler.removeCallbacksAndMessages(null);

            }
        });


    }*/
}