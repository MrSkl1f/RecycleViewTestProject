package com.example.testproject.multithread;

import android.graphics.Color;

import com.example.testproject.models.Element;
import com.github.javafaker.Faker;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ElementsInitService {
	
	private volatile ArrayList<Element> elements = new ArrayList<>();
	private int maxElements = 1000000;
	private Faker faker = Faker.instance();
	private volatile PublishSubject<ArrayList<Element>> elementsInitStream;
	private volatile PublishSubject<Integer> addButtonStream;
	
	public ElementsInitService(final PublishSubject<ArrayList<Element>> stream,
		final PublishSubject<Integer> btnObserver,
		final ArrayList<Element> startElements) {
		elementsInitStream = stream;
		addButtonStream = btnObserver;
		elements = startElements;
	}
	
	public ArrayList<Element> getElements() {
		return elements;
	}
	
	public void initElements() {
		final Thread initThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i = elements.size() + 1; i < maxElements; i++) {
					elements.add(new Element(
						i + 1,
						faker.name().name(),
						Color.parseColor(faker.color().hex())
					));
				}
				elementsInitStream.onNext(elements);
				addButtonStream.onNext(1);
			}
		});
		initThread.start();
	}
}
