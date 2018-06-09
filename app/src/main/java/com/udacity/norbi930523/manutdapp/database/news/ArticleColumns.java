package com.udacity.norbi930523.manutdapp.database.news;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface ArticleColumns {

    @PrimaryKey
    @DataType(DataType.Type.INTEGER)
    String _ID = "_id";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String TITLE = "title";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String AUTHOR = "author";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String SUMMARY = "summary";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String CONTENT = "content";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String DATE = "date";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String IMAGE_URL = "image_url";

}
