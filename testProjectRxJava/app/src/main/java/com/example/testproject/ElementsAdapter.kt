package com.example.testproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject

class ElementsAdapter(private val elements: MutableList<Element>) : RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder>() {

	val elementClickStream: PublishSubject<View> = PublishSubject.create()

	override fun getItemCount(): Int = elements.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_element, parent, false);

		elementClickStream.subscribe() { v ->
			val element = v.tag as Element
			when (v.id) {
				R.id.moreImageViewButton -> {
					deleteElement(element)
				}
				else -> {
					print(elements.size)
					Toast.makeText(parent.context, "Element: ${element.name}", Toast.LENGTH_SHORT).show()
				}
			}
		}

		return ElementsViewHolder(view)
	}

	override fun onBindViewHolder(holder: ElementsViewHolder, position: Int) {
		val element = elements[position]
		holder.bind(element)
	}

	fun deleteElement(element: Element) {
		val elementIdToDelete = elements.indexOfFirst { it.id == element.id }
		if (elementIdToDelete != -1) {
			elements.removeAt(elementIdToDelete)
		}
		notifyDataSetChanged()
	}

	fun addElement(element: Element) {
		elements.add(element)
		notifyDataSetChanged()
	}

	inner class ElementsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val imageView: ImageView by lazy { view.findViewById(R.id.moreImageViewButton) as ImageView }
		private val textView: TextView by lazy { view.findViewById(R.id.elementTextView) as TextView }

		init {
			view.setOnClickListener { v -> elementClickStream.onNext(v) }
			imageView.setOnClickListener { v -> elementClickStream.onNext(v) }
		}

		fun bind(element: Element) {
			itemView.tag = element
			itemView.setBackgroundColor(element.color)
			imageView.tag = element
			textView.text = element.name
		}
	}
}