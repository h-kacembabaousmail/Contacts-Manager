package com.babaousmail_hadj_kacem.tp6;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
public class ContactsDb extends SQLiteOpenHelper {

    // Requête SQL de création de la table
    private static final String sqlCreate =
            "CREATE TABLE contacts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "tel TEXT" +
                    ");";

    // Requête SQL de suppression de la table si elle existe (pour upgrade)
    private static final String sqlDrop =
            "DROP TABLE IF EXISTS contacts;";

    // Constructeur : initialise la base de données nommée 'name' avec version 1
    public ContactsDb(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    // Appelé lors de la création initiale de la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    // Appelé lors d'une mise à jour de version de la base
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(sqlDrop);
        db.execSQL(sqlCreate);
    }

    // Insère un nouveau contact en base
    public boolean insertContact(Contact person) {
        String sql = "INSERT INTO contacts (name, tel) " +
                "VALUES('" + person.getName() + "','" + person.getTel() + "')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        return true;
    }

    // Récupère un contact à partir de son numéro de téléphone
    public Contact getContact(String tel) {
        String sql = "SELECT * FROM contacts WHERE tel = '" + tel + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.getCount() == 0) return null;
        cur.moveToFirst();
        String name = cur.getString(1);
        tel = cur.getString(2);
        cur.close();
        return new Contact(name, tel);
    }

    // Récupère tous les contacts enregistrés
    public ArrayList<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.getCount() == 0) return null;

        ArrayList<Contact> list = new ArrayList<>();
        if (cur.moveToFirst()) {
            do {
                String name = cur.getString(1);
                String tel  = cur.getString(2);
                list.add(new Contact(name, tel));
            } while (cur.moveToNext());
        }
        cur.close();
        return list;
    }

    // Supprime un contact via l'objet Contact
    public void deleteContact(Contact contact) {
        String sql = "DELETE FROM contacts WHERE tel = '" + contact.getTel() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

    // Met à jour un contact en remplaçant l'ancien numéro
    public boolean updateContact(String oldTel, String newName, String newTel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE contacts SET name = ?, tel = ? WHERE tel = ?;";
        db.execSQL(sql, new Object[]{ newName, newTel, oldTel });
        return true;
    }

    // Supprime un contact en fonction du numéro de téléphone
    public boolean deleteContactByTel(String tel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM contacts WHERE tel = ?;";
        db.execSQL(sql, new Object[]{ tel });
        return true;
    }

}
