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
import yazilim.hilal.yesil.yhycamera.databinding.FragmentGaleryPictureBinding;
import yazilim.hilal.yesil.yhycamera.listener.OnPhoneImagesObtained;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;

public class PictureGaleryFragment extends Fragment {



    //View Objects
    private FragmentGaleryPictureBinding binding;



    //Class Objects
    public Vector<PhonePhoto> listOfPictureData;

    public GaleryItemAdapter adapter;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_galery_picture, container, false);


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



        getPicturesByOrderByDate(new OnPhoneImagesObtained() {
            @Override
            public void onComplete(Vector<PhonePhoto> listAllData) {
                listOfPictureData = listAllData;
                adapter.setDataList(listOfPictureData);
            }

            @Override
            public void onError() {

            }
        });

        return binding.getRoot();


    }


    private void getPicturesByOrderByDate(OnPhoneImagesObtained listener){

        Vector<PhonePhoto> listAllData = new Vector<PhonePhoto>();

        String[] projection = new String[] {
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };
        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


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
