package org.glyme.cmcc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private volatile String cookie;

    private TextView tv;
    private ImageView iv;
    private EditText edcode;

    private void refreshVerifyCode() {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("获取验证码...\n" + tv.getText());
                    }
                });
                cookie = CMCCLogin.getCookie();

                if (cookie == null) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("获取验证码失败！！\n" + tv.getText());
                        }
                    });
                    return;
                }

                final ByteBuffer buf = CMCCLogin.getVerifyCode(cookie);

                if (buf == null) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("获取验证码失败！！\n" + tv.getText());
                        }
                    });
                    return;
                }

                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(buf.array(), 0, buf.limit());
                        iv.setImageBitmap(bitmap);
                        tv.setText("获取验证码成功！！\n" + tv.getText());
                        String code=String.valueOf(NNDetector.detect(bitmap));
                        edcode.setText(code);
                    }
                });

            }
        });
    }

    private void doLogin() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("登陆中...\n" + tv.getText());
                    }
                });

                final String user = ((TextView) findViewById(R.id.editText2)).getText().toString();
                final String pass = ((TextView) findViewById(R.id.editText3)).getText().toString();
                final String code = ((TextView) findViewById(R.id.editText4)).getText().toString();

                if (CMCCLogin.login(user, pass, code, cookie)) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(iv, "登陆成功", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            tv.setText("登陆成功！！\n" + tv.getText());
                            SharedPreferences sp = MainActivity.this.getSharedPreferences("org.glyme.cmcc.preference_key", Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed=sp.edit();
                            ed.putString("user",user);
                            ed.putString("pass",pass);
                            ed.commit();
                        }
                    });
                } else {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(iv, "登陆失败", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            tv.setText("登陆失败！！\n" + tv.getText());
                        }
                    });
                    refreshVerifyCode();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        iv = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView2);
        edcode = (EditText) findViewById(R.id.editText4);

        SharedPreferences sp = getSharedPreferences("org.glyme.cmcc.preference_key", Context.MODE_PRIVATE);
        ((TextView) findViewById(R.id.editText2)).setText(sp.getString("user",""));
        ((TextView) findViewById(R.id.editText3)).setText(sp.getString("pass",""));

        refreshVerifyCode();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshVerifyCode();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
