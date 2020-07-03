package yazilim.hilal.yesil.yhycamera.adapter.galery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
import yazilim.hilal.yesil.yhycamera.pojo.DataClass;

public class GaleryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    //Class Objects
    private  Context context;
    private Vector<PhoneAlbum> albums = new Vector<PhoneAlbum>();


    //Variables
    private LayoutInflater mInflater;

    public GaleryItemAdapter(Context context) {

        this.context = context;

        mInflater = LayoutInflater.from(context);

    }


    @Override
    public int getItemViewType(int position) {

        PhoneAlbum data = albums.get(position);

        if(isImageFile(data.getCoverUri()) ){
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

        PhoneAlbum data = albums.get(position);

        File file = new File(data.getCoverUri());
        switch (holder.getItemViewType()){
            case 1:
          ViewHolderPhoto viewHolder = (ViewHolderPhoto) holder;
                if(file.exists()){


                    Glide.with(context)
                            .load(file)
                            .centerCrop()
                            .into(viewHolder.binding.takenPhoto);
                }


                break;


            case 2:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo)holder;
                if(file.exists()){
                    Glide.with(context)
                            .asBitmap()
                            .centerCrop()
                            .load(file)
                            .into(viewHolderVideo.binding.takenVideo);

                }

                break;

        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
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


    public void setDataList(Vector<PhoneAlbum> albums){

        this.albums = albums;
        this.notifyDataSetChanged();


    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
}
