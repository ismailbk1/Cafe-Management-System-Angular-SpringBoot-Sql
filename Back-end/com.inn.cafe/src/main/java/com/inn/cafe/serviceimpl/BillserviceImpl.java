package com.inn.cafe.serviceimpl;

import com.inn.cafe.constents.CafeConstents;
import com.inn.cafe.dao.BillDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.model.Bill;
import com.inn.cafe.service.Billservice;
import com.inn.cafe.utils.Cafeutils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillserviceImpl implements Billservice {
    @Autowired
    JwtFilter jwtFilter;


    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<String> generatReport(Map<String, Object> requestMap) {
        try {
            log.info("the request {}",requestMap);
log.info(requestMap.get("name").toString());
String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(boolean) requestMap.get("isGenerate")) {
                    fileName = (String) requestMap.get("uuid");
                } else {
                    fileName = Cafeutils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }
                String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: " + requestMap.get("contactNumber") + "\n" +
                        "Email: " + requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstents.LOCATION + "\\" + fileName + "" +
                        ".pdf"));
                document.open();
                setBorderPdf(document);
                Paragraph parg = new Paragraph("CAFE " + requestMap.get("name") +  "\n \n \n", getFont("header"));
                parg.setAlignment(Element.ALIGN_CENTER);
                document.add(parg);
                Paragraph parg2 = new Paragraph(data + "\n \n", getFont("data"));
                document.add(parg2);
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = Cafeutils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(table, Cafeutils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total : " + requestMap.get("totalAmount") + "\n" +
                        "Thank you for visiting, Please visit again", getFont("data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
            }
            return Cafeutils.getResonseEntity("Required Data Not Found", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return Cafeutils.getResonseEntity(CafeConstents.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            list = billDao.getAllBills();

        } else {
            list = billDao.getBillByUsername(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("inside getpdf : requestMap {}", requestMap);
        try {
log.info("validted {}",validateRequestMap(requestMap));
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
                log.info("inside the if in the getpdf : requestMap {}", requestMap);

                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);

            }
            String locationPath = CafeConstents.LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";


            if (Cafeutils.isFileExist(locationPath)) {
                byteArray = getByteArray(locationPath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);

            } else {
                requestMap.put("isGenerate", false);
                generatReport(requestMap);
                byteArray = getByteArray(locationPath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional optional = billDao.findById(id);
            if (!optional.isEmpty()) {
                billDao.deleteById(id);
                return new ResponseEntity<>("Bill deleted successfully ", HttpStatus.OK);
            }
            return new ResponseEntity<>("Bill id does not Exist ", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Bill deleted successfully ", HttpStatus.OK);
    }

    private byte[] getByteArray(String locationPath) throws Exception {
        File initialeFile = new File(locationPath);
        InputStream targetStream = new FileInputStream(initialeFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell( data.get("quantity").toString());
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));

    }


    private void addTableHeader(PdfPTable table) throws DocumentException {

        log.info("inside addTableHeader ");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columntitre -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columntitre));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

    }

    private void setBorderPdf(Document document) throws DocumentException {
        log.info("inside setBorderPdf ");

        Rectangle rec = new Rectangle(577, 825, 18, 15);
        rec.enableBorderSide(1);
        rec.enableBorderSide(2);
        rec.enableBorderSide(4);
        rec.enableBorderSide(8);
        rec.setBorderColor(BaseColor.BLACK);
        rec.setBorderWidth(1);
        document.add(rec);

    }

    private Font getFont(String type) {
        log.info("inside getFont");
        switch (type) {
            case "header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();

        }

    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt(requestMap.get("totalAmount").toString()));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");


    }

}
