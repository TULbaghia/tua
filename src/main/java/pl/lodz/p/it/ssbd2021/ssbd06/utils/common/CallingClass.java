package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

/**
 * Interfejs reprezentujący klasę rozpoczynającą transakcję aplikacyjną
 */
public interface CallingClass {
    /**
     * Zwraca identyfikator transakcji aplikacyjnej
     *
     * @return identufikator transakcji aplikacyjnej
     */
    String getTransactionId();

    /**
     * Zwraca informację czy ostatnia transakcja aplikacyjna zostałą zatwierdzona czy odwołana
     *
     * @return true - jeżeli odwołana, false - zatwierdzona
     */
    boolean isLastTransactionRollback();
}
