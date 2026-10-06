package com.example.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DiceViewModel : ViewModel() {

    private val diceModel = DiceModel()

    private val _diceValues = MutableLiveData<List<Int>>(List(5) { 1 })
    val diceValues: LiveData<List<Int>> get() = _diceValues

    private val _buttonText = MutableLiveData("ROLL")
    val buttonText: LiveData<String> get() = _buttonText

    private var rollJob: Job? = null

    fun rollOrRestart() {
        rollJob?.cancel()

        rollJob = viewModelScope.launch {
            _buttonText.value = "RESTART"

            coroutineScope {
                // 5 паралельних задач (async) - по одній для кожної кістки
                val deferreds = (0 until 5).map { index ->
                    async {
                        val duration = (1500L..3000L).random()

                        diceModel.rollSingleDie(duration) { newFace ->
                            val currentList = _diceValues.value?.toMutableList() ?: MutableList(5) { 1 }
                            currentList[index] = newFace
                            _diceValues.value = currentList
                            // .value замість .postValue - UI-потік
                        }
                    }
                }

                deferreds.awaitAll()
            }

            _buttonText.value = "ROLL"
        }
    }
}