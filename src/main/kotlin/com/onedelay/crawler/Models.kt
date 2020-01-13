package com.onedelay.crawler

data class News(val category: String,
                val rank: Int,
                val name: String,
                val url: String,
                val content: String,
                val img: String)

data class HotIssue(val rank: Int,
                    val name: String,
                    val url: String)

data class WeeklyItem(val headline: String,
                      val contents: String,
                      val url: String)