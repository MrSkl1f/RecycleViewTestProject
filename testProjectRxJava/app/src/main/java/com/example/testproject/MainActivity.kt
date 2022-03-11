package com.example.testproject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.javafaker.Faker
import io.reactivex.rxjava3.subjects.PublishSubject

class MainActivity : AppCompatActivity() {

	private lateinit var adapter: ElementsAdapter
	val faker: Faker = Faker.instance()
	var countOfElements: Long = 4

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		adapter = ElementsAdapter(createElementsList())

		val layoutManager = LinearLayoutManager(this)
		val recyclerView: RecyclerView = findViewById(R.id.recycleView)
		recyclerView.layoutManager = layoutManager
		recyclerView.adapter = adapter

		var button: Button = findViewById(R.id.addButton)
		button.setOnClickListener(object : View.OnClickListener {
			override fun onClick(v: View?) {
				countOfElements++
				adapter.addElement(
					Element(
						id = countOfElements,
						name = faker.name().name(),
						color = Color.parseColor(faker.color().hex())
					)
				)
			}
		})
	}

	fun createElementsList(): MutableList<Element> {
		var elements: MutableList<Element> = (1..countOfElements).map {
			Element(
				id = it.toLong(),
				name = faker.name().name(),
				color = Color.parseColor(faker.color().hex())
			)
		}.toMutableList()
		return elements
	}

	override fun onDestroy() {
		super.onDestroy()
	}
}