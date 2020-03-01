package com.studenttomsk.testzcft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText expression;
    Button resBtn, one, two, three, four, five, six, seven, eight, nine, zero, div, imp, minus, plus, bro,brc,zap,del;

    TextView res;
    Stack <Double> stackDigit = new Stack<Double>();
    Stack <Character> stackOper = new Stack<Character>();

    String strExp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expression = (EditText)findViewById(R.id.EditStr);
        resBtn = (Button) findViewById(R.id.resultButton);
        expression.setInputType(InputType.TYPE_NULL);
        resBtn.setOnClickListener(this);
        one = (Button)findViewById(R.id.one);
        two = (Button)findViewById(R.id.two);
        three = (Button)findViewById(R.id.three);
        four = (Button)findViewById(R.id.four);
        five = (Button)findViewById(R.id.five);
        six = (Button)findViewById(R.id.six);
        seven = (Button)findViewById(R.id.seven);
        eight = (Button)findViewById(R.id.eight);
        nine = (Button)findViewById(R.id.nine);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        imp = (Button)findViewById(R.id.imp);
        div = (Button)findViewById(R.id.div);
        brc = (Button)findViewById(R.id.brC);
        bro = (Button)findViewById(R.id.brO);
        zero = (Button)findViewById(R.id.zero);
        zap = (Button)findViewById(R.id.zap);
        del = (Button)findViewById(R.id.del);


        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        imp.setOnClickListener(this);
        div.setOnClickListener(this);
        brc.setOnClickListener(this);
        bro.setOnClickListener(this);
        zero.setOnClickListener(this);
        zap.setOnClickListener(this);
        del.setOnClickListener(this);


    }



    public int priority(Character op){
        int y = 0;
        if(op == '+' || op == '-')
            y=1;
        if(op == '*'||op == '/')
            y=2;
        return y;
    }
    public double toCount(double t1,double t2, char op){
        double res = 0;
        switch (op){
            case '+':{
               res = t2 + t1;
               break;
            }
            case '-':{
                res = t2 - t1;
                break;
            }
            case '*':{
                res = t2 * t1;
                break;
            }
            case '/':{
                res = t2 / t1;
                break;
            }
        }
        return res;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resultButton:{

                double result = -1;
                double tempres;
                strExp = expression.getText().toString();
                strExp+=',';
                String temp = "";
                boolean unOp = false;
                boolean unOps = false;
                int countBrack = 0;
                for(int i = 0; i<strExp.length()-1;i++){

                    if(strExp.charAt(i)== '(' || strExp.charAt(i)==')'){
                        countBrack++;
                    }
                }
                if(countBrack%2!=0){
                    expression.setText("Ошибка в выражении");
                    break;
                }
                for(int i=0;i<strExp.length();i++) {
                    if (Character.isDigit(strExp.charAt(i)) || strExp.charAt(i) == '.') {
                        temp += strExp.charAt(i);
                    } else {
                        try {
                            char tempCh = strExp.charAt(i);
                            if (!stackOper.empty()) {
                                if (unOp) {
                                    stackDigit.push(Double.parseDouble(temp) * (-1));
                                    stackOper.push(tempCh);
                                    unOp = false;
                                    temp = "";
                                    continue;
                                } else if (tempCh == '-' && i == 0 && strExp.charAt(i + 1) != '(') {
                                    unOp = true;
                                    continue;
                                }
                                else if(tempCh == '-' && stackOper.peek()=='(' && temp=="" && strExp.charAt(i+1)!='('){
                                    unOp = true;
                                    temp = "";
                                    continue;
                                }
                            }
                            if (tempCh != '(') {
                                if (unOp) {
                                    double t = stackDigit.pop();
                                    stackDigit.push(t * (-1));
                                    unOp = false;
                                }
                                if (temp != "" && tempCh != ',') {
                                    stackDigit.push(Double.parseDouble(temp));
                                }
                                if (!stackOper.empty()) {
                                    if (stackOper.peek() == ')') {
                                        char y = stackOper.pop();
                                        if (!stackOper.empty()) {
                                            if (stackOper.peek() == '(') {
                                                stackOper.pop();
                                            } else {
                                                stackOper.push(y);
                                            }
                                        }
                                    }
                                    if (!stackOper.empty()) {
                                        if (tempCh == ')' || stackOper.peek() == ')') {
                                            if (stackOper.peek() == ')')
                                                stackOper.pop();
                                            while (stackOper.peek() != '(') {
                                                tempres = toCount(stackDigit.pop(), stackDigit.pop(), stackOper.pop());
                                                stackDigit.push(tempres);
                                                result = tempres;
                                            }

                                            stackOper.pop();
                                            if (!stackOper.empty()) {
                                                if (stackOper.peek() == '-') {
                                                    stackOper.pop();
                                                    stackDigit.pop();
                                                    result = result * (-1);
                                                    stackDigit.push(result);
                                                }
                                                temp = "";
                                                continue;
                                            }

                                        }
                                    }
                                }
                                if (tempCh == ',' && temp != "") {
                                    stackDigit.push(Double.parseDouble(temp));
                                }
                                if (stackOper.empty()) {
                                    stackOper.push(strExp.charAt(i));
                                } else if (priority(stackOper.peek()) >= priority(strExp.charAt(i))) {
                                    tempres = toCount(stackDigit.pop(), stackDigit.pop(), stackOper.pop());
                                    stackDigit.push(tempres);
                                    result = tempres;
                                    stackOper.push(strExp.charAt(i));
                                } else {
                                    stackOper.push(strExp.charAt(i));
                                }
                            } else {
                                stackOper.push(tempCh);
                            }
                            temp = "";
                        }
                        catch (Exception E){
                            expression.setText("Ошибка в выражении");
                        }
                    }
                }
                while (true){
                    try{
                        if(stackOper.peek()==',' || stackOper.peek()=='('){
                            stackOper.pop();
                        }
                        else {
                            result = toCount(stackDigit.pop(), stackDigit.pop(), stackOper.pop());
                            stackDigit.push(result);
                        }
                    }
                    catch(Exception e){
                        break;
                    }
                }
                expression.setText(Double.toString(result));
                break;
            }
            case R.id.del:{
                Editable currentText = expression.getText();
                if(currentText.length() > 0){

                    currentText.delete(expression.length()-1,expression.length());
                    expression.setText(currentText);
                    expression.setSelection(currentText.length());
                }
                break;
            }
            case R.id.zap:{
                if(expression.getText().toString()!="") {
                    expression.getText().insert(expression.getSelectionStart(), ".");
                }
                break;
            }
            case R.id.zero:{
                expression.getText().insert(expression.getSelectionStart(),"0");
                break;
            }
            case R.id.one:{
                expression.getText().insert(expression.getSelectionStart(),"1");
                break;
            }
            case R.id.two:{
                expression.getText().insert(expression.getSelectionStart(),"2");
                break;
            }
            case R.id.three:{
                expression.getText().insert(expression.getSelectionStart(),"3");
                break;
            }
            case R.id.four:{
                expression.getText().insert(expression.getSelectionStart(),"4");
                break;
            }
            case R.id.five:{
                expression.getText().insert(expression.getSelectionStart(),"5");
                break;
            }
            case R.id.six:{
                expression.getText().insert(expression.getSelectionStart(),"6");
                break;
            }
            case R.id.seven:{
                expression.getText().insert(expression.getSelectionStart(),"7");
                break;
            }
            case R.id.eight:{
                expression.getText().insert(expression.getSelectionStart(),"8");
                break;
            }
            case R.id.nine:{
                expression.getText().insert(expression.getSelectionStart(),"9");
                break;
            }
            case R.id.plus:{
                int n = 0;
                String st = expression.getText().toString();
                if(!TextUtils.isEmpty(st)) {
                    if(st.charAt(st.length()-1)!='+' && st.charAt(st.length()-1)!='-' && st.charAt(st.length()-1)!='*' && st.charAt(st.length()-1)!='/') {
                        expression.getText().insert(expression.getSelectionStart(), "+");
                    }
                }

                break;
            }
            case R.id.minus:{
                expression.getText().insert(expression.getSelectionStart(),"-");
                break;
            }
            case R.id.imp:{
                String st = expression.getText().toString();
                if(!TextUtils.isEmpty(st)) {
                    if(st.charAt(st.length()-1)!='+' && st.charAt(st.length()-1)!='-' && st.charAt(st.length()-1)!='*' && st.charAt(st.length()-1)!='/') {
                        expression.getText().insert(expression.getSelectionStart(), "*");
                    }

                }
                break;
            }
            case R.id.div:{
                String st = expression.getText().toString();
                if(!TextUtils.isEmpty(st)) {
                    if (st.charAt(st.length() - 1) != '+' && st.charAt(st.length() - 1) != '-' && st.charAt(st.length() - 1) != '*' && st.charAt(st.length() - 1) != '/') {
                        expression.getText().insert(expression.getSelectionStart(), "/");
                    }
                }
                break;
            }
            case R.id.brO:{

                expression.getText().insert(expression.getSelectionStart(),"(");
                break;
            }
            case R.id.brC:{
                expression.getText().insert(expression.getSelectionStart(),")");
                break;
            }

        }
    }

}
