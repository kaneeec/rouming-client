package cz.pikadorama.roumingclient.data

import cz.pikadorama.roumingclient.orElse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

data class Topic(val posted: String, val comments: Int, val upvotes: Int, val downvotes: Int, val link: String,
                 val title: String) {

    fun imageDirectLink(): String = link.replace("roumingShow.php?file=", "upload/")

    fun toTopicDao(): TopicDao = TopicDao(posted, comments, upvotes, downvotes, link, title)

    companion object Factory {
        fun fromRowElement(row: Element, firstColumnIndex: Int, urlPrefix: String): Topic {
            val columns = row.getElementsByTag("td")
            val date = columns[firstColumnIndex].text()
            val comments = columns[firstColumnIndex + 1].getElementsByTag("a").first().text().toInt()
            val upvotes = columns[firstColumnIndex + 2].getElementsByTag("font").first().text().toInt()
            val downvotes = columns[firstColumnIndex + 4].getElementsByTag("font").first().text().toInt()
            val link = urlPrefix + columns[firstColumnIndex + 5].getElementsByTag("a").first().attr("href")
            val name = columns[firstColumnIndex + 5].getElementsByTag("a").first().text()
            return Topic(date, comments, upvotes, downvotes, link, name)
        }

        fun fromResponse(response: String): List<Topic> {
            val document = Jsoup.parse(response)
            val tableInfo = getTableWrapperAndIndex(document)
            val table = tableInfo.first.getElementsByTag("table").first()
            return table.getElementsByTag("tr").map { fromRowElement(it, tableInfo.second, tableInfo.third) }
        }

        private fun getTableWrapperAndIndex(document: Document): Triple<Element, Int, String> {
            var firstColumnIndex = 1
            var urlPrefix = ""
            val wrapper = document.getElementsByClass("middle mw700")
                    .orElse {
                        firstColumnIndex = 0
                        urlPrefix = "http://www.rouming.cz/"
                        document.getElementsByClass("middle mw900")
                    }
                    .orElse { throw IllegalStateException() }
            return Triple(wrapper.first(), firstColumnIndex, urlPrefix)
        }
    }

}

