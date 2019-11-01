package com.example.firstspringboot.commons;

public class Constants {
    public static class Message{
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILURE = "FAILURE";
    }
    public static class InformationType{

        /**
         * 新闻类型
         */
        public static final String NEWS_TYPE = "information.type.news";
        /**
         * 新增 新闻类型
         */
        public static final Integer ADD_NEWS_TYPE = 1;
        /**
         * 公告类型
         */
        public static final String ANNOUNCEMENT_TYPE = "information.type.announcement";
        /**
         * 新增 公告类型
         */
        public static final Integer ADD_ANNOUNCEMENT_TYPE = 2;
        /**
         * 全部类型
         */
        public static final String ALL_INFORMATION = "information.all";
    }
}
