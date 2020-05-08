package yazilim.hilal.yesil.yhycamera.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.activity.MainActivity;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenPhotoBinding;
import yazilim.hilal.yesil.yhycamera.databinding.AdapterTakenVideoBinding;
import yazilim.hilal.yesil.yhycamera.fragments.CameraFragment;
import yazilim.hilal.yesil.yhycamera.fragments.TakenFragment;
import yazilim.hilal.yesil.yhycamera.pojo.DataClass;

public class DataAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static List<DataClass> list;


    private LayoutInflater mInflater;
    private Context context;

    public DataAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
        this.context = context;
        if(list == null){
            list = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case 1:
                AdapterTakenPhotoBinding binding = DataBindingUtil.inflate(mInflater, R.layout.adapter_taken_photo, parent, false);
                return new ViewHolderPhoto(binding.getRoot(),binding);
            case 2:
                AdapterTakenVideoBinding bindingg = DataBindingUtil.inflate(mInflater, R.layout.adapter_taken_video, parent, false);
                return new ViewHolderVideo(bindingg.getRoot(),bindingg);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        DataClass data = list.get(position);

        switch (holder.getItemViewType()) {
            case 1:
                ViewHolderPhoto viewHolder = (ViewHolderPhoto) holder;
                File imgFile = new  File(data.getPath());

                if(imgFile.exists()){




                    Glide.with(context)
                            .load(new File(data.getPath()))
                            .centerCrop()
                            .into(viewHolder.binding.takenPhoto);
                }





                break;

            case 2:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo)holder;

                File videoFile = new  File(data.getPath());

                if(videoFile.exists()) {
                    Glide.with(context)
                            .asBitmap()
                            .centerCrop()
                            .load(data.getPath())
                            .into(viewHolderVideo.binding.takenVideo);
                }

                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {

        DataClass data = list.get(position);

        if(data.getType() == DataClass.DataType.Photo ){
            return 1;
        }else{
            return 2;
        }
    }

    public class ViewHolderPhoto extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterTakenPhotoBinding binding;
        public ViewHolderPhoto(View convertView,AdapterTakenPhotoBinding binding) {
            super(convertView);

            this.binding = binding;
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            startFragment(getAdapterPosition());


        }
    }

    public class ViewHolderVideo extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterTakenVideoBinding binding;

        public ViewHolderVideo(View convertView,AdapterTakenVideoBinding binding) {
            super(convertView);

            this.binding = binding;
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            startFragment(getAdapterPosition());





        }
    }


    private void startFragment(int position){
        TakenFragment fragment = new TakenFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("currentItem",position);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).addToBackStack(null)
                .commit();
    }


    public void addToList(DataClass data){

        list.add(data);
        notifyDataSetChanged();
    }

    private MainActivity getActivity(){
        return (MainActivity)context;
    }
}
