package com.imooc.searchforid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	// 布局元素
	private EditText edt_IDinput;
	private Button btn_search, btn_clear;
	private TextView tv_sex, tv_birthday, tv_address;
	private String edtStr;
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		edt_IDinput = (EditText) findViewById(R.id.edt_IDnum);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		tv_sex = (TextView) findViewById(R.id.show_sex);
		tv_birthday = (TextView) findViewById(R.id.show_birthday);
		tv_address = (TextView) findViewById(R.id.show_address);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		// 设置监听事件
		btn_search.setOnClickListener(this);
		btn_clear.setOnClickListener(this);

	}
	
	public boolean isConnected(){
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mInfo = manager.getActiveNetworkInfo();
		if(mInfo!=null){
			return mInfo.isAvailable();
		}else{
			return false;
		}
	}


	@Override
	public void onClick(View v) {
		edtStr = edt_IDinput.getText().toString();
		if (v.getId() == R.id.btn_search) {
			if (isIDcode()) {
				//检测网络及验证
				if(isConnected()){
					new MyAsyncTask(tv_sex, tv_birthday, tv_address, edtStr, pb)
					.execute();
				}else{
					Toast.makeText(MainActivity.this, "网络未连接", 2500).show();
					return;
				}
			} else {
				tv_sex.setText("请输入正确的身份证号码！");
				tv_address.setText("");
				tv_birthday.setText("");
				return;
			}
		} else if (v.getId() == R.id.btn_clear) {
			edt_IDinput.setText("");
			tv_sex.setText("");
			tv_address.setText("");
			tv_birthday.setText("");
		}
	}

	/**
	 * 匹配是否是正确的身份证号码
	 * 
	 * @return
	 */
	private boolean isIDcode() {
		Pattern p = Pattern.compile("^\\d{17}\\w$");
		Matcher m = p.matcher(edtStr);
		if (m.find()) {
			return true;
		}
		return false;
	}

}
