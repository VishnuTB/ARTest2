package io.area51.artest2.nodes;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;

import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.area51.artest2.R;
import timber.log.Timber;

public class AugmentedEarthLayoutNode extends AnchorNode {

    private CompletableFuture<ViewRenderable> completableFutureEarth;
    private AugmentedImage augmentedImage;

    public AugmentedEarthLayoutNode(Context context) {
        if (completableFutureEarth == null) {
            completableFutureEarth = ViewRenderable.builder()
                    .setView(context, R.layout.layout_ar_view_earth)
                    .build();
        }
    }

    public AugmentedImage getImage() {
        return augmentedImage;
    }

    public void setAugmentedImage(AugmentedImage augmentedImage) {
        this.augmentedImage = augmentedImage;
        if (!completableFutureEarth.isDone()) {
            CompletableFuture.allOf(completableFutureEarth)
                    .thenAccept((Void aVoid) -> setAugmentedImage(augmentedImage))
                    .exceptionally(throwable -> {
                        Timber.i("Exception loading Layout");
                        return null;
                    })
                    .handle((notUsed, throwable) -> {
                        try {
                            ViewRenderable layoutRenderable = completableFutureEarth.get();
                            AppCompatTextView mTextViewTitle = layoutRenderable.getView().findViewById(R.id.textViewTitle);
                            mTextViewTitle.setText(R.string.text_welcome_to_earth);
                            mTextViewTitle.setTextSize(40f);

                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    });
        }


        setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
        Node earthNode;
        earthNode = new Node();
        earthNode.setParent(this);

        earthNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));

        Quaternion localRotation = new Quaternion();
        localRotation.set(new Vector3(-1.0f, 0.0f, 0.0f), 90);
        earthNode.setLocalRotation(localRotation);

        earthNode.setRenderable(completableFutureEarth.getNow(null));
    }

}