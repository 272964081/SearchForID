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
	public static String WARNNING = "�������Ϣ�������������ݣ��������֤����";

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
	 * ����JSON����
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
			// �������ָ�View
			setTextToView(sexStr, addStr, birthStr);
		} catch (JSONException e) {
			e.printStackTrace();
			setTextToView(WARNNING," "," ");
		}
	}

	public void setTextToView(String sexStr, String addressStr, String birthStr) {
		if ("M".equals(sexStr)) {
			sex.setText("�Ա�  ��");
		} else if("F".equals(sexStr)){
			sex.setText("�Ա�  Ů");
		}else{
			sex.setText(sexStr);
			address.setText("");
			birth.setText("" );
			progressBar.setVisibility(View.GONE);
			return;
		}
		birth.setText("��������:" + birthStr); // ����
		address.setText("���᣺" + addressStr); // ����
		progressBar.setVisibility(View.GONE);
	}

	/**
	 * ��ȡJSON����
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
			// ����apikey��HTTP header
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
