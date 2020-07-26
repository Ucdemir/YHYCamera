package yazilim.hilal.yesil.yhycamera.models;

import java.util.ArrayList;
import java.util.List;

public class AlbumsWithTypes implements Cloneable {

    public List<String> photoAlbums = new ArrayList<>();
    public List<String> videoAlbums = new ArrayList<>();




    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
