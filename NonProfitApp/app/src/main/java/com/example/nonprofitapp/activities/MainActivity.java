package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.R;
import com.example.nonprofitapp.ui.login.LoginActivity;
import com.example.nonprofitapp.viewmodels.MainViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.Date;


import java.util.Arrays;
import java.util.List;


/*
 * Hello Pro team! This is a basic hello world app generated by Android Studio.
 *
 * Check out /res/layout/activity_main.xml for the GUI. Double clicking the text should let you
 * edit the TextView object.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String VOLUNTEER_LOGIN = "com.example.nonprofitapp.VOLUNTEER_LOGIN"; // Sent with intent as a label
    public static final String VOLUNTEER = "VOLUNTEER";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String SELECTED_BAG = "com.example.nonprofitapp.BAG";
    public static final String YEAR = "com.example.nonprofitapp.YEAR";
    public static final String MONTH = "com.example.nonprofitapp.MONTH";
    public static final String DAY = "com.example.nonprofitapp.DAY";
    public static final String HOUR = "com.example.nonprofitapp.HOUR";
    public static final String MINUTE = "com.example.nonprofitapp.MINUTE";
    public static final String FOOD_BANK_BUTTON = "com.example.nonprofitapp.FOOD_BANK_BUTTON"; // For food bank selection
    public static final int SIGN_IN_VOLUNTEER = 55555;
    public static final int SIGN_IN_CUSTOMER = 333;

    MainViewModel viewModel;
    DataRepository dataRepository;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Button customerSignIn = (Button) findViewById(R.id.customer_sign_in);
        customerSignIn.setOnClickListener(this);
        TextView volunteerSignIn = (TextView) findViewById(R.id.volunteer_sign_in);
        volunteerSignIn.setOnClickListener(this);


        welcome = findViewById(R.id.welcome_text);
        if (viewModel.isLoggedIn()) {
            Intent launchFoodBankSel = new Intent(MainActivity.this, Foodbank_Selection_Page.class);
            startActivity(launchFoodBankSel);
        }
    }

    /*
     * Handles both the customer sign in and volunteer sign in buttons.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customer_sign_in:
                viewModel.setVolunteer(false);
                firebaseLogin();
                break;
            case R.id.volunteer_sign_in:
                viewModel.setVolunteer(true);
                firebaseLogin();
                break;
        }
    }

    public void firebaseLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                //new AuthUI.IdpConfig.AnonymousBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        Intent launchFoodBankSel = new Intent(this, Foodbank_Selection_Page.class);
        Intent[] launchTwo = new Intent[2];
        launchTwo[0] = launchFoodBankSel;
        launchTwo[1] = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        startActivities(launchTwo);
    }


    public void customerClick() {
        Intent launch = new Intent(this, LoginActivity.class);
        launch.putExtra(VOLUNTEER_LOGIN, false);
        startActivity(launch);
    }

    public void volunteerClick() {
        Intent launch = new Intent(this, LoginActivity.class);
        launch.putExtra(VOLUNTEER_LOGIN, true);
        startActivity(launch);
    }

    /** Called after user logs in as a customer and selects a food bank */
    public void selectGroceryBag(View view) {
        Intent intent = new Intent(this, GroceryBagSelectionActivity.class);
        startActivity(intent);
    }

    public void sendMessageFoodbank(View view) {
        Intent intent = new Intent(this, Foodbank_Selection_Page.class);
        startActivity(intent);
    }



    public void sendMessageWindow(View view) {
        //do something
        Intent intent = new Intent(this, Window_Display.class);
        startActivity(intent);

    }
}