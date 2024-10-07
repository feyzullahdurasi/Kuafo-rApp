package com.example.kuafrapp.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.kuafrapp.R

class SearchViewFragment : Fragment(R.layout.fragment_search_view) {

    private lateinit var searchEditText: EditText
    private lateinit var closeButton: Button
    private var isShowing: Boolean = true // Binding equivalent to 'show'

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText = view.findViewById(R.id.searchEditText)
        closeButton = view.findViewById(R.id.closeButton)

        // Handle search word input
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Handle search word changes
                val searchWord = s.toString()
                // You can use the searchWord variable as needed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Handle close button click
        closeButton.setOnClickListener {
            toggleSearchView()
        }
    }

    private fun toggleSearchView() {
        isShowing = !isShowing
        if (isShowing) {
            // Code to show search view
            searchEditText.visibility = View.VISIBLE
            closeButton.visibility = View.VISIBLE
        } else {
            // Code to hide search view (mimicking the SwiftUI `show.toggle()`)
            searchEditText.visibility = View.GONE
            closeButton.visibility = View.GONE
        }
    }
}
