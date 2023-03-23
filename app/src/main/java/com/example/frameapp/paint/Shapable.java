package com.example.frameapp.paint;

import android.graphics.Path;


public interface Shapable {

    Path getPath();

    FirstCurrentPosition getFirstLastPoint();

    void setShap(ShapesInterface shape);
}
