package com.github.akagawatsurunaki.arisucleaner.util;

import java.util.ArrayList;

public class EvictingList<E> extends ArrayList<E> {
    private final int maxSize;

    public EvictingList(int maxSize) {
        super(maxSize);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (size() >= maxSize) {
            remove(0);
        }
        return super.add(e);
    }
}