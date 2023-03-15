package debtechllc.deb.talez.paint;

import android.graphics.Path;


public interface Shapable {

    Path getPath();

    FirstCurrentPosition getFirstLastPoint();

    void setShap(ShapesInterface shape);
}
