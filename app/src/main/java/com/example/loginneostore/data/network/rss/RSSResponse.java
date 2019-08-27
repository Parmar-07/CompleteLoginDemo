package com.example.loginneostore.data.network.rss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict = false)
public class RSSResponse {

    @Element(name = "title")
    @Path("channel")
    private String channelTitle;

    @ElementList(name = "item", inline = true)
    @Path("channel")
    private List<RSSItemModel> articleList;

    /**
     * @return the channelTitle
     */
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * @param channelTitle the channelTitle to set
     */
    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    /**
     * @return the articleList
     */
    public List<RSSItemModel> getArticleList() {
        return articleList;
    }

    /**
     * @param articleList the articleList to set
     */
    public void setArticleList(List<RSSItemModel> articleList) {
        this.articleList = articleList;
    }

}
