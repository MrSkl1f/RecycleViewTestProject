package com.example.testproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testproject.databinding.ActivityMainBinding
import com.example.testproject.models.Element
import com.example.testproject.models.ElementService
import com.example.testproject.models.ElementsListener

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var adapter: ElementsAdapter

	private val elementsService: ElementService
		get() = (applicationContext as App).elementsService

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		adapter = ElementsAdapter(object : ElementsActionListener {
			override fun onElementMove(element: Element, moveBy: Int) {
				elementsService.moveElements(element, moveBy)
			}

			override fun onElementDelete(element: Element) {
				elementsService.deleteElement(element)
			}

			override fun onElementDetails(element: Element) {
				Toast.makeText(this@MainActivity, "Element: ${element.name}", Toast.LENGTH_SHORT).show()
			}
		})
		val layoutManager = LinearLayoutManager(this)
		binding.recycleView.layoutManager = layoutManager
		binding.recycleView.adapter = adapter

		binding.addButton.setOnClickListener(object : View.OnClickListener {
			override fun onClick(v: View?) {
				elementsService.addElement()
			}
		})

		elementsService.addListener(elementsListener)
	}

	override fun onDestroy() {
		super.onDestroy()
		elementsService.removeListener(elementsListener)
	}

	private val elementsListener: ElementsListener = {
		adapter.elements = it
	}
}