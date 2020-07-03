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
    private GaleryItemAdapter adapter;

    //Vector<PhoneAlbum> allData;

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



        getDataOrderByDate(new OnPhoneImagesObtained() {
            @Override
            public void onComplete(Vector<PhoneAlbum> albums) {
                adapter.setDataList(albums);
            }

            @Override
            public void onError() {

            }
        });

        return binding.getRoot();
    }


    private void getDataOrderByDate(OnPhoneImagesObtained listener){


        Vector<PhoneAlbum> allData = new Vector<PhoneAlbum>();

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
                String bucketName;
                String data;
                String imageId;
                int bucketNameColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int imageUriColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);

                int imageIdColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID );

                do {
                    // Get the field values
                    bucketName = cur.getString( bucketNameColumn );
                    data = cur.getString( imageUriColumn );
                    imageId = cur.getString( imageIdColumn );

                    // Adding a new PhonePhoto object to phonePhotos vector
                    PhonePhoto phonePhoto = new PhonePhoto();
                    phonePhoto.setAlbumName( bucketName );
                    phonePhoto.setPhotoUri( data );
                    phonePhoto.setId( Integer.valueOf( imageId ) );


                    PhoneAlbum album = new PhoneAlbum();

                    album.setId( phonePhoto.getId() );
                    album.setName( bucketName );
                    album.setCoverUri( phonePhoto.getPhotoUri());
                    album.getAlbumPhotos().add( phonePhoto );


                    allData.add(album);



                } while (cur.moveToNext());
            }

            cur.close();

            listener.onComplete(allData);
        } else {

        }

    }
}
