package com.lovisgod.travelmantics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_insert.*
import java.util.*

class InsertActivity : AppCompatActivity() {

    val firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = firebaseDatabase.reference.child("traveldeals")
//    var travelDeal = intent.getParcelableExtra<TravelDeal>("Deal")
    var travelDeal: TravelDeal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
        }
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
            var deal:TravelDeal = TravelDeal(id,title,description, price, "")
            databaseReference.child(id).setValue(deal)
        }else {
            var deal:TravelDeal = TravelDeal(travelDeal!!.id,title,description, price, "")
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
}
