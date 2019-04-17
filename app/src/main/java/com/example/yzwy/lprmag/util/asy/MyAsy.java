package com.example.yzwy.lprmag.util.asy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class MyAsy extends AsyncTask<String, Void, Bitmap> {

	ImageView img;

	public MyAsy(ImageView img) {
		super();
		this.img = img;
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {

		byte[] byTe= DataNetUrl.myData(arg0[0]);
		
		Bitmap bmap = BitmapFactory.decodeByteArray(byTe, 0, byTe.length);
		return bmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if(result !=null){
			img.setImageBitmap(result);
		}
		super.onPostExecute(result);
	}

}
