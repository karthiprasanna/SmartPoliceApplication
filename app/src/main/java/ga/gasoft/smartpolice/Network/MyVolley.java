package ga.gasoft.smartpolice.Network;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import ga.gasoft.smartpolice.Application.App;

public class MyVolley
{
	private static RequestQueue mRequestQueue;
	// private static ImageLoader mImageLoader;
	public static final String TAG = "VolleyTask";
	
	private MyVolley()
	{
		// no instances
	}
	
	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public static synchronized RequestQueue getRequestQueue()
	{
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if(mRequestQueue == null)
		{
			mRequestQueue = Volley.newRequestQueue(App.getApplicationInstance().getApplicationContext(),1);
		}
		
		return mRequestQueue;
	}
	
	/**
	 * @return The Volley ImageLoader, ImageLoader and Request queue will be
	 *         created if it is null
	 */
	
	// public static synchronized ImageLoader getImageLoader()
	// {
	// // lazy initialize the request queue, the queue instance will be
	// // created when it is accessed for the first time
	// if(mImageLoader == null)
	// {
	// mImageLoader = new ImageLoader(getRequestQueue(), new
	// VolleyImageCache(APP.getApplicationInstance().getCacheDir(),
	// getDefaultLruCacheSize()));
	// }
	//
	// return mImageLoader;
	// }
	
	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public static <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		
		VolleyLog.d("Adding request to queue: %s", req.getUrl());
		
		getRequestQueue().add(req);
	}
	
	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 * @paramtag
	 */
	public static <T> void addToRequestQueue(Request<T> req)
	{
		// set the default tag if tag is empty
		req.setTag(TAG);
		
		getRequestQueue().add(req);
	}
	
	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public static void cancelPendingRequests(String tag)
	{
		if(mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}
	
	public static void cancelPendingRequests()
	{
		if(mRequestQueue != null)
		{
			mRequestQueue.cancelAll(TAG);
		}
	}
	
	public static void clearCache()
	{
		if(mRequestQueue != null)
		{
			mRequestQueue.getCache().clear();
		}
	}
	
	public static int getDefaultLruCacheSize()
	{
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 7;
		
		return cacheSize;
	}
	
}
