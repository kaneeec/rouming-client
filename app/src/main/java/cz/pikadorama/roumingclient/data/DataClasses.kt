package cz.pikadorama.roumingclient.data

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

data class Topic(val posted: String, val comments: Int, val upvotes: Int, val downvotes: Int, val link: String,
                 val name: String) {

    fun imageDirectLink(): String = link.replace("roumingShow.php?file=", "upload/")

    companion object Factory {
        fun fromRowElement(row: Element, firstColumnIndex: Int = 0): Topic {
            val columns = row.getElementsByTag("td")
            val date = columns[firstColumnIndex].text()
            val comments = columns[firstColumnIndex + 1].getElementsByTag("a").first().text().toInt()
            val upvotes = columns[firstColumnIndex + 2].getElementsByTag("font").first().text().toInt()
            val downvotes = columns[firstColumnIndex + 4].getElementsByTag("font").first().text().toInt()
            val link = columns[firstColumnIndex + 5].getElementsByTag("a").first().attr("href")
            val name = columns[firstColumnIndex + 5].getElementsByTag("a").first().text()
            return Topic(date, comments, upvotes, downvotes, link, name)
        }

        fun fromResponse(response: String): List<Topic> {
            val document = Jsoup.parse(response)
            val tableInfo = getTableWrapperAndIndex(document)
            val table = tableInfo.first.getElementsByTag("table").first()
            return table.getElementsByTag("tr").map { fromRowElement(it, firstColumnIndex = tableInfo.second) }
        }

        private fun getTableWrapperAndIndex(document: Document): Pair<Element, Int> {
            var wrapper = document.getElementsByClass("middle mw700")
            var firstColumnIndex = 1  // page has two different layouts for listing topics, we need to index differently
            if (wrapper.isEmpty()) {
                wrapper = document.getElementsByClass("middle mw900")
                firstColumnIndex = 0
            }
            return Pair(wrapper.first(), firstColumnIndex)
        }
    }

}


