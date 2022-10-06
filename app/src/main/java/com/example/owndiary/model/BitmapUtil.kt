package com.example.owndiary.model

import android.graphics.Bitmap

fun cropCenterBitmap(src: Bitmap, w: Int, h: Int) : Bitmap? {
    if(src == null)
        return null;

    if(src.width < w && src.height < h)
        return src;

    //crop 크기
    val ratio = h.toDouble()/w
    var cw = src.width; // crop width
    var ch = (cw * ratio).toInt() // crop height

    //시작점
    var x = 0;
    var y = (src.height/2) - (ch/2)

    if(ch>src.height){
        ch = src.height
        cw = (ch*(1/ratio)).toInt()

        x = (src.width/2) - (cw/2)
        y  = 0
    }


    return Bitmap.createBitmap(src, x, y, cw, ch);
}
