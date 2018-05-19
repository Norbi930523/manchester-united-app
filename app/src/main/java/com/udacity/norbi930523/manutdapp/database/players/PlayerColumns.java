package com.udacity.norbi930523.manutdapp.database.players;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface PlayerColumns {

    @PrimaryKey
    @DataType(DataType.Type.INTEGER)
    String _ID = "_id";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String SQUAD_NUMBER = "squad_number";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String FIRST_NAME = "first_name";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String LAST_NAME = "last_name";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String IMAGE_URL = "image_url";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String BIRTHDATE = "birthdate";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String BIRTHPLACE = "birthplace";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String POSITION = "position";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String JOINED = "joined";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String JOINED_FROM = "joined_from";

    @DataType(DataType.Type.TEXT)
    String INTERNATIONAL = "international";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String APPEARANCES = "appearances";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String GOALS = "goals";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String BIO = "bio";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String LAST_UPDATE = "last_update";

}
