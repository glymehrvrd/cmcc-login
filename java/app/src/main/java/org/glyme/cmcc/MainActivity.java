package org.glyme.cmcc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private volatile String cookie;

    private TextView tv;
    private ImageView iv;

    private void refreshVerifyCode() {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("状态：获取验证码...");
                    }
                });
                cookie = CMCCLogin.getCookie();

                if (cookie == null) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("状态：获取验证码失败！！");
                        }
                    });
                    return;
                }

                final ByteBuffer buf = CMCCLogin.getVerifyCode(cookie);

                if (buf == null) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("状态：获取验证码失败！！");
                        }
                    });
                    return;
                }

                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(buf.array(), 0, buf.limit());
                        iv.setImageBitmap(bitmap);
                        tv.setText("状态：获取验证码成功！！");
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
                        tv.setText("状态：登陆中...");
                    }
                });

                String user = ((TextView) findViewById(R.id.editText2)).getText().toString();
                String pass = ((TextView) findViewById(R.id.editText3)).getText().toString();
                String code = ((TextView) findViewById(R.id.editText4)).getText().toString();

                if (CMCCLogin.login(user, pass, code, cookie)) {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("状态：登陆成功！！");
                        }
                    });
                } else {
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("状态：登陆失败！！");
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        iv = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView);

        refreshVerifyCode();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshVerifyCode();
            }
        });

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
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
