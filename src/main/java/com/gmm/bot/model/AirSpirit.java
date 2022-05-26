package com.gmm.bot.model;

import com.gmm.bot.enumeration.GemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AirSpirit {
    private GemType gemType;
    private Integer x;
    private Integer y;
    private Integer gem;

    @Override
    public String toString() {
        return "AirSpirit{" +
                "gemType=" + gemType +
                ", x=" + x +
                ", y=" + y +
                ", number=" + gem +
                '}';
    }
}
