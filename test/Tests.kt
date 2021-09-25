import net.auoeke.extensions.isArray
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.contracts.ExperimentalContracts

@Testable
class Tests {
    @ExperimentalContracts
    @Test
    fun test() {
        val a: Any = arrayOf("123", "234")

        if (a.isArray<String>()) {
            a.forEach {
                println(it)
            }
        }
    }
}
