package yazilim.hilal.yesil.yhycamera.fragments.galery;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.net.URLConnection;
import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.activity.MainActivity;
import yazilim.hilal.yesil.yhycamera.data.DataManager;
import yazilim.hilal.yesil.yhycamera.databinding.FragmentGaleryBinding;
import yazilim.hilal.yesil.yhycamera.listener.OnPhoneImagesObtained;
import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;
import static yazilim.hilal.yesil.yhycamera.data.DataManager.logD;


public class YHYGalery  extends Fragment{


    //View Objects
    private FragmentGaleryBinding binding;
    private  TabLayout tabLayout;



    //Class objects
    private Context context;
    private KProgressHUD hud;
    private Vector<PhoneAlbum> albums;
    private Vector<PhoneAlbum> allData;
    private AlbumPagerAdapter adapterAlbum;



    //Class Variables
    private  String currentAlbumName;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_galery, container, false);


       tabLayout =  binding.albumTabLayout;
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_all)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_pictures)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_videos)));



        adapterAlbum = new AlbumPagerAdapter(this);

        binding.albumViewPager.setAdapter(adapterAlbum);

      /* hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.loading))
                .setDetailsLabel(getString(R.string.getting_albums))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.7f)
                .show();*/


         /*getPhoneAlbums(context, new OnPhoneImagesObtained() {
             @Override
             public void onComplete(Vector<PhoneAlbum> albums) {

                 if(albums.size()>0){

                     currentAlbumName = albums.get(0).getName();
                     YHYGalery.this.albums = albums;
                     binding.txtCurrentAlbum.setText(currentAlbumName);

                 }

                 hud.dismiss();
             }

             @Override
             public void onError() {

             }
         });*/

        /* getDataOrderByDate(new OnPhoneImagesObtained() {
             @Override
             public void onComplete(Vector<PhoneAlbum> albums) {

                 adapterAlbum.se
             }

             @Override
             public void onError() {

             }
         });*/


         binding.selectAlbum.setOnClickListener(v->{

             dialogSelectAlbums();

         });

         return binding.getRoot();

    }

    public static void getPhoneAlbums( Context context , OnPhoneImagesObtained listener ){
        // Creating vectors to hold the final albums objects and albums names
        Vector<PhoneAlbum> albumsWithPictures = new Vector<>();
        Vector< String > albumsNames = new Vector<>();

        // which image properties are we querying
        String[] projection = new String[] {
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };

        // content: style URI for the "primary" external storage volume
       /* Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;*/



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
                null        // Ordering
        );

        if ( cur != null && cur.getCount() > 0 ) {
            Log.i("DeviceImageManager"," query count=" + cur.getCount());

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

                    if ( albumsNames.contains( bucketName ) ) {
                        for ( PhoneAlbum album : albumsWithPictures ) {
                            if ( album.getName().equals( bucketName ) ) {
                                album.getAlbumPhotos().add( phonePhoto );
                                Log.i( "DeviceImageManager", "A photo was added to album => " + bucketName );
                                break;
                            }
                        }
                    } else {
                        PhoneAlbum album = new PhoneAlbum();
                        Log.i( "DeviceImageManager", "A new album was created => " + bucketName );
                        album.setId( phonePhoto.getId() );
                        album.setName( bucketName );
                        album.setCoverUri( phonePhoto.getPhotoUri() );
                        album.getAlbumPhotos().add( phonePhoto );
                        Log.i( "DeviceImageManager", "A photo was added to album => " + bucketName );

                        albumsWithPictures.add( album );
                        albumsNames.add( bucketName );





                       // logD("AlbumName:"+ bucketName+" , isVideo"+Boolean.toString(isImageFile(data)));
                    }

                } while (cur.moveToNext());
            }

            cur.close();
            listener.onComplete( albumsWithPictures );
        } else {
            listener.onError();
        }




    }





    private void dialogSelectAlbums(){


        AlertDialog.Builder adb = new AlertDialog.Builder(getContext(),R.style.CornerRadiusDialog);


        adb.setSingleChoiceItems(convertAllbumToArray(albums), 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {

                switch (n){

                    case 0:



                        break;

                    case 1:




                        break;

                    case 2:




                        break;

                    case 3:




                        break;

                }
                d.dismiss();


            }

        });
        adb.setNegativeButton("İptal", null);

        adb.setTitle("Sıralama");
        adb.show();

    }

    public CharSequence[] convertAllbumToArray(Vector<PhoneAlbum> albums){

        CharSequence[] arr = new CharSequence[albums.size()];

        for(int k =0 ; k<albums.size(); k++){
            arr[k] = albums.get(k).getName();
        }

        return arr;
    }

    private class AlbumPagerAdapter extends FragmentStateAdapter {
        public AlbumPagerAdapter(Fragment fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0:
                    return new AllGaleryFragment();
                case 1:
                    return new PictureGaleryFragment();

                case 2:

                    return new VideosGaleryFragment();


                default:

                    return new AllGaleryFragment();
            }


        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
}


