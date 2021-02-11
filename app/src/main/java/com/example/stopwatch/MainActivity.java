package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int minutes = 0;
    private int seconds = 0;
    private int hundredth = 0;
    private final long INTERVAL = 1;

    private TextView minsTextView;
    private TextView secsTextView;
    private TextView hundsTextView;

    private final String PAUSE = "paused";
    private final String START = "started";
    private final String RESUME = "resumed";
    private String action = START;

    private final Handler handler = new Handler();
    private Runnable runnable;
    private Button startBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minsTextView = findViewById(R.id.minute);
        secsTextView = findViewById(R.id.seconds);
        hundsTextView = findViewById(R.id.hundredth);

        startBtn = findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setBackgroundResource(R.drawable.rounded_button);
                // Show the reset button when we click the start button for the first time
                if (action.equals(START)) {
                    showResetButton();
                }
                // We only need the start button to be manipulated to show
                // either Start, Stop or Resume.
                if (action.equals(START) || action.equals(RESUME)) {
                    startBtn.setText(R.string.stop);
                    startBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.danger));
                    action = PAUSE;
                    startWatch();
                } else if (action.equals(PAUSE)) {
                    // If action is PAUSE, then we need to Pause it.
                    startBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.resume));
                    startBtn.setText(R.string.resume);
                    action = RESUME;
                    pauseWatch();
                }
            }
        });
    }

    public void showResetButton() {
        LinearLayout btnLinearLayout = findViewById(R.id.buttonContainer);

        Button resetBtn = new Button(getApplicationContext());
            resetBtn.setText(R.string.reset);
            resetBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            resetBtn.setTextSize(18);
            resetBtn.setBackgroundResource(R.drawable.rounded_button);
//            resetBtn.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.reset_btn_tint));
            resetBtn.setAllCaps(false);
            resetBtn.setPadding(16, 0, 16, 0);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0F

        );
            buttonLayoutParams.setMargins(16, 0, 16, 0);
        resetBtn.setLayoutParams(buttonLayoutParams);


        // When the reset button is clicked, it should reset the watch,
        // remove the reset button and active the start button.
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWatch();
                btnLinearLayout.removeView(resetBtn);
                startBtn.setText(R.string.start);
                action = START;
            }
        });

        btnLinearLayout.addView(resetBtn);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPixelsToDp(float px){
        return px / ((float) getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPixel(float dp){
        return dp * ((float) getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method is called when the start button is clicked,
     * and the watch is not paused.
     */
    public void startWatch() {
        runnable = new Runnable() {
            @Override
            public void run() {
                updateWatch();
                handler.postDelayed(this, INTERVAL); // Optional, to repeat the task.
            }
        };
        handler.postDelayed(runnable, INTERVAL); // Optional, to repeat the task.
    }

    /**
     * This method is called when the start button is clicked,
     * and the watch is running.
     */
    public void pauseWatch() {
        handler.removeCallbacks(runnable);
    }

    /**
     * Reset minutes, seconds and hundredth to 0.
     */
    public void resetWatch() {
        pauseWatch();
        minutes = 0;
        seconds = 0;
        hundredth = 0;
        updateAllTime();
    }

    /**
     * Modifies the minutes, seconds or hundredth time such that hundredth is within the range
     * 00 to 99, seconds is within the range 00 to 60 and minutes is 00 or greater.
     */
    public void updateWatch() {
        hundredth += 1;
        if (hundredth >= 99) {
            seconds += 1;
            if (seconds >= 60) {
                minutes += 1;
            }
        }
        updateAllTime();
        hundredth = hundredth % 99;
        seconds = seconds % 60;
    }

    /**
     *
     * @param time minutes, seconds or hundredth
     * @return formats minutes, seconds or hundredth based on their input.
     *          It makes sure the string format has 2 characters
     *          (e.g 00, 02, 98, 60).
     */
    public String format(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }

    /**
     * Displays the watch to the user.
     * @param textView view that display the minutes, seconds or hundredth text.
     * @param time minutes, seconds or hundredth.
     */
    public void updateSingleTime(TextView textView, int time) {
        textView.setText(format(time));
    }

    public void updateAllTime() {
        updateSingleTime(minsTextView, minutes);
        updateSingleTime(secsTextView, seconds);
        updateSingleTime(hundsTextView, hundredth);
    }



}