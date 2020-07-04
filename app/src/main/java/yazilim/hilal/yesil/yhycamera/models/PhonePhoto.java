package yazilim.hilal.yesil.yhycamera.models;

public class PhonePhoto {

    private int id;
    private String photoName;
    private String photoUri;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri( String photoUri ) {
        this.photoUri = photoUri;
    }
}