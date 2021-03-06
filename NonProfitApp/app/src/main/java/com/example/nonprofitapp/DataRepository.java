package com.example.nonprofitapp;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.StructuredQuery;

public class DataRepository {
    private static DataRepository sInstance;
    private DataWrapper dataWrapper;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private CollectionReference foodBankOrders;
    private DocumentReference foodBank;
    private DocumentReference bag;

    boolean isVolunteer;

    private static final String TAG = DataRepository.class.getName();

    /**
     * Should be accessed through getInstance().
     */
    private DataRepository() {
        dataWrapper = new DataWrapper();
        db = FirebaseFirestore.getInstance();
    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    /**
     * Call when you need to reset user.
     */
    public void initUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            Log.i(TAG, "Refreshed user!");
            FirebaseAuth.getInstance().getCurrentUser().reload();
            user = FirebaseAuth.getInstance().getCurrentUser();
            Log.i(TAG, "User: " + getUser().getDisplayName() +
                    " from: " + getUser().getProviderId());
        } else {
            Log.i(TAG, "NULL USER");
        }

    }

    /**
     * Normal getter
     * @return
     */
    public FirebaseUser getUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user;
    }

    public boolean isLoggedIn() {
        initUser();
        return (getUser() != null);
    }

    public void setFoodBank(String foodBankName) {
        foodBank = db.collection("/foodbanks/").document(foodBankName);
        foodBankOrders = foodBank.collection("orders");
    }
    public CollectionReference getFoodBanks() {
        return db.collection("/foodbanks/");
    }

    public CollectionReference getBags() {
        return getFoodBank().collection("bags");
    }

    public void setBag(String bagName) {
        bag = getFoodBank().collection("bags").document(bagName);
    }


    /**
     * Messing with getting orders and returning a normal object. This is going to eat time on the
     * main thread, so it is not ideal. Uses class wide object orders.
     */
    //private Task<QuerySnapshot> orders = new ArrayList<>();
    synchronized public Task<QuerySnapshot> getOrdersNonLive() {
        Log.i(TAG, "Launched task");
        return foodBankOrders.get();
    }

    public void validateEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public CollectionReference getFoodBankOrders() {
        return foodBankOrders;
    }

    public DocumentReference getFoodBank() {
        return foodBank;
    }

    public DocumentReference getBag() {
        return bag;
    }

    public void setDataWrapper(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public CollectionReference getVolList() {
        return db.collection("/users/volunteers/uids");
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public void setVolunteer(boolean volunteer) {
        isVolunteer = volunteer;
    }
}
