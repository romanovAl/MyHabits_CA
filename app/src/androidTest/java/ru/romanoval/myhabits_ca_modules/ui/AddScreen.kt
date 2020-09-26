package ru.romanoval.myhabits_ca_modules.ui

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.edit.KTextInputLayout
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.spinner.KSpinner
import com.agoda.kakao.spinner.KSpinnerItem
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.romanoval.myhabits_ca_modules.R

class AddScreen : Screen<AddScreen>(){

    val addButtonMain = KButton{withId(R.id.fabMainFragment)}

    val addButton = KButton { withId(R.id.addAndEditFab)}

    val nameInput = KTextInputLayout{withId(R.id.habitNameInputLayout)}

    val descriptionInput = KTextInputLayout{ withId(R.id.habitDescriptionInputLayout)}

    val priorityInput = KTextInputLayout{ withId(R.id.habitPriorityInputLayout)}

    val periodInput = KTextInputLayout{withId(R.id.habitPeriodInputLayout)}

    val goodButton = KButton{withId(R.id.radioButtonGood)}

    val badButton = KButton{withId(R.id.radioButtonBad)}

    val countView = KEditText{withId(R.id.habitDoneAddEdit)}

    class Item(parent : Matcher<View>) : KRecyclerItem<Item>(parent){
        val title = KTextView(parent) {withId(R.id.habitNameRecyclerElement)}
        val description = KTextView(parent) {withId(R.id.habitDescriptionRecyclerElement)}
        val period = KTextView(parent) {withId(R.id.habitPeriodRecyclerElement)}
        val type = KTextView(parent) {withId(R.id.habitTypeRecyclerElement)}
        val priority = KTextView(parent) {withId(R.id.habitPriorityRecyclerElement)}
    }

    val recycler = KRecyclerView({
        withId(R.id.recyclerGoodHabits)
    }, itemTypeBuilder = {
        itemType(::Item)
    })
}