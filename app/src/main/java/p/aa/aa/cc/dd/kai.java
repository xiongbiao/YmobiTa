package p.aa.aa.cc.dd;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.data.tools.listener.XListener;

import ta.aie.mobi.ymobita.aa.R;


/**
 * Created by xiongbiao on 18/1/3.
 */
public class kai extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.tt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User();
                user.uid = "111";
                user.uName = "111name";
                user.objectId=("sssss");
                user.uAge = "111age";
                user.insertObject(kai.this, new XListener() {
                    @Override
                    public void onSuccess(String var1) {
                        log("onSuccess");
                    }

                    @Override
                    public void onFailure(String var1) {
                        log("onFailure");
                    }
                });

            }
        });
    }



    private void log(String msg){
        Log.d("TAG",msg);

    }

}
