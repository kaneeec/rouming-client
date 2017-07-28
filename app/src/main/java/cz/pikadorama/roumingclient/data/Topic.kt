package cz.pikadorama.roumingclient.data

import android.provider.BaseColumns
import cz.pikadorama.simpleorm.DbDataType
import cz.pikadorama.simpleorm.annotation.DbColumn
import cz.pikadorama.simpleorm.annotation.DbTable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

@DbTable(name = "Topic", mappingClass = TopicQueryHelper::class)
data class Topic(
        @DbColumn(name = BaseColumns._ID, type = DbDataType.INTEGER,
                  properties = "primary key autoincrement") var id: Int?,
        @DbColumn(name = COL_POSTED, type = DbDataType.TEXT) var posted: String,
        @DbColumn(name = COL_COMMENTS, type = DbDataType.INTEGER) var comments: Int,
        @DbColumn(name = COL_UPVOTES, type = DbDataType.INTEGER) var upvotes: Int,
        @DbColumn(name = COL_DOWNVOTES, type = DbDataType.INTEGER) var downvotes: Int,
        @DbColumn(name = COL_LINK, type = DbDataType.TEXT) var link: String,
        @DbColumn(name = COL_TITLE, type = DbDataType.TEXT) var title: String,
        @DbColumn(name = COL_TYPE, type = DbDataType.TEXT) var type: Type,
        @DbColumn(name = COL_FAVED, type = DbDataType.TEXT) var faved: Boolean) {

    enum class Type(val firstColumnIndex: Int, val urlPrefix: String) {
        LATEST(1, ""), TOP(0, "http://www.rouming.cz/")
    }

    constructor(posted: String, comments: Int, upvotes: Int, downvotes: Int, link: String, title: String, type: Type,
                faved: Boolean = false) : this(null, posted, comments, upvotes, downvotes, link, title, type, faved)

    fun imageDirectLinks(): List<String> = listOf(imageDirectLink(link, "upload"),
                                                  imageDirectLink(link, "archived"),
                                                  imageDirectLink(link, "signed"))

    private fun imageDirectLink(link: String, opt: String): String = link.replace("roumingShow.php?file=", "$opt/")

    companion object {
        const val COL_POSTED = "posted"
        const val COL_COMMENTS = "comments"
        const val COL_UPVOTES = "upvotes"
        const val COL_DOWNVOTES = "downvotes"
        const val COL_LINK = "link"
        const val COL_TITLE = "title"
        const val COL_TYPE = "type"
        const val COL_FAVED = "faved"

        fun fromRowElement(row: Element, type: Type): Topic {
            val columns = row.getElementsByTag("td")
            val date = columns[type.firstColumnIndex].text()
            val comments = columns[type.firstColumnIndex + 1].getElementsByTag("a").first().text().toInt()
            val upvotes = columns[type.firstColumnIndex + 2].getElementsByTag("font").first().text().toInt()
            val downvotes = columns[type.firstColumnIndex + 4].getElementsByTag("font").first().text().toInt()
            val link = type.urlPrefix + columns[type.firstColumnIndex + 5].getElementsByTag("a").first().attr("href")
            val name = columns[type.firstColumnIndex + 5].getElementsByTag("a").first().text()
            return Topic(date, comments, upvotes, downvotes, link, name, type)
        }

        fun fromResponse(response: String): List<Topic> {
            val document = Jsoup.parse(response)
            val tableInfo = getTableWrapperAndIndex(document)
            val table = tableInfo.first.getElementsByTag("table").first()
            return table.getElementsByTag("tr").map {
                fromRowElement(it, tableInfo.second)
            }
        }

        private fun getTableWrapperAndIndex(document: Document): Pair<Element, Type> {
            val latestWrapper = document.getElementsByClass("middle mw700")
            if (!latestWrapper.isEmpty()) {
                return Pair(latestWrapper.first(), Type.LATEST)
            }

            val topWrapper = document.getElementsByClass("middle mw900")
            if (!topWrapper.isEmpty()) {
                return Pair(topWrapper.first(), Type.TOP)
            }

            throw IllegalStateException()
        }
    }
}
