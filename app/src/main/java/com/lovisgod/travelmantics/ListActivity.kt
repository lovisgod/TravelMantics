package com.lovisgod.travelmantics

import Adapter.DealAdapter
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListActivity : AppCompatActivity() {

    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var userid = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference: DatabaseReference = firebaseDatabase.reference.child(userid!!)
    val dealAdapter: DealAdapter = DealAdapter()
    val firebasAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var deals: ArrayList<TravelDeal> = ArrayList<TravelDeal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Latest Deals"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val recyclerView:RecyclerView = findViewById(R.id.deals_recycler)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = dealAdapter
        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var td = p0.getValue(TravelDeal::class.java)
                td?.let {
                    deals.add(td)
                    dealAdapter.setDealSList(deals)
                }
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var td = p0.getValue(TravelDeal::class.java)
                td?.let {
                    deals.add(td)
                    dealAdapter.setDealSList(deals)
                }
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var td = p0.getValue(TravelDeal::class.java)
                td?.let {
                    deals.add(td)
                    dealAdapter.setDealSList(deals)
                }
            }
        }
        databaseReference.addChildEventListener(childEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_activity_menu -> {
                val intent: Intent = Intent(this, InsertActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout_menu -> {
              loguserout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }


    }

    private fun loguserout(){
        val snackbar = make(findViewById(android.R.id.content), "Do you want to log out", LENGTH_LONG)
            snackbar.setAction("Yes", View.OnClickListener {
            var FirebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
            FirebaseAuth.signOut()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        })
// show the snackbar
        snackbar.show()
    }

}
