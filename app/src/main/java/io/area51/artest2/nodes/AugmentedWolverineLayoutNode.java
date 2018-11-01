package io.area51.artest2.nodes;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

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

public class AugmentedWolverineLayoutNode extends AnchorNode {

    private CompletableFuture<ViewRenderable> completableFutureWolverine;
    private AugmentedImage augmentedImage;

    public AugmentedWolverineLayoutNode(Context context) {
        if (completableFutureWolverine == null) {
            completableFutureWolverine = ViewRenderable.builder()
                    .setView(context, R.layout.layout_ar_view_wolverine)
                    .build();
        }
    }

    public AugmentedImage getImage() {
        return augmentedImage;
    }

    public void setAugmentedImage(AugmentedImage augmentedImage) {
        this.augmentedImage = augmentedImage;
        if (!completableFutureWolverine.isDone()) {
            CompletableFuture.allOf(completableFutureWolverine)
                    .thenAccept((Void aVoid) -> setAugmentedImage(augmentedImage))
                    .exceptionally(throwable -> {
                        Timber.i("Exception loading Layout");
                        return null;
                    })
                    .handle((notUsed, throwable) -> {
                        try {
                            ViewRenderable viewRenderableWolverine = completableFutureWolverine.get();
                            AppCompatImageView mImageViewWolverine = viewRenderableWolverine.getView().findViewById(R.id.imageViewWolverine);
                            AppCompatImageButton mImageButtonMoreInfo = viewRenderableWolverine.getView().findViewById(R.id.imageButtonMoreInfo);
                            AppCompatTextView mTextViewAboutWolverine = viewRenderableWolverine.getView().findViewById(R.id.textViewAboutWolverine);
                            mImageButtonMoreInfo.setOnClickListener(view -> {
                                if (mTextViewAboutWolverine.getVisibility() == View.GONE) {
                                    mTextViewAboutWolverine.setVisibility(View.VISIBLE);
                                    mImageButtonMoreInfo.setImageResource(android.R.drawable.arrow_up_float);
                                } else {
                                    mImageButtonMoreInfo.setImageResource(android.R.drawable.arrow_down_float);
                                    mTextViewAboutWolverine.setVisibility(View.GONE);
                                }
                            });

                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    });
        }

        setAnchor(augmentedImage.createAnchor(augmentedImage.getCenterPose()));
        Node wolverineNode;

        wolverineNode = new Node();
        wolverineNode.setParent(this);

        wolverineNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));

        Quaternion localRotation = new Quaternion();
        localRotation.set(new Vector3(-1.0f, 0.0f, 0.0f), 90);
        wolverineNode.setLocalRotation(localRotation);

        wolverineNode.setRenderable(completableFutureWolverine.getNow(null));

    }

}