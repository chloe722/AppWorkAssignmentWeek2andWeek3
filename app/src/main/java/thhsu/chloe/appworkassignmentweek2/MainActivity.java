package thhsu.chloe.appworkassignmentweek2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupOnClick((ImageButton) findViewById(R.id.button0),"0");
        setupOnClick((ImageButton) findViewById(R.id.button1),"1");
        setupOnClick((ImageButton) findViewById(R.id.button2),"2");
        setupOnClick((ImageButton) findViewById(R.id.button3), "3");
        setupOnClick((ImageButton) findViewById(R.id.button4), "4");
        setupOnClick((ImageButton) findViewById(R.id.button5), "5");
        setupOnClick((ImageButton) findViewById(R.id.button6), "6");
        setupOnClick((ImageButton) findViewById(R.id.button7), "7");
        setupOnClick((ImageButton) findViewById(R.id.button8), "8");
        setupOnClick((ImageButton) findViewById(R.id.button9), "9");
        setupSymbolOnClick((ImageButton) findViewById(R.id.buttonPlus), "+");
        setupSymbolOnClick((ImageButton) findViewById(R.id.buttonMinus), "-");
        setupSymbolOnClick((ImageButton) findViewById(R.id.buttonTimes), "*");
        setupSymbolOnClick((ImageButton) findViewById(R.id.buttonDivide), "/");
        setupSymbolOnClick((ImageButton) findViewById(R.id.buttonPoint),".");
        setupSymbolOnClick((ImageButton) findViewById(R.id.buttonPercent),"%");
        onClickEqual((ImageButton) findViewById(R.id.buttonEquals));
        onClickReset((ImageButton) findViewById(R.id.buttonC));
    }

    private void onClickEqual(ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
        }
        });
    }

    private void onClickReset(ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void setupOnClick(ImageButton b, final String digit) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mathInput = (TextView) findViewById(R.id.mathInput);
                mathInput.setText(mathInput.getText() + digit);
            }
        });
    }

    private void setupSymbolOnClick(ImageButton b, final String calSymbol) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mathInput = (TextView) findViewById(R.id.mathInput);
                String mathInputText = (String) mathInput.getText();

                if (mathInputText.length() > 0) {
                    String lastSymbol = mathInputText.substring(mathInputText.length() - 1); //get last symbol

                    if (lastSymbol.equals("+") | lastSymbol.equals("-") | lastSymbol.equals("*") | lastSymbol.equals("/") | lastSymbol.equals("%")| lastSymbol.equals(".")) {
                        mathInput.setText(mathInputText.substring(0, mathInput.length() - 1) + calSymbol); // replace last symbol
                    } else {
                        mathInput.setText(mathInput.getText() + calSymbol);
                    }
                    if (calSymbol.equals("%")) {
                        calculate();
                    }
                }
            }
        });
    }

    private void calculate(){
        try{
            _calculate();
        }catch(Exception e){
            Log.e("calculation error", e.getMessage());
            alertErrorDialog();
        }
    }

    private void alertErrorDialog() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    private void _calculate(){
        TextView mathInput = (TextView) findViewById(R.id.mathInput);
        TextView mathResult = (TextView) findViewById(R.id.mathResult);
        String expressionText = (String) mathInput.getText();

        Pattern multiply = Pattern.compile("([\\d.]+)[*]([\\d.]+)");
        Pattern division = Pattern.compile("([\\d.]+)[/]([\\d.]+)");
        Pattern minusPlus = Pattern.compile("(-?[\\d.]+)([-+])([\\d.]+)");
        Pattern percent = Pattern.compile("([\\d.]+)[%]");


        Matcher m;
        m = percent.matcher(expressionText); // Initialize the matcher
        while(m.find()){ // execute the matcher
            String allMatch = m.group(0); // all match
            String num1 = m.group(1);// first bracket is group
            Double x = Double.parseDouble(num1) / 100;
            expressionText = expressionText.replace(allMatch, Double.toString(x));
            m = percent.matcher(expressionText); // restart the match to look for new  mutiply string
        }

        m = multiply.matcher(expressionText); // Initialize the matcher
        while(m.find()){ // execute the matcher
            String allMatch = m.group(0); // all match
            String num1 = m.group(1);// first bracket is group 1
            String num2 = m.group(2);// second bracket is group 2
            Double x = Double.parseDouble(num1) * Double.parseDouble(num2);
            expressionText = expressionText.replace(allMatch, Double.toString(x));
            m = multiply.matcher(expressionText); // restart the match to look for new  mutiply string
        }

        m = division.matcher(expressionText);
        while(m.find()){
            String allMatch = m.group(0);
            String num1 = m.group(1);
            String num2 = m.group(2);
            Double x = Double.parseDouble(num1) / Double.parseDouble(num2);
            expressionText = expressionText.replace(allMatch, Double.toString(x));
            m = division.matcher(expressionText);
        }

        m = minusPlus.matcher(expressionText);
        while(m.find()){
            String allMatch = m.group(0);
            String num1 = m.group(1);
            String num2 = m.group(3);
            String symbol = m.group(2);

            if(symbol.equals("+")){
            Double x = Double.parseDouble(num1) + Double.parseDouble(num2);
            expressionText = expressionText.replace(allMatch, Double.toString(x));
            m = minusPlus.matcher(expressionText);}
            else{
                Double x = Double.parseDouble(num1) - Double.parseDouble(num2);
                expressionText = expressionText.replace(allMatch, Double.toString(x));
                m = minusPlus.matcher(expressionText);
            }
        }


        if(expressionText.substring(expressionText.length()-2).equals(".0")){ // Retrieve if  string end with ".0"
            expressionText = expressionText.substring(0, expressionText.length()-2); // If so, remove it
        }
        mathResult.setText(expressionText);
    }

    private void reset(){
        TextView mathResult = (TextView) findViewById(R.id.mathResult);
        TextView mathInput = (TextView) findViewById(R.id.mathInput);
        mathResult.setText("");
        mathInput.setText("");
    }


}


