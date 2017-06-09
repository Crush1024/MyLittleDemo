package com.wendu.rongclouddemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wendu.rongclouddemo.R;
import com.wendu.rongclouddemo.bean.LoginResult;
import com.wendu.rongclouddemo.bean.Registed;
import com.wendu.rongclouddemo.bean.Registing;
import com.wendu.rongclouddemo.http.HttpUtils;
import com.wendu.rongclouddemo.utils.MyToast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etPsw1;
    private EditText etPsw2;
    private TextView mainBtnSIGNUP;
    private TextView tvCheck;
    private int checkState = 0;
    private LoginResult.ResultBean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.et_name);
        etPsw1 = (EditText) findViewById(R.id.et_psw1);
        etPsw2 = (EditText) findViewById(R.id.et_psw2);
        mainBtnSIGNUP = (TextView) findViewById(R.id.main_btn_SIGN_UP);
        tvCheck = ((TextView) findViewById(R.id.tv_check));
        mainBtnSIGNUP.setOnClickListener(this);
        tvCheck.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_btn_SIGN_UP:
                boolean et_name = TextUtils.isEmpty(etName.getText());
                boolean et_psw1 = TextUtils.isEmpty(etPsw1.getText());
                boolean et_psw2 = TextUtils.isEmpty(etPsw2.getText());
                if(et_name){
                    Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_psw1){
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_psw2){
                    Toast.makeText(this, "请再次输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!etPsw1.getText().toString().equals(etPsw2.getText().toString())){
                    Toast.makeText(this, "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    etPsw1.setText("");
                    etPsw2.setText("");
                    return;
                }
                signUp(etName.getText().toString(),etPsw1.getText().toString());
                break;
            case R.id.tv_check:
                boolean et_name1 = TextUtils.isEmpty(etName.getText());
                if(et_name1){
                    Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpUtils.init().isRegist(etName.getText().toString()).enqueue(new Callback<Registed>() {
                    @Override
                    public void onResponse(Call<Registed> call, Response<Registed> response) {
                        Registed registed = response.body();
                        Log.i("signuo",registed.toString() );
                        String code = registed.getCode();
                        String message = registed.getMessage();
                        if("01".equals(code)){
                            MyToast.showToast(SignUpActivity.this,message);
                            checkState = 1;
                        }else if("10".equals(code)){
                            MyToast.showToast(SignUpActivity.this,message);
                            etName.setText("");
                        }
                    }
                    @Override
                    public void onFailure(Call<Registed> call, Throwable t) {

                    }
                });
                break;
        }
    }

    /**
     * 发送请求
     * @param et_name
     * @param et_psw1
     */
    private void signUp(final String et_name, final String et_psw1) {
        HttpUtils.init().regist(et_name,et_psw1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Registing>() {
                    @Override
                    public void accept(Registing registing) throws Exception {
                        String code = registing.getCode();
                        String message = registing.getMessage();
                        Log.i("signuo", "accept: "+code);
                        if("01".equals(code)){
                            HttpUtils.init().login(et_name,et_psw1).enqueue(new Callback<LoginResult>() {
                                @Override
                                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                                    LoginResult loginResult = response.body();
                                    String message = loginResult.getMessage();
                                    String code = loginResult.getCode();
                                    if("01".equals(code)){
                                        Log.i("signuo", "login: "+code);
                                        result = loginResult.getResult();
                                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                        finish();
                                    }else if("10".equals(code)){
                                        MyToast.showToast(SignUpActivity.this,message);
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginResult> call, Throwable t) {

                                }
                            });
                        }else if("10".equals(code)){
                            MyToast.showToast(SignUpActivity.this,message);
                        }
                    }
                });

    }
}
