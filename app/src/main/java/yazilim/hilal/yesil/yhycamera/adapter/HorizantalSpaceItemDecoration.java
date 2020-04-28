package yazilim.hilal.yesil.yhycamera.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizantalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizantalSpaceHeight;

    public HorizantalSpaceItemDecoration(int horizantalSpaceHeight) {
        this.horizantalSpaceHeight = horizantalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizantalSpaceHeight;
    }
}
