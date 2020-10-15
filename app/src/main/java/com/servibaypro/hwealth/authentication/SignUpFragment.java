package com.servibaypro.hwealth.authentication;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.servibaypro.hwealth.R;
import com.servibaypro.hwealth.databinding.SignUpFragmentBinding;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";

    private AuthenticationViewModel mViewModel;
    private SignUpFragmentBinding mBinding;
    private String mEmail;
    private String mPassword;


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = SignUpFragmentBinding.inflate(inflater,container,false);
        View view = mBinding.getRoot();





        mBinding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmail = mBinding.emailEditText.getText().toString();
                mPassword = mBinding.passwordEditText.getText().toString();
               if(!(mEmail.isEmpty() || mPassword.isEmpty())){
                   if(mViewModel.isEmailValid(mEmail) && mViewModel.isPasswordValid(mPassword)){
                       if(mBinding.checkbox.isChecked()){
                           showDialog();
                           mViewModel.createUser(mEmail,mPassword);
                       }
                       else
                           Toast.makeText(getContext(),"Read and check the privacy policy",Toast.LENGTH_SHORT).show();


                   }

                   else
                       Toast.makeText(getContext(),"email must end with '@gmail.com', and password must be greater than 6 characters",
                               Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(getContext(),"Error : empty fields",Toast.LENGTH_SHORT).show();
               }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getIsCreated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    if(mBinding.switchBtn.isChecked())
                        mViewModel.registerPractitioner();
                    hideDialog();
                    Navigation.findNavController(getView()).navigate(R.id.action_signUpFragment_to_signInFragment);
                }
                if(!aBoolean)
                    Toast.makeText(getContext(),"Something went wrong,check your internet connection and try again",
                            Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showDialog(){
        Log.i(TAG, "showDialog: "+ mBinding.progressBar.getVisibility());
        if(mBinding.progressBar.getVisibility() == View.GONE)
            mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog(){
        if(mBinding.progressBar.getVisibility() == View.VISIBLE)
            mBinding.progressBar.setVisibility(View.GONE);
    }

}