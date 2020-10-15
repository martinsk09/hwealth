package com.servibaypro.hwealth.authentication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.servibaypro.hwealth.R;
import com.servibaypro.hwealth.databinding.DialogResetpasswordBinding;

import java.util.Objects;

public class PasswordResetDialog extends DialogFragment {

    private static final String TAG = "PasswordResetDialog";

    //widgets
    private EditText mEmail;
    private DialogResetpasswordBinding mBinding;
    private AuthenticationViewModel mViewModel;

    //vars
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DialogResetpasswordBinding.inflate(inflater,container,false);
        final View view = mBinding.getRoot();
        mEmail = mBinding.emailPasswordReset;
        mContext = getActivity();

        TextView confirmDialog = (TextView) view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEmail.getText().toString().isEmpty()){
                    Log.d(TAG, "onClick: attempting to send reset link to: " + mEmail.getText().toString());
                    mViewModel.sendPasswordResetEmail(mEmail.getText().toString());
                    if(mViewModel.isResetSuccess)
                        Toast.makeText(mContext, "Password Reset Link Sent to Email",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, "No User is Associated with that Email",
                                Toast.LENGTH_SHORT).show();

                    Objects.requireNonNull(getDialog()).dismiss();
                   // Navigation.findNavController(view).navigate(R.id.action_passwordResetDialog_to_signInFragment);
                }

            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);

    }
}

