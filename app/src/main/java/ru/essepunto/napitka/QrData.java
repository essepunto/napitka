package ru.essepunto.napitka;

public class QrData {
    private String barcode;
    private double price1;
    private double price2;

    public QrData(String data) {
        String[] dataArray = data.split("&");
        for (String s : dataArray) {
            String[] item = s.split("=");
            switch (item[0]) {
                case "barcode":
                    barcode = item[1];
                    break;
                case "price1":
                    price1 = Double.parseDouble(item[1]);
                    break;
                case "price2":
                    price2 = Double.parseDouble(item[1]);
                    break;
                default:
                    break;
            }
        }
    }

    public String getBarcode() {
        return barcode;
    }

    public double getPrice1() {
        return price1;
    }

    public double getPrice2() {
        return price2;
    }
}
