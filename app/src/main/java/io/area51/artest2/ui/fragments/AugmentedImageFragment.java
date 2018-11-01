package io.area51.artest2.ui.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

public class AugmentedImageFragment extends ArFragment {

    public static final String DEFAULT_IMAGE_1 = "image_1.jpg";
    public static final String DEFAULT_IMAGE_2 = "image_2.jpg";
    private static final String DEFAULT_DATABASE_NAME = "image_database.imgdb";

    private static final double MIN_OPENGL_VERSION = 3.0;
    private static final String IMAGE_PATH = "https://www.famousbirthdays.com/faces/jackman-hugh-image.jpg";
    private String TAG = AugmentedImageFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            String openGlVersionString = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getDeviceConfigurationInfo()
                    .getGlEsVersion();
            if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
                Timber.i("Sceneform requires OpenGL ES 3.0 or later");
                Toast.makeText(context, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Timber.i("Sceneform requires OpenGL ES 3.0 or later");
            Toast.makeText(context, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);
        getArSceneView().getPlaneRenderer().setEnabled(false);
        return view;
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = super.getSessionConfiguration(session);
        config.setFocusMode(Config.FocusMode.AUTO);
        if (!setupAugmentedImageDatabase(config, session)) {
            Toast.makeText(getActivity(), "Could not setup augmented image database", Toast.LENGTH_SHORT).show();
        }
        return config;
    }

    private boolean setupAugmentedImageDatabase(Config config,
                                                Session session) {
        AugmentedImageDatabase augmentedImageDatabase;
        AssetManager assetManager = getContext() != null ? getContext().getAssets() : null;
        if (assetManager == null) {
            return false;
        }

        //region Dynamic database creation
//        augmentedImageDatabase = new AugmentedImageDatabase(session);
//        Bitmap bitmap = null;
//        try (InputStream inputStream = getContext().getAssets().open(DEFAULT_IMAGE_1)) {
//            bitmap = BitmapFactory.decodeStream(inputStream);
//        } catch (IOException e) {
//            Log.e(TAG, "I/O exception loading augmented image bitmap.", e);
//        }
//        if (bitmap != null) {
//            augmentedImageDatabase.addImage(DEFAULT_IMAGE_1, bitmap);
//        }
        //endregion

        //region From Image path
        /*Glide.with(getActivity())
                .asBitmap()
                .load(IMAGE_PATH)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Toast.makeText(getActivity(), "Image loaded from path", Toast.LENGTH_SHORT).show();
                        augmentedImageDatabase.addImage(DEFAULT_IMAGE_1, resource);
                        config.setAugmentedImageDatabase(augmentedImageDatabase);
                        session.configure(config);
                    }
                });*/
        //endregion

        //region From local database
        try (InputStream is = getContext().getAssets().open(DEFAULT_DATABASE_NAME)) {
            augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, is);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Unable to load ImageDatabase", Toast.LENGTH_SHORT).show();
            Timber.e("IO exception loading augmented image database");
            return false;
        }

        config.setAugmentedImageDatabase(augmentedImageDatabase);
        session.configure(config);
        //endregion

        return true;

    }

}