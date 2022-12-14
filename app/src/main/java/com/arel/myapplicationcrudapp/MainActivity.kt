package com.arel.myapplicationcrudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etNama : EditText
    private lateinit var etAlamat : EditText
    private lateinit var btnSave : Button
    private lateinit var listMhs : ListView
    private lateinit var ref: DatabaseReference
    private lateinit var mhsList: MutableList<Mahasiswa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("mahasiswa")

        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        btnSave = findViewById(R.id.btn_save)
        listMhs = findViewById(R.id.lv_mhs)
        btnSave.setOnClickListener(this)

        mhsList = mutableListOf()

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    mhsList.clear()
                    for(h in p0.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null) {
                            mhsList.add(mahasiswa)
                        }
                    }

                    val adapter = MahasiswaAdapter(this@MainActivity, R.layout.item_mhs, mhsList)
                    listMhs.adapter = adapter
                }
            }

        })

        listMhs.setOnItemClickListener{ parent, view, position, id ->
            val mahasiswa = mhsList.get(position)

            //INTENT

            val intent = Intent(this@MainActivity, AddMataKuliahActivity::class.java)
            intent.putExtra(AddMataKuliahActivity.EXTRA_ID, mahasiswa.id)
            intent.putExtra(AddMataKuliahActivity.EXTRA_NAMA, mahasiswa.nama)
            startActivity(intent)

            //INTENT

        }
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData(){
        val nama: String = etNama.text.toString().trim()
        val alamat: String = etAlamat.text.toString().trim()

        if (nama.isEmpty()) {
            etNama.error = "Isi Nama!"
            return
        }

        if(alamat.isEmpty()) {
            etAlamat.error = "Isi Alamat!"
            return
        }



        val mhsId: String? = ref.push().key

        val mhs = Mahasiswa(mhsId, nama, alamat)

        if (mhsId !=null) {
            ref.child(mhsId).setValue(mhs).addOnCompleteListener{
                Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

}