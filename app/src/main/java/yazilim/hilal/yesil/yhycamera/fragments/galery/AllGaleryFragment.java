package yazilim.hilal.yesil.yhycamera.fragments.galery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.adapter.galery.GaleryItemAdapter;
import yazilim.hilal.yesil.yhycamera.databinding.FragmentGaleryAllBinding;
import yazilim.hilal.yesil.yhycamera.listener.OnPhoneImagesObtained;
import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;

public class AllGaleryFragment  extends Fragment {


    //View Objects
    FragmentGaleryAllBinding binding;


    //Class Objects
    private GridLayoutManager glm;//Adapter
    public GaleryItemAdapter adapter;

    private Context context;

    public Vector<PhonePhoto> listOfAllGaleryData;

    public AllGaleryFragment(){

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_galery_all, container, false);



        adapter = new GaleryItemAdapter(context);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false){

            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {

                lp.width = getWidth()/3;
                lp.height = getWidth()/3;
                return true;
            }
        });
        binding.recyclerView.setAdapter(adapter);



        getMixedDataOrderByDate(new OnPhoneImagesObtained() {
            @Override
            public void onComplete(Vector<PhonePhoto> listAllData) {
                listOfAllGaleryData = listAllData;
                adapter.setDataList(listOfAllGaleryData);
        }

            @Override
            public void onError() {

            }
        });


       // binding.txtCurrentAlbum.setText(getString(R.string.albumAll));

        return binding.getRoot();
    }


    private void getMixedDataOrderByDate(OnPhoneImagesObtained listener){


        Vector<PhonePhoto> listAllData = new Vector<PhonePhoto>();

        String[] projection = new String[] {
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };
        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC, " + MediaStore.Video.VideoColumns.DATE_TAKEN  + " DESC";

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        Uri queryUri = MediaStore.Files.getContentUri("external");

        // Make the query.
        Cursor cur = context.getContentResolver().query(queryUri,
                projection, // Which columns to return
                selection,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                orderBy     // Ordering
        );

        if ( cur != null && cur.getCount() > 0 ) {


            if (cur.moveToFirst()) {

                int columndDataName = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int imageUriColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);

                int imageIdColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID );

                do {



                    PhonePhoto phonePhoto = new PhonePhoto();

                    phonePhoto.setId( Integer.valueOf( cur.getString( imageIdColumn)));
                    phonePhoto.setPhotoName( cur.getString( columndDataName ) );
                    phonePhoto.setPhotoUri( cur.getString(imageUriColumn));


                    listAllData.add(phonePhoto);



                } while (cur.moveToNext());
            }

            cur.close();

            listener.onComplete(listAllData);
        } else {

        }

    }
}
