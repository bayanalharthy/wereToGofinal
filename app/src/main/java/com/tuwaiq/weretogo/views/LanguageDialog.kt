package com.tuwaiq.weretogo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tuwaiq.weretogo.R
import com.tuwaiq.weretogo.databinding.LanguageBottomSheetBinding

class LanguageDialog(var selectedUnit: String, val onLanguageSelected: (String) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: LanguageBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LanguageBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (selectedUnit) {
            "ar" -> binding.btnArabic.isChecked = true
            "en" -> binding.btnEnglish.isChecked = true
        }

        binding.unitsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_arabic -> onLanguageSelected.invoke("ar")
                R.id.btn_english -> onLanguageSelected.invoke("en")
            }
            dismiss()
        }
    }
}