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
import com.example.testproject.multithread.ElementsInitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var adapter: ElementsAdapter
	private lateinit var elementsInitService: ElementsInitService
	private lateinit var elementService: ElementService
	private val addButtonStream: PublishSubject<Int> = PublishSubject.create()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		adapter = ElementsAdapter(object : ElementsActionListener {
			override fun onElementMove(element: Element, moveBy: Int) {
				elementService.moveElements(element, moveBy)
			}

			override fun onElementDelete(element: Element) {
				elementService.deleteElement(element)
			}

			override fun onElementDetails(element: Element) {
				Toast.makeText(this@MainActivity, "Element: ${element.name}", Toast.LENGTH_SHORT).show()
			}
		})

		addButtonStream
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe() { binding.addButton.isEnabled = true }

		elementService = ElementService()
		elementsInitService = ElementsInitService(
			elementService.elementsInitStream, addButtonStream,
			elementService.getElements().toList() as ArrayList<Element>?
		)
		elementsInitService.initElements()

		val layoutManager = LinearLayoutManager(this)
		binding.recycleView.layoutManager = layoutManager
		binding.recycleView.adapter = adapter

		binding.addButton.isEnabled = false


		binding.addButton.setOnClickListener(object : View.OnClickListener {
			override fun onClick(v: View?) {
				elementService.addElement()
			}
		})

		elementService.addListener(elementsListener)
	}

	override fun onDestroy() {
		super.onDestroy()
		elementService.removeListener(elementsListener)
	}

	private val elementsListener: ElementsListener = {
		adapter.elements = it
	}
}
