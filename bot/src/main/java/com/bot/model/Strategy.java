package com.bot.model;

import com.bot.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Strategy {
    private String name;
    private String key;
    private List<Strategy> nested;
}
