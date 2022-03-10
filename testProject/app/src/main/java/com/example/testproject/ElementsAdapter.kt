package com.example.testproject

import android.database.Observable
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.example.testproject.databinding.ItemElementBinding
import com.example.testproject.models.Element
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.random.Random

interface ElementsActionListener {

	fun onElementMove(element: Element, moveBy: Int)

	fun onElementDelete(element: Element)

	fun onElementDetails(element: Element)
}

class ElementsAdapter(
	private val actionListener: ElementsActionListener
) : RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder>(), View.OnClickListener {

	var elements: List<Element> = emptyList()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onClick(v: View) {
		val element = v.tag as Element
		when (v.id) {
			R.id.moreImageViewButton -> {
				showPopupMenu(v)
			}
			else -> {
				actionListener.onElementDetails(element)
			}
		}
	}

	override fun getItemCount(): Int = elements.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ItemElementBinding.inflate(inflater, parent, false)

		binding.root.setOnClickListener(this)
		binding.moreImageViewButton.setOnClickListener(this)

		return ElementsViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ElementsViewHolder, position: Int) {
		val element = elements[position]

		holder.itemView.tag = element
		holder.itemView.setBackgroundColor(element.color)

		with(holder.binding) {
			moreImageViewButton.tag = element

			elementTextView.text = element.name
		}
	}

	private fun showPopupMenu(view: View) {
		val popupMenu = PopupMenu(view.context, view)
		val context = view.context
		val element = view.tag as Element
		val position = elements.indexOfFirst { it.id == element.id }

		popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, R.string.move_up).apply {
			isEnabled = position > 0
		}
		popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, R.string.move_down).apply {
			isEnabled = position < elements.size - 1
		}
		popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, R.string.remove)

		popupMenu.setOnMenuItemClickListener {
			when (it.itemId) {
				ID_MOVE_UP -> {
					actionListener.onElementMove(element, -1)
				}
				ID_MOVE_DOWN -> {
					actionListener.onElementMove(element, 1)
				}
				ID_REMOVE -> {
					actionListener.onElementDelete(element)
				}
			}
			return@setOnMenuItemClickListener true
		}

		popupMenu.show()
	}

	class ElementsViewHolder(
		val binding: ItemElementBinding
	) : RecyclerView.ViewHolder(binding.root)

	companion object {
		private const val ID_MOVE_UP = 1
		private const val ID_MOVE_DOWN = 2
		private const val ID_REMOVE = 3
	}
}