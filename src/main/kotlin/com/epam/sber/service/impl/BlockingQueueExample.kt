package com.epam.sber.service.impl

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


class BlockingQueueExample {
    private val drop: BlockingQueue<String>
    private val DONE = "done"
    private val messages = arrayOf(
        "Мама пошла готовить обед",
        "Мама позвала к столу",
        "Дети кушают молочную кашу",
        "А что ест папа?"
    )

    internal inner class Producer : Runnable {
        override fun run() {
            try {
                var cnt = 0
                for (i in messages.indices) {
                    drop.put(messages[i])
                    if (++cnt < 3) Thread.sleep(2000)
                }
                drop.put(DONE)
            } catch (e: InterruptedException) {
                System.err.println(e.message)
            }
        }
    }

    internal inner class Consumer : Runnable {
        override fun run() {
            try {
                var msg: String? = null
                while (drop.take().also { msg = it } != DONE) println(msg)
            } catch (e: InterruptedException) {
                System.err.println(e.message)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            BlockingQueueExample()
        }
    }

    init {
        drop = ArrayBlockingQueue(1, true)
        Thread(Producer()).start()
        Thread(Consumer()).start()
    }
}