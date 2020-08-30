package ru.romanoval.myhabits_ca_modules.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.romanoval.domain.model.Habit
import ru.romanoval.domain.model.PostDone
import ru.romanoval.domain.model.response.ErrorModel
import ru.romanoval.domain.useCases.api.DeleteHabitsApiUseCase
import ru.romanoval.domain.useCases.api.GetHabitsApiUseCase
import ru.romanoval.domain.useCases.api.PostHabitsDoneApiUseCase
import ru.romanoval.domain.useCases.api.PutHabitsApiUseCase
import ru.romanoval.domain.useCases.db.DeleteAllHabitsDBUseCase
import ru.romanoval.domain.useCases.db.GetHabitsDBUseCase
import ru.romanoval.domain.useCases.db.InsertHabitsDBUseCase
import ru.romanoval.domain.useCases.sort.SetSortedHabitsUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getHabitsUseCase: GetHabitsDBUseCase,
    private val getHabitsFromApiUseCase: GetHabitsApiUseCase,
    private val insertHabitsUseCase: InsertHabitsDBUseCase,
    private val deleteAllHabitsInDbUseCase: DeleteAllHabitsDBUseCase,
    private val setSortHabitsUseCase: SetSortedHabitsUseCase,
    private val deleteHabitsInApiUseCase: DeleteHabitsApiUseCase,
    private val putHabitsInApiUseCase: PutHabitsApiUseCase,
    private val postHabitsDoneUseCase: PostHabitsDoneApiUseCase
) : ViewModel() {

    val error: MutableLiveData<ErrorModel> by lazy { MutableLiveData<ErrorModel>() }
    val isLoading: MutableLiveData<Boolean> by lazy{ MutableLiveData<Boolean>() }
    val habits : LiveData<List<Habit>> = getHabitsUseCase.getHabits().asLiveData()

    val isSorted = false

    fun setSortedHabits(newHabits: List<Habit>){
        setSortHabitsUseCase.setSortedHabits(newHabits)
    }

    fun uploadToApi(){

        isLoading.value = true

        val postDones : MutableList<PostDone> = mutableListOf()

        getHabitsFromApiUseCase.execute {
            onComplete {habitsFromApi ->
                deleteHabitsInApiUseCase.habits = habitsFromApi
                deleteHabitsInApiUseCase.execute {
                    onComplete {
                        putHabitsInApiUseCase.habits = habits.value ?: emptyList()
                        putHabitsInApiUseCase.execute {
                            onComplete {
                                getHabitsFromApiUseCase.execute {
                                    onComplete {
                                        postHabitsDoneUseCase.habitsFromApi = it
                                        postHabitsDoneUseCase.habitsFromDb = habits.value ?: emptyList()
                                        postHabitsDoneUseCase.execute {
                                            onComplete {
                                                isLoading.value = false
                                            }
                                            onError { err ->
                                                error.value = err
                                                isLoading.value = false
                                            }
                                        }
                                    }
                                }
                            }
                            onError {
                                error.value = it
                                isLoading.value = false
                            }
                        }
                    }
                    onError {
                        error.value = it
                        isLoading.value = false
                    }
                }
            }
            onError {
                error.value = it
                isLoading.value = false
            }
        }

        //TODO сделать сообщение об ошибке null
    }

    fun downloadFromApi(){

        isLoading.value = true
        deleteAllHabitsInDbUseCase.execute {
            onComplete {
                getHabitsFromApiUseCase.execute {
                    onComplete { habitsFromApi ->
                        insertHabitsUseCase.habits = habitsFromApi
                        insertHabitsUseCase.execute {
                            onComplete {
                                isLoading.value = false
                            }
                            onError {
                                isLoading.value = false
                                error.value = it
                            }
                        }
                    }
                    onError {
                        isLoading.value = false
                        error.value = it
                    }
                }
            }
            onError {
                isLoading.value = false
                error.value = it
            }
        }

    }


}