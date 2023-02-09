package edu.northeastern.numad23sp_zhangjinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class prime_directive extends AppCompatActivity implements View.OnClickListener{

    private Button btnFindPrimes;
    private Button btnTerminateSearch;
    private TextView txtLatestPrimeFound;
    private TextView txtCurrentNumber;
    private CheckBox checkBoxPacifierSwitch;

    private int curNum = 3;
    private String lastPrimeFoundString;
    private String currentNumberString;
    private MyThread endThread; //thread
    private Handler textHandler = new Handler(); //communicate for the main thread and the Backend thread
    private boolean threadRunning;//thread is running or not? In the lifecycle of activity, when the mobile is rotated, system will create a new activity, so we need to store the stage of the thread


    @Override
    protected void onCreate(Bundle savedInstanceState) { //bundle is used for to store the current stage key_value
        super.onCreate(savedInstanceState); //super is used to inform father class that I will use onCreate function
        setContentView(R.layout.activity_prime_directive);

//        button find primes
        btnFindPrimes = (Button) findViewById(R.id.button_find_prime);
        btnFindPrimes.setOnClickListener(this);

//        button terminate search
        btnTerminateSearch = (Button) findViewById(R.id.button_terminate_search);
        btnTerminateSearch.setOnClickListener(this);

//        textview
        txtLatestPrimeFound = (TextView) findViewById(R.id.textView_latest_prime_found);
        txtCurrentNumber = (TextView) findViewById(R.id.textView_current_number);

//        checkBox
        checkBoxPacifierSwitch = (CheckBox) findViewById(R.id.checkBox_pacifier_switch);

        if(savedInstanceState != null) { //not the first time enter
            threadRunning = savedInstanceState.getBoolean("Thread stage");
        }
        if(threadRunning){ //if the thread is running, continue find the primes;
            findPrimes();
        }

    }

    public void onClick(View view){
        if(view.getId() == R.id.button_find_prime){
            findPrimes();
        } else if (view.getId() == R.id.button_terminate_search){
            terminateSearch();
        }
    }

    private void findPrimes() {
        if(endThread == null){
            endThread = new MyThread();
            endThread.start();
            threadRunning = true;//if the thread is running, true
        }
    }

    //this function was not been used, when we rotated the mobile, onDestroy will run automatically
//    @Override
//    public void onDestroy() { //used before the activity be destroyed, for clean the resource
//        terminateSearch();
//        super.onDestroy();
//        threadRunning = false;//stop the thread
//    }

    private void terminateSearch() {
        if(endThread != null) {
            endThread.stopRun();
            endThread = null;
            threadRunning = false;
            curNum = 3;// if terminated, start at 3
        }
    }

//    save the stage and then to restore it
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Latest Prime Text", lastPrimeFoundString);
        outState.putString("Current Number Text", currentNumberString);
        outState.putInt("Current Number", curNum);
        outState.putBoolean("Thread stage", threadRunning); // store it and get it in the function of onCreate
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(threadRunning){   //don't make them disappear when the search is terminated, but they do not need to be preserved if the screen is rotated while the search is not running
            lastPrimeFoundString = savedInstanceState.getString("Latest Prime Text");
            currentNumberString = savedInstanceState.getString("Current Number Text");
            curNum = savedInstanceState.getInt("Current Number");
            txtLatestPrimeFound.setText(lastPrimeFoundString);  //set the textView again
            txtCurrentNumber.setText(currentNumberString);
        }
    }

//    inform the user really want to quit
    @Override
    public void onBackPressed() {
        if(endThread == null){
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("The search is running. Do you want to quit?")
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        prime_directive.super.onBackPressed();
                        finish();
                    })
                    .setNegativeButton(R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    //    class of thread: run and stopRun
    class MyThread extends Thread{
        private volatile boolean finish = false;

        public void run(){
            while(!finish){
                textHandler.post(()->{
                    currentNumberString = "Current Number: " + curNum;  //use string to store the info
                    txtCurrentNumber.setText(currentNumberString);  //textView only use string as setText
                    if(isPrime(curNum)){
                        lastPrimeFoundString = "Last Prime Found: " + curNum; // if isPrime, update it; or use last one
                        txtLatestPrimeFound.setText(lastPrimeFoundString);
                    }
                });
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                curNum += 2;
            }
        }


        public void stopRun(){
            finish = true;
        }


        private boolean isPrime(int curNum) {
            for(int i = 2; i <= curNum / i; i ++){
                if(curNum % i == 0) return false;
            }
            return true;
        }
    }
}

//Note:
//1. Click into the primeDirective interface, enter this activity, the first is to enter the onCreate method to create buttons, etc. At this time, if it is the first time to enter this activity, endThread and threadRunning is empty at the beginning, so there will not be any start.
//2. If you click findPrime, the findPrime method is executed, so an endThread and ThreadRunning are created; if you click end, the same thing happens.
//3. If you click findPrime again, a new thread is created because it was terminated before, and the onStore method is not used here, and the previous number can be run because the int variable is global.
//4.But if the screen is rotated, the Android device destroys the previous activity by default, so we have to use the bundle to store the previous stage, and then the system re-enters the onCreate method after the rotation, at this time, because there is a threadRunning state, start again to findPrime to create a new endThread start() and run with the previously saved state start() and run it with the previously saved state.
//1.点击进入primeDirective界面，进入这个activity，首先是进入onCreate方法创建button等，此时如果是第一次进入这个activity，endThread和threadRunning一开始是空的，所以不会有任何开始。
//2.点击findPrime，执行findPrime方法，于是创建了一个endThread和ThreadRunning；如果点击结束同理。
//3.如果再次点击findPrime，因为之前被terminate了，所以是新建的一个thread，而且这里没有用到onStore的方法，能接着之前的数字跑是因为int变量是全局的。
//4.但是如果是如果旋转屏幕，安卓设备默认销毁之前的activity，所以我们要用bundle把之前的stage存下来，然后旋转之后系统重新进入onCreate方法，这时候因为有threadRunning状态，再次开始findPrime新建一个endThread，通过.start（）接着之前保存的状态去跑。
//
