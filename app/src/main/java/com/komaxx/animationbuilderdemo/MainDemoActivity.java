package com.komaxx.animationbuilderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.komaxx.androidanimationbuilder.AndroidAnimationBuilder;

public class MainDemoActivity extends Activity {
    View animatedView;
    View startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo);

        animatedView = findViewById(R.id.animated);
        startButton = findViewById(R.id.btn_start);

        findViewById(R.id.btn_start).setOnClickListener(v -> runAnimationTest());
    }

    private void runAnimationTest() {
        AndroidAnimationBuilder builder = new AndroidAnimationBuilder(animatedView);

        builder.setDefaultStepDuration(1000)
                .alpha(0).ms(10).run(view -> startButton.setEnabled(false))
                .then().alpha(1).rotateBy(-90)

                // some spinning fun
                .then().rotateBy(180)
                .then().rotateBy(-285 -360)
                .then().rotateBy(15)

                .then().run(view -> view.setTranslationX(1500)).translateX(-1500)
                .then().translateX(100).ms(100)

                .then().translateY(-100).scaleX(3f).ms(500)
                .then().run(view -> view.setBackgroundColor(randomColor()))
                .then().scaleX(0).scaleY(0).ms(1000)

                // everything back to the start
                .then().reset().run(view -> startButton.setEnabled(true))
                .execute();

    }

    private int randomColor() {
        return 0xFF000000 | (int)Double.doubleToLongBits(Math.random());
    }
}
