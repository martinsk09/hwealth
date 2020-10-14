package com.servibaypro.hwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void navigateToSignUp(View view) {
        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment2);
    }

    public void navigateToSignIn(View view) {
       Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment);
    }
}