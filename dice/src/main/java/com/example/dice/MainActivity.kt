package com.example.dice

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageViews: Array<ImageView>

    private val drawables = arrayOf(
        R.drawable.die_1, R.drawable.die_2,
        R.drawable.die_3, R.drawable.die_4,
        R.drawable.die_5, R.drawable.die_6
    )

    private val viewModel: DiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViews = arrayOf(
            binding.die1, binding.die2, binding.die3, binding.die4, binding.die5
        )

        binding.rollButton.setOnClickListener {
            viewModel.rollOrRestart()
        }

        viewModel.diceValues.observe(this) { values ->
            for (i in values.indices) {
                imageViews[i].setImageResource(drawables[values[i] - 1])
            }
        }

        viewModel.buttonText.observe(this) { text ->
            binding.rollButton.text = text // ROLL -> RESTART -> ROLL
        }
    }
}