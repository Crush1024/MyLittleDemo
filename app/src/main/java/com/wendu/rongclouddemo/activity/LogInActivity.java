package com.wendu.rongclouddemo.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wendu.rongclouddemo.R;
import com.wendu.rongclouddemo.bean.LoginResult;
import com.wendu.rongclouddemo.http.HttpUtils;
import com.wendu.rongclouddemo.utils.JellyInterpolator;
import com.wendu.rongclouddemo.utils.MyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;
    private EditText et_name;
    private EditText et_psw;
    private TextView toSignUp;
    private LoginResult.ResultBean result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initView();
    }
    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        et_name = ((EditText) findViewById(R.id.et_name));
        et_psw = ((EditText) findViewById(R.id.et_psw));
        toSignUp = ((TextView) findViewById(R.id.tv_to_sign_up));
        mBtnLogin.setOnClickListener(this);
        toSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_login:
                String name = et_name.getText().toString();
                String psw = et_psw.getText().toString();
                boolean name_isEmpty = TextUtils.isEmpty(et_name.getText());
                boolean psw_isEmpty = TextUtils.isEmpty(et_psw.getText());
                if(name_isEmpty||psw_isEmpty){
                    return;
                }
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                inputAnimator(mInputLayout, mWidth, mHeight);
                logIn(name,psw);
                break;
            case R.id.tv_to_sign_up:
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
                break;
        }
    }

    private void logIn(String name, String string) {
        HttpUtils.init().login(name,string).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult loginResult = response.body();
                String message = loginResult.getMessage();
                String code = loginResult.getCode();
                if("01".equals(code)){
                    result = loginResult.getResult();
                    startActivity(new Intent(LogInActivity.this,MainActivity.class));
                    finish();
                }else if("10".equals(code)){
                    MyToast.showToast(LogInActivity.this,message);
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                MyToast.showToast(LogInActivity.this,"失败");
            }
        });
    }

    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

                startActivity(new Intent(LogInActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
}
