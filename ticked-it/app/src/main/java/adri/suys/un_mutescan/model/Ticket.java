package adri.suys.un_mutescan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A ticket is a ticket generated, sold, bought for an event
 */
public class Ticket implements Serializable {

    private final String buyer;
    private final String ticket_type;
    private String seat_type;
    private final String barcode;
    private final String error;
    private String is_validated;
    private String cart_number;

    /**
     *
     * @param barcodeText the barcode value written on the ticket
     * @param name the name of the buyer
     * @param ticketType the type of the ticket (vip, child, adult, etc.)
     * @param seatType the seat number (nothing if it is a stand up ticket)
     * @param errorMessage the error message (nothing if the ticket is valid) if the ticket is unvalid
     */
    public Ticket(String name, String ticketType, String seatType, String barcodeText, String errorMessage, String isScanned, String cartNumber) {
        this.buyer = name;
        this.ticket_type = ticketType;
        this.seat_type = seatType;
        this.barcode = barcodeText;
        this.error = errorMessage;
        this.is_validated = isScanned;
        this.cart_number = cartNumber;
    }

    public String getSeatValue(){
        if (seat_type.equals("") || seat_type.equals("N/A")){
            return "Siège: N/A";
        } else if (seat_type.equals("Placement libre")){
            return "Placement libre";
        } else {
            List<String> seatDetails = getSeatDetails();
            return "B:" + seatDetails.get(0) + " | R:" + seatDetails.get(1) + " | S:" + seatDetails.get(2);
        }
    }

    private List<String> getSeatDetails(){
        List<String> seatDetails = new ArrayList<>();
        seat_type = seat_type.replace("\\s+","");
        String[] arr = seat_type.split("-");
        String blk = (arr[0].split(":"))[1];
        String row = (arr[1].split(":"))[1];
        String seat = (arr[2].split(":"))[1];
        seatDetails.add(blk);
        seatDetails.add(row);
        seatDetails.add(seat);
        return seatDetails;
    }

    public String getName() {
        return buyer;
    }

    public String getTicketType() {
        return ticket_type;
    }

    public String getBarcodeText() {
        return barcode;
    }

    public boolean isScanned(){
        return is_validated.equals("vrai");
    }

    public void setIs_validated(boolean scanned){
        is_validated = scanned ? "vrai" : "faux";
    }

    public String getCartNumber(){
        return cart_number;
    }

    public String getError(){ return error; }

    @Override
    public String toString() {
        return buyer + " - " + is_validated;
    }

}
