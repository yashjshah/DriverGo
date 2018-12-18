package com.example.jayon.drivergo.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.UserModel;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class IntroActivityFragment extends Fragment {
    Button googleSignin;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_intro, container, false);
          pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
         editor = pref.edit();

        editor.commit();

        googleSignin=(Button)view.findViewById(R.id.signinGooglebutton);
        mAuth = FirebaseAuth.getInstance();
        Button signin =(Button)view.findViewById(R.id.Signin);
        Button signup =(Button)view.findViewById(R.id.signup);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(getActivity(),gso);

        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),SigninActivity.class));

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),Signup1Activity.class));

            }
        });

        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("check", "2");  // Saving string
                editor.commit();

                signIn();

            }
        });

        return view;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String scope = "oauth2:"+Scopes.EMAIL+" "+ Scopes.PROFILE;
                            String accessToken = GoogleAuthUtil.getToken(getActivity(), account.getAccount(), scope, new Bundle());
                            Log.d("token", "accessToken:"+accessToken); //accessToken:ya29.Gl...

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GoogleAuthException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AsyncTask.execute(runnable);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Error", "Google sign in failed", e);
                // ...
            }
        }
    }

    String id="";
    int already=0;

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("users");
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                                        UserModel model = data.getValue(UserModel.class);

                                        if (!(data.getKey().equals(user.getUid()))){
                                            already=1;
                                            Log.v("xxxx", already+"");

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            if (already==1){
                                UserModel userModel = new UserModel(user.getPhotoUrl().toString(), "Hello, there! iam using Driver Go.", user.getDisplayName().toString(), "", user.getEmail().toString(), "", "", "", "0.0", "0.0", "Client", "", "");
                                DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                                reference0.setValue(userModel);
                                already=0;

                            }



                            UserModel userModel = new UserModel(user.getPhotoUrl().toString(), "Hello, there! iam using Driver Go.", user.getDisplayName().toString(), "", user.getEmail().toString(), "", "", "", "0.0", "0.0", "Client", "", "");
                            DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                            reference0.setValue(userModel);

                            Intent intent = new Intent(getActivity(), AllOffersActivity.class);

                            intent.putExtra("id",user.getUid());
                            intent.putExtra("name",user.getDisplayName());
                            intent.putExtra("email",user.getEmail());
                            intent.putExtra("photo",user.getPhotoUrl().toString());
                            id=user.getPhotoUrl().toString();
                            startActivity(intent);


                        } else {
                            Snackbar.make(getView(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
