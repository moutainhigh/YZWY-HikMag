package com.example.yzwy.lprmag.util.asy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ============================================================================
 * 完整下载类
 * 
 */
public class DataNetUrl {

	public static byte[] myData(String urlPath) {
		try {

			URL u = new URL(urlPath);

			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			
			//-----------清除缓存
			conn.setDoOutput(true); 
			conn.setUseCaches(false);
			
			if (conn.getResponseCode() == 200) {

				InputStream itsm = conn.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				byte[] by = new byte[1024];

				int len = 0;

				while ((len = itsm.read(by)) != -1) {
					baos.write(by, 0, len);
				}

				byte[] bytes = baos.toByteArray();
				//Log.e("tag", bytes.length + "");
				return bytes;

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
