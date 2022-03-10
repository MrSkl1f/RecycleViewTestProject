package com.example.testproject

import android.app.Application
import com.example.testproject.models.ElementService

class App : Application() {

	val elementsService = ElementService()
}