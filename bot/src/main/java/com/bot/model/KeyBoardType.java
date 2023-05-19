package com.bot.model;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public enum KeyBoardType {
    WITHOUT_KEYBOARD(0),
    TWO_ROW(2),
    THREE_ROW(3),
    FOUR_ROW(4),
    VERTICAL(1),
    HORIZONTAL(999);
    private int elementInRow;

    KeyBoardType(int elementInRow) {
        this.elementInRow = elementInRow;
    }

    public int getElementsInRow() {
        return elementInRow;
    }

}
