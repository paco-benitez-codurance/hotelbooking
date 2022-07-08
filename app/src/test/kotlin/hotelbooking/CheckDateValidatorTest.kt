package hotelbooking

import java.time.LocalDate
import kotlin.test.*


class CheckDateValidatorTest {
    companion object {
        private val ONE_DATE = LocalDate.of(2022, 1, 1)
    }

    private val dateValidator = CheckDateValidator()

    @Test
    fun checkoutDate_shouldFail_withSameCheckinCheckoutDate() {
        assertFalse {
            dateValidator.isValid(
                ONE_DATE,
                ONE_DATE
            )
        }
    }

    @Test
    fun checkoutDate_shouldFail_withCheckoutInThePastOfCheckin() {
        assertFalse {
            dateValidator.isValid(
                ONE_DATE,
                ONE_DATE.minusDays(1)
            )
        }
    }

    @Test
    fun checkoutDate_shouldBe_atLeastOneDayAfterCheckin() {
        assertTrue {
            dateValidator.isValid(
                ONE_DATE,
                ONE_DATE.plusDays(1)
            )
        }
    }
}
