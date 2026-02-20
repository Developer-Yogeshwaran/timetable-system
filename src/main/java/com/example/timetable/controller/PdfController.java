package com.example.timetable.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin
public class PdfController {

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody List<Map<String, Object>> timetableList) {

        try {
            if (timetableList == null || timetableList.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Generated Timetable"));
            document.add(new Paragraph(" "));

            float[] columnWidths = {80, 60, 80, 80, 80, 100};
            Table table = new Table(columnWidths);

            table.addHeaderCell("Day");
            table.addHeaderCell("Period");
            table.addHeaderCell("Start");
            table.addHeaderCell("End");
            table.addHeaderCell("Type");
            table.addHeaderCell("Subject");

            for (Map<String, Object> entry : timetableList) {

                String day = entry.getOrDefault("day", "-").toString();
                String period = entry.getOrDefault("periodNumber", "-").toString();
                String start = entry.getOrDefault("startTime", "-").toString();
                String end = entry.getOrDefault("endTime", "-").toString();
                String type = entry.getOrDefault("type", "-").toString();

                String subjectName = "-";
                Object subj = entry.get("subject");
                if (subj instanceof Map) {
                    Object name = ((Map) subj).get("name");
                    subjectName = name != null ? name.toString() : "-";
                } else if (subj != null) {
                    subjectName = subj.toString();
                }

                table.addCell(day);
                table.addCell(period);
                table.addCell(start);
                table.addCell(end);
                table.addCell(type);
                table.addCell(subjectName);
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timetable.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
