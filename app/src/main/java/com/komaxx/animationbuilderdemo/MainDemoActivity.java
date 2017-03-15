/*
 MIT license, do whatever

 Copyright (c) 2017 Matthias Schicker

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */


package com.komaxx.animationbuilderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.komaxx.androidanimationbuilder.AndroidAnimationBuilder;

import static android.view.View.GONE;

public class MainDemoActivity extends Activity {
    View animatedView;
    View startButton;
    TextView pauseLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo);

        animatedView = findViewById(R.id.animated);
        startButton = findViewById(R.id.btn_start);
        pauseLabel = (TextView) findViewById(R.id.lbl_pause);

//        findViewById(R.id.btn_start).setOnClickListener(v -> runSimpleAnimationTest());
        findViewById(R.id.btn_start).setOnClickListener(v -> runComplexAnimationTest());
    }

    private void runSimpleAnimationTest() {
        new AndroidAnimationBuilder(animatedView)
                .rotateBy(20)
                .then().rotateBy(-40)
                .then().reset()
                .execute();
    }

    private void runComplexAnimationTest() {
        // Create the animation builder. The view is only kept as a weak reference
        // so don't worry about lifecycle. Create whenever.
        AndroidAnimationBuilder builder = new AndroidAnimationBuilder(animatedView);

        // set some ground rules: Every step with no assigned duration now takes 1 second
        builder.setDefaultStepDuration(1000)

                // first step: quick preparations.
                .alpha(0).ms(10).run(unused -> startButton.setEnabled(false))

                // dramatic entrance: Appear spinning
                .then().alpha(1).rotateBy(-90)

                // some more spinning fun. All those steps will take 1 second each.
                .then().rotateBy(180)
                .then().rotateBy(-285 -360)
                .then().rotateBy(15)

                // some moving fun
                .then().translateX(-1000).ms(600)
                // hop out of the screen before moving:
                // 'run(..)' is executed before the animation of the step is started
                .then().run(view -> view.setTranslationX(1500)).translateX(-1500)
                .then().translateX(0).ms(100)

                // phew. Let's have a break
                // show the pause while we're having it!
                .runAfter(view -> {
                    // AndroidAnimationBuilder,,,ception
                    // For the duration of the pause, be flashy AF
                    new AndroidAnimationBuilder(pauseLabel)
                            .setDefaultStepDuration(33)
                            .run(label -> label.setVisibility(View.VISIBLE))
                            .then()
                                .run(label -> ((TextView)label).setTextColor(randomColor()))
                                .repeat(33)
                            .runAfter(label -> label.setVisibility(GONE))
                            .execute();
                })
                .pause(1000)

                .translateY(-100).scaleX(2f).ms(500).runAfter(view -> view.setBackgroundColor(randomColor()))

                .then().rotateBy(90).scaleX(1).scaleY(3).decelerate()

                // finish the show!
                // reset: Undo everything that is not defined in the step to what it was
                // before the animation was started.
                .then().reset().scaleX(0).scaleY(0).ms(2000).accelerate()

                // everything back to the start
                .then().reset().ms(1).runAfter(unused -> startButton.setEnabled(true))
                .execute();

    }

    private int randomColor() {
        return 0xFF000000 | (int)Double.doubleToLongBits(Math.random());
    }
}
