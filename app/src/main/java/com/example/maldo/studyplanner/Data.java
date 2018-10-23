package com.example.maldo.studyplanner;

import android.provider.BaseColumns;

public class Data {

    // PATHWAYS TABLE
    public static final class pathwaysEntry implements BaseColumns
    {
        public static final String TABLE_PATHWAYS = "pathways";
//        public static final String COLUMN_PATH_ID = "_path_id"; // auto increment number?
        public static final String COLUMN_PATH_NAME = "_path_name"; // "Core", "Software Engineering", "Database Architecture", "Networking", "Multi Media Web Development"
    }

}


