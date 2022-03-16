package com.example.testproject.models

import android.graphics.Color
import com.github.javafaker.Faker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

typealias ElementsListener = (elements: List<Element>) -> Unit

class ElementService {

	val elementsInitStream: PublishSubject<ArrayList<Element>> = PublishSubject.create()

	private var elements = mutableListOf<Element>()

	private val listeners = mutableSetOf<ElementsListener>()

	private val faker = Faker.instance()

	init {
		elements = (1..5).map {
			Element(
				id = it.toLong(),
				name = faker.name().name(),
				color = Color.parseColor(faker.color().hex())
			)
		}.toMutableList()

		elementsInitStream
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe() { it ->
				elements = it
				notifyChanges()
			}
	}

	fun getElements(): List<Element> {
		return elements
	}

	fun deleteElement(element: Element) {
		val elementIdToDelete = elements.indexOfFirst { it.id == element.id }
		if (elementIdToDelete != -1) {
			elements.removeAt(elementIdToDelete)
			notifyChanges()
		}
	}

	fun moveElements(element: Element, moveBy: Int) {
		val lastIndex = elements.indexOfFirst { it.id == element.id }
		if (lastIndex == -1) return
		val newIndex = lastIndex + moveBy
		if (newIndex < 0 || newIndex >= elements.size) return
		Collections.swap(elements, lastIndex, newIndex)
		notifyChanges()
	}

	fun addElement() {
		val newElement = Element(
			id = elements.size.toLong(),
			name = faker.name().name(),
			color = Color.parseColor(faker.color().hex())
		)
		elements.add(newElement)
		notifyChanges()
	}

	fun addListener(listener: ElementsListener) {
		listeners.add(listener)
		listener.invoke(elements)
	}

	fun removeListener(listener: ElementsListener) {
		listeners.remove(listener)
	}

	private fun notifyChanges() {
		listeners.forEach { it.invoke(elements) }
	}
}