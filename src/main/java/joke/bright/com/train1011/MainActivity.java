package joke.bright.com.train1011;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgress hprogress;
    private RoundProgress rprogress;
    private static final int MSG_INFO = 101;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int progress = rprogress.getProgress();

            progress++;
            rprogress.setProgress(progress);
            hprogress.setProgress(progress);
            if(progress >=100){
                handler.removeMessages(MSG_INFO);
            }
            handler.sendEmptyMessageDelayed(MSG_INFO, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rprogress = (RoundProgress) findViewById(R.id.rp);
        hprogress = (HorizontalProgress) findViewById(R.id.hp);
        handler.sendEmptyMessageDelayed(MSG_INFO, 100);
    }
}
