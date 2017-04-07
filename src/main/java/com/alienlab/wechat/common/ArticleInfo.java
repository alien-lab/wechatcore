package com.alienlab.wechat.common;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class ArticleInfo extends BaseInfo {
	// 图文消息个数，限制为10条以内
			private int ArticleCount;
			// 多条图文消息信息，默认第一个item为大图
			private List<ArticleObject> Articles;

			public int getArticleCount() {
				return ArticleCount;
			}

			public void setArticleCount(int articleCount) {
				this.ArticleCount = articleCount;
			}

			public List<ArticleObject> getArticles() {
				return Articles;
			}

			public void setArticles(List<ArticleObject> articles) {
				this.Articles = articles;
			}
			
			@Override
			public String getPushInfo(){
//				JSONObject newsinfo=new JSONObject();
//				newsinfo.put("touser", this.getToUserName());
//				newsinfo.put("msgtype","news");
//				newsinfo.put("news",JSONObject.toJSON(this));
//				return newsinfo.toJSONString();
				return "";
			}
}
