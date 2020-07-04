package yazilim.hilal.yesil.yhycamera.listener;

import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.models.AlbumsWithTypes;
import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;
import yazilim.hilal.yesil.yhycamera.models.PhonePhoto;

public interface OnGaleryMixedDataGot {
    public void onComplete(Vector<PhonePhoto> listAllData);

    public void onComplete(Vector<PhoneAlbum> albumsWithData, AlbumsWithTypes allAllbumNames);

}
