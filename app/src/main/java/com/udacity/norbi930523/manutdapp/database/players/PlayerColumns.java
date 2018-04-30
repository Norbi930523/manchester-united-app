package com.udacity.norbi930523.manutdapp.database.players;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface PlayerColumns {

    @PrimaryKey
    @DataType(DataType.Type.INTEGER)
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    String SQUAD_NUMBER = "squad_number";

    @DataType(DataType.Type.TEXT)
    String FIRST_NAME = "first_name";

    @DataType(DataType.Type.TEXT)
    String LAST_NAME = "last_name";

    @DataType(DataType.Type.TEXT)
    String IMAGE_URL = "image_url";

    @DataType(DataType.Type.TEXT)
    String BIRTHDATE = "birthdate";

    @DataType(DataType.Type.TEXT)
    String BIRTHPLACE = "birthplace";

    @DataType(DataType.Type.INTEGER)
    String POSITION = "position";

    @DataType(DataType.Type.TEXT)
    String JOINED = "joined";

    @DataType(DataType.Type.TEXT)
    String JOINED_FROM = "joined_from";

    @DataType(DataType.Type.TEXT)
    String INTERNATIONAL = "international";

    @DataType(DataType.Type.INTEGER)
    String APPEARANCES = "appearances";

    @DataType(DataType.Type.INTEGER)
    String GOALS = "goals";

    @DataType(DataType.Type.INTEGER)
    String LAST_UPDATE = "last_update";

}
