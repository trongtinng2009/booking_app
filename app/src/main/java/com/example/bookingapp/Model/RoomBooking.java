package com.example.bookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class RoomBooking implements Parcelable {
    private String id,voucherref,cardnumber;
    private Room room;
    private User userguest,useraccount;
    private Timestamp checkin,checkout,bookingday,accdate;
    private int status,totalkid,totaladult,totalroom;
    private double totalprice;

    public static enum RoomBookingState
    {
        BOOKING(1),PAID(2),ENDBOOKING(3)
        ,ENDRATING(4),CANCELED(5);
        public int value;
        private RoomBookingState(int value)
        {
            this.value = value;
        }
    }
    public static final Parcelable.Creator<RoomBooking> CREATOR =
            new Creator<RoomBooking>() {
                @Override
                public RoomBooking createFromParcel(Parcel source) {
                    return new RoomBooking(source);
                }

                @Override
                public RoomBooking[] newArray(int size) {
                    return new RoomBooking[size];
                }
            };
    RoomBooking(Parcel in)
    {
        this.id = in.readString();
        this.useraccount = in.readParcelable(User.class.getClassLoader());
        this.voucherref = in.readString();
        this.room = (Room) in.readSerializable();
        this.cardnumber = in.readString();
        this.userguest = in.readParcelable(User.class.getClassLoader());
        this.checkin = in.readParcelable(Timestamp.class.getClassLoader());
        this.checkout = in.readParcelable(Timestamp.class.getClassLoader());
        this.bookingday = in.readParcelable(Timestamp.class.getClassLoader());
        this.accdate = in.readParcelable(Timestamp.class.getClassLoader());
        this.status = in.readInt();
        this.totalkid = in.readInt();
        this.totaladult = in.readInt();
        this.totalprice = in.readDouble();
        this.totalroom = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(useraccount,flags);
        dest.writeString(voucherref);
        dest.writeSerializable(room);
        dest.writeString(cardnumber);
        dest.writeParcelable(userguest,flags);
        dest.writeParcelable(checkin,flags);
        dest.writeParcelable(checkout,flags);
        dest.writeParcelable(bookingday,flags);
        dest.writeParcelable(accdate,flags);
        dest.writeInt(status);
        dest.writeInt(totalkid);
        dest.writeInt(totaladult);
        dest.writeDouble(totalprice);
        dest.writeInt(totalroom);
    }

    public RoomBooking() {
    }

    public RoomBooking(String id, User useraccount, String voucherref, Room room, String cardnumber,
                       User userguest, Timestamp checkin, Timestamp checkout,
                       Timestamp bookingday, Timestamp accdate, int status, int totalkid, int totaladult,
                       int totalroom,double totalprice) {
        this.id = id;
        this.useraccount = useraccount;
        this.voucherref = voucherref;
        this.room = room;
        this.cardnumber = cardnumber;
        this.userguest = userguest;
        this.checkin = checkin;
        this.checkout = checkout;
        this.bookingday = bookingday;
        this.accdate = accdate;
        this.status = status;
        this.totalkid = totalkid;
        this.totaladult = totaladult;
        this.totalprice = totalprice;
        this.totalroom = totalroom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalroom() {
        return totalroom;
    }

    public void setTotalroom(int totalroom) {
        this.totalroom = totalroom;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(User useraccount) {
        this.useraccount = useraccount;
    }

    public Timestamp getBookingday() {
        return bookingday;
    }

    public void setBookingday(Timestamp bookingday) {
        this.bookingday = bookingday;
    }

    public Timestamp getAccdate() {
        return accdate;
    }

    public void setAccdate(Timestamp accdate) {
        this.accdate = accdate;
    }

    public String getVoucherref() {
        return voucherref;
    }

    public void setVoucherref(String voucherref) {
        this.voucherref = voucherref;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public User getUserguest() {
        return userguest;
    }

    public void setUserguest(User userguest) {
        this.userguest = userguest;
    }

    public Timestamp getCheckin() {
        return checkin;
    }

    public void setCheckin(Timestamp checkin) {
        this.checkin = checkin;
    }

    public Timestamp getCheckout() {
        return checkout;
    }

    public void setCheckout(Timestamp checkout) {
        this.checkout = checkout;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalkid() {
        return totalkid;
    }

    public void setTotalkid(int totalkid) {
        this.totalkid = totalkid;
    }

    public int getTotaladult() {
        return totaladult;
    }

    public void setTotaladult(int totaladult) {
        this.totaladult = totaladult;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }
}
