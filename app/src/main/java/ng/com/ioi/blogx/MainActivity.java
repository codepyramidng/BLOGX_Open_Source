package ng.com.ioi.blogx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView newsList;
    private FirebaseFirestore newsDB;
    private FirebaseFirestore currencyDB;

    private List<DataModel> NewsAdapter;

    private DataAdapter dataAdapter;

    private AdView mAdView;

    TextView Bitcoin,Litecoin, Ethereum, nairaChange, dollarChange;

    public void onStart(){
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, "ca-app-pub-1843467277834046~6095659392");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bitcoin = findViewById(R.id.bitcoin);
        Litecoin = findViewById(R.id.litecoin);
        Ethereum = findViewById(R.id.ethereum);

        nairaChange = findViewById(R.id.nairaChange);
        dollarChange = findViewById(R.id.dollarChange);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        newsList = findViewById(R.id.newsList);
        newsDB = FirebaseFirestore.getInstance();

        NewsAdapter = new ArrayList<>();
        dataAdapter = new DataAdapter(NewsAdapter);

        newsList.setHasFixedSize(true);
        newsList.setLayoutManager(new LinearLayoutManager(this));
        newsList.setAdapter(dataAdapter);

        newsDB.collection("NewsDB")
                .orderBy("PostTime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {

                    Log.d("Firebase Log", "Error" + e.getMessage());
                } for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        DataModel dataModel = doc.getDocument().toObject(DataModel.class);
                        NewsAdapter.add(dataModel);
                        dataAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        //BitCoin
        DocumentReference bitcoin = FirebaseFirestore.getInstance().document("CurrencyDB/BitcoinValue");
        bitcoin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String liteValue = documentSnapshot.getString("BitcoinValue");
                    Bitcoin.setText(liteValue);
                }
            }
        });

        //LiteCoin
        DocumentReference litecoin = FirebaseFirestore.getInstance().document("CurrencyDB/LitecoinValue");
        litecoin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String liteValue = documentSnapshot.getString("LitecoinValue");
                    Litecoin.setText(liteValue);
                }
            }
        });

        //Ethereum
        DocumentReference ethereum = FirebaseFirestore.getInstance().document("CurrencyDB/EthereumValue");
        ethereum.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String ethereumValue = documentSnapshot.getString("EthereumValue");
                    Ethereum.setText(ethereumValue);
                }
            }
        });

        //Naira
        DocumentReference naira = FirebaseFirestore.getInstance().document("CurrencyDB/NairaValue");
        naira.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String nairaValue = documentSnapshot.getString("NairaValue");
                    nairaChange.setText(nairaValue);
                }
            }
        });

        //Naira
        DocumentReference dollar = FirebaseFirestore.getInstance().document("CurrencyDB/DollarValue");
        dollar.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String dollarValue = documentSnapshot.getString("DollarValue");
                    dollarChange.setText(dollarValue);
                }
            }
        });

        /*//BitCoin
        //DocumentReference bitcoin = FirebaseFirestore.getInstance().document("CurrencyDB/BitcoinValue");
        bitcoin.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //DataModel dataModel = documentSnapshot.toObject(DataModel.class);

                    String bitcoinValue = documentSnapshot.getString("BitcoinValue");
                    Bitcoin.setText(bitcoinValue);
                }
            }
        });

        //LiteCoin
        //DocumentReference litecoin = FirebaseFirestore.getInstance().document("CurrencyDB/LitecoinValue");
        litecoin.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //DataModel dataModel = documentSnapshot.toObject(DataModel.class);

                    String liteValue = documentSnapshot.getString("LitecoinValue");
                    Litecoin.setText(liteValue);
                }
            }
        });

        //Ethereum
        //DocumentReference ethereum = FirebaseFirestore.getInstance().document("CurrencyDB/EthereumValue");
        ethereum.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //DataModel dataModel = documentSnapshot.toObject(DataModel.class);

                    String ethereumValue = documentSnapshot.getString("EthereumValue");
                    Ethereum.setText(ethereumValue);
                }
            }
        });

        //Naira
        //DocumentReference naira = FirebaseFirestore.getInstance().document("CurrencyDB/NairaValue");
        naira.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //DataModel dataModel = documentSnapshot.toObject(DataModel.class);

                    String nairaValue = documentSnapshot.getString("NairaValue");
                    nairaChange.setText(nairaValue);
                }
            }
        });

        //Dollar
        //DocumentReference dollar = FirebaseFirestore.getInstance().document("CurrencyDB/DollarValue");
        dollar.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //DataModel dataModel = documentSnapshot.toObject(DataModel.class);

                    String dollarValue = documentSnapshot.getString("DollarValue");
                    dollarChange.setText(dollarValue);
                }
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            startActivity(new Intent(this, About.class));
        } else if (id == R.id.calculator) {
            startActivity(new Intent(this, Converter.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void ViewNews(View view) {
    }
}
