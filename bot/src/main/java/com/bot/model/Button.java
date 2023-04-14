package com.bot.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Serhii_Udaltsov on 6/7/2021
 */
@Data
@Builder
public class Button {
    private String value;
    private String callback;

}
