package com.example.timetable.controller;

import com.example.timetable.entity.TimetableEntry;
import com.example.timetable.dto.TimetableRequest;
import com.example.timetable.service.TimetableService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    // =========================
    // GENERATE TIMETABLE
    // =========================
    @PostMapping("/generate")
    public List<TimetableEntry> generate(@RequestBody TimetableRequest request) {
        return timetableService.generateTimetable(request);
    }

    // =========================
    // GET LAST GENERATED
    // =========================
    @GetMapping("/last")
    public List<TimetableEntry> getLastGenerated() {
        return timetableService.getLastGeneratedTimetable();
    }

    // =========================
    // GENERATE PDF
    // =========================
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf() {

        try {

            List<TimetableEntry> timetableList =
                    timetableService.getLastGeneratedTimetable();

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

            for (TimetableEntry entry : timetableList) {

                table.addCell(entry.getDay());
                table.addCell(String.valueOf(entry.getPeriodNumber()));
                table.addCell(entry.getStartTime().toString());
                table.addCell(entry.getEndTime().toString());
                table.addCell(entry.getType());

                if (entry.getSubject() != null) {
                    table.addCell(entry.getSubject().getName());
                } else {
                    table.addCell("-");
                }
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=timetable.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
