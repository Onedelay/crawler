package com.onedelay.crawler

data class DaumNews(
        val rank: Int,
        val url: String,
        val title: String,
        val thumbContent: String,
        val thumbImageUrl: String,
        val company: String
)

data class NaverNews(
        val category: String,
        val url: String,
        val title: String
)

data class HotIssue(val rank: Int,
                    val name: String,
                    val url: String)

data class WeeklyItem(val headline: String,
                      val contents: String,
                      val url: String)