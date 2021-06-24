package com.example.booklisting;

public class book {
    String title;
    String authors;
    String imgURL;
    String url;
    book(String a, String b, String c, String d)
    {
        title=a;
        authors=b;
        imgURL=c;
        url=d;
    }

    String getTitle()
    {
        return title;
    }

    String getAuthors()
    {
        return authors;
    }

    String getImgURL()
    {
        return imgURL;
    }
    String getUrl()
    {
        return url;
    }

}
