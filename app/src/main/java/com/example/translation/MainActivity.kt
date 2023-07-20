package com.example.translation

import android.app.ProgressDialog.show
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.translation.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var arrayAdapter: ArrayAdapter<String>
    private var items= arrayOf("Arabic", "English")
    private var conditions = DownloadConditions.Builder().requireWifi().build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        arrayAdapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, items)
        binding.languageFrom.setAdapter(arrayAdapter)
        binding.languageTo.setAdapter(arrayAdapter)



        binding.translateButton.setOnClickListener {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val translator = Translation.getClient(options)
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translator.translate(binding.textFrom.text.toString())
                        .addOnSuccessListener {
                            binding.textTo.text = it
                        }
                }
                .addOnFailureListener{
                    Snackbar.make(binding.textFrom, it.message.toString() ,Snackbar.LENGTH_SHORT).show()
                }
        }

    }

    private fun selectFrom(): String {
        return when(binding.textFrom.text.toString()){
            ""->TranslateLanguage.ENGLISH
            "Arabic" -> TranslateLanguage.ARABIC
            "English" -> TranslateLanguage.ENGLISH
            else-> TranslateLanguage.ENGLISH
        }
    }

    private fun selectTo(): String {
        return when(binding.textTo.text.toString()){
            ""->TranslateLanguage.ARABIC
            "Arabic" -> TranslateLanguage.ARABIC
            "English"->TranslateLanguage.ENGLISH
            else-> TranslateLanguage.ARABIC
        }
    }


}