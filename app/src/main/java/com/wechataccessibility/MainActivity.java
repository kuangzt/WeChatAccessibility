package com.wechataccessibility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author
 * @Description:
 * @date 2017/12/11 22:49
 * @copyright
 */
public class MainActivity extends Activity implements View.OnClickListener{

    EditText mNameEt;
    Button mOkBt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        mNameEt = (EditText) findViewById(R.id.name);
        mOkBt = (Button) findViewById(R.id.ok);
        mOkBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ok:
                if (TextUtils.isEmpty(mNameEt.getText())) {
                    Toast.makeText(this,R.string.name_cannot_null,Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("chatname",mNameEt.getText().toString());
                    editor.commit();
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    startActivity(LaunchIntent);

                }
                break;
        }
    }
}
