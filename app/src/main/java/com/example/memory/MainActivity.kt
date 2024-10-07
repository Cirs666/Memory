package com.example.memory

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var buttons: MutableList<Button>
    private lateinit var images: MutableList<Int>
    private var clickedFirst: Button? = null
    private var clickedSecond: Button? = null
    private var matchCount = 0
    private var isProcessing = false
    private val handler = Handler()
    private var errorCount = 0 // Contador de errores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)

        // Inicializa las imágenes y los botones
        initializeGame()

        // Botón de reiniciar
        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            initializeGame() // Reinicia el juego
        }
    }

    private fun initializeGame() {
        // Reinicia el contador de coincidencias y errores
        matchCount = 0
        errorCount = 0 // Reinicia el contador de errores
        isProcessing = false
        clickedFirst = null
        clickedSecond = null

        // Lista de imágenes (6 pares)
        images = mutableListOf(
            R.drawable.pikachu, R.drawable.pikachu,
            R.drawable.snorlax, R.drawable.snorlax,
            R.drawable.charmeleon, R.drawable.charmeleon,
            R.drawable.mewtwo, R.drawable.mewtwo,
            R.drawable.oshawott, R.drawable.oshawott,
            R.drawable.onix, R.drawable.onix
        )

        // Baraja las imágenes
        images.shuffle()

        // Añade los botones al GridLayout
        buttons = mutableListOf()
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.setBackgroundResource(R.drawable.mapa) // Imagen de fondo de las cartas
            button.tag = images[i] // Asigna la imagen a cada botón
            button.isEnabled = true // Asegúrate de que los botones estén habilitados

            // Asigna un índice a cada botón
            button.setOnClickListener { onCardClicked(button) }
            buttons.add(button)
        }
    }

    private fun onCardClicked(button: Button) {
        // Evita que el usuario siga clickeando durante una comparación
        if (isProcessing || button.background.constantState == getDrawable(R.drawable.mapa)?.constantState) {
            return
        }

        // Muestra la imagen de la carta
        button.setBackgroundResource(button.tag as Int)

        if (clickedFirst == null) {
            clickedFirst = button
        } else {
            clickedSecond = button
            isProcessing = true

            handler.postDelayed({
                checkForMatch()
            }, 1000)
        }
    }

    private fun checkForMatch() {
        if (clickedFirst?.tag == clickedSecond?.tag) {
            // Coincidencia encontrada
            Toast.makeText(this, "¡Coincidencia!", Toast.LENGTH_SHORT).show()
            matchCount++

            if (matchCount == images.size / 2) {
                // Juego completado
                Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show()
                // Aquí podrías mostrar un diálogo o reiniciar automáticamente
            }
        } else {
            // No coincide
            clickedFirst?.setBackgroundResource(R.drawable.mapa) // Vuelve a mostrar la parte trasera
            clickedSecond?.setBackgroundResource(R.drawable.mapa) // Vuelve a mostrar la parte trasera

            errorCount++ // Incrementa el contador de errores

            if (errorCount >= 5) {
                // Se alcanzó el límite de errores
                Toast.makeText(this, "¡Has perdido! 5 errores alcanzados.", Toast.LENGTH_LONG).show()
                // Aquí podrías mostrar un diálogo o reiniciar automáticamente
                initializeGame() // Reinicia el juego
            }
        }
        clickedFirst = null
        clickedSecond = null
        isProcessing = false
    }
}
