package de.jaehrig.akalj.domain;

import static java.util.Objects.requireNonNull;

public enum GarbageType {
    RESTMUELL("Restmüll"),
    BIO("Bioabfall"),
    PAPIER("Papiermüll"),
    WERTSTOFF("Wertstoff");

    private final String niceName;

    GarbageType(String s) {
        niceName = requireNonNull(s);
    }

    @Override
    public String toString() {
        return niceName;
    }
}
