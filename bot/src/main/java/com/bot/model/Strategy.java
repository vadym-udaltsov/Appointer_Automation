package com.bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Strategy {
    private String name;
    private String key;
    private List<Strategy> nested;
}
