package io.area51.artest2.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.area51.artest2.R;
import io.area51.artest2.nodes.AugmentedEarthLayoutNode;
import io.area51.artest2.nodes.AugmentedWolverineLayoutNode;
import io.area51.artest2.ui.fragments.AugmentedImageFragment;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final Map<AugmentedImage, AugmentedWolverineLayoutNode> wolverineLayoutNodeHashMap = new HashMap<>();
    private final Map<AugmentedImage, AugmentedEarthLayoutNode> earthLayoutNodeHashMap = new HashMap<>();
    public ArFragment arFragment;
    private View introView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ux_fragment);
        introView = findViewById(R.id.introLayout);

        arFragment.getArSceneView()
                .getScene()
                .addOnUpdateListener(this::updateFrame);

//        arFragment.getArSceneView()
//                .getScene()
//                .addOnUpdateListener(frameTime -> {
//                    arFragment.onUpdate(frameTime);
//                    MainActivity.this.onUpdate();
//                });

//        initializeGallery();

    }

    private void updateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        // If there is no frame or ARCore is not tracking yet, just return.
        if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
            return;
        }

        Collection<AugmentedImage> updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    String text = "Detected Image " + augmentedImage.getIndex();
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "PAUSED : Image Name ::: " + augmentedImage.getName());
                    break;

                case TRACKING:
                    introView.setVisibility(View.GONE);
                    Log.e(TAG, "TRACKING : Image Name ::: " + augmentedImage.getName());
                    switch (augmentedImage.getName()) {
                        case AugmentedImageFragment.DEFAULT_IMAGE_1:
                            if (!earthLayoutNodeHashMap.containsKey(augmentedImage)) {
                                AugmentedEarthLayoutNode earthLayoutNode = new AugmentedEarthLayoutNode(this);
                                earthLayoutNode.setAugmentedImage(augmentedImage);
                                earthLayoutNodeHashMap.put(augmentedImage, earthLayoutNode);
                                arFragment.getArSceneView()
                                        .getScene()
                                        .addChild(earthLayoutNode);
                            }
                            break;
                        case AugmentedImageFragment.DEFAULT_IMAGE_2:
                            if (!wolverineLayoutNodeHashMap.containsKey(augmentedImage)) {
                                AugmentedWolverineLayoutNode wolverineLayoutNode = new AugmentedWolverineLayoutNode(this);
                                wolverineLayoutNode.setAugmentedImage(augmentedImage);
                                wolverineLayoutNodeHashMap.put(augmentedImage, wolverineLayoutNode);
                                arFragment.getArSceneView()
                                        .getScene()
                                        .addChild(wolverineLayoutNode);
                            }
                            break;
                    }
                    break;

                case STOPPED:
                    wolverineLayoutNodeHashMap.remove(augmentedImage);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wolverineLayoutNodeHashMap.isEmpty()) {
            introView.setVisibility(View.VISIBLE);
        }
    }

}