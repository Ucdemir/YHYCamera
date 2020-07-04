package yazilim.hilal.yesil.yhycamera.adapter.galery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.net.URLConnection;
import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.adapter.DataAdapter;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterGaleryItemPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterGaleryItemVideoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterHorizantalTakenPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterHorizantalTakenVideoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenVideoBinding;
import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;
import yazilim.hilal.yesil.yhycamera.pojo.DataClass;

public class GaleryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    //Class Objects
    private  Context context;
    private Vector<PhonePhoto> albumPhotos = new Vector<PhonePhoto>();


    //Variables
    private LayoutInflater mInflater;

    public GaleryItemAdapter(Context context) {

        this.context = context;

        mInflater = LayoutInflater.from(context);

    }


    @Override
    public int getItemViewType(int position) {

        PhonePhoto data = albumPhotos.get(position);

        if(isImageFile(data.getPhotoUri()) ){
            return 1;
        }else{
            return 2;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        switch (viewType){
            case 1:
               AdapterGaleryItemPhotoBinding binding = DataBindingUtil.inflate(
                        mInflater, R.layout.adapter_galery_item_photo, parent, false);

                return new ViewHolderPhoto(binding.getRoot(),binding);

            case 2:
                AdapterGaleryItemVideoBinding bindingg = DataBindingUtil.inflate(
                        mInflater, R.layout.adapter_galery_item_video, parent, false);

                return new ViewHolderVideo(bindingg.getRoot(),bindingg);


        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PhonePhoto data = albumPhotos.get(position);

        File file = new File(data.getPhotoUri());
        switch (holder.getItemViewType()){
            case 1:
          ViewHolderPhoto viewHolder = (ViewHolderPhoto) holder;

                viewHolder.binding.root.setBackgroundColor(Color.parseColor("#ffffff"));
                if(file.exists()){


                    Glide.with(context)
                            .load(file)
                            .centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    viewHolder.binding.root.setBackgroundColor(Color.parseColor("#484848"));
                                    return false;
                                }
                            })
                            .into(viewHolder.binding.takenPhoto);
                }


                break;


            case 2:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo)holder;

                viewHolderVideo.binding.root.setBackgroundColor(Color.parseColor("#ffffff"));
                if(file.exists()){
                    Glide.with(context)
                            .asBitmap()
                            .centerCrop()
                            .load(file)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {


                                    viewHolderVideo.binding.root.setBackgroundColor(Color.parseColor("#484848"));

                                    return false;
                                }
                            })
                            .into(viewHolderVideo.binding.takenVideo);

                }

                break;

        }
    }

    @Override
    public int getItemCount() {
        return albumPhotos.size();
    }

    public class ViewHolderPhoto extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterGaleryItemPhotoBinding binding;

        public ViewHolderPhoto(View convertView,AdapterGaleryItemPhotoBinding binding) {
            super(convertView);

            this.binding = binding;
            convertView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            //startFragment(getAdapterPosition());


        }
    }

    public class ViewHolderVideo extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterGaleryItemVideoBinding binding;


        public ViewHolderVideo(View convertView,AdapterGaleryItemVideoBinding binding) {
            super(convertView);

            this.binding = binding;
            convertView.setOnClickListener(this);
        }




        @Override
        public void onClick(View v) {




        }
    }


    public void setDataList(Vector<PhonePhoto> photoAlbums){

        this.albumPhotos = photoAlbums;
        this.notifyDataSetChanged();


    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
}
