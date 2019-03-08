package hkucs.card24;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.Jep;
import com.singularsys.jep.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    LinearLayout setupGameLayout;
    LinearLayout gameLayout;

    Button startGame;
    EditText targetNumber;

    Button rePick;
    Button checkInput;
    Button clear;
    Button left;
    Button right;
    Button plus;
    Button minus;
    Button multiply;
    Button divide;
    TextView expression;
    ImageButton[] cards = new ImageButton[4];
    int[] data = new int[4];
    int[] card = new int[4];
    int[] imageCount = new int[4];
    int target;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews(); // all findViewById statements
        setListeners(); // add event listeners to components
        pickCard(); // randomly generate cards, which replaces the original initCardImage() function

        Log.d("test", "app started");
    }

    private void findViews(){
        setupGameLayout = (LinearLayout)findViewById(R.id.setupGameLayout);
        gameLayout = (LinearLayout)findViewById(R.id.gameLayout);

        targetNumber = (EditText)findViewById(R.id.targetNumber);
        startGame = (Button)findViewById(R.id.startGame);

        rePick = (Button)findViewById(R.id.repick);
        checkInput = (Button)findViewById(R.id.checkinput);
        left = (Button)findViewById(R.id.left);
        right = (Button)findViewById(R.id.right);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        multiply = (Button)findViewById(R.id.multiply);
        divide = (Button)findViewById(R.id.divide);
        clear = (Button)findViewById(R.id.clear);
        expression = (TextView)findViewById(R.id.input);

        ArrayList<Integer> cardIDList = new ArrayList<Integer>(
                Arrays.asList(R.id.card1, R.id.card2, R.id.card3, R.id.card4)
        );
        for(int i=0; i<4; i++){
            cards[i] = (ImageButton) findViewById( cardIDList.get(i) );
        }
    }

    private void setListeners(){

        startGame.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                target = Integer.parseInt(targetNumber.getText().toString());

                // close the keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                setupGameLayout.setVisibility(View.GONE);
                gameLayout.setVisibility(View.VISIBLE);
            }
        });

        // cards
        for(int i=0; i<4; i++) {
            final int cardId = i;
            cards[i].setOnClickListener(new ImageButton.OnClickListener() {
                public void onClick(View view) {
                    clickCard(cardId);
                }
            });
        }

        // clear
        clear.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                setClear();
            }
        });

        // (
        left.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                expression.append("(");
            }
        });

        // )
        right.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                expression.append(")");
            }
        });

        // +
        plus.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                expression.append("+");
            }
        });

        // -
        minus.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                expression.append("-");
            }
        });

        // *
        multiply.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                expression.append("*");
            }
        });

        // /
        divide.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                expression.append("/");
            }
        });

        // =
        checkInput.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                String inputStr = expression.getText().toString();
                if (checkInput(inputStr)) {
                    Toast.makeText(MainActivity.this, "Correct Answer",
                            Toast.LENGTH_SHORT).show();
                    pickCard();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Answer",
                            Toast.LENGTH_SHORT).show();
                    setClear();
                }
            }
        });

        // pick
        rePick.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                pickCard();
            }
        });
    }

    private void clickCard(int i) {
        int resId;
        String num;
        Integer value;
        if (imageCount[i] == 0) {
            resId = getResources().getIdentifier("back_0","drawable", "hkucs.card24");
            cards[i].setImageResource(resId);
            cards[i].setClickable(false);
            value = data[i];
            num = value.toString();
            expression.append(num);
            imageCount[i]++;
        }
    }

    private void pickCard(){

        // generate 4 non-repeated int [1, 13]
        boolean isRepeated;
        do{
            for(int i=0; i<4; i++){
                data[i] = random_in_range(1,13);
            }

            // check if repeated
            isRepeated = false;
            for(int i=0; i<3; i++){
                for(int j=i+1; j<4; j++){
                    if(data[i]==data[j]){
                        isRepeated = true;
                        break;
                    }
                }

                if(isRepeated) break;
            }
        }while(isRepeated);

        // select random suit for the card
        for(int i=0; i<4; i++){
            int suit = random_in_range(0, 3);
            card[i] = data[i] + suit*13;

            Log.d("test", String.format("data[%d]=%d, card[%d]=%d", i, data[i], i, card[i]));
        }

        setClear();
    }

    private int random_in_range(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    private void setClear(){
        int resID;
        expression.setText("");
        for (int i = 0; i < 4; i++) {
            imageCount[i] = 0;
            resID = getResources().getIdentifier("card"+data[i],
                    "drawable", "hkucs.card24");
            cards[i].setImageResource(resID);
            cards[i].setClickable(true);
        }
    }

    private boolean checkInput(String input) {
        Jep jep = new Jep();
        Object res;
        try {
            jep.parse(input);
            res = jep.evaluate();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Wrong Expression", Toast.LENGTH_SHORT).show();
            return false;
        } catch (EvaluationException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Wrong Expression", Toast.LENGTH_SHORT).show();
            return false;
        }
        Double ca = (Double)res;
        if (Math.abs(ca - target) < 1e-6){
            return true;
        }
        return false;
    }

}
