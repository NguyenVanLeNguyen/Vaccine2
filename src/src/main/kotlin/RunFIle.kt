import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis
fun main() {
    fun increaseByThread(){
        val c = AtomicInteger()
        for (i in 1..1_000_000)
            thread(start = true) {
                c.addAndGet(i)
            }
        println("By Thread : ${c.get()}")
    }



    fun increaseByCoroutines(){
        val c = AtomicInteger()
        for (i in 1..1_000_000)
            launch {
                c.addAndGet(i)
            }
        println("By Coroutines : ${c.get()}")
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

}