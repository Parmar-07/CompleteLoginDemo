package com.example.loginneostore.util

import com.example.loginneostore.data.network.rss.RSSResponse

fun String.applyHtmlRef(name: String): String {

    if (this.isEmpty())
        return ""



    return """
        <a href = "$this"
          style = "text-decoration: none;
          color:white"> &#9679;
          $name &nbsp; &nbsp;
          </a>
    """
}


fun String.convertToNewsChar(): String {

    if (this.isEmpty())
        return ""



    return this.replace("#39;", "`")
}


fun RSSResponse.newsListToString(): String {

    val linksArray = StringBuffer()
    for (reader in articleList) {
        linksArray.append(
            "${
            reader.link.applyHtmlRef(
                reader.title.convertToNewsChar()
            )} "
        )

    }

    return linksArray.toString()
}


fun String.htmlNewsPreview(textSize: Int): String {

    if (this.isEmpty())
        return ""

    return """
        <html>
        <head>
        <meta
        http-equiv="Content-Type"
        content="text/html;charset=utf-8">
        <meta
        name="viewport"
        content="width=device-width">
        </head>
        <body
        style="display: flex;
        background-color:transparent">
        <center>
        <marquee
        behavior="scroll"
        scrollamount = "4"
        direction="left">
        <font
        face = "Lato-Regular"
        size="$textSize">
        $this
        </marquee>
        </center>
        </body>
        </html>
        """


}