package io.area51.artest2.nodes;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.area51.artest2.R;

public class DefaultLayoutNode extends AnchorNode {

    private static final String TAG = DefaultLayoutNode.class.getSimpleName();
    private static CompletableFuture<ViewRenderable> completableFutureDefault;
    private AugmentedImage augmentedImage;

    public DefaultLayoutNode(Context context) {
        if (completableFutureDefault == null) {
            completableFutureDefault = ViewRenderable.builder()
                    .setView(context, R.layout.layout_ar_view_default)
                    .build();
        }
    }

    AugmentedImage getImage() {
        return augmentedImage;
    }

    public void setAugmentedImage(AugmentedImage augmentedImage) {
        this.augmentedImage = augmentedImage;
        if (!completableFutureDefault.isDone()) {
            CompletableFuture.allOf(completableFutureDefault)
                    .thenAccept((Void aVoid) -> setAugmentedImage(augmentedImage))
                    .exceptionally(throwable -> {
                        Log.e(TAG, "Exception loading", throwable);
                        return null;
                    })
                    .handle((notUsed, throwable) -> {
                        try {
                            ViewRenderable viewRenderableDefault = completableFutureDefault.get();
                            AppCompatTextView mTextView = viewRenderableDefault.getView().findViewById(R.id.textViewProductName);
                            mTextView.setText(augmentedImage.getName());
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    });
        }

        setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
        Node defaultNode;

        defaultNode = new Node();
        defaultNode.setParent(this);

        defaultNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));

        Quaternion localRotation = new Quaternion();
        localRotation.set(new Vector3(-1.0f, 0.0f, 0.0f), 90);
        defaultNode.setLocalRotation(localRotation);

        defaultNode.setRenderable(completableFutureDefault.getNow(null));

    }

}
