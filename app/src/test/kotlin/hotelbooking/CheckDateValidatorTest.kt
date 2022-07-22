package hotelbooking

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate



class CheckDateValidatorTest : StringSpec({

    val ONE_DATE = LocalDate.of(2022, 1, 1)
    val dateValidator = CheckDateValidator()

    "Should fail when checkoutDate and checkin are the same" {
        dateValidator.isValid(
            ONE_DATE,
            ONE_DATE
        ) shouldBe false
    }

    "Should fail when checkout date is in the past of checkin" {
        dateValidator.isValid(
            ONE_DATE,
            ONE_DATE.minusDays(1)
        ) shouldBe false
    }

    "Checkout should be at least one day after check in" {
        dateValidator.isValid(
            ONE_DATE,
            ONE_DATE.plusDays(1)
        ) shouldBe true
    }
})
