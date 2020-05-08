package yazilim.hilal.yesil.yhycamera.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterPlayVideoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterShowPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenVideoBinding;
import yazilim.hilal.yesil.yhycamera.pojo.DataClass;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

public class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;




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
                   viewHolderVideo.binding.videoView.seekTo(1);


                   viewHolderVideo.binding.btnPlay.setOnClickListener(v -> {
                       MediaController mediaController = new MediaController(context);
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


    public class ViewHolderVideo extends RecyclerView.ViewHolder {

        private AdapterPlayVideoBinding binding;

        ViewHolderVideo(View itemView,AdapterPlayVideoBinding binding) {
            super(itemView);
            this.binding = binding;

        }
    }


}