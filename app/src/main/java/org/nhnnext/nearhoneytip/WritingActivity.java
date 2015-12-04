package org.nhnnext.nearhoneytip;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

public class WritingActivity extends AppCompatActivity implements ImagePickFragment.OnHidePhotoListListener{

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);

        ImagePickFragment fragment = new ImagePickFragment(this);
        fragmentTransaction.add(R.id.imagegrid, fragment);
        fragmentTransaction.commit();

        // hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    public void onHidePhotoList(String path) {
        Log.i("writingAct:" , path);
    }
}
