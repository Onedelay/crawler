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

    // fixme
    @GetMapping("/naver")
    @ResponseBody
    fun getNaverNews(@RequestParam(value = "category") category: String?): List<News> {
        var count = 0
        val list = ArrayList<News>()

        val doc = Jsoup.connect("https://news.naver.com/").get()
        val elements = doc.select("ul.section_list_ranking")
        for (element in elements) {
            val cat = element.parents()[0].select("h5").text()
            if (category == cat) {
                for ((j, child) in element.children().withIndex()) {
                    val title = child.select("a").attr("title")
                    val url = "https://news.naver.com/" + child.select("a").attr("href")
                    val content by lazy {
                        if (count++ < 4) {
                            // 해당 url 클릭해서 내용 일부 발췌
                            // 3개까지만 실행하도록 분기 (너무 오래 걸림)
                            Jsoup.connect(url).get().select("meta[property=og:description]").attr("content")
                        } else {
                            ""
                        }
                    }
                    list.add(News(cat, j + 1, title, url, content, "https://t1.daumcdn.net/cfile/tistory/2212C6335790D35004"))
                }
            }
        }

        return list
    }

    @GetMapping("/naver_issue")
    @ResponseBody
    fun getNaverHotIssue(): List<HotIssue> {
        val list = ArrayList<HotIssue>()

        val doc = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?where=main/").get()
        val elements = doc.select("div.ranking_box")[0].selectFirst("ul.ranking_list").children()

        for ((i, element) in elements.withIndex()) {
            val name = element.selectFirst("span.item_title").text()
            val url = "https://search.naver.com/search.naver?where=nexearch&query=${name}"

            list.add(HotIssue(i + 1, name, url))
        }

        return list
    }

    // fixme
    @GetMapping("/android_weekly")
    @ResponseBody
    fun getAndroidWeekly(@RequestParam("count", required = false) count: Int?): List<WeeklyItem> {
        val list = mutableListOf<WeeklyItem>()

        val doc = Jsoup.connect("https://androidweekly.net/").get()

        val elements = doc.select("div.sections")[0].children()

        var remainCount = count ?: 13

        for (element in elements) {
            if (remainCount > 0) {
                try {
                    list.add(
                            WeeklyItem(
                                    headline = element.selectFirst("a.article-headline").text(),
                                    contents = element.selectFirst("p").text(),
                                    url = element.selectFirst("a.article-headline").attr("href")
                            )
                    )

                    remainCount--
                } catch (exception: NullPointerException) {
                    continue
                }
            } else {
                break
            }
        }

        return list
    }
}