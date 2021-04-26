package pl.lodz.p.it.ssbd2021.ssbd06.security;

public class Role {
    public static final String Admin = "Admin";
    public static final String Manager = "Manager";
    public static final String Client = "Client";

    public static final String AddAccessLevel = "addAccessLevel";
    public static final String DeleteAccessLevel = "deleteAccessLevel";
    public static final String EditOwnAccountDetails = "editOwnAccountDetails";
    public static final String EditOtherAccountDetails = "editOtherAccountDetails";
    public static final String BlockAccount = "blockAccount";
    public static final String UnblockAccount = "unblockAccount";
    public static final String EditOwnPassword = "editOwnPassword";
    public static final String EditOtherPassword = "editOtherPassword";
    public static final String GetAccountAuthInfo = "getAccountAuthInfo";
    public static final String GetAllAccounts = "getAllAccounts";
    public static final String GetOwnAccountInfo = "getOwnAccountInfo";
    public static final String GetOtherAccountInfo = "getOtherAccountInfo";
    public static final String ChangeAccessLevel = "changeAccessLevel";
    public static final String RefreshToken = "refreshToken";

    public static final String AddHotel = "addHotel";
    public static final String DeleteHotel = "deleteHotel";
    public static final String UpdateHotel = "updateHotel";
    public static final String AddManagerToHotel = "addManagerToHotel";
    public static final String DeleteManagerFromHotel = "deleteManagerFromHotel";
    public static final String GenerateReport = "generateReport";

    public static final String AddHotelRating = "addHotelRating";
    public static final String DeleteHotelRating = "deleteHotelRating";
    public static final String UpdateHotelRating = "updateHotelRating";
    public static final String HideHotelRating = "hideHotelRating";

    public static final String BookReservation = "bookReservation";
    public static final String CancelReservation = "cancelReservation";
    public static final String EndReservation = "endReservation";
    public static final String GetAllActiveReservations = "getAllActiveReservations";
    public static final String GetAllArchiveReservations = "getAllArchiveReservations";

    public static final String AddCity = "addCity";
    public static final String DeleteCity = "deleteCity";
    public static final String UpdateCity = "updateCity";
    public static final String GetAllCities = "getAllCities";

    public static final String AddBox = "addBox";
    public static final String DeleteBox = "deleteBox";
    public static final String UpdateBox = "updateBox";
    public static final String GetAllBoxes = "getAllBoxes";
}
