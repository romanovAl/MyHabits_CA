package ru.romanoval.myhabits_ca_modules.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.common.utilities.getResourceString
import com.agoda.kakao.edit.TextInputLayoutAssertions
import com.google.android.material.textfield.TextInputLayout
import org.junit.Rule
import org.junit.Test
import ru.romanoval.myhabits_ca_modules.R

@LargeTest
class AddUITests {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    private val screen = AddScreen()

    @Test
    fun add_screen_empty_test() {
        screen {

            //-----------------------------------------------

            addButtonMain {
                click()
            }
            addButton {
                click()
            }
            nameInput {
                hasError(getResourceString(R.string.Field_must_not_be_empty))
            }
            descriptionInput {
                hasError(getResourceString(R.string.Field_must_not_be_empty))
            }
            periodInput {
                hasError(getResourceString(R.string.Field_must_not_be_empty))
            }

            //-----------------------------------------------

            nameInput.edit.typeText("name")
            Espresso.closeSoftKeyboard()
            addButton {
                click()
            }
            nameInput {
                checkErrorIsNull()
            }
            descriptionInput {
                hasError(getResourceString(R.string.Field_must_not_be_empty))
            }
            periodInput {
                hasError(getResourceString(R.string.Field_must_not_be_empty))
            }

            //-----------------------------------------------

            descriptionInput.edit.typeText("description")
            Espresso.closeSoftKeyboard()
            addButton {
                click()
            }
            nameInput {
                checkErrorIsNull()
            }
            descriptionInput {
                checkErrorIsNull()
            }
            periodInput {
                hasError(getResourceString(R.string.Field_must_not_be_empty))
            }

            //-----------------------------------------------

            periodInput {
                click()
                Espresso.onView(withText(getResourceString(R.string.period_aday)))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click())
            }
            Espresso.closeSoftKeyboard()

            priorityInput {
                click()
                Espresso.onView(withText(getResourceString(R.string.high_priorirty)))
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(ViewActions.click())
            }
            Espresso.closeSoftKeyboard()

            countView.typeText("23")
            Espresso.closeSoftKeyboard()

            addButton {
                click()
            }

            recycler {
                childAt<AddScreen.Item>(0) {
                    title.hasText("name")
                    description.hasText("description")
                    period.hasText("23 ${getResourceString(R.string.times)} ${getResourceString(R.string.period_aday)}")
                    priority.hasText("${getResourceString(R.string.high_priorirty)} ${getResourceString(R.string.priority)}")
                    type.hasText(getResourceString(R.string.good_habit))
                }
            }

        }
    }

    private fun TextInputLayoutAssertions.checkErrorIsNull() {
        view.check(ViewAssertion { view, notFoundException ->
            if (view is TextInputLayout) {
                if (view.error != null) {
                    throw AssertionError(
                        "Expected error is null," +
                                " but actual is ${view.error}"
                    )
                }
            } else {
                notFoundException?.let { throw AssertionError(it) }
            }
        })
    }


}