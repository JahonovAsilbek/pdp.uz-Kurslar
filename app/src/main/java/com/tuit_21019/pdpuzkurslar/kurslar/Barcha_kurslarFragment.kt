package com.tuit_21019.pdpuzkurslar.kurslar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuit_21019.pdpuzkurslar.DataBase.DbHelper
import com.tuit_21019.pdpuzkurslar.R
import com.tuit_21019.pdpuzkurslar.adapters.KurslarAdapter
import com.tuit_21019.pdpuzkurslar.models.Kurs
import kotlinx.android.synthetic.main.add_kurs_dialog.view.*
import kotlinx.android.synthetic.main.fragment_barcha_kurslar.view.*

private const val ARG_PARAM1 = "navigation"


class Barcha_kurslarFragment : Fragment() {
    private var param1: String? = null

    lateinit var db: DbHelper

    lateinit var root: View
    lateinit var kurslarAdapter: KurslarAdapter
    lateinit var kurslarList: ArrayList<Kurs>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        @SuppressLint("UseRequireInsteadOfGet")
        db = DbHelper(this.context!!)

        root = inflater.inflate(R.layout.fragment_barcha_kurslar, container, false)

        setToolbar()
        loadData()
        kurslarList = db.getAllKurs()

        kurslarAdapter = KurslarAdapter(kurslarList)
        root.kurslar_recyclerview.adapter = kurslarAdapter
        root.kurslar_recyclerview.layoutManager = LinearLayoutManager(root.context)

        setMyItemClick()

        return root
    }

    private fun loadData() {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                Barcha_kurslarFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    private fun setToolbar() {
        root.toolbar.title = "Barcha kurslar"
        if (param1 == "guruhlar") {
            root.toolbar.menu.getItem(0).isVisible = false
        } else if (param1 == "mentorlar") {
            root.toolbar.menu.getItem(0).isVisible = false
            //toolbar menuni to'g'rilash hammasida ham menu yoq ekan
        }

        root.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        root.toolbar.setOnMenuItemClickListener { item ->
            if (item?.itemId == R.id.add_menu_btn) {
                val dialog = AlertDialog.Builder(root.context)
                val view = LayoutInflater.from(root.context).inflate(R.layout.add_kurs_dialog, null, false)
                dialog.setView(view)
                dialog.setPositiveButton("Qo'shish", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val kurs_nomi_et = view.add_kurs_dialog_et.text.toString()
                        if (kurs_nomi_et.trim() != "") {
                            db.insertKurs(Kurs(kurs_nomi_et))
                            dialog?.cancel()
                            Toast.makeText(root.context, "Muvaffaqiyatli qo'shildi!", Toast.LENGTH_LONG).show()
                            kurslarList.add(Kurs(kurs_nomi_et))
                            kurslarAdapter.notifyItemInserted(kurslarList.size - 1)
                            kurslarAdapter.notifyItemRangeChanged(kurslarList.size - 1, kurslarList.size)
                        } else {
                            Toast.makeText(root.context, "Kurs nomi bo'sh bo'lishi mumkin emas!", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                dialog.setNegativeButton("Yopish", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.cancel()
                    }
                })
                dialog.show()
            }
            true
        }
    }

    private fun setMyItemClick() {
        kurslarAdapter.kursItemClick = object : KurslarAdapter.KursItemClick {
            override fun kursitemClick(kurs: Kurs) {
                val bundle = Bundle()
                bundle.putSerializable("kurs", kurs)
                if (param1 == "mentorlar") {
                    findNavController().navigate(R.id.mentorlarFragment, bundle)
                } else if (param1 == "guruhlar") {
                    findNavController().navigate(R.id.guruhlarFragment, bundle)
                } else if (param1 == "kurs_haqida") {
                    findNavController().navigate(R.id.kurshaqidaFragment, bundle)
                }
            }
        }
    }
}