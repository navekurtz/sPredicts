package com.example.spredicts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    //fragment params qulities
    private String mParam1;
    private String mParam2;
    //simple privates for the design of the activity
    private EditText mFullName, mEmail, mPassword, mRePassword;
    private TextView Registertv,subtitletv;
    private Button Registerb;
    //Firebase authentication
    private FirebaseAuth mAuth;

    public RegisterFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    private void setContentView(int fragment_register) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Registertv=view.findViewById(R.id.tv_logo);
        Registertv=view.findViewById(R.id.tv_subtitle2);
        mFullName=view.findViewById(R.id.et_name);
        mEmail=view.findViewById(R.id.et_email2);
        mPassword=view.findViewById(R.id.et_password2);
        mRePassword=view.findViewById(R.id.et_repassword);
        Registerb=view.findViewById(R.id.btn_register);
        mAuth=FirebaseAuth.getInstance();

        //An function that register the user with his email, full name and password to the stock
        Registerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if (password.length()<6){
                    mPassword.setError("Password must be > 6 characters");
                    return;
                }
                //here the user are created
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"User created", Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(),"Swipe left, Go to the login screen", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(),"Error ! ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return view;
    }
}