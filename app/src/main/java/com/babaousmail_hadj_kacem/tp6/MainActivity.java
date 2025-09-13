package com.babaousmail_hadj_kacem.tp6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 100;
    ArrayList<Contact> contactList;
    myContactAdapter adapter;
    Button addBtn, showBtn;
    EditText nameEdit, telEdit;
    ListView listView;
    ContactsDb myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        adapter = new myContactAdapter(this, contactList);

        listView = findViewById(R.id.list_contacts);
        listView.setAdapter(adapter);
        myDB = new ContactsDb(this, "contacts.db");

        nameEdit = findViewById(R.id.editText_name);
        telEdit  = findViewById(R.id.editText_phone);
        addBtn   = findViewById(R.id.button_add);
        showBtn  = findViewById(R.id.button_show);

        // gestion du clic sur Ajouter
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString().trim();
                String tel  = telEdit.getText().toString().trim();
                if (name.isEmpty() || tel.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Les deux champs sont obligatoires",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Contact newContact = new Contact(name, tel);
                myDB.insertContact(newContact);

                contactList.add(newContact);
                adapter.notifyDataSetChanged();
                nameEdit.setText("");
                telEdit.setText("");
                Toast.makeText(MainActivity.this,
                        "Contact ajouté",
                        Toast.LENGTH_SHORT).show();

                makeCallWithPermission(tel);
            }
        });

        // gestion du clic sur Afficher
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Contact> all = myDB.getAllContacts();
                if (all != null) {
                    contactList.clear();
                    contactList.addAll(all);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Aucun contact en base",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Demande de permission et lancement de l'appel téléphonique
    private void makeCallWithPermission(String telnum) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + telnum));
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL);
        } else {
            startActivity(intent);
        }
    }

    // Résultat de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this,
                    "Permission d'appel refusée",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
