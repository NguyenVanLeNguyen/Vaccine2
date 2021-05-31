import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.async
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun increaseByThread(){
    val c = AtomicInteger()
    for (i in 1..10000)
        thread(start = true) {
            c.addAndGet(i)
        }
    println("By Thread : ${c.get()}")
}



fun increaseByCoroutines(){
    val deferred = (1..10000).map { n ->
        runBlocking {
            async {
                n
            }
        }

    }
    runBlocking {
        val sum = deferred.sumOf { it.await() }
        println("By Coroutines : ${sum}")
    }
}
fun main(args:Array<String>){
    val threadTimeMeasure = measureTimeMillis {
        increaseByThread()
    }

    val coroutineTimeMeasure = measureTimeMillis {
        increaseByCoroutines()
    }

    println("Compare Thread vs Coroutines : ${threadTimeMeasure} <> ${coroutineTimeMeasure}")

}