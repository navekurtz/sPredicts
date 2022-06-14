package com.example.spredicts;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    //fragment params qulities
    private String mParam1;
    private String mParam2;

    //simple privates for the design of the activity
    private TextView Logintv,subtitle2tv;
    private EditText LoginEmail, LoginPassword;
    private Button Loginb;
    //Firebase authentication
    private FirebaseAuth fAuth;


    public LoginFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Logintv = view.findViewById(R.id.tv_title);
        subtitle2tv = view.findViewById(R.id.tv_subtitle2);
        LoginEmail = view.findViewById(R.id.et_email2);
        LoginPassword = view.findViewById(R.id.et_password2);
        Loginb = view.findViewById(R.id.btn_login);
        fAuth = FirebaseAuth.getInstance();

        //An function that check if the user register to the system and enter him if he was registered
        Loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = LoginEmail.getText().toString().trim();
                String password = LoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    LoginEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    LoginPassword.setError("Password is required");
                    return;
                }

                if (password.length()<6){
                    LoginPassword.setError("Password must be > 6 characters");
                    return;
                }

                //authenticate the user - enter him to the stock/מאגר
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"Logged in successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity().getApplication(), MainActivity2.class);
                            startActivity(intent);
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Error !");
                            alertDialog.setMessage("The email or password are incorrect");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                });
            }
        });
        return view;
    }
}