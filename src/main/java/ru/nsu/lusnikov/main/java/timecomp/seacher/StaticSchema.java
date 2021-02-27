package ru.nsu.lusnikov.main.java.timecomp.seacher;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class StaticSchema {
    @NonNull int compressCount;
    Map<Integer, RoundInfo> rounds = new HashMap<>();

    public StaticSchema(StaticSchema staticSchema) {
        compressCount = staticSchema.getCompressCount();
        rounds = new HashMap<>();
        rounds.putAll(staticSchema.getRounds());
    }

    public double getDelta() {
        int c =  compressCount;
        double delta = 0;
        for (Map.Entry<Integer, RoundInfo> entry : rounds.entrySet()) {
           delta += entry.getValue().percent/100.0*c;
           c = entry.getValue().percent;
        }
        return delta;
    }

    public double getDeltaWithNewRound(int per) {
        double percent =  per/100.0;
        int c =  compressCount;
        float delta = 0;
        for (Map.Entry<Integer, RoundInfo> entry : rounds.entrySet()) {
            delta += (entry.getValue().percent / 100.0) *compressCount;
            c = entry.getValue().percent;
        }
        return  delta + c*percent;
    }

    @Data
    public static class RoundInfo {
        @NonNull int percent;
        @NonNull int quotas;
    }
}
