# 📲 Contacts Manager — Android (Java)

## 📖 Short Description
A simple Android application to manage contacts (name + phone number) with local SQLite storage.  
It allows adding, displaying, editing, deleting, and calling contacts directly from the interface.

---

## ✨ Features
- ➕ Add a contact (name + number) and save it into SQLite  
- 📋 Display all stored contacts (ListView with a custom adapter)  
- ✏️ Edit a contact (pre-filled dialog) and update it in the database  
- 🗑️ Delete a contact (remove from database and update UI)  
- 📞 Make a phone call (with `CALL_PHONE` permission handling)  
- 🗂️ Uses a `contacts` table (id, name, tel) managed by `ContactsDb` (`SQLiteOpenHelper`)  

---

## ⚙️ Technical Notes
- The database is named **`contacts.db`** and contains the **`contacts`** table  
- `updateContact` and `deleteContactByTel` use parameterized queries (to prevent SQL injection)  
- The app checks and requests the **`CALL_PHONE`** permission before making a call  
- Calling from the list uses **`ACTION_DIAL`** (opens the dialer) to avoid the permission if preferred  
