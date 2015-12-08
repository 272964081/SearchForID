package com.imooc.searchforid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {

	private static String apiKey = "be2fffdb29cbf44b3777bea792a5462f";
	private String httpUrl = "http://apis.baidu.com/apistore/idservice/id";
	private TextView sex, birth, address;
	private String idCode;
	private ProgressBar progressBar;
	public static String WARNNING = "输入的信息有误或无相关数据，请检查身份证号码";

	public MyAsyncTask(TextView sex, TextView birth, TextView address,
			String edtStr, ProgressBar pb) {
		super();
		this.sex = sex;
		this.birth = birth;
		this.address = address;
		this.idCode = edtStr;
		this.progressBar = pb;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(Void... params) {
		String str = request(httpUrl, "id=" + idCode);
		return str;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		json4String(result);
	}
	

	/**
	 * 解析JSON数据
	 * 
	 * @param result
	 */
	private void json4String(String result) {
		String sexStr = null, addStr = null, birthStr = null;
		try {
			JSONObject jsonObj = new JSONObject(result);
			JSONObject jsData = jsonObj.getJSONObject("retData");
			sexStr = jsData.getString("sex");
			addStr = jsData.getString("address");
			birthStr = jsData.getString("birthday");
			// 设置文字给View
			setTextToView(sexStr, addStr, birthStr);
		} catch (JSONException e) {
			e.printStackTrace();
			setTextToView(WARNNING," "," ");
		}
	}

	public void setTextToView(String sexStr, String addressStr, String birthStr) {
		if ("M".equals(sexStr)) {
			sex.setText("性别：  男");
		} else if("F".equals(sexStr)){
			sex.setText("性别：  女");
		}else{
			sex.setText(sexStr);
			address.setText("");
			birth.setText("" );
			progressBar.setVisibility(View.GONE);
			return;
		}
		birth.setText("出生年月:" + birthStr); // 生日
		address.setText("籍贯：" + addressStr); // 籍贯
		progressBar.setVisibility(View.GONE);
	}

	/**
	 * 获取JSON数据
	 * 
	 * @param httpUrl
	 * @param httpArg
	 * @return
	 */
	public String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", apiKey);
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
