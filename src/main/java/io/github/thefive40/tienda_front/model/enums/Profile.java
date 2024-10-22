package io.github.thefive40.tienda_front.model.enums;

import lombok.Getter;

@Getter
public enum Profile {
    IMAGE_RADIUS ( 20 ), IMAGE_CENTER_X ( 20 ), IMAGE_CENTER_Y ( 20 );
    private int value;
    Profile ( int value ) {
        this.value = value;
    }
}
