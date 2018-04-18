package com.udacity.norbi930523.manutdapp.database.news;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface ArticleColumns {

    @PrimaryKey
    @DataType(DataType.Type.INTEGER)
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    String SUBTITLE = "subtitle";

    @DataType(DataType.Type.TEXT)
    String SUMMARY = "summary";

    @DataType(DataType.Type.TEXT)
    String CONTENT = "content";

    @DataType(DataType.Type.TEXT)
    String DATE = "date";

    @DataType(DataType.Type.TEXT)
    String IMAGE_URL = "image_url";

}
