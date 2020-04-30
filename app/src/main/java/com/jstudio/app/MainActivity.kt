package com.jstudio.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jstudio.jkit.*
import com.jstudio.jkit.adapter.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    val adapter = object : ListAdapter<String>(
        this, arrayListOf(
            "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue",
            "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy",
            "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John",
            "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith",
            "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith", "Sam", "Sue",
            "Billy", "John", "Smith", "Sam", "Sue", "Billy", "John", "Smith"
        ), true
    ) {
        override fun setViewLayout(type: Int): Int = R.layout.item_rv_name
        override fun fillContent(holder: Holder, position: Int, entity: String) {
            holder.setTextByString(R.id.text, entity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text.text = System.currentTimeMillis().toDate("yyyy-MM-dd HH:mm")
        text.singleTap {
            if (text.rotation == 0f) {
                text.fadeIn()
                text.rotate(180f)
                return@singleTap
            }
            text.rotate(0f)
            text.fadeOut()
            doMainNine()
        }
        view.visibility = View.GONE
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) view.expand()
            else view.collapse()
        }


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    fun doMain() = runBlocking {
        println("A-" + Thread.currentThread().name)
        val job = GlobalScope.launch(Dispatchers.Main) {
            delay(2000L)
            // 启动一个新协程并保持对这个作业的引用
            println("B-" + Thread.currentThread().name)
        }
        println("C-" + Thread.currentThread().name)
//        job.join() // 等待直到子协程执行结束
    }

    fun doMainTwo() {
        GlobalScope.launch {
            println("A-" + Thread.currentThread().name)
            withContext(Dispatchers.Main) {
                delay(2000L)
                println("C-" + Thread.currentThread().name)
            }
            println("B-" + Thread.currentThread().name)
        }
        val job = CoroutineScope(Dispatchers.Main)
    }

    fun doMainThree() = runBlocking {
        println("A-" + Thread.currentThread().name)
        launch {
            delay(2000L)
            println("B-" + Thread.currentThread().name)
        }

        println("C-" + Thread.currentThread().name)
        coroutineScope {
            println("D-" + Thread.currentThread().name)
            launch {
                delay(3000L)
                println("E-" + Thread.currentThread().name)
            }
            delay(1000L)
            println("F-" + Thread.currentThread().name)
        }
        println("G-" + Thread.currentThread().name)
    }

    fun doMainFour() = runBlocking {
        val job = GlobalScope.launch {
            println("A-" + Thread.currentThread().name)
            repeat(1000) {
                delay(50L)
                println("B-" + Thread.currentThread().name)
            }
        }
        delay(1000L)
        job.cancelAndJoin()
        println("C-" + Thread.currentThread().name)
    }

    fun doMainFive() = runBlocking {
        val job = launch(Dispatchers.Default) {
            while (isActive) {
                println("looping")
                delay(5000L)
            }
        }
        delay(1300L) // 等待一段时间
        println("is job active: ${job.isActive}")
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("is job active: ${job.isActive}")
        println("main: Now I can quit.")
    }

    fun doMainSix() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } catch (e: Exception) {
                println("job: I'm in exception")
            } finally {
                println("job: I'm running finally")
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")
    }

    fun doMainSeven() = runBlocking {
        suspend fun doSomethingUsefulOne(): Int {
            delay(1500L); return 13
        }

        suspend fun doSomethingUsefulTwo(): Int {
            delay(1000L); return 29
        }

        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            println("async one")
            val two = async { doSomethingUsefulTwo() }
            println("async two")
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    fun doMainEight() {
        suspend fun doSomethingUsefulOne(): Int {
            delay(1500L); return 13
        }

        suspend fun doSomethingUsefulTwo(): Int {
            delay(1000L); return 29
        }

        fun somethingUsefulOneAsync() = GlobalScope.async { doSomethingUsefulOne() } //返回值类型是 Deferred<Int>
        fun somethingUsefulTwoAsync() = GlobalScope.async { doSomethingUsefulTwo() } //返回值类型是 Deferred<Int>
        val oneDeferred = somethingUsefulOneAsync()
        val twoDeferred = somethingUsefulTwoAsync()
        println("waiting for answer")
        GlobalScope.launch { println("The answer is ${oneDeferred.await() + twoDeferred.await()}") }
    }

    fun doMainNine() = runBlocking {
        launch(Dispatchers.Unconfined) {
            // 非受限的——将和主线程一起工作
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch {
            // 父协程的上下文，主 runBlocking 协程
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
    }
}
