
import net.auoeke.extensions.*
import org.junit.jupiter.api.*
import org.junit.platform.commons.annotation.*
import kotlin.io.println

@Testable
class Tests {
    @Test
    fun test() {
        println(type.source!!.resolve("META-INF/MANIFEST.MF"))
    }
}
