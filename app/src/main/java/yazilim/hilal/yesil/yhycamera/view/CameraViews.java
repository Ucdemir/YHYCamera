package yazilim.hilal.yesil.yhycamera.view;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.fragments.camera2.CameraFragment;

import static yazilim.hilal.yesil.yhycamera.fragments.camera2.CameraFragment.CAMERA_BACK;

public class CameraViews {


    public static CameraViews instance;


    public static CameraViews getInstance(){
        if(instance == null){

            instance = new CameraViews();
            instance.setInitial();
        }

        return instance;
    }

    private  CameraViews(){

    }


   public class ViewFlash{
        private int image;
        private CameraFragment.FlashMode mode;

        public int getImage() {
            return image;
        }

        public ViewFlash setImage(int image) {
            this.image = image;
            return this;
        }

        public CameraFragment.FlashMode getMode() {
            return mode;
        }

        public ViewFlash setMode(CameraFragment.FlashMode mode) {
            this.mode = mode;
            return this;
        }
    }


    public class ActiveModel{
        private String cameraId;

        public String getCameraId() {
            return cameraId;
        }

        public ActiveModel setCameraId(String cameraId) {
            this.cameraId = cameraId;
            return this;
        }
    }

    public class ViewVideoAndTakePicture{
        boolean isVideo;

        public boolean isVideo() {
            return isVideo;
        }

        public ViewVideoAndTakePicture setVideo(boolean video) {
            isVideo = video;

            return this;
        }
    }

   public class ViewIsThereData{
        boolean isTheredata;

       public boolean isTheredata() {
           return isTheredata;
       }

       public ViewIsThereData setTheredata(boolean theredata) {
           isTheredata = theredata;
           return this;
       }
   }


    private ViewFlash viewFlash = new ViewFlash();
    private ActiveModel activeModel = new ActiveModel();
    private ViewVideoAndTakePicture viewVideoAndTakePicture = new ViewVideoAndTakePicture();
    private ViewIsThereData viewIsThereData = new ViewIsThereData();


    public ViewFlash getViewFlash() {
        return viewFlash;
    }

    public void setViewFlash(ViewFlash viewFlash) {
        this.viewFlash = viewFlash;
    }

    public ActiveModel getActiveModel() {
        return activeModel;
    }

    public void setActiveModel(ActiveModel activeModel) {
        this.activeModel = activeModel;
    }

    public ViewVideoAndTakePicture getViewVideoAndTakePicture() {
        return viewVideoAndTakePicture;
    }

    public void setViewVideoAndTakePicture(ViewVideoAndTakePicture viewVideoAndTakePicture) {
        this.viewVideoAndTakePicture = viewVideoAndTakePicture;
    }

    public ViewIsThereData getViewIsThereData() {
        return viewIsThereData;
    }

    public void setViewIsThereData(ViewIsThereData viewIsThereData) {
        this.viewIsThereData = viewIsThereData;
    }

    private void setInitial(){

        viewFlash.mode = CameraFragment.FlashMode.NoFlash;
        viewFlash.image = R.drawable.no_flash;

        activeModel.cameraId = CAMERA_BACK;
        viewVideoAndTakePicture.isVideo = false;
        viewIsThereData.isTheredata = false;
    }


}
