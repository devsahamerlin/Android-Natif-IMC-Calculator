package com.merlin.saha.imccalculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private var editTextPoids: EditText? = null
    private var editTextTaille: EditText? = null
    private var buttonCalculer: Button? = null
    private var textViewResultat: TextView? = null
    private var textViewCategorie: TextView? = null
    private var imageViewCategorie: ImageView? = null
    private var linearLayoutViewResultat: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initializeViews()
        setupCalculateButton()
    }

    private fun initializeViews() {
        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        textViewResultat = findViewById(R.id.textViewResultat)
        textViewCategorie = findViewById(R.id.textViewCategorie)
        imageViewCategorie = findViewById(R.id.imageViewCategorie)
        linearLayoutViewResultat = findViewById(R.id.linearLayoutViewResultat)
    }

    private fun setupCalculateButton() {
        buttonCalculer!!.setOnClickListener { calculerIMC() }
    }

    private fun calculerIMC() {
        try {
            val poidsStr = editTextPoids!!.text.toString().trim { it <= ' ' }
            val tailleStr = editTextTaille!!.text.toString().trim { it <= ' ' }

            if (poidsStr.isEmpty() || tailleStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return
            }

            val poids = poidsStr.toDouble()
            val taille = tailleStr.toDouble()

            if (poids <= 0 || taille <= 0) {
                Toast.makeText(this, "Veuillez entrer des valeurs positives", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            val tailleEnMetres = taille / 100.0

            val imc = poids / (tailleEnMetres * tailleEnMetres)

            afficherResultat(imc)
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Veuillez entrer des nombres valides", Toast.LENGTH_SHORT).show()
        }
    }

    private fun afficherResultat(imc: Double) {
        val resultatFormate = String.format("%.2f", imc)
        textViewResultat!!.text = "Votre IMC est: $resultatFormate"

        val categorie = determinerCategorie(imc)
        afficherCategorie(categorie)
    }

    private fun determinerCategorie(imc: Double): CategorieIMC {
        return if (imc < 18.5) {
            CategorieIMC.MAIGREUR
        } else if (imc >= 18.5 && imc < 25) {
            CategorieIMC.NORMAL
        } else if (imc >= 25 && imc < 30) {
            CategorieIMC.SURPOIDS
        } else if (imc >= 30 && imc < 40) {
            CategorieIMC.OBESITE_MODEREE
        } else {
            CategorieIMC.OBESITE_SEVERE
        }
    }

    private fun afficherCategorie(categorie: CategorieIMC) {
        textViewCategorie!!.text = categorie.nom
        textViewCategorie!!.setBackgroundColor(ContextCompat.getColor(this, categorie.couleur))

        Log.d("BMI_DEBUG", "Attempting to load image resource: ${categorie.image}")

        //linearLayoutViewResultat!!.setBackgroundColor(ContextCompat.getColor(this, categorie.couleur))
        imageViewCategorie!!.setImageResource(categorie.image)
    }

    private enum class CategorieIMC(val nom: String, val couleur: Int, val image: Int) {
        MAIGREUR("Maigreur", R.color.bleu_maigreur, R.drawable.maigre),
        NORMAL("Normal", R.color.vert_normal, R.drawable.normal),
        SURPOIDS("Surpoids", R.color.jaune_surpoids, R.drawable.surpoids),
        OBESITE_MODEREE("Obésité modérée", R.color.orange_obesite_moderee, R.drawable.obese),
        OBESITE_SEVERE("Obésité sévère", R.color.rouge_obesite_severe, R.drawable.t_obese)
    }
}