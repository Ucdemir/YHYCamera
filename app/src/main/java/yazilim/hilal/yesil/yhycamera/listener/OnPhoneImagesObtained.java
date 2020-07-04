package yazilim.hilal.yesil.yhycamera.listener;

import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.models.AlbumsWithTypes;
import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;

public interface OnPhoneImagesObtained extends OnGaleryMixedDataGot {


    @Override default void onComplete(Vector<PhonePhoto> listAllData){

    }
    @Override default void onComplete(Vector<PhoneAlbum> albumsWithData, AlbumsWithTypes allAllbumNames){

    }
    void onError();

}
