package com.onedelay.crawler

import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.*

@RestController
class CrawlingController {

    @GetMapping("/daum")
    @ResponseBody
    fun getDaumNews(): List<DaumNews> {
        val list = ArrayList<DaumNews>()

        val doc = Jsoup.connect("https://news.daum.net/ranking/popular/").get()

        val elements = doc.select("div.rank_news")[0].selectFirst("ul.list_news2").children()

        for ((i, element) in elements.withIndex()) {
            val link = element.select("a").first()
            val url = link.attr("href")
            val title = link.select("img").attr("alt")
            val thumbContent = element.select("span.link_txt").text()
            val thumbImageUrl = element.select("img").attr("src")
            val company = element.select("span.info_news").text()

            list.add(
                    DaumNews(
                            rank = i + 1,
                            url = url,
                            title = title,
                            thumbContent = thumbContent,
                            thumbImageUrl = thumbImageUrl,
                            company = company
                    )
            )
        }

        return list
    }

    @GetMapping("/naver")
    @ResponseBody
    fun getNaverNews(): List<NaverNews> {
        val list = ArrayList<NaverNews>()

        val doc = Jsoup.connect("https://news.naver.com/main/home.nhn").get()

        val sections = doc.selectFirst("div.main_content_inner._content_inner")
                .select("div.main_component.droppable")

        for (section in sections) {
            val category = section.select("div.com_header").select("a").text().split(' ')

            val categoryName = if (category.isNotEmpty()) category[0] else ""

            for (news in section.select("li")) {
                val link = news.select("a")

                val href = link.attr("href")
                val url = if (href.contains("https://news.naver.com/")) {
                    href
                } else {
                    "https://news.naver.com$href"
                }

                val title = link.text()

                list.add(NaverNews(categoryName, url, title))
            }
        }

        return list
    }

    @GetMapping("/naver_issue")
    @ResponseBody
    fun getNaverHotIssue(): List<NaverHotIssue> {
        val list = ArrayList<NaverHotIssue>()

        val doc = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?where=main/").get()
        val elements = doc.select("div.ranking_box")[0].selectFirst("ul.ranking_list").children()

        for ((i, element) in elements.withIndex()) {
            val name = element.selectFirst("span.item_title").text()
            val url = "https://search.naver.com/search.naver?where=nexearch&query=${name}"

            list.add(NaverHotIssue(i + 1, name, url))
        }

        return list
    }

    @GetMapping("/android_weekly")
    @ResponseBody
    fun getAndroidWeekly(): List<AndroidWeekly> {
        val list = mutableListOf<AndroidWeekly>()

        val doc = Jsoup.connect("https://androidweekly.net/").get()

        val elements = doc.selectFirst("div.sections").children()

        for (element in elements) {
            if (element.selectFirst("h2")?.text() == "Place a sponsored post") break

            val link = element.select("a")

            if (link.text().isNotEmpty()) {
                list.add(
                        AndroidWeekly(
                                headline = link.text(),
                                contents = element.select("p").text(),
                                url = link.attr("href")
                        )
                )
            }
        }

        return list
    }
}