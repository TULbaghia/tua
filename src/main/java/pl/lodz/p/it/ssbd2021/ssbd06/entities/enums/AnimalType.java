package pl.lodz.p.it.ssbd2021.ssbd06.entities.enums;

/**
 * Typ wyliczeniowy reprezentujący typy zwierząt.
 */
public enum AnimalType{
    DOG,
    CAT,
    RODENT,
    BIRD,
    RABBIT,
    LIZARD,
    TURTLE;

    public static AnimalType valueOf(long value){
        return values()[Math.toIntExact(value)];
    }
}