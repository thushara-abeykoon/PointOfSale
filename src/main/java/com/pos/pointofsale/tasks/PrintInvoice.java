package com.pos.pointofsale.tasks;

import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.InvoiceOrderItems;
import javafx.concurrent.Task;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintInvoice extends Task<Void> {
    private String orderId;
    private String totalPrice;
    private PrintService printService;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();

    public PrintInvoice(String orderId, String totalPrice, PrintService printService) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.setPrintService(printService);
    }

    private void print(){
        try {
            String jrxmlPath = "src/main/resources/com/pos/pointofsale/jasper/invoice.jrxml";
            Map<String,Object> parameters = new HashMap<>();
            parameters.put("orderId",orderId);
            parameters.put("totalAmount",totalPrice);
            List<InvoiceOrderItems> orderItemsList = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT order_item.itm_id,i.itm_name, i.price, order_item.quantity, order_item.quantity*i.price as total FROM order_item JOIN item i on i.itm_id = order_item.itm_id  WHERE order_id = ?");
            preparedStatement.setObject(1,orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1) + resultSet.getString(2) + resultSet.getString(3) + resultSet.getString(4) + resultSet.getString(5));
                orderItemsList.add(new InvoiceOrderItems(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }
            JRBeanCollectionDataSource orderItems = new JRBeanCollectionDataSource(orderItemsList);
            parameters.put("OrderItems",orderItems);
            JasperReport report = JasperCompileManager.compileReport(jrxmlPath);
            JasperPrint print = JasperFillManager.fillReport(report,parameters,new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(print,"src/main/resources/com/pos/pointofsale/jasper/invoice.pdf");


            //Code to print
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
            printerJob.defaultPage(pageFormat);
            printerJob.setPrintService(printService);
            JRPrintServiceExporter exporter;
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(MediaSizeName.INVOICE);
            printRequestAttributeSet.add(new Copies(1));
            exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService.getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();

        } catch (JRException | SQLException | PrinterException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected Void call() throws Exception {
        print();
        return null;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PrintService getPrintService() {
        return printService;
    }

    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }
}
