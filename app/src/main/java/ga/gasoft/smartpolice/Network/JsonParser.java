package ga.gasoft.smartpolice.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonParser
{
	private static JsonParser mJsonParser = new JsonParser();
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private JsonParser()
	{}
	
	public static JsonParser getInstance()
	{
		
		return mJsonParser;
	}
	
	public synchronized <T> void parseJsonArrayResponse(String response, ListDecodeCallback<T> callback, Class<T> type)
	{
		ListDecodeTask<T> task = new ListDecodeTask<T>(callback, type);
		task.execute(response);
	}
	
	public synchronized <T> void parseJsonObjectResponse(String response, ObjectDecodeCallback<T> callback, Class<T> type)
	{
		ObjectDecodeTask<T> task = new ObjectDecodeTask<T>(callback, type);
		task.execute(response);
	}
	
	private class ObjectDecodeTask<T> extends AsyncTask<String, Void, T>
	{
		private volatile boolean running = true;
		private Exception exceptionThrown = null;
		
		private ObjectDecodeCallback<T> callback;
		private Class<T> mType;
		
		public ObjectDecodeTask(ObjectDecodeCallback<T> callback, Class<T> type)
		{
			this.callback = callback;
			this.mType = type;
		}
		
		@Override
		protected void onCancelled()
		{
			running = false;
		}
		
		@Override
		protected T doInBackground(String... responses)
		{
			while(running)
			{
				try
				{
					String response = responses[0];
					
					mapper.setVisibility(com.fasterxml.jackson.annotation.PropertyAccessor.FIELD, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					
					T item = mapper.readValue(response, this.mType);
					
					return item;
				}
				catch(JsonParseException exception)
				{
					Log.d("exception=", "1");
					exception.printStackTrace();
					exceptionThrown = exception;
					return null;
				}
				catch(JsonMappingException exception)
				{
					Log.d("exception=", "2");
					exception.printStackTrace();
					exceptionThrown = exception;
					return null;
				}
				catch(IOException exception)
				{
					Log.d("exception=", "3");
					exception.printStackTrace();
					exceptionThrown = exception;
					return null;
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(T result)
		{
			
			if(exceptionThrown != null)
			{
				callback.onError(exceptionThrown);
				
			}
			else
			{
				
				callback.onSuccess(result);
			}
		}
	}
	
	private class ListDecodeTask<T> extends AsyncTask<String, Void, List<T>>
	{
		private volatile boolean running = true;
		private Exception exceptionThrown = null;
		
		private ListDecodeCallback<T> callback;
		private Class<T> mType;
		
		public ListDecodeTask(ListDecodeCallback<T> callback, Class<T> type)
		{
			this.callback = callback;
			this.mType = type;
		}
		
		@Override
		protected void onCancelled()
		{
			running = false;
		}
		
		@Override
		protected List<T> doInBackground(String... responses)
		{
			while(running)
			{
				try
				{
					String response = responses[0];
					
					ObjectMapper mapper = new ObjectMapper();
					
					mapper.setVisibility(com.fasterxml.jackson.annotation.PropertyAccessor.FIELD, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					
					List<T> listItems;
					
					listItems = mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, this.mType));
					
					return listItems;
				}
				catch(JsonParseException exception)
				{
					Log.d("exception=", "1");
					exception.printStackTrace();
					exceptionThrown = exception;
					return null;
				}
				catch(JsonMappingException exception)
				{
					Log.d("exception=", "2");
					exception.printStackTrace();
					exceptionThrown = exception;
					return null;
				}
				catch(IOException exception)
				{
					Log.d("exception=", "3");
					exception.printStackTrace();
					exceptionThrown = exception;
					return null;
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<T> result)
		{
			
			if(exceptionThrown != null)
			{
				callback.onError(exceptionThrown);
				
			}
			else
			{
				
				callback.onSuccess(result);
			}
		}
	}
	
	public interface ListDecodeCallback<T>
	{
		void onSuccess(List<T> responseList);
		
		void onError(Exception exception);
	}
	
	public interface ObjectDecodeCallback<T>
	{
		void onSuccess(T responseObject);
		
		void onError(Exception exception);
	}
	
	static void jsonObjectClear(JSONObject jsonObject)
	{
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) jsonObject.keys();
		while(keys.hasNext())
		{
			keys.next();
			keys.remove();
		}
	}
	
	static boolean jsonObjectContainsValue(JSONObject jsonObject, Object value)
	{
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) jsonObject.keys();
		while(keys.hasNext())
		{
			Object thisValue = jsonObject.opt(keys.next());
			if(thisValue != null && thisValue.equals(value))
			{
				return true;
			}
		}
		return false;
	}
	
	static Set<String> jsonObjectKeySet(JSONObject jsonObject)
	{
		HashSet<String> result = new HashSet<String>();
		
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) jsonObject.keys();
		while(keys.hasNext())
		{
			result.add(keys.next());
		}
		
		return result;
	}
	
	static void jsonObjectPutAll(JSONObject jsonObject, Map<String, Object> map)
	{
		Set<Map.Entry<String, Object>> entrySet = map.entrySet();
		for(Map.Entry<String, Object> entry : entrySet)
		{
			try
			{
				jsonObject.putOpt(entry.getKey(), entry.getValue());
			}
			catch(JSONException e)
			{
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	static Collection<Object> jsonObjectValues(JSONObject jsonObject)
	{
		ArrayList<Object> result = new ArrayList<Object>();
		
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (Iterator<String>) jsonObject.keys();
		while(keys.hasNext())
		{
			result.add(jsonObject.opt(keys.next()));
		}
		
		return result;
	}
	
}
