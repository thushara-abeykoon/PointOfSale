package com.pos.pointofsale.model;

import javax.print.PrintService;

public class PrinterComboBox {
    private PrintService printService;
    private String printerName;

    public PrinterComboBox(PrintService printService, String printerName) {
        this.printService = printService;
        this.printerName = printerName;
    }

    public PrintService getPrintService() {
        return printService;
    }

    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String toString(){
        return printerName;
    }
}
