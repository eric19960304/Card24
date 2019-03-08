package hkucs.card24;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageButton[] cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Integer> cardIDList = new ArrayList<Integer>(
                Arrays.asList(R.id.card1, R.id.card2, R.id.card3, R.id.card4)
        );

        cards = new ImageButton[4];
        for(int i=0; i<4; i++){
            cards[i] = (ImageButton) findViewById( cardIDList.get(i) );

            cards[i].setOnClickListener(new View.OnClickListener() {
                // testing: try to get image from internet and set it to cards' background image
                @Override
                public void onClick(View v) {
                    (new FlipCard()).execute(
                            "https://cdn2.bigcommerce.com/n-d57o0b/1kujmu/products/297/images/924/2D__57497.1440113502.1280.1280.png?c=2",
                            v.getId());
                }
            });
        }

        initCardImage();
        Log.d("mytag", "app started");
    }

    private void initCardImage() {
        for (int i = 0; i < 4; i++) {
            int resID = getResources().getIdentifier("back_0","drawable", "hkucs.card24");
            cards[i].setImageResource(resID);
        }
    }

    private class FlipCard extends AsyncTask<Object, Void, Object[]> {

        protected Object[] doInBackground(Object... params){
            String url = (String) params[0];
            Integer cardId = (Integer) params[1];
            Log.d("mytag", "doInBackground");
            return (new Object[]{ loadImageFromNetwork(url), cardId });
        }

        protected void onPostExecute(Object[] params) {
            Bitmap img = (Bitmap) params[0];
            Integer cardId = (Integer) params[1];
            Log.d("mytag", "onPostExecute: ready to set image "+img.toString());
            ImageButton card = (ImageButton) findViewById( cardId );
            card.setImageBitmap(img);

        }

        private Bitmap loadImageFromNetwork(String url) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream ((InputStream)new URL(url).getContent());
                Log.d("mytag", "downloaded");
                return bitmap;
            } catch (Exception e) {
                Log.d("mytag", e.toString());
            }

            return null;
        }

    }
}
