package com.E3N.pix.service;

import java.util.function.Function;

public sealed interface Either<L, R> {
    // how to use https://claude.ai/share/cb560223-39a9-4d3b-bf96-c3b3b607bee6
    record Left<L, R>(L value) implements Either<L, R> {
    }

    record Right<L, R>(R value) implements Either<L, R> {
    }

    static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    default boolean isLeft() {
        return this instanceof Left<L, R>;
    }

    default boolean isRight() {
        return this instanceof Right<L, R>;
    }

    default <T> T fold(Function<L, T> onLeft, Function<R, T> onRight) {
        return switch (this) {
            case Left<L, R> left -> onLeft.apply(left.value());
            case Right<L, R> right -> onRight.apply(right.value());
        };
    }
}
