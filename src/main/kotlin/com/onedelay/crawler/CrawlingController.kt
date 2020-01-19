package com.onedelay.crawler

import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.*

@RestController
class CrawlingController {

    @GetMapping("/daum")
    @ResponseBody
    fun getDaumNews(@RequestParam(value = "category") category: String): List<News> {
        var count = 0 // 가장 많이본 뉴스 3개만 가져오도록 카운트
        val list = ArrayList<News>()

        val doc = Jsoup.connect("https://media.daum.net/society/").get()
        val element = doc.select("div.aside_g.aside_ranking").select("ul.tab_aside.tab_media")[0]
        for (item in element.children()) {
            val cat = item.select("a.link_tab").text()
            if (category == cat) {
                val ch = item.select("ol.list_ranking")
                for ((i, child) in ch.withIndex()) {
                    for ((j, c) in child.children().withIndex()) {
                        val title = c.select("strong.tit_g").select("a").text().trim()
                        val url = c.select("strong.tit_g").select("a").attr("href")
                        val content by lazy {
                            if (count++ < 4) {
                                // 해당 url 에서 내용 일부 발췌
                                // 3개까지만 실행하도록 분기 (너무 오래 걸림)
                                Jsoup.connect(url).get().select("meta[property=og:description]").attr("content")
                            } else {
                                ""
                            }
                        }
                        list.add(News(cat, (i * 10) + (j + 1), title, url, content, "https://t1.daumcdn.net/daumtop_chanel/op/20170315064553027.png"))
                    }
                }
            }
        }

        return list
    }

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

    @GetMapping("/daum_issue")
    @ResponseBody
    fun getDaumHotIsuue(): List<HotIssue> {
        val list = ArrayList<HotIssue>()

        val doc = Jsoup.connect("https://www.daum.net/").get()
        val elements = doc.select("div.hotissue_mini").select("ol")[0].children()
        for ((i, element) in elements.withIndex()) {
            val name = element.selectFirst("a").text()
            val url = element.selectFirst("a").attr("href")
            list.add(HotIssue(i + 1, name, url))
        }
        return list
    }

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