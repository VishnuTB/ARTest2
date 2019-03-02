package io.area51.artest2.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.TooltipCompat;

import io.area51.artest2.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppCompatButton openSceneform = findViewById(R.id.buttonOpenSceneForm);

        // Listeners
        openSceneform.setOnClickListener(v -> startActivity(AugmentedImageActivity.getIntent(HomeActivity.this)));

        TooltipCompat.setTooltipText(openSceneform, getString(R.string.text_tooltip_google_sceneform));

    }

}