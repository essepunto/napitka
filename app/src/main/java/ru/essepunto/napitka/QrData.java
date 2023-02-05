package ru.essepunto.napitka;

class QrData {
    private String barcode;
    private double price1;
    private double price2;

    public QrData(String data) {
        String[] elements = data.split("&");
        for (String element : elements) {
            String[] keyValue = element.split("=");
            switch (keyValue[0]) {
                case "barcode":
                    barcode = keyValue[1];
                    break;
                case "price1":
                    price1 = Double.parseDouble(keyValue[1]);
                    break;
                case "price2":
                case "price3":
                case "price4":
                    price2 = Double.parseDouble(keyValue[1]);
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


