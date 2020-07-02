package yazilim.hilal.yesil.yhycamera.listener;

import java.util.Vector;

import yazilim.hilal.yesil.yhycamera.models.PhoneAlbum;

public interface OnPhoneImagesObtained {

    void onComplete( Vector<PhoneAlbum> albums );
    void onError();
}
