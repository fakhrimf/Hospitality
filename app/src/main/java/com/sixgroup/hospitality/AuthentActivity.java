//package com.sixgroup.hospitality;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseException;
//import com.google.firebase.FirebaseTooManyRequestsException;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
//import com.google.firebase.auth.PhoneAuthProvider;
//
//import java.util.concurrent.TimeUnit;
//
//public class AuthentActivity extends AppCompatActivity {
//    EditText phone, OTP;
//    Button btnGenOTP, btnVerify;
//    FirebaseAuth mAuth;
//    String verificationID;
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_register);
//        phone = findViewById(R.id.phoneInput);
//        btnGenOTP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(TextUtils.isEmpty(phone.getText().toString())){
//                    Toast.makeText(AuthentActivity.this, "Enter Valid Phone Number",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    String number = phone.getText().toString();
//                    sendverificationcode(number);
//                }
//            }
//        });
//        btnVerify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                verifycode();
//            }
//        });
//
//    }
//
//    private void sendverificationcode(String phoneNumber) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNumber)
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(this)
//                        .setCallbacks(mCallbacks)
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//            final String code = credential.getSmsCode();
//            if(code != null){
//                verifycode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(AuthentActivity.this, "Verification Failed",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s,
//                @NonNull PhoneAuthProvider.ForceResendingToken token) {
//            super.onCodeSent(s, token);
//            verificationID = s;
//
//        }
//    };
//
//    private void verifycode(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
//        signinbyCredentials(credential);
//    }
//
//    private void signinbyCredentials(PhoneAuthCredential credential) {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(AuthentActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(AuthentActivity.this, LoginActivity.class));
//                            finish();
//                        }
//                    }
//                });
//    }
//}
