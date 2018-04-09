package ga.gasoft.smartpolice.Network;

import org.json.JSONObject;

import java.util.List;

public class CallBacks
{
	
	public static interface ObjectCallBackListener<T>
	{
		void onSuccess(T responseObject);
		
		void onError(String errorMessage);
		
		void onErrorWithData(JSONObject errorjson);
		
		void onStart();
	}
	
	public static interface ListCallBackListener<T>
	{
		void onSuccess(List<T> responseList);
		
		void onError(String errorMessage);
		
		void onStart();
	}
	
	public static interface StringCallBackListener
	{
		void onSuccess(String successMessage);
		
		void onError(String errorMessage);
		
		void onStart();
	}
	
}
