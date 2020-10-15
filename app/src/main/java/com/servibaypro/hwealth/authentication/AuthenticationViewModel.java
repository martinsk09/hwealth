package com.servibaypro.hwealth.authentication;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    public boolean isResetSuccess = false;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private MutableLiveData<Boolean> isAuth = new MutableLiveData<>();
    private MutableLiveData<Boolean> isCreated = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSignInSuccesss = new MutableLiveData<>();

    private static final String TAG = "AuthenticationViewModel";


    public AuthenticationViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("hwealth");
    }

    LiveData<Boolean> getAuthState(){
        return isAuth;
    }

    LiveData<Boolean> getIsCreated(){
        return isCreated;
    }

    LiveData<Boolean> getIsSignSuccess(){
        return isSignInSuccesss;
    }

    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){
                 isCreated.setValue(true);
                 Log.i(TAG, "onComplete: task completed " + isCreated);
                 sendVerificationemail();
             }
             else
                 isCreated.setValue(false);
            }
        });

    }

    private void sendVerificationemail() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }
    }

    public boolean isPasswordValid(String password){
        if(password.length() >= 6){
            return true;
        }
        return false;
    }

    public boolean isEmailValid(String email){
        if(email.endsWith("@gmail.com")){
            return true;
        }
        return false;
    }

   public void setUpFirebaseAuthChangedListener(){
     mAuthStateListener = new FirebaseAuth.AuthStateListener() {
         @Override
         public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
             FirebaseUser user = firebaseAuth.getCurrentUser();
             if(user != null){
                 Log.i(TAG, "onAuthStateChanged: "+user.getDisplayName());
                 if(user.isEmailVerified()){
                     Log.i(TAG, "onAuthStateChanged: "+user.getEmail());
                     Log.i(TAG, "onAuthStateChanged: "+ user.isEmailVerified());
                     isAuth.setValue(true);
                 }
                 else  {
                     isAuth.setValue(false);
                     FirebaseAuth.getInstance().signOut();
                 }

             }
         }
     };
   }

   public void signInUser(String email , String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    isSignInSuccesss.setValue(true);
                else
                    isSignInSuccesss.setValue(false);
            }
        });
   }

    public void sendPasswordResetEmail(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Password Reset Email sent.");
                            isResetSuccess = true;
                        }else{
                            Log.d(TAG, "onComplete: No user associated with that email.");
                            isResetSuccess = false;

                        }
                    }
                });
    }

   public void addAuthState(){
        mAuth.addAuthStateListener(mAuthStateListener);
   }

   public void removeAuthState(){
       mAuth.removeAuthStateListener(mAuthStateListener);
   }

   public void registerPractitioner(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mReference.child("users").child("health workers").child(user.getUid())
                    .setValue(user.getEmail());
        }

   }

}