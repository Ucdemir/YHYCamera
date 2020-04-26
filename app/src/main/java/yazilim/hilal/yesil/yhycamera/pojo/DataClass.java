package yazilim.hilal.yesil.yhycamera.pojo;

public class DataClass {

    public enum DataType{
        Photo,Video
    }

    private DataType type;
    private String path = "";


    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
