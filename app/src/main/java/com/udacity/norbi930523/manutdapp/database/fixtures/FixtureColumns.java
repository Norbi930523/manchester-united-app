package com.udacity.norbi930523.manutdapp.database.fixtures;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface FixtureColumns {

    @PrimaryKey
    @DataType(DataType.Type.TEXT)
    String _ID = "_id";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String DATE = "date";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String COMPETITION = "competition";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String OPPONENT = "opponent";

    @NotNull
    @DataType(DataType.Type.TEXT)
    String VENUE = "venue";

    @DataType(DataType.Type.TEXT)
    String RESULT = "result";

    @NotNull
    @DataType(DataType.Type.INTEGER)
    String LAST_UPDATE = "last_update";

}
