package com.example.translation

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.translation.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val supportedLanguages = arrayOf(
        TranslateLanguage.ARABIC,
        TranslateLanguage.ENGLISH,
        TranslateLanguage.HINDI,
        TranslateLanguage.DUTCH,
        TranslateLanguage.GERMAN,
        TranslateLanguage.FRENCH,
        TranslateLanguage.JAPANESE,
        TranslateLanguage.KOREAN,
        TranslateLanguage.BENGALI,
        TranslateLanguage.GREEK,
        TranslateLanguage.BULGARIAN,
        TranslateLanguage.ITALIAN,
        TranslateLanguage.INDONESIAN
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, supportedLanguages)
        binding.languageFrom.setAdapter(arrayAdapter)
        binding.languageTo.setAdapter(arrayAdapter)

        binding.translateButton.setOnClickListener {
            val sourceLanguage = binding.languageFrom.text.toString()
            val targetLanguage = binding.languageTo.text.toString()

            if (sourceLanguage.isEmpty() || targetLanguage.isEmpty()) {
                Snackbar.make(binding.root, "Please select source and target languages.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            translateText(sourceLanguage, targetLanguage, binding.textFrom.text.toString())
        }
    }

    private fun translateText(sourceLanguage: String, targetLanguage: String, text: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded(DownloadConditions.Builder().requireWifi().build())
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        binding.textTo.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        Snackbar.make(binding.root, "Translation failed: ${exception.message}", Snackbar.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Snackbar.make(binding.root, "Model download failed: ${exception.message}", Snackbar.LENGTH_SHORT).show()
            }
    }
}
