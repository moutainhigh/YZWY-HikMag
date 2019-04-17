package com.example.yzwy.lprmag.util.asy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.yzwy.lprmag.view.XCRoundRectImageView;

public class MyAsyXcr extends AsyncTask<String, Void, Bitmap> {

    XCRoundRectImageView xcRoundRectImageView;


    public MyAsyXcr(XCRoundRectImageView xcRoundRectImageView) {
        super();
        this.xcRoundRectImageView = xcRoundRectImageView;
    }

    @Override
    protected Bitmap doInBackground(String... arg0) {

        byte[] byTe = DataNetUrl.myData(arg0[0]);

        Bitmap bmap = BitmapFactory.decodeByteArray(byTe, 0, byTe.length);
        return bmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            xcRoundRectImageView.setImageBitmap(result);
        }
        super.onPostExecute(result);
    }

}
