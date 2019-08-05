package com.lovisgod.travelmantics

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.net.toFile
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_insert.*
import java.util.*

class InsertActivity : AppCompatActivity() {

    val firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    var userid = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference: DatabaseReference = firebaseDatabase.reference.child(userid!!)
//    var travelDeal = intent.getParcelableExtra<TravelDeal>("Deal")
    var travelDeal: TravelDeal? = null

    var imageUri:Uri? = null
    var imageurl:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTitle("Add Deals")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        var txtTitle: EditText = findViewById(R.id.txttitle)
        var txtDescription: EditText = findViewById(R.id.txtdescription)
        var txtPrice: EditText = findViewById(R.id.txtprice)
        var intent = intent
        this.travelDeal = intent.getParcelableExtra<TravelDeal>("Deal")

        travelDeal?.let {
            txttitle.setText(travelDeal?.title)
            txtprice.setText(travelDeal?.price)
            txtdescription.setText(travelDeal?.description)
            val imageuri = Uri.parse(travelDeal?.imageUrl)
            imageurl = travelDeal?.imageUrl
//            deal_image.setImageURI(imageuri)
            Picasso.get().load(imageuri).into(deal_image)
        }

        pick_image.setOnClickListener {
            PickUp()
        }
    }

    private fun PickUp() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .start(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                if (imageurl == null){
                    val snackbar = Snackbar.make(findViewById(android.R.id.content), "PLEASE ADD DEALS BEFORE DELETE", Snackbar.LENGTH_LONG)
                    // show the snackbar
                    snackbar.show()
                }else{
                    saveDeals()
                    Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()
                    clean()
                    displayDealLists()
                }

                return true
            }
            R.id.delete_menu -> {
                deleteDeals()
                displayDealLists()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }


    }

    private fun saveDeals() {
        var title: String = txttitle.text.toString()
        var price: String = txtprice.text.toString()
        var description: String = txtdescription.text.toString()
        var userid = FirebaseAuth.getInstance().currentUser?.uid
        var id : String = UUID.randomUUID().toString()
        if(travelDeal?.id == null) {
            var deal:TravelDeal = TravelDeal(id,title,description, price, imageurl!!)
            databaseReference.child(id).setValue(deal)
        }else {
            var deal:TravelDeal = TravelDeal(travelDeal!!.id,title,description, price, imageurl!!)
            databaseReference.child(travelDeal!!.id).setValue(deal)
        }

    }

    private fun deleteDeals() {
        if(travelDeal?.id == null) {
            val snackbar =
                Snackbar.make(findViewById(android.R.id.content), "PLEASE ADD DEALS BEFORE DELETE", Snackbar.LENGTH_LONG)
            // show the snackbar
            snackbar.show()
        }else {

            databaseReference.child(travelDeal!!.id).removeValue()
            Toast.makeText(this, "Deal has been deleted", Toast.LENGTH_LONG).show()

        }
    }

    private fun displayDealLists() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    private  fun clean () {
        txttitle.setText("")
        txtdescription.setText("")
        txtprice.setText("")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //here we get the data back from the gallery
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            Log.d("TAG", "imageuri-> : " + imageUri.toString())
            uploadImage()
        } else {
            //startActivity(new Intent(this, HomeActivity.class));
            Toast.makeText(this, " ERROR!! -> Please Pick an Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadImage(){
        val firebaseStorage:FirebaseStorage = FirebaseStorage.getInstance()
        val storageref = firebaseStorage.reference.child("deals_picture")
        val fileref = storageref.child(imageUri?.lastPathSegment!!)
        fileref.putFile(imageUri!!).addOnSuccessListener{
             it.storage.downloadUrl.addOnCompleteListener {
                imageurl = it.result.toString()
                 deal_image.setImageURI(imageUri)
            }

        }
    }
}
