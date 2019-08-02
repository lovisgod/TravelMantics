package com.lovisgod.travelmantics

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_insert.*
import java.util.*

class InsertActivity : AppCompatActivity() {

    val firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = firebaseDatabase.reference.child("traveldeals")
//    var travelDeal = intent.getParcelableExtra<TravelDeal>("Deal")
    var travelDeal: TravelDeal? = null

    var imageUri:Uri? = null


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
            deal_image.setImageURI(imageuri)
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
                saveDeals()
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()
                clean()
                displayDealLists()
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
        var id : String = UUID.randomUUID().toString()
        if(travelDeal?.id == null) {
            var deal:TravelDeal = TravelDeal(id,title,description, price, imageUri.toString())
            databaseReference.child(id).setValue(deal)
        }else {
            var deal:TravelDeal = TravelDeal(travelDeal!!.id,title,description, price, travelDeal!!.imageUrl)
            databaseReference.child(travelDeal!!.id).setValue(deal)
        }

    }

    private fun deleteDeals() {
        if(travelDeal?.id == null) {
            Toast.makeText(this, "Please save a deal before deleting", Toast.LENGTH_LONG).show()
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
            deal_image.setImageURI(imageUri)
        } else {
            //startActivity(new Intent(this, HomeActivity.class));
            Toast.makeText(this, " ERROR!! -> Please Pick an Image", Toast.LENGTH_LONG).show()
        }
    }
}
