package com.servibaypro.hwealth.authentication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.servibaypro.hwealth.R;
import com.servibaypro.hwealth.databinding.SignInFragmentBinding;

public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";

    private SignInFragmentBinding mBinding;
    private AuthenticationViewModel mViewModel;



    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = SignInFragmentBinding.inflate(getLayoutInflater());
        final View view = mBinding.getRoot();



        mBinding.signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mBinding.emailEditText.getText().toString();
                String password = mBinding.passwordEditText.getText().toString();
                if(!(email.isEmpty() || password.isEmpty())){
                    if(mViewModel.isEmailValid(email) && mViewModel.isPasswordValid(password)){
                        showDialog();
                        mViewModel.signInUser(email,password);
                    }

                    else
                        Toast.makeText(getContext(),"No such user",
                                Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"Error : empty fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordResetDialog resetDialog = new PasswordResetDialog();
                resetDialog.show(getParentFragmentManager(),"reset dialog");
                //Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_passwordResetDialog);
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        mViewModel.setUpFirebaseAuthChangedListener();
        mViewModel.getAuthState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                hideDialog();
                if(aBoolean){
                    Log.i(TAG, "onChanged: "+aBoolean);
                    Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_mainFragment);
                }
                else
                    Toast.makeText(getContext(),"Check your mail for verification link",Toast.LENGTH_SHORT).show();

            }
        });
        mViewModel.getIsSignSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    hideDialog();
                    Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_mainFragment);
                }
                else {
                    Toast.makeText(getContext(),"An error occurred, check your credentials and internet",
                            Toast.LENGTH_SHORT).show();
                    hideDialog();
                }

            }
        });
        // TODO: Use the ViewModel
    }

    private void showDialog(){
        if(mBinding.progressBar.getVisibility() == View.GONE)
            mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if(mBinding.progressBar.getVisibility() == View.VISIBLE)
            mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.removeAuthState();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.addAuthState();
    }
}