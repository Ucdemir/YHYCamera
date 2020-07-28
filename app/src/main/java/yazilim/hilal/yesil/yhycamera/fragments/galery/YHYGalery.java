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
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.activity.MainActivity;
import yazilim.hilal.yesil.yhycamera.data.DataManager;
import yazilim.hilal.yesil.yhycamera.databinding.FragmentGaleryBinding;
import yazilim.hilal.yesil.yhycamera.listener.OnPhoneImagesObtained;
import yazilim.hilal.yesil.yhycamera.models.AlbumsWithTypes;
import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;


import static yazilim.hilal.yesil.yhycamera.data.DataManager.logD;


public class YHYGalery  extends Fragment{


    //View Objects
    private FragmentGaleryBinding binding;
    private  TabLayout tabLayout;



    //Class objects
    private  AllGaleryFragment allGaleryFragment = new AllGaleryFragment();
    private PictureGaleryFragment pictureGaleryFragment = new PictureGaleryFragment();
    private VideosGaleryFragment videosGaleryFragment = new VideosGaleryFragment();

    private CharSequence[] allAlbums;
    private CharSequence[] pictureAlbums;
    private CharSequence[]  videoAlbums;

    private Context context;
    private KProgressHUD hud;

    private Vector<PhoneAlbum> albumsWithData;
    private AlbumsWithTypes allAllbumNames = new AlbumsWithTypes();

    private AlbumPagerAdapter adapterAlbum;



    //Class Variables
    private  String currentAlbumName;
    private int currentTabIndex = 0;


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

       hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.loading))
                .setDetailsLabel(getString(R.string.getting_albums))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.7f)
                .show();


        getAllAlbumsWithTheirData(context, new OnPhoneImagesObtained() {
             @Override
             public void onComplete(Vector<PhoneAlbum> albumsWithData, AlbumsWithTypes allAllbumNames) {

                 if(albumsWithData.size()>0){


                     YHYGalery.this.albumsWithData = albumsWithData;
                     YHYGalery.this.allAllbumNames = allAllbumNames;
                     setAlbumsArr();


                 }

                 hud.dismiss();
             }

             @Override
             public void onError() {

             }
         });

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





            switch (currentTabIndex){
                case 0:

                    dialogSelectAlbums(context,allAlbums );
                    break;


                case 1:

                    dialogSelectAlbums(context, pictureAlbums);


                    break;


                case 2:

                    dialogSelectAlbums(context, videoAlbums);

                    break;

            }
        });



        currentAlbumName = getString(R.string.albumAll);

        binding.txtCurrentAlbum.setText(currentAlbumName);

        binding.albumViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                currentTabIndex = position;

                binding.albumTabLayout.setScrollPosition(position,0f,true);



                switch (position){
                    case 0 :
                        int currentAlbumIndex = findAllAlbumPositionInsideDialog(currentAlbumName);


                        if(currentAlbumIndex == 0){
                            allGaleryFragment.adapter.setDataList(allGaleryFragment.listOfAllGaleryData);
                            binding.txtCurrentAlbum.setText(getString(R.string.albumAll));
                        }else {
                            allGaleryFragment.adapter.setDataList(albumsWithData.get(findAlbumPositionForAllAlbum(currentAlbumName)).getAlbumAll());


                            binding.txtCurrentAlbum.setText(currentAlbumName);
                        }

                        break;



                    case 1:
                        currentAlbumIndex = findPictureAlbumPositionInsideDialog(currentAlbumName);




                        if(currentAlbumIndex == 0){

                            pictureGaleryFragment.adapter.setDataList(pictureGaleryFragment.listOfPictureData);
                            binding.txtCurrentAlbum.setText(getString(R.string.albumAll));

                        }else {
                            pictureGaleryFragment.adapter.setDataList(albumsWithData.get(findAlbumPositionForAllAlbum(currentAlbumName)).getAlbumPhotos());
                            binding.txtCurrentAlbum.setText(currentAlbumName);
                        }
                        break;



                    case 2:

                        currentAlbumIndex = findVideoAlbumPositionInsideDialog(currentAlbumName);

                        if(currentAlbumIndex == 0){

                            videosGaleryFragment.adapter.setDataList(videosGaleryFragment.listOfVideoGaleryData);
                            binding.txtCurrentAlbum.setText(getString(R.string.albumAll));

                        }else {
                            videosGaleryFragment.adapter.setDataList(albumsWithData.get(findAlbumPositionForAllAlbum(currentAlbumName)).getAlbumVideos());
                            binding.txtCurrentAlbum.setText(currentAlbumName);

                        }

                        break;
                }


            }


        });



         return binding.getRoot();

    }

    public static void getAllAlbumsWithTheirData( Context context , OnPhoneImagesObtained listener ){
        // Creating vectors to hold the final albums objects and albums names
        Vector<PhoneAlbum> albumsWithData = new Vector<>();
        AlbumsWithTypes  listOfAlbums = new  AlbumsWithTypes();

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


        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC, " + MediaStore.Video.VideoColumns.DATE_TAKEN  + " DESC";
        // Make the query.
        Cursor cur = context.getContentResolver().query(queryUri,
                projection, // Which columns to return
                selection,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                orderBy      // Ordering
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

               a: do {
                    // Get the field values
                    bucketName = cur.getString( bucketNameColumn );
                    data = cur.getString( imageUriColumn );
                    imageId = cur.getString( imageIdColumn );

                    // Adding a new PhonePhoto object to phonePhotos vector
                    PhonePhoto phonePhoto = new PhonePhoto();
                    phonePhoto.setPhotoName( bucketName );
                    phonePhoto.setPhotoUri( data );
                    phonePhoto.setId( Integer.valueOf( imageId ) );


                   PhoneAlbum album = new PhoneAlbum();
                   Log.i( "DeviceImageManager", "A new album was created => " + bucketName );
                   album.setId( phonePhoto.getId() );
                   album.setName( bucketName );
                   album.setCoverUri( phonePhoto.getPhotoUri() );


                   Log.i( "DeviceImageManager", "A photo was added to album => " + bucketName );

                   boolean isAlbumNameFound = false;

                   if(isImageFile(data)) {

                       for(String iterateAlbumName : listOfAlbums.photoAlbums){
                           if(iterateAlbumName.equals(bucketName)){
                              isAlbumNameFound = true;
                           }

                       }

                       if(!isAlbumNameFound){
                           listOfAlbums.photoAlbums.add(bucketName);
                       }

                       album.getAlbumPhotos().add( phonePhoto );

                   }
                   isAlbumNameFound = false;
                   if(!isImageFile(data)) {

                       for(String iterateAlbumName : listOfAlbums.videoAlbums){
                           if(iterateAlbumName.equals(bucketName)){
                            isAlbumNameFound = true;
                           }

                       }


                       if(!isAlbumNameFound){
                           listOfAlbums.videoAlbums.add(bucketName);
                       }

                       album.getAlbumVideos().add( phonePhoto );

                   }

                   album.getAlbumAll().add(phonePhoto);
                   albumsWithData.add( album );




                } while (cur.moveToNext());
            }

            cur.close();
            listener.onComplete( albumsWithData,listOfAlbums );
        } else {
            listener.onError();
        }




    }


    public  void dialogSelectAlbums(Context context,CharSequence[] arr){


        AlertDialog.Builder adb = new AlertDialog.Builder(context, R.style.CornerRadiusDialog);


        adb.setSingleChoiceItems(arr, findDialogCurrentIndex(arr), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {


                currentAlbumName = arr[n].toString();

                switch (currentTabIndex){

                    case 0:


                        if (n == 0) {
                            allGaleryFragment.adapter.setDataList(allGaleryFragment.listOfAllGaleryData);
                            binding.txtCurrentAlbum.setText(getString(R.string.albumAll));

                        }else{
                            allGaleryFragment.adapter.setDataList(albumsWithData.get(findAlbumPositionForAllAlbum(currentAlbumName)).getAlbumAll());
                            binding.txtCurrentAlbum.setText(arr[n]);
                        }





                        break;


                    case 1:


                        if(n != 0){
                            pictureGaleryFragment.adapter.setDataList(albumsWithData.get(findAlbumPositionForAllAlbum(currentAlbumName)).getAlbumPhotos());
                            binding.txtCurrentAlbum.setText(arr[n]);
                        }else{
                            pictureGaleryFragment.adapter.setDataList(pictureGaleryFragment.listOfPictureData);
                            binding.txtCurrentAlbum.setText(getString(R.string.albumAll));
                        }

                        break;


                    case 2:

                        if(n != 0){
                            videosGaleryFragment.adapter.setDataList(albumsWithData.get(findAlbumPositionForAllAlbum(currentAlbumName)).getAlbumVideos());
                            binding.txtCurrentAlbum.setText(arr[n]);
                        }else{
                            videosGaleryFragment.adapter.setDataList(videosGaleryFragment.listOfVideoGaleryData);
                            binding.txtCurrentAlbum.setText(getString(R.string.albumAll));
                        }


                        break;
                }






                d.dismiss();


            }

        });
        adb.setNegativeButton("İptal", null);

        adb.setTitle("Albüm Seçiniz");
        adb.show();

    }





    public CharSequence[] convertAllbumToArray(AlbumsWithTypes allAllbumName) {

         List<String> allAlbumNames = new ArrayList<>();
        //CharSequence[] arr = new CharSequence[allAllbumName.videoAlbums.size() + allAllbumName.photoAlbums.size() + 1];

        allAlbumNames.add(getString(R.string.albumAll));

            a:for(String current : allAllbumName.videoAlbums){

                for(String iterate : allAlbumNames){

                    if(iterate.equals(current)){
                        continue a;
                    }
                }

                allAlbumNames.add(current);
            }



            a:for(String current : allAllbumName.photoAlbums){

                for(String iterate : allAlbumNames){

                    if(iterate.equals(current)){
                        continue a;
                    }
                }

                allAlbumNames.add(current);
            }








       /* arr[0] = getString(R.string.albumAll);

       int k = 0;
        for( ; k<allAllbumName.videoAlbums.size(); k++){
            arr[k+1] = allAllbumName.videoAlbums.get(k);
        }*/


      /*  a:for(int z =0 ; z<allAllbumName.photoAlbums.size(); z++){
            CharSequence current = allAllbumName.photoAlbums.get(z);
            for(int t=0; t< allAllbumName.videoAlbums.size();t++){
                CharSequence ch = arr[t];

                if(ch.equals(current)){
                    continue a;
                }

            }
            arr[k+1] = current;
            k++;
        }*/

       /* for(int z =0 ; z<allAllbumName.photoAlbums.size(); z++){
            arr[k+1] = allAllbumName.photoAlbums.get(z);
            k++;
        }*/

        return allAlbumNames.toArray(new CharSequence[allAlbumNames.size()]);


        //return arr;
    }

    private class AlbumPagerAdapter extends FragmentStateAdapter {
        public AlbumPagerAdapter(Fragment fa) {
            super(fa);
        }





        @Override
        public long getItemId(int position) {


            return super.getItemId(position);
        }

        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0:


                    return allGaleryFragment;
                case 1:

                    return pictureGaleryFragment;
                case 2:

                    return videosGaleryFragment;


                default:

                    return allGaleryFragment;
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

    private int findAlbumPosition( CharSequence[] arr,String name){


        for(int k=0 ; k<arr.length;k++){
            CharSequence currentName = arr[k];
            if(currentName.equals(name)){

                return k;
            }

        }


        return 0;
    }

    private int findAlbumPositionForAllAlbum(String name){

        for(int k=0; k<albumsWithData.size(); k++){

            PhoneAlbum phoneAlbum = albumsWithData.get(k);

            if(phoneAlbum.getName().equals(name)){


                return k;
            }
        }


        return 0;

    }

    private int findAllAlbumPositionInsideDialog(String name){

        for(int k=0; k < allAlbums.length; k++){
            CharSequence albumName = allAlbums[k];
            if(albumName.equals(name)){

                return k;
            }

        }
        return 0;
    }

    private int findPictureAlbumPositionInsideDialog(String name){

        for(int k=0; k < pictureAlbums.length; k++){
            CharSequence albumName = pictureAlbums[k];
            if(albumName.equals(name)){

                return k;
            }

        }
        return 0;
    }
    private int findVideoAlbumPositionInsideDialog(String name){

        for(int k=0; k < videoAlbums.length; k++){
            CharSequence albumName = videoAlbums[k];
            if(albumName.equals(name)){

                return k;
            }

        }
        return 0;
    }

    private int findDialogCurrentIndex(CharSequence[] arr){


        for(int k=0 ; k<arr.length; k++){
            String current = arr[k].toString();
            if(current.equals(currentAlbumName)){

                return k;
            }

        }

        return 0;
    }


    private void setAlbumsArr(){


        allAlbums = convertAllbumToArray(allAllbumNames);





        try {
            AlbumsWithTypes temp = (AlbumsWithTypes) allAllbumNames.clone();
            temp.videoAlbums = new ArrayList<>();

            pictureAlbums = convertAllbumToArray(temp);
            temp = (AlbumsWithTypes) allAllbumNames.clone();
            temp.photoAlbums = new ArrayList<>();
            videoAlbums =  convertAllbumToArray(temp);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }



    }
}


