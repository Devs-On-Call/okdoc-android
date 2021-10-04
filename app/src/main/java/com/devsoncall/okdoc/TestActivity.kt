package com.devsoncall.okdoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        var testList = mutableListOf(
            Test("Follow", false),
            Test("Option 1", false),
            Test("hahahahh", false),
            Test("idkwhat to type", false),
            Test("asdasdasd", false),
            Test("sadsadsad", false),
            Test("hellowgoodbye", false)
        )

        val adapter = TestAdapter(testList)
        items.adapter = adapter
        items.layoutManager = LinearLayoutManager(this)

        addText.setOnClickListener{
            val title = inputText.text.toString()
            val test = Test(title, false)
            testList.add(test)
//            adapter.notifyDataSetChanged()    // update whole recycler view - not efficient
            adapter.notifyItemInserted(testList.size - 1)
        }
    }
}