package ru.nsu.lusnikov.main.java.timecomp.seacher;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class BookInfo {
    @NonNull String name;
    @NonNull Map<Integer, Map<String , Long>> info = new HashMap<>();
}
