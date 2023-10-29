package pl.edu.pb.wi.zadanie3systemymobilne;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();
		if(fragmentManager.findFragmentById(R.id.fragment_container) == null){
			Fragment fragment = createFragment();
			fragmentManager.beginTransaction()
					.add(R.id.fragment_container,fragment)
					.commit();
		}
	}
	protected abstract Fragment createFragment();
}
